////////////////////////////////////////////////////////////////////////////////////////
// ----------------------Global Javascript stuff and initilizing of other functions-----------------------
function init() {

	this.databaseEntryDictionary = {
		"A_minus": "Zählerstand Wirkenergie A-",
		"A_plus": "Zählerstand Wirkenergie A+",
		"P_r": "Wirkleistung Phase 1",
		"P_s": "Wirkleistung Phase 2",
		"P_t": "Wirkleistung Phase 3",
		"P_total": "Wirkleistung Gesamt",
		"S_r": "Scheinleistung Phase 1",
		"S_s": "Scheinleistung Phase 2",
		"S_t": "Scheinleistung Phase 3",
		"S_total": "Scheinleistung Gesamt",
		"I_r": "Strom Phase 1",
		"I_s": "Strom Phase 2",
		"I_t": "Strom Phase 3",
		"I_avg": "Strom Durchschnitt",
		"Frequency": "Netzfrequenz",
		"R_minus": "Zählerstand Blindenergie R-",
		"R_plus": "Zählerstand Blindenergie R+",
		"Q_r": "Blindleistung Phase 1",
		"Q_s": "Blindleistung Phase 2",
		"Q_t": "Blindleistung Phase 3",
		"Q_total": "Blindleistung Gesamt",
		"U_r": "Spannung Phase 1",
		"U_s": "Spannung Phase 2",
		"U_t": "Spannung Phase 3",
		"U_avg": "Spannung Durchschnitt",
	}

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

	this.chartTopologyDiv = document.getElementById("liveSVGCard");

	document.addEventListener("DOMContentLoaded", onLoadFunction);

	/// Function to plot images again on window resize
	function onLoadFunction(e) {
		plotTopology();
		window.addEventListener("resize", plotTopology);
	}

	/// Get Tooltips to wait for data load
	setTimeout(function () {
			$('[data-toggle="tooltip"]').tooltip();
		}, 500
	);

	// To lazy to add bootstrap classes to all elements in index html. This is the helper function.
	bootstrapStyleMultipleElements();

	$(document).on('change', 'input:radio[name="flowSelection"]', function (event) {
		plotTopology();
	});

}

init();


////////////////////////////////////////////////////////////////////////////////////////
// ----------------------Handlers for Loading Data from database--------------------------------
function getMeterData(id) {
	var url = "http://localhost:4720/readings?id=" + id;
	return $.ajax({
		url: url,
		type: "GET",
		error: function () {
			console.log('Error ${error}');
		}
	});
}

function getDeviceInfo(id) {
	var url = "http://localhost:4720/deviceById?id=" + id;
	return $.ajax({
		url: url,
		type: "GET",
		error: function () {
			console.log('Error ${error}');
		}
	});
}

function getFlowValues(id) {
	var url = "http://localhost:4720/latest?cutoff=P5D";
	return $.ajax({
		url: url,
		type: "GET",
		error: function () {
			console.log('Error ${error}');
		}
	});
}


////////////////////////////////////////////////////////////////////////////////////////
// ----------------------------------Graph Plotting--------------------------------------------

