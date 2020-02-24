#!/bin/bash

UNAME=$(uname | tr "[:upper:]" "[:lower:]")
# If Linux, try to determine specific distribution
if [ "$UNAME" == "linux" ]; then
    # If available, use LSB to identify distribution
    if [ -f /etc/lsb-release -o -d /etc/lsb-release.d ]; then
        export DISTRO=$(lsb_release -i | cut -d: -f2 | sed s/'^\t'//)
    # Otherwise, use release info file
    else
        export DISTRO=$(ls -d /etc/[A-Za-z]*[_-][rv]e[lr]* | grep -v "lsb" | cut -d'/' -f3 | cut -d'-' -f1 | cut -d'_' -f1)
    fi
fi
# For everything else (or if above failed), just use generic identifier
[ "$DISTRO" == "" ] && export DISTRO=$UNAME
unset UNAME
if [ ! "$DISTRO" == "Ubuntu" ]; then
    echo "This deployment method works on Ubuntu only. Please change your distribution. Aborting..."
    exit 1
fi

apt update && apt install tomcat8 maven openjdk-11-jdk curl -y

printf "=================\r\nConfiguring Tomcat\r\n=================\r\n"

touch ./server.xml
cat <<EOCONF >./server.xml
<?xml version="1.0" encoding="UTF-8"?>
<Server port="8005" shutdown="SHUTDOWN">
	  <Listener className="org.apache.catalina.startup.VersionLoggerListener" />
	  <!-- Security listener. Documentation at /docs/config/listeners.html
	  <Listener className="org.apache.catalina.security.SecurityListener" />
	  -->
	  <!--APR library loader. Documentation at /docs/apr.html -->
	  <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
	  <!-- Prevent memory leaks due to use of particular java/javax APIs-->
	  <Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />
	  <Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />
	  <Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener" />

	  <!-- Global JNDI resources
		   Documentation at /docs/jndi-resources-howto.html
	  -->
	  <GlobalNamingResources>
		<!-- Editable user database that can also be used by
			 UserDatabaseRealm to authenticate users
		-->
		<Resource name="UserDatabase" auth="Container"
				  type="org.apache.catalina.UserDatabase"
				  description="User database that can be updated and saved"
				  factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
				  pathname="conf/tomcat-users.xml" />
	  </GlobalNamingResources>

	<Service name="configserver">
		<Connector port="8888" protocol="HTTP/1.1" connectionTimeout="20000"/>
		<Engine name="configserver8888" defaultHost="localhost">
			<Host name="localhost" appBase="configserver" unpackWARs="true" autoDeploy="true"/>
		</Engine>
	</Service>

	<Service name="eurekaserver">
		<Connector port="8761" protocol="HTTP/1.1" />
		<Engine name="eurekaserver8761" defaultHost="localhost">
			<Host name="localhost" appBase="eurekaserver" unpackWARs="true" autoDeploy="true" />
		</Engine>
	</Service>

	<Service name="mqttclient">
		<Connector port="1883" protocol="HTTP/1.1" />
		<Engine name="mqttclient1883" defaultHost="localhost">
			<Host name="localhost" appBase="mqttclient" unpackWARs="true" autoDeploy="true" />
		</Engine>
	</Service>

	<Service name="timescaleDbReader">
		<Connector port="4720" protocol="HTTP/1.1" />
		<Engine name="timescaleDbReader4720" defaultHost="localhost">
			<Host name="localhost" appBase="timescaleDbReader" unpackWARs="true" autoDeploy="true" />
		</Engine>
	</Service>

	<Service name="timescaleDbWriter">
		<Connector port="4721" protocol="HTTP/1.1" />
		<Engine name="timescaleDbWriter4721" defaultHost="localhost">
			<Host name="localhost" appBase="timescaleDbWriter" unpackWARs="true" autoDeploy="true" />
		</Engine>
	</Service>

	<Service name="view">
		<Connector port="8080" protocol="HTTP/1.1" />
		<Engine name="view8080" defaultHost="localhost">
			<Host name="localhost" appBase="view" unpackWARs="true" autoDeploy="true" />
		</Engine>
	</Service>
</Server>
EOCONF

rm /etc/tomcat8/server.xml
mv ./server.xml /etc/tomcat8/server.xml
chown tomcat8 /etc/tomcat8/server.xml

printf "=================\r\nRestarting Tomcat service\r\n=================\r\n"

systemctl daemon-reload
systemctl restart tomcat8.service

printf "=================\r\nBuilding the project\r\n=================\r\n"

mvn clean install package -DskipTests

printf "=================\r\nCopying .war-files...\r\n=================\r\n"

FILE_LIST="configserver
eurekaserver
mqttclient
timescaleDbReader
timescaleDbWriter
view"

CATALINA_BASE=$(ps aux | grep -Eo -m 1 'catalina.base[^ ]*' | grep -Eo '/(.*)')

systemctl stop tomcat8.service

while IFS= read -r line
do
	mkdir $CATALINA_BASE/$line
	cp $line/target/$line.war $CATALINA_BASE/$line/ROOT.war
	chown -R tomcat8 $CATALINA_BASE/$line
done < <(printf '%s\n' "$FILE_LIST")

systemctl restart tomcat8.service

printf "=================\r\nWaiting for Tomcat to autodeploy .war-files...\r\n=================\r\n"

sleep 5

printf "=================\r\nHealthchecking Services\r\n=================\r\n"

HEALTH_URLS="localhost:8888/actuator/health
localhost:8761/actuator/health
localhost:1883/actuator/health
localhost:4720/actuator/health
localhost:4721/actuator/health"

while IFS= read -r URL
do
	HTTP_RESPONSE=$(curl --silent --write-out "HTTPSTATUS:%{http_code}" $URL)
	HTTP_BODY=$(echo $HTTP_RESPONSE | sed -e 's/HTTPSTATUS\:.*//g')
	HTTP_STATUS=$(echo $HTTP_RESPONSE | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
	TRY_COUNTER=0
	# TODO status is always 404 -> investigate
	while [ ! "$HTTP_STATUS" -eq 200 ]
	do
		[ $TRY_COUNTER -gt 2 ] && echo "Services not starting. Aborting." && exit 1
		TRY_COUNTER="expr $TRY_COUNTER + 1"
		WAIT_TIME="expr $TRY_COUNTER * 5"
	    printf "Tomcat still busy. Waiting %ds." "$WAIT_TIME"
	    sleep "$WAIT_TIME"
	done
	if [[ ! "$HTTP_BODY" =~ "\{\"status\":\"UP\"\}" ]]; then
		echo "Automatic Healthcheck failed. Please manually investigate."
		exit 1
	fi
done < <(printf '%s\n' "$HEALTH_URLS")

printf "=================\r\nAll Healthy.\r\n=================\r\n"
printf "=================\r\nInstall finished.\r\n=================\r\n"

