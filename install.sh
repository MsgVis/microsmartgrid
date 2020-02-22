#!/usr/bin/env

if command -v apt >/dev/null 2>&1
then
	apt update && apt install tomcat8 maven openjdk-11-jdk
elif command -v yay >/dev/null 2>&1
then
	yay -Syu tomcat8 maven jdk11-openjdk
elif command -v pacman >/dev/null 2>&1
then
	pacman -Syu tomcat8 maven jdk11-openjdk
else
	echo "I require at least apt, yay, or pacman to install openjdk11, tomcat8 and maven.";
	exit 1;
fi
printf "=================\r\nBuilding the project=================\r\n"
mvn clean install package -DskipTests
systemctl start tomcat8.service
#TODO change server.xml

#TODO copy and ROOT.war-rename the wars to correct directories

#TODO restart catalina
