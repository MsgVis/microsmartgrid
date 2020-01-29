// ----------------------These are all functions for the History Graph-----------------------
// This draws the Topology Graph using d3

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


function drawGraphs() {

document.addEventListener("DOMContentLoaded", function(event) {

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

			let width = 700;
			let height = 700;

			let circleRadius = 18;
			let nodeDistanceX = 45;
			let padding = 10;


			const root = this.tree(data, width, height/15);

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
				.attr("height", height);

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
					parseTimeSVG(d);
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
				.attr("x", "-12px")
				.attr("y", "-12px")
				.attr("width", "24px")
				.attr("height", "24px")
				.style("fill", "#ffffff");
		})
	})



}
// This sets the colors for the node subtypes.
// TODO: Eventuell ins JSON übertragen. Sollte Configurierbar sein.
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
// This calculates and creates the tree object (with nodes positions) from a json object
function tree(data, width, nodeDistanceX){
	const root = d3.hierarchy(data);
	// Set initial position of root node
	root.dx = nodeDistanceX;
	root.dy = width / (root.height + 1);
	return d3.tree().nodeSize([nodeDistanceX, root.dy])(root);
}
////////////////////////////////////////////////////////////////////////////////////////


// ----------------------These are all functions for the History Graph-----------------------
// This draws the Time Graph using d3
function parseTimeSVG(dataBaseIds){

	let id = dataBaseIds.data.id;
	this.deviceData = dataBaseIds.data;

    // Delete old chart
	d3.selectAll("#historySVG > *").remove();
	d3.selectAll(".form-group > *").remove();
	if(id != 0) {
		getMeterData(id).then(function (deviceData) {

			var data = deviceData
			plotTimeSeries(data,  Object.keys(data[0]).sort()[0])

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
				.attr("for","meterValues_"+dataBaseIds)
				.text("Select Meter Value:")
				.insert("select", ".form-group")
				.attr("id","meterValues_"+dataBaseIds)
				.on("change", dropdownChange)
				.attr("class","form-control");

			dropdown.selectAll("option")
				.data(meterValueNames)
				.enter().append("option")
				.attr("value", function (d) { return d; })
				.text(function (d) {
					return d[0].toUpperCase() + d.slice(1,d.length); // capitalize 1st letter
				});






		})
	}


}

function plotTimeSeries(data, plotItem){

	// Delete old chart
	d3.selectAll("#historySVG > *").remove();
	//d3.selectAll(".form-group > *").remove();

	// Setting Window Parameter
	let margin = {top: 0, right: 30, bottom: 200, left: 100};
	let width = 700 - margin.left - margin.right;
	let height = 700 - margin.top - margin.bottom;

	// Attaching svg to dom
	let svg = d3.select("#historySVG")
		.attr("width", width + margin.left + margin.right)
		.attr("height", height + margin.top + margin.bottom)
		.append("g")
		.attr("transform",
			"translate(" + margin.left + "," + margin.right + ")");

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

	// // Add Name
	// d3.select(".form-group")
	// 	.insert("text", ".form-group")
	// 	.attr("x", (width / 2))
	// 	.attr("y", height + 50)
	// 	.attr("text-anchor", "middle")
	// 	.style("font-size", "16px")
	// 	.style("font-weight", "bold")
	// 	.text(typeDictionary[this.deviceData.subtype] +": "+ this.deviceData.databaseId);

}



var shownHistoryDbIds = [];
function parseHistorySVGHelper(d){
	if(shownHistoryDbIds.includes(d.data.databaseId)){ //already shown -> remove from graph
		shownHistoryDbIds.splice(shownHistoryDbIds.indexOf(d.data.databaseId), 1);
	} else { //not shown -> add to graph
		shownHistoryDbIds.push(d.data.databaseId);
	}
	parseTimeSVG(shownHistoryDbIds);
}
////////////////////////////////////////////////////////////////////////////////////////

function getHistoryData(databaseId){
	console.log(databaseId);
	switch(databaseId){
		case "PPG":
			return [{
				"id": "PPG",
				"time": d3.timeParse("%s")("1573407925"),
				"value": "100"
			}, {
				"id": "PPG",
				"time": d3.timeParse("%s")("1573417925"),
				"value": "105"
			}, {
				"id": "PPG",
				"time":d3.timeParse("%s")("1573427925"),
				"value": "120"
			}];
			break;
		case "KA2013":
			return [{
				"id": "KA2013",
				"time": d3.timeParse("%s")("1573407925"),
				"value": "100"
			}, {
				"id": "KA2013",
				"time": d3.timeParse("%s")("1573417925"),
				"value": "105"
			}, {
				"id": "KA2013",
				"time":d3.timeParse("%s")("1573427925"),
				"value": "20"
			}];
			break;
	}
}
////////////////////////////////////////////////////////////////////////////////////////









drawGraphs();
setTimeout( function() {
		$('[data-toggle="tooltip"]').tooltip();
	}, 500
);