// --------------------------------------Topology--------------------------------------------
//// Main plotting function
function plotTopology() {

	// Delete old chart
	d3.selectAll("#liveSVG > *").remove();

	let chartTopologyDiv = this.chartTopologyDiv;

	d3.json('json/DAI_Smart_Micro_Grid.json').then(function (data) {


		getFlowValues().then(function (flowData) {
			// // Plot Root in Timeseries as default
			let preparedData = {}
			preparedData.data = data;
			prepareForPlotting(preparedData,true);


			// Extract the width and height that was computed by CSS.
			let width = chartTopologyDiv.clientWidth;


			let circleRadius = width / 50;
			let nodeDistanceX = width / 22;


			const root = tree(data, width, nodeDistanceX);

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


			let defaulOpacity = 10;
			let linkListFinal = addCornersToLinks(root, flowData, defaulOpacity);

			const link = g.append("g")
				.attr("fill", "none")
				.attr("stroke", "#dadada")
				.attr("stroke-opacity", 3)
				.selectAll("path")
				.data(linkListFinal)
				.join("path")
				.attr("d", d3.linkVertical()
					.x(d => d.y)
					.y(d => d.x))
				.attr("stroke-width", defaulOpacity);

			/// Moving dashes svg for Flow
			const dashes = g.append("g")
				.attr("fill", "none")
				.attr("stroke", "#FFFFE0")
				.attr("stroke-opacity", 3)
				.selectAll("path")
				.data(linkListFinal)
				.join("path")
				.attr("d", d3.linkHorizontal()
					.x(d => d.y)
					.y(d => d.x))
				.attr("stroke-width", function (d) {

					let width = (defaulOpacity / 2);
					let weight = 0;

					if (d.source.node) {
						let totalFlow = d.source.totalFlow;
						if (d.source.flowInNetwork) {
							weight += (d.source.flowInNetwork / totalFlow);
						}
						if (d.source.flowOutNetwork) {
							weight += (d.source.flowOutNetwork / totalFlow);
						}
					} else if (d.target.node) {
						let totalFlow = d.target.totalFlow;
						if (d.target.flowInNetwork) {
							weight += (d.target.flowInNetwork / totalFlow);
						}
						if (d.target.flowOutNetwork) {
							weight += (d.target.flowOutNetwork / totalFlow);
						}
					}else{
						weight = 1;
					}
					if(weight < 0.1 & weight!= 0){
						weight = 0.1;
					}
					return width * weight;

				})
				.attr("class", "flow")
				.each(recusiveAddingOfDashes);


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
				.attr("class", "node")
				.attr("title", function (d) {
					return "<strong>" + typeDictionary[d.data.subtype] + ":</strong>\n" + d.data.databaseId;
				})
				.on("click", function (d) {
					prepareForPlotting(d);
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
				.attr("x", -(circleRadius * 1.2) / 2)
				.attr("y", -(circleRadius * 1.2) / 2)
				.attr("width", circleRadius * 1.2)
				.attr("height", circleRadius * 1.2)
				.style("fill", "#ffffff");

			/// Labels für Flow
			node.append("text")
				.attr("dx", function (d){
					if(d.data.id === 1){ //This is root node
						return -(circleRadius + 6);
					}else {
						return circleRadius + 6;
					}
				})
				.attr("text-anchor", function(d){
					if(d.data.id === 1){ //This is root node
						return "end";
					}else {
						return "start";
					}
				})
				.attr("dy", ".35em")
				.attr("class", function(d){
					if(d.data.id != 0) {
						return "nodeFlowLabels";
					}
				})
				.style("opacity", 0)
				.text(function(d){
					// Fill with flow data values and arrows
					//if(d.flowOutNetwork)
					//
					if( typeof d.flowInNetwork != "undefined"){
						return "In: " + d.flowInNetwork + " A";
					}
				});


			// Add Play Button for Flows
			drawPlayButton(flowAnimate, dashes, recusiveAddingOfDashes);
		})

	})
}


//// Draw GUI elements
// This draws Play svg
function drawPlayButton(flowAnimate, flow, setFlowDash) {
	///power button
	let size = $("#playButton").siblings('p').first().height();

	let svg = d3.select("#playButton")
		.attr("width", size)
		.attr("height", size);

	svg.append("image")
		.attr("xlink:href", "icons/powerOn2.png")
		.attr("x", "0")
		.attr("y", "0")
		.attr("width", size)
		.attr("height", size)
		.style("fill", "#ffffff")
		.on("mouseover", function(){
			let svg = d3.select("#liveSVG");
			svg.selectAll(".nodeFlowLabels")
				.style("opacity", 0)
				.transition()
				.duration(100)
				.style("opacity", 1);
			flowAnimate();
		})
		.on("mouseout", function () {
			//cancel all transitions by making a new one
			let svg = d3.select("#liveSVG");
			svg.selectAll(".nodeFlowLabels")
				.style("opacity", 1)
				.transition()
				.duration(100)
				.style("opacity", 0);

			flow.transition();
			flow
				.style("opacity", 0)
				.each(function (d) {
					setFlowDash.call(this, d);
				})
		});
}


//// Help functions
// This calculates and creates the tree object (with nodes positions) from a json object
function tree(data, width, nodeDistanceX) {
	const root = d3.hierarchy(data);
	// Set initial position of root node
	root.dx = nodeDistanceX;
	root.dy = width / (root.height + 1);
	return d3.tree().nodeSize([nodeDistanceX, root.dy])(root);
}

// Redesigns links that are calculated by tree to get corner instead of round edges.
// This gives a more electrical engineering design.
function addCornersToLinks(root, flowData = flowData, defaultOpacity = defaultOpacity) {

		// Makes <g> elements for links
		let linkList = root.links();
		var linkListFinal = [];
		for (var i = 0; i < linkList.length; i++) {
			let currentLink = linkList[i];
			if (currentLink.target) {


				let liveSelectedValue = $('input[name=flowSelection]:checked').val();
				/// Get total amount to calculated percentages
				let totalAmount = 0;
				flowData.forEach(function (number) {
					totalAmount += number[liveSelectedValue];
				});
				currentLink.source.totalFlow = totalAmount;
				currentLink.target.totalFlow = totalAmount;
				currentLink.source.flowValue = liveSelectedValue;
				currentLink.target.flowValue = liveSelectedValue;
				////////////////////////


				// Add width property to Source and Target
				let sourceID = currentLink.source.data.id;
				let targetID = currentLink.target.data.id;
				///// mark these points as nodes
				if(sourceID != 0 ){
					currentLink.source.node = true;
				}
				if(targetID != 0){
					currentLink.target.node = true;
				}

				if(sourceID != 0) {
					let sourceLiveData = flowData.filter(function (number) {
						return number.Device_id === sourceID
					});
					let liveWeightValueSource = sourceLiveData[0][liveSelectedValue];
					currentLink.source.flowInNetwork = liveWeightValueSource;
					if(liveWeightValueSource === null){
						currentLink.source.flowInNetwork = 0;
					}
				}

				if(targetID != 0) {
					let targetLiveData = flowData.filter(function (number) {
						return number.Device_id === targetID
					});
					let liveWeightValueTarget = targetLiveData[0][liveSelectedValue];
					currentLink.target.flowInNetwork = liveWeightValueTarget;
					if(liveWeightValueTarget === null){
						currentLink.target.flowInNetwork = 0;
					}
				}
				///////////////////////




				//Add new Links
				let distanceY = Math.abs((currentLink.target.y - currentLink.source.y) / 2);
				let paddingEdge = -defaultOpacity/2;
				// Vorzeichen bei unterschiedlichen Richtungen
				if (Math.sign(currentLink.target.x - currentLink.source.x) === 1) {
					paddingEdge = paddingEdge * -1;
				}
				let centerPoint = {
					x: currentLink.source.x,
					y: currentLink.source.y + distanceY
				};
				let edgePointIn = {
					x: currentLink.target.x + paddingEdge,
					y: currentLink.source.y + distanceY
				};
				let edgePointOut = {
					x: currentLink.target.x,
					y: currentLink.source.y + distanceY + Math.abs(paddingEdge)
				};
				let firstLink = {
					source: currentLink.source,
					target: centerPoint
				};
				let secondLink = {
					source: centerPoint,
					target: edgePointIn
				};
				let thirdLink = {
					source: edgePointOut,
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
		return linkListFinal;

}

// This sets the colors for the node subtypes.
function setNodeColor(d) {
	var color;
	if (d.data.type === "POWERGRID") {
		color = "#7a0606";
	} else if (d.data.type === "PRODUCER") {
		color = "#1abc9c";
	} else if (d.data.type === "ELECTYRICMETER") {
		color = "#022449";
	} else if (d.data.type === "CONSUMER") {
		color = "#558dca";
	} else if (d.data.type === "STORAGE") {
		color = "#b8dce6";
	} else {
		color = "#c2c5cc";
	}
	return color;
}

// Addes further dashes to first dash with stroke-offset
function recusiveAddingOfDashes(d) {
	var d3this = d3.select(this);
	var totalLength = d3this.node().getTotalLength();
	d3this
		.style("opacity", 0)
		.attr('stroke-dasharray', 7)
		.attr('stroke-dashoffset', function(d){

			let direction = 100;
			 //// 4 cases
			// if (d.source.flowInNetwork) {
			// 	 direction = 0;
			// }
			// if (d.source.flowOutNetwork) {
			// 	direction = 100;
			// }
			// if (d.target.flowInNetwork) {
			// 	direction = 0;
			// }
			// if (d.target.flowOutNetwork) {
			// 	direction = 100;
			// }
			return direction;


		}) //set to minus for reverse
}

// Flow Animation of dashes
function flowAnimate(nodeData) {
	let svg = d3.select("#liveSVG");
	var links = svg.selectAll(".flow");
	links
		.attr("stroke-dashoffset", function(d){
			if(d.target.direction == "in"){
				return 0;
			}else{
				return 100;
			}
		})
		.style("opacity", 0.7)
		.transition()
		.duration(3000)
		.ease(d3.easeLinear)
		.attr("stroke-dashoffset", function(d){
			if(d.target.direction == "in"){
				return 100;
			}else{
				return 0;
			}
		})
		.on("end", function() {
			flowAnimate();
		});


}




// ----------------------------------------Time Series Graph-----------------------------------------
//// Main plotting functions
function prepareForPlotting(dataBaseIds, initial = false) {

	let deviceName = dataBaseIds.data.databaseId + " (" + typeDictionary[dataBaseIds.data.subtype] + ")" ;
	$("#headerTimeseries").text(deviceName);

	let id = dataBaseIds.data.id;
	this.deviceData = dataBaseIds.data;

	// Delete old chart
	d3.selectAll("#historySVGRow > *").remove();
	d3.selectAll(".form-group > *").remove();
	if (id != 0) {
		getMeterData(id).then(function (data) {

			plotTimeSeries(data, Object.keys(data[0]).sort()[0],initial);
			drawDropDownMenu(data)

		})
	}

}

function plotTimeSeries(data, plotItem, initial = false) {

	// Delete old chart
	d3.selectAll("#historySVG > *").remove();

	// Setting Window Parameter
	let chartTimeDiv = document.getElementById("historySVGCard");
	let width = chartTimeDiv.clientWidth;
	let height = $(window).height() / 2.2;
	let margin = {top: height / 10, right: width / 10, bottom: height / 10, left: width / 10};

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
			var time = new Date(d["Timestamp"]);
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
				var time = new Date(d["Timestamp"]);
				return x((time));
			})
			.y(function (d) {
				return y(d[plotItem])
			})
		)

	if(initial === false) {
		var target = $("#data")
		if (target.length) {
			$('html, body').animate({
				scrollTop: (target.offset().top - 56)
			}, 1000, "easeInOutExpo");
			return false;
		}
		scrollToPage("#data");
	}

}

function drawDropDownMenu(data) {

	databaseEntryDictionary = this.databaseEntryDictionary;
	///// Create Options:
	// Handler for dropdown value change
	var dropdownChange = function () {
		var newMeterValue = d3.select(this).property('value');
		plotTimeSeries(data, newMeterValue);
	};

	// Get names of Meter Values
	var meterValueNames = Object.keys(data[0]).sort();
	meterValueNames.splice(meterValueNames.indexOf('Device_id'), 1); //remove id
	meterValueNames.splice(meterValueNames.indexOf('Timestamp'), 1);
	meterValueNames.splice(meterValueNames.indexOf('Meta_information'), 1);

	var dropdown = d3.select(".form-group")
		.insert("select", ".form-group")
		.attr("id", "meterValues_" + this.dataBaseIds)
		.on("change", dropdownChange)
		.attr("class", "form-control");

	dropdown.selectAll("option")
		.data(meterValueNames)
		.enter().append("option")
		.attr("value", function (d) {
			return d;
		})
		.text(function (d) {
			return databaseEntryDictionary[d];
		});

}

//// Help functions
//// Scroll to timeseries after click on topology node
function scrollToPage(targetID) {
	var target = $(targetID)
	if (target.length) {
		$('html, body').animate({
			scrollTop: (target.offset().top - 56)
		}, 1000, "easeInOutExpo");
		return false;
	}
}


function bootstrapStyleMultipleElements(){

	$(".form-check").addClass("pb-2");

}












