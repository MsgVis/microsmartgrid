////////////////////////////////////////////////////////////////////////////////////////
function calculateDiffDays(val) {
	const today = new Date(new Date().toLocaleDateString())
	const newDate = new Date(val);
	const diffTime = Math.abs(today - newDate);
	const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
	return diffDays;
}

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

	this.dropdownTopologyDictionary = {
		"R": "Zählerstand Blindenergie (R-,R+)",
		"A": "Zählerstand Wirkenergie (A-,A+)",
		"I_r": "Strom Phase 1",
		"I_s": "Strom Phase 2",
		"I_t": "Strom Phase 3",
		"I_avg": "Strom Durchschnitt",
		"U_r": "Spannung Phase 1",
		"U_s": "Spannung Phase 2",
		"U_t": "Spannung Phase 3",
		"U_avg": "Spannung Durchschnitt",
		"Q_r": "Blindleistung Phase 1",
		"Q_s": "Blindleistung Phase 2",
		"Q_t": "Blindleistung Phase 3",
		"Q_total": "Blindleistung Gesamt",
		"P_r": "Wirkleistung Phase 1",
		"P_s": "Wirkleistung Phase 2",
		"P_t": "Wirkleistung Phase 3",
		"P_total": "Wirkleistung Gesamt",
		"S_r": "Scheinleistung Phase 1",
		"S_s": "Scheinleistung Phase 2",
		"S_t": "Scheinleistung Phase 3",
		"S_total": "Scheinleistung Gesamt",
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

	this.unitLibrary = {
		"A": "J",
		"P_r": "W",
		"P_s": "W",
		"P_t": "W",
		"P_total": "W",
		"S_r": "VA",
		"S_s": "VA",
		"S_t": "VA",
		"S_total": "VA",
		"I_r": "A",
		"I_s": "A",
		"I_t": "A",
		"I_avg": "A",
		"Frequency": "Hz",
		"R": "kvarh",
		"Q_r": "var",
		"Q_s": "var",
		"Q_t": "var",
		"Q_total": "var",
		"U_r": "V",
		"U_s": "V",
		"U_t": "V",
		"U_avg": "V",
	}

	this.chartTopologyDiv = document.getElementById("liveSVGCard");


	debugger;
	document.addEventListener("DOMContentLoaded", onLoadFunction);
	$("input[name='date']").val(new Date().toLocaleDateString());

	/// Function to plot images again on window resize
	function onLoadFunction(e) {
		plotTopology(1);
		drawDropDownMenu();
		window.addEventListener("resize", function(){
			let val = $("input[name='date']").val();
			const diffDays = calculateDiffDays(val);
			plotTopology(diffDays)
		});
	}

	/// Get Tooltips to wait for data load
	setTimeout(function () {
			$('[data-toggle="tooltip"]').tooltip();
		}, 500
	);

	// To lazy to add bootstrap classes to all elements in index html. This is the helper function.
	bootstrapStyleMultipleElements();

	$(document).on('change', 'input[name="date"]', function (event) {
		let val = $(this).val();
		const diffDays = calculateDiffDays(val);
		plotTopology(diffDays);
	});

}

init();


