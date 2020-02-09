////////////////////////////////////////////////////////////////////////////////////////
// ----------------------Global Javascript stuff and initilizing of other functions-----------------------
function init() {
	this.chartTopologyDiv = document.getElementById("liveSVGCard");

	this.databaseEntryDictionary = {
		"active_energy_A_minus" : "Zählerstand Wirkenergie A-",
		"active_energy_A_plus" : "Zählerstand Wirkenergie A+",
		"active_power_P1" : "Wirkleistung Phase 1",
		"active_power_P2" : "Wirkleistung Phase 2",
		"active_power_P3" : "Wirkleistung Phase 3",
		"active_power_P_total" : "Wirkleistung Gesamt",
		"apparent_power_S1" : "Scheinleistung Phase 1",
		"apparent_power_S2" : "Scheinleistung Phase 2",
		"apparent_power_S3" : "Scheinleistung Phase 3",
		"apparent_power_S_total" : "Scheinleistung Gesamt",
		"current_I1" : "Strom Phase 1",
		"current_I2" : "Strom Phase 2",
		"current_I3" : "Strom Phase 3",
		"current_I_avg" : "Strom Durchschnitt",
		"frequency_grid" : "Netzfrequenz",
		"reactive_energy_R_minus" : "Zählerstand Blindenergie R-",
		"reactive_energy_R_plus" : "Zählerstand Blindenergie R+",
		"reactive_power_Q1" : "Blindenergie Phase 1",
		"reactive_power_Q2" : "Blindenergie Phase 2",
		"reactive_power_Q3" : "Blindenergie Phase 3",
		"reactive_power_Q_total" : "Blindenergie Gesamt",
		"voltage_U1" : "Spannung Phase 1",
		"voltage_U2" : "Spannung Phase 2",
		"voltage_U3" : "Spannung Phase 3",
		"voltage_U_avg" : "Spannung Durchschnitt",
	}

	document.addEventListener("DOMContentLoaded", onLoadFunction);

	function onLoadFunction(e) {
//do the magic you want
		plotTopology();// if you want to trigger resize function immediately, call it

		window.addEventListener("resize", plotTopology);
	}

	setTimeout(function () {
			$('[data-toggle="tooltip"]').tooltip();
		}, 500
	);


	$('.close-icon').on('click', function () {
		$('liveSVGCard').fadeOut();
	})
}

init();


////////////////////////////////////////////////////////////////////////////////////////
// ----------------------Handlers for Loading Data from database--------------------------------
function getMeterData(id) {
	var url = "http://localhost:4711/readings?id="+id+"&start=2000-01-01+00:00:00&end=2030-01-01+00:00:00&step=1";
	return $.ajax({
		url:url,
		type: "GET",
		error: function(){
			console.log('Error ${error}');
		}
	});
}

function getDeviceInfo(id) {
	var url = "http://localhost:4711/device?id="+id;
	return $.ajax({
		url:url,
		type: "GET",
		error: function(){
			console.log('Error ${error}');
		}
	});
}


////////////////////////////////////////////////////////////////////////////////////////
// ----------------------------------Graph Plotting--------------------------------------------

