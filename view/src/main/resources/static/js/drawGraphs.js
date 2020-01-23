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
			let typeDictionary = {
				"POWERGRID": "Power Grid",
				"LIGHT": "Light",
				"SOLARPANEL": "Solar Panel",
				"ELECTRICMETER": "Electric Meter",
				"WINDMILL": "Windmill",
				"CHARGINGSTATION": "Charging Station",
				"BATTERY": "Battery",
				"CP": "Charging Point"
			};

			let iconLibrary = {
				"POWERGRID": "powerGrid.png",
				"LIGHT": "light.png",
				"SOLARPANEL": "solarPanel.png",
				"ELECTRICMETER": "electricMeter.png",
				"WINDMILL": "windmill.png",
				"CHARGINGSTATION": "chargingStation.png",
				"BATTERY": "battery.png",
				"CP": "carPlug.png"
			};

			let width = 1000;
			let height = 1000;

			let circleRadius = 18;
			let nodeDistanceX = 45;
			let padding = 10;


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
	if(id != 0) {
		getMeterData(id).then(function (deviceData) {

			let margin = {top: 10, right: 30, bottom: 50, left: 100};
			let width = 1000 - margin.left - margin.right;
			let height = 500 - margin.top - margin.bottom;

			let svg = d3.select("#historySVG")
				.attr("width", width + margin.left + margin.right)
				.attr("height", height + margin.top + margin.bottom)
				.append("g")
				.attr("transform",
					"translate(" + margin.left + "," + margin.right + ")");

			let data = deviceData;
			if (!data[0]) {
				return;
			}
			let sumstat = d3.nest()
				.key(function (d) {
					return d.id
				})
				.entries(data.flat());
			console.log(sumstat);

			let x = d3.scaleTime()
				.domain(d3.extent(data, function (d) {
					var time = new Date(d["timestamp"]);
					return (time);
				}))
				.range([0, width]);
			svg.append("g")
				.attr("transform", "translate(0," + height + ")")
				.call(d3.axisBottom(x));

			let y = d3.scaleLinear()
				.domain([0, d3.max(data, function (d) {
					return +d["current_I1"];
				})])
				.range([height, 0]);
			svg.append("g")
				.call(d3.axisLeft(y));

			let res = sumstat.map(function (d) {
				return d.key
			});
			let color = d3.scaleOrdinal()
				.domain(res)
				.range(["#FF0000"]);

			svg.selectAll(".line")
				//.data(sumstat)
				.enter()
				.append("path")
				//.datum(sumstat)
				.attr("fill", "none")
				.attr("stroke", function (d) {
					return "#7a0606"
				})
				.attr("stroke-width", 1.5)
				.attr("d", function (d) {
					d3.line()
						.x(function (d) {
							var time = new Date(d["timestamp"]);
							return x((time));
						})
						.y(function (d) {
							return y(d["current_I1"])
						})
				});
		})
	}

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