////////////////////////////////////////////////////////////////////////////////////////
// ----------------------Handlers for Loading Data from database--------------------------------
function getMeterData(P1D,P0D,id) {
	var url = "http://localhost:4720/readings?since="+ P1D + "&until="+ P0D+ "&id=" + id;
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

function getFlowValues(diffDays) {
	diffDays = diffDays + 1;
	var url = "http://localhost:4720/latest?cutoff=P"+diffDays+"D";
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
function plotTopology(diffDays) {

	// Delete old chart
	d3.selectAll("#liveSVG > *").remove();
	d3.selectAll("#form-group-topology > *").remove();

	let chartTopologyDiv = this.chartTopologyDiv;


	d3.json('json/DAI_Smart_Micro_Grid.json').then(function (data) {


		getFlowValues(diffDays).then(function (flowData) {
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
				.attr("transform", `translate(${ width/7 }, ${root.dx - x0})`);


			let defaulOpacity = 10;
			let linkListFinal = addCornersToLinks(root, flowData, defaulOpacity, false);
			let linkListFinalDashes = addCornersToLinks(root, flowData, defaulOpacity, true);


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
				.attr("stroke-width", function(d){
					let weight = 0;
					let extraOpacity = 10;
					if(d.hasNode) {
						Object.keys(d).forEach(function (key) {
							var element = d[key];
							if (element.node) {
								if(element.totalFlow!=0) {
									weight += (Math.abs(element.flowInNetwork) + Math.abs(element.flowOutNetwork)) / element.totalFlow;
									if (weight > 1) {
										weight = 1.4;
									}
								}else{
									weight = 0;
								}
							}
						})
						extraOpacity = weight * 10;
					}
					return defaulOpacity + extraOpacity;
				});

			const dashes = createDashes(g, linkListFinalDashes, defaulOpacity);

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
				.attr("r", function(d){
					if(d.data.id === 0){
						return 13;
					}else{
						return circleRadius
					}
				});

			node.append("image")
				.attr("xlink:href", function (d) {
					return "icons/" + iconLibrary[d.data.subtype];
				})
				.attr("x", -(circleRadius * 1.2) / 2)
				.attr("y", -(circleRadius * 1.2) / 2)
				.attr("width", circleRadius * 1.2)
				.attr("height", circleRadius * 1.2)
				.style("fill", "#ffffff");

			createFlowLabel(node, circleRadius);


			drawPlayButton(flowAnimate, dashes, recusiveAddingOfDashes);



		})

	})
}


//// Draw GUI elements
// This draws Play svg
function drawPlayButton(flowAnimate, dashes, setFlowDash) {
	if($("#playButton")[0].control.checked){
		let svg = d3.select("#liveSVG");
		svg.selectAll(".nodeFlowLabels")
			.style("opacity", 0)
			.transition()
			.duration(100)
			.style("opacity", 1);
		flowAnimate();
	}else{

		dashes[0].transition();
		dashes[0]
			.style("opacity", 0)
			.each(function (d) {
				setFlowDash.call(this, d);
			})

		dashes[1].transition();
		dashes[1]
			.style("opacity", 0)
			.each(function (d) {
				setFlowDash.call(this, d);
			})
	}

	$("#playButton").on('change', function() {
		if ($(this)[0].control.checked) {
			$(this).val('true');
			let svg = d3.select("#liveSVG");
			svg.selectAll(".nodeFlowLabels")
				.style("opacity", 0)
				.transition()
				.duration(100)
				.style("opacity", 1);
			flowAnimate();
		}
		else {
			$(this).val('false');
			//cancel all transitions by making a new one
			let svg = d3.select("#liveSVG");
			svg.selectAll(".nodeFlowLabels")
				.style("opacity", 1)
				.transition()
				.duration(100)
				.style("opacity", 0);

			dashes[0].transition();
			dashes[0]
				.style("opacity", 0)
				.each(function (d) {
					setFlowDash.call(this, d);
				})

			dashes[1].transition();
			dashes[1]
				.style("opacity", 0)
				.each(function (d) {
					setFlowDash.call(this, d);
				})
		}});
}

function createFlowLabel(node, circleRadius) {
	/// Labels für Flow
	let text = node.append("text")
		.text(null);

	let twoLines = false;

	function labelText(d, direction) {
		// Fill with flow data values and arrows
		if(direction === "in") {
			return "In: " + d.flowInNetwork + " " +this.unitLibrary[d.flowValue];
		}else{
			return "Out: " + d.flowOutNetwork + " " + this.unitLibrary[d.flowValue];
		}
	}


	let lineHeight = 1.1
	let y = ".35em";

	let unitLibrary = this.unitLibrary;

	text.append("tspan")
		.attr("dx", function (d) {
			if (d.data.id === 8) { //This is root node
				return -(circleRadius + 6);
			} else {
				return circleRadius + 6;
			}
		})
		.attr("text-anchor", function (d) {
			if (d.data.id === 8) { //This is root node
				return "end";
			} else {
				return "start";
			}
		})
		.attr("x", 0)
		.attr("y", y)
		.attr("dy", function (d) {
			if (typeof d.flowInNetwork != "undefined" && typeof d.flowOutNetwork != "undefined") {
				if (d.flowInNetwork === d.flowOutNetwork) {
					return ".35em";
				} else {
					return "-.60em";
				}
			}

		})
		.attr("class", function (d) {
			if (d.data.id != 0) {
				return "nodeFlowLabels";
			}
		})
		.style("opacity", 0)
		.text(function (d) {
			if (typeof d.flowInNetwork != "undefined" && typeof d.flowOutNetwork != "undefined") {
				if(d.flowInNetwork === d.flowOutNetwork) {
					d.twoLines = false;
					if(d.flowInNetwork === 0 && d.flowOutNetwork === 0){
						return "0 " + unitLibrary[d.flowValue];
					}else if (d.data.direction == "PLUS") {
						return labelText(d, "out");
					} else if (d.data.direction == "MINUS") {
						return labelText(d, "in");
					}
				}else{
					d.twoLines = true;
					return labelText(d, "in");
				}
			}
		});

		text.append("tspan")
			.attr("y", y)
			.attr("dx", function (d) {
				if (d.data.id === 8) { //This is root node
					return -(circleRadius + 6);
				} else {
					return circleRadius + 6;
				}
			})
			.attr("text-anchor", function (d) {
				if (d.data.id === 8) { //This is root node
					return "end";
				} else {
					return "start";
				}
			})
			.attr("x", 0)
			.attr("dy", function (d) {
				return ".60em";
			})
			.attr("class", function (d) {
				if (d.data.id != 0) {
					return "nodeFlowLabels";
				}
			})
			.style("opacity", 0)
			.text(function (d) {
				if(d.twoLines === true){
					return labelText(d, "out");
				}
			});




}

function createDashes(g, linkListFinal, defaulOpacity) {
	/// Moving dashes svg for Flow
	const dashes = _createDashes(g, linkListFinal, defaulOpacity, -1, "flow",false);
	const dashesBack = _createDashes(g, linkListFinal, defaulOpacity, 1, "flowBack",true);

	return [dashes,dashesBack];
}

function _createDashes(g, linkListFinal, defaulOpacity, dashOffset, className,backFlow) {
	const dashes = g.append("g")
		.attr("fill", "none")
		.selectAll("path")
		.data(linkListFinal)
		.join("path")
		.attr("d", d3.linkHorizontal()
			.x(d => d.y)
			.y(d => d.x))
		.attr("fake", function(d){d.backFlow =  backFlow})
		.attr("stroke-width", function (d) {
			return _createDashWidth(defaulOpacity, d);
		})
		.attr("stroke-opacity", 3)
		.attr("stroke", function(d) {
			if (d.negativeFlow) {
				return "#ffe7ce";
			} else {
				return "#FFFFE0";
			}
		})
		.attr("class", className)
		.attr("id", "dash")
		.attr("transform", function (d) {
			return _repositionLine(d, dashOffset, defaulOpacity);
		})
		.each(recusiveAddingOfDashes);
	return dashes;
}

function _createDashWidth(defaulOpacity, d) {
	let width = (defaulOpacity );
	let weight = 0;
	let weightIn = 0;
	let weightOut = 0;


	if(d.hasNode) {
		Object.keys(d).forEach(function (key) {
			let data = d[key];
			let totalFlow = data.totalFlow;
			if (data.flowInNetwork) {
				weightIn = (Math.abs(data.flowInNetwork) / totalFlow);
				if(weightIn> 1){
					weightIn = Math.abs(data.flowInNetwork)/(Math.abs(data.flowInNetwork)+Math.abs(data.flowOutNetwork));
				}
			}
			if (data.flowOutNetwork) {
				weightOut = (Math.abs(data.flowOutNetwork) / totalFlow);
				if(weightOut> 1){
					weightOut = Math.abs(data.flowOutNetwork)/(Math.abs(data.flowInNetwork)+Math.abs(data.flowOutNetwork));
				}
			}
		})
		if(d.oneWay){
			weight = weightIn + weightOut;
		}else if(d.backFlow){
			weight = weightOut*0.7;
		} else{
			weight = weightIn*0.7;
		}
	}else{
		weight = 0.2; // Kante ohne Knoten
	}




	/// Korrektur von zu kleinen Werten
	if (weight < 0.1 & weight != 0) {
		weight = 0.1;
	}
	return width * weight;
}

function _repositionLine(d, dif, defaulOpacity) {

	if(!d.oneWay) {

		let weight = 0;
		if(d.hasNode) {
			Object.keys(d).forEach(function (key) {
				var element = d[key];
				if (element.node) {
					if(d.backFlow) {
						weight = Math.abs(element.flowInNetwork) / element.totalFlow;
					}else{
						weight =  Math.abs(element.flowOutNetwork) / element.totalFlow;
					}
					if(weight > 1){
						weight = 1;
					}
				}
			})
			if(weight < 0.1){
				dif = dif
			}else{
				dif = dif * 4;
			}
		} else {
			dif = dif * 2;
		}

		let dx = Math.abs(d.target.x - d.source.x);
		let dy = Math.abs(d.target.y - d.source.y);
		if (dx === 0) {
			dy = dif;
		} else {
			dx = dif;
		}
		return "translate(" + dx + "," + dy + ")";
	}

}

//Flow Animation of dashes
function flowAnimate(nodeData) {
	let svg = d3.select("#liveSVG");
	let links = svg.selectAll(".flow")
		.attr("stroke-dashoffset", function(d){
			if(d.hasNode){
				return _setStrokeOffset(d, 0, 999);
			}else{
				return _setStrokeOffset(d, 0, 500);
			}

		})
		.style("opacity", 0.7)
		.transition()
		.duration(50000)
		.ease(d3.easeLinear)
		.attr("stroke-dashoffset", function(d) {
			if(d.hasNode){
				return _setStrokeOffset(d, 999,0);
			}else{
				return _setStrokeOffset(d, 500,0);
			}
		})
		.on("end", function() {
			flowAnimate();
		});

	let linksBack = svg.selectAll(".flowBack")
		.attr("stroke-dashoffset", function(d){
			if(d.hasNode){
				return _setStrokeOffset(d, 999,0);
			}else{
				return _setStrokeOffset(d, 500,0);
			}
		})
		.style("opacity", function(d){
			if(d.oneWay){
				return 0;
			}else{
				return 0.7;
			}
		})
		.transition()
		.duration(30000)
		.ease(d3.easeLinear)
		.attr("stroke-dashoffset", function(d) {
			if(d.hasNode){
				return _setStrokeOffset(d, 0, 999);
			}else{
				return _setStrokeOffset(d, 0, 500);
			}
		})
		.on("end", function() {
			flowAnimate();
		});


}

function _setStrokeOffset(d, offsetOne, offsetTwo) {
	if (d.target.node && d.target.oneWay) {
			if (d.target.data.direction == "MINUS") {
				return offsetOne;
			} else {
				return offsetTwo;
			}
	} else if (d.source.node) {
		if( d.source.oneWay) {
			if (d.source.data.direction == "MINUS") {
				return offsetTwo;
			} else {
				return offsetOne;
			}
		} else{
			return offsetTwo;
		}
	} else {
		return offsetOne;
	}
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

// Add Live data to Nodes and get value in dropdown menu
function getFlowDisplayOption(flowData, currentLink) {
	let liveSelectedValue = document.getElementById("select-topology").value;//$('input[name=flowSelection]:checked').val();
	currentLink.source.flowValue = liveSelectedValue;
	currentLink.target.flowValue = liveSelectedValue;
	return liveSelectedValue;
}

function setLiveDataValues(currentLink, flowData, liveSelectedValue) {


	// Add width property to Source and Target
	let sourceID = currentLink.source.data.id;
	let targetID = currentLink.target.data.id;

	Object.keys(currentLink).forEach(function(key){

		let element = currentLink[key];

		// Add width property to Source and Target
		let iD = element.data.id;

		if (iD != 0) {


			element.node = true;
			let returnedData = flowData.filter(function (number) {
				return number.Device_id === iD
			});
			flowDataToObjectVal(liveSelectedValue, returnedData, element);
			/// Get total amount to calculated percentages
			let totalAmount = 0;
			flowData.forEach(function (number) {
				if (liveSelectedValue === "A" || liveSelectedValue === "R") {
					if(number.Device_id != "8") {
						let val1 = Math.abs(number[liveSelectedValue + "_minus"]);
						let val2 = Math.abs(number[liveSelectedValue + "_plus"]);
						if(val1) {
							totalAmount += val1;
						}
						if(val2){
							totalAmount += val2;
						}
					}
				}else {
					if(Math.abs(number[liveSelectedValue])){
						totalAmount += Math.abs(number[liveSelectedValue]);
					}
				}
			});
			if(totalAmount) {
				element.totalFlow = totalAmount;
			}else{
				element.totalFlow = 0;
			}

		}

		if(element.node){
			if(element.flowOutNetwork === element.flowInNetwork){
				element.oneWay = true;
			} else{
				element.oneWay = false;
			}
		}

	})
}

function flowDataToObjectVal(liveSelectedValue, data, currentLink) {
	let selectionIn = "";
	let selectionOut = "";

	/// We have two cases where we have two different values for direction or not.
	/// if direction "direction": "BOTH",
	if (liveSelectedValue === "A" || liveSelectedValue === "R") {
		selectionOut = liveSelectedValue + "_plus";
		selectionIn = liveSelectedValue + "_minus";
	} else {
		selectionIn = liveSelectedValue;
		selectionOut = liveSelectedValue
	}

	let valueIn = data[0][selectionIn];
	let valueOut = data[0][selectionOut];

	// if(valueIn) {
		currentLink.flowInNetwork = valueIn;
	// }else{
	// 	currentLink.flowInNetwork = 0;
	// }
	// if(valueOut){
		currentLink.flowOutNetwork = valueOut;
	// }else{
	// 	currentLink.flowOutNetwork = 0;
	// }

	if (valueIn === null) {
		currentLink.flowInNetwork = 0;
	}
	if (valueIn <0) {
		currentLink.negativeFlow = true;
	}

	if (valueOut === null) {
		currentLink.flowOutNetwork = 0;
	}
	if (valueOut <0) {
		currentLink.negativeFlow = true;
	}



}

// This gives a more electrical engineering design.
function addCornersToLinks(root, flowData = flowData, defaultOpacity = defaultOpacity, isDash) {

		// Makes <g> elements for links
		let linkList = root.links();
		var linkListFinal = [];
		for (var i = 0; i < linkList.length; i++) {
			let currentLink = linkList[i];
			if (currentLink.target) {

				let liveSelectedValue = getFlowDisplayOption(flowData, currentLink);
				setLiveDataValues(currentLink, flowData, liveSelectedValue);

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
				let edgePointIn = {}
				if(isDash) {
					edgePointIn = {
						x: currentLink.target.x,
						y: currentLink.source.y + distanceY
					};
				}else{
					edgePointIn = {
						x: currentLink.target.x + paddingEdge,
						y: currentLink.source.y + distanceY
					};
				}
				let edgePointOut = {}
				if(isDash){
					edgePointOut = {
						x: currentLink.target.x,
						y: currentLink.source.y + distanceY
					};
				}else{
					edgePointOut = {
						x: currentLink.target.x,
						y: currentLink.source.y + distanceY - Math.abs(paddingEdge*2)
					};
				}
				let firstLink = {
					source: currentLink.source,
					target: centerPoint,
					hasNode:true,
					oneWay: false
				};
				let secondLink = {
					source: centerPoint,
					target: edgePointIn,
					hasNode:false,
					oneWay: false
				};
				let thirdLink = {
					source: edgePointOut,
					target: currentLink.target,
					hasNode:true,
					oneWay: false
				};

				if(currentLink.source.data.id === 0) {
					firstLink.hasNode = false;
				}
				if(currentLink.target.data.id === 0){
					thirdLink.hasNode = false;
				}

				if(currentLink.source.oneWay){
					firstLink.oneWay = true;
				}
				if(currentLink.target.oneWay){
					thirdLink.oneWay = true;
				}
				if(currentLink.source.negativeFlow){
					firstLink.negativeFlow = true;
				}
				if(currentLink.target.negativeFlow){
					thirdLink.negativeFlow = true;
				}

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
		.attr('stroke-dasharray', function(d){
			if(d.hasNode){
				return 6
			}else{
				return 3
			}
		})
		.attr('stroke-dashoffset', function(d){
			return 999;
		}) //set to minus for reverse
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
	d3.selectAll("#form-group-timeseries > *").remove();
	if (id != 0) {
		getMeterData("PT4H","P0D",id).then(function (data) {
			plotTimeSeries(data, Object.keys(data[0]).sort()[0],initial);
			drawDropDownMenuTimeseries(data);
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

function drawDropDownMenuTimeseries(data) {

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

	var dropdown = d3.select("#form-group-timeseries")
		.insert("select", "#form-group-timeseries")
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

function drawDropDownMenu() {


	databaseTopologyDictionary = this.dropdownTopologyDictionary;
	///// Create Options:
	// Handler for dropdown value change
	var dropdownChange = function () {
		debugger;
		let val = $("input[name='date']").val();
		const diffDays = calculateDiffDays(val);
		plotTopology(diffDays);
	};

	// Get names of Meter Values
	var meterValueNames = Object.keys(databaseTopologyDictionary);

	var dropdown = d3.select("#select-topology")
		.on("change", dropdownChange);

	dropdown.selectAll("option")
		.data(meterValueNames)
		.enter().append("option")
		.attr("value", function (d) {
			return d;
		})
		.text(function (d) {
			return databaseTopologyDictionary[d];
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