// --------------------------------------Topology--------------------------------------------
//// Main plotting function
function plotTopology(){

	// Delete old chart
	d3.selectAll("#liveSVG > *").remove();

	d3.json('json/DAI_Smart_Micro_Grid.json').then(function (data) {
			this.typeDictionary = {
				"POWERGRID": "Power Grid",
				"LIGHT": "Light",
				"SOLARPANEL": "Solar Panel",
				"ELECTRICMETER": "Electric Meter",
				"WINDMILL": "Windmill",
				"CHARGINGSTATION": "Charging Station",
				"BATTERY": "Battery",
				"CP": "Charging Point"
			};

			this.iconLibrary = {
				"POWERGRID": "powerGrid.png",
				"LIGHT": "light.png",
				"SOLARPANEL": "solarPanel.png",
				"ELECTRICMETER": "electricMeter.png",
				"WINDMILL": "windmill.png",
				"CHARGINGSTATION": "chargingStation.png",
				"BATTERY": "battery.png",
				"CP": "carPlug.png"
			};




			// Extract the width and height that was computed by CSS.
			//let padding = 20;
			let width = this.chartTopologyDiv.clientWidth;
			let height = $(window).height()/1.5;


			let circleRadius = width/50;
			let nodeDistanceX =  width/20;


			const root = this.tree(data, width, nodeDistanceX);

			let x0 = Infinity;
			let x1 = -x0;
			root.each(d => {
				if (d.x > x1) x1 = d.x;
				if (d.x < x0) x0 = d.x;
			});


			// Syling of SVG-Canvas
			let svg = d3.select("#liveSVG")
				.attr("viewbox", [0, 0, width, x1 - x0 + root.dx * 2])
				.attr("width", width)
				.attr("height", x1 - x0 + root.dx * 2);

			// Makes <g> dom element, that is around the graph for style elements
			const g = svg.append("g")
				.attr("font-family", "sans-serif")
				.attr("font-size", 10)
				.attr("transform", `translate(${root.dy / 3}, ${root.dx - x0})`);


			// Makes <g> elements for links
			let linkList = root.links();
			var linkListFinal = [];
			for (var i = 0; i < linkList.length; i++) {
				let currentLink = linkList[i];
				if (currentLink.target) {
					//Add new Links
					let distanceX = Math.abs((currentLink.target.x - currentLink.source.x) / 2);
					let distanceY = Math.abs((currentLink.target.y - currentLink.source.y) / 2);
					// Vorzeichen bei unterschiedlichen Richtungen
					if (Math.sign((currentLink.target.x - currentLink.source.x)) === 0) {
						distanceX = distanceX * -1;
					}
					let centerPoint = {
						x: currentLink.source.x,
						y: currentLink.source.y + distanceY
					};
					let edgePoint = {
						x: currentLink.target.x,
						y: currentLink.source.y + distanceY
					};
					let firstLink = {
						source: currentLink.source,
						target: centerPoint
					};
					let secondLink = {
						source: centerPoint,
						target: edgePoint
					};
					let thirdLink = {
						source: edgePoint,
						target: currentLink.target
					};
					//Only add unique entries
					if (linkList.indexOf(firstLink) === -1) {
						linkListFinal.push(firstLink);
					}
					if (linkList.indexOf(secondLink) === -1) {
						linkListFinal.push(secondLink);
					}
					if (linkList.indexOf(thirdLink) === -1) {
						linkListFinal.push(thirdLink);
					}
				}
			}
			const link = g.append("g")
				.attr("fill", "none")
				.attr("stroke", "#404040")
				.attr("stroke-opacity", 1)
				.attr("stroke-width", 1.5)
				.selectAll("path")
				.data(linkListFinal)
				.join("path")
				.attr("d", d3.linkHorizontal()
					.x(d => d.y)
					.y(d => d.x));

			// Makes <g> dom elements for children nodes
			const node = g.append("g")
				.attr("stroke-linejoin", "round")
				.attr("stroke-width", 3)
				.selectAll("g")
				.data(root.descendants())
				.join("g")
				.attr("transform", d => `translate(${d.y},${d.x})`)
				.attr("data-html", "true")
				.attr("data-toggle", "tooltip")
				.attr("data-placement", "right")
				.attr("title", function (d) {
					return "<strong>" + typeDictionary[d.data.subtype] + ":</strong>\n" + d.data.databaseId;

				})
				.on("click", function (d) {
					prepareForPlotting(d);
					//$(window).scrollTop(0);
				});

			node.append("circle")
				.attr("fill", function (d) {
					return setNodeColor(d);
				})
				.attr("data-html", "true")
				.attr("r", circleRadius);

			node.append("image")
				.attr("xlink:href", function (d) {
					return "icons/" + iconLibrary[d.data.subtype];
				})
				.attr("x", -(circleRadius*1.2)/2)
				.attr("y", -(circleRadius*1.2)/2)
				.attr("width", circleRadius*1.2)
				.attr("height", circleRadius*1.2)
				.style("fill", "#ffffff");
		})
}

//// Help functions
// This calculates and creates the tree object (with nodes positions) from a json object
function tree(data, width, nodeDistanceX){
	const root = d3.hierarchy(data);
	// Set initial position of root node
	root.dx = nodeDistanceX;
	root.dy = width / (root.height + 1);
	return d3.tree().nodeSize([nodeDistanceX, root.dy])(root);
}

// This sets the colors for the node subtypes.
function setNodeColor(d){
    var color;
    if(d.data.type === "POWERGRID"){
        color = "#7a0606";
    }else if(d.data.type === "PRODUCER"){
        color = "#1abc9c";
    }else if(d.data.type === "ELECTYRICMETER"){
		color = "#022449";
    }else if(d.data.type === "CONSUMER"){
		color = "#558dca";
    }else if(d.data.type === "STORAGE"){
        color = "#b8dce6";
    }else{
		color = "#c2c5cc";
	}
    return color;
}


// ----------------------------------------Time Series Graph-----------------------------------------
//// Main plotting functions
function prepareForPlotting(dataBaseIds){

	let id = dataBaseIds.data.id;
	this.deviceData = dataBaseIds.data;

    // Delete old chart
	d3.selectAll("#historySVGRow > *").remove();
	d3.selectAll(".form-group > *").remove();
	if(id != 0) {
		getMeterData(id).then(function (data) {

			/// Create html card
			d3.select("#historySVGRow")
				.insert("div", "#historySVGRow")
				.attr("class","card")
				.attr("id","historySVGCard")
				.insert("div", ".card")
				.attr("class","card-block")
				.insert("div", ".card-block")
				.attr("class","col-md-5")
				.attr("id","cardBlockDef")
				.insert("svg", ".cardBlockDef")
				.attr("id","historySVG");





			plotTimeSeries(data,  Object.keys(data[0]).sort()[0])
			drawDropDownMenu(data)


		})
	}

}

function drawDropDownMenu(data){

	databaseEntryDictionary = this.databaseEntryDictionary;
	///// Create Options:
	// Handler for dropdown value change
	var dropdownChange = function() {
		var newMeterValue = d3.select(this).property('value');
		plotTimeSeries(data, newMeterValue);
	};

	// Get names of Meter Values
	var meterValueNames = Object.keys(data[0]).sort();
	meterValueNames.splice(meterValueNames.indexOf('id'), 1); //remove id
	meterValueNames.splice(meterValueNames.indexOf('timestamp'), 1);
	meterValueNames.splice(meterValueNames.indexOf('metaInformation'), 1);

	var dropdown = d3.select(".form-group")
		.insert("label", ".form-group")
		.attr("for","meterValues_"+ this.dataBaseIds)
		.text("Meter Value:")
		.insert("select", ".form-group")
		.attr("id","meterValues_"+ this.dataBaseIds)
		.on("change", dropdownChange)
		.attr("class","form-control");

	dropdown.selectAll("option")
		.data(meterValueNames)
		.enter().append("option")
		.attr("value", function (d) { return d; })
		.text(function (d) {
			return databaseEntryDictionary[d];
		});

}

function plotTimeSeries(data, plotItem){

	// Delete old chart
	d3.selectAll("#historySVG > *").remove();

	// Setting Window Parameter

	let chartTimeDiv = document.getElementById("historySVGCard");
	let width = chartTimeDiv.clientWidth;
	let height = $(window).height()/2.2;
	let margin = {top: height/10, right: width/10, bottom: height/10, left: width/10};

	// Attaching svg to dom
	let svg = d3.select("#historySVG")
		.attr("width", width - margin.right)
		.attr("height", height + margin.top + margin.bottom)
		.append("g")
		.attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");

	// Check that data is not empty
	if (!data[0]) {
		return;
	}

	// Add X axis --> it is a date format
	let x = d3.scaleTime()
		.domain(d3.extent(data, function (d) {
			var time = new Date(d["timestamp"]);
			return (time);
		}))
		.range([0, width]);
	svg.append("g")
		.attr("transform", "translate(0," + height + ")")
		.call(d3.axisBottom(x));

	// Add Y axis
	let y = d3.scaleLinear()
		.domain([d3.min(data, function (d) {
			return +d[plotItem];
		}), d3.max(data, function (d) {
			return +d[plotItem];
		})])
		.range([height, 0]);
	svg.append("g")
		.call(d3.axisLeft(y));

	// Add the line
	svg.append("path")
		.datum(data)
		.attr("fill", "none")
		.attr("stroke", "steelblue")
		.attr("stroke-width", 1.5)
		.attr("d", d3.line()
			.x(function (d) {
				var time = new Date(d["timestamp"]);
				return x((time));
			})
			.y(function (d) {
				return y(d[plotItem])
			})
		)


	var target = $("#data")
	if (target.length) {
		$('html, body').animate({
			scrollTop: (target.offset().top - 56)
		}, 1000, "easeInOutExpo");
		return false;
	}

	scrollToPage("#data");

}

//// Help functions
//// Scroll to timeseries after click on topology node
function scrollToPage(targetID){
	var target = $(targetID)
	if (target.length) {
		$('html, body').animate({
			scrollTop: (target.offset().top - 56)
		}, 1000, "easeInOutExpo");
		return false;
	}
}









