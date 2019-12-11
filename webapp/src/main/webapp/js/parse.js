function getData(){
    let text = {
        "id": "0",
        "type": "powerGrid",
        "subtype": "powerGrid",
        "databaseId": "PPG",
        "depth": 0,
        "icon": "powerGrid.png",
        "children": [{
            "id": "1",
            "type": "consumer",
            "subtype": "light",
            "databaseId": "KA2013",
            "depth": "1",
            "icon": "light.png",
            "children": []
        }, {
            "id": "2",
            "type": "producer",
            "subtype": "solarPanel",
            "databaseId": "sunfun",
            "depth": "1",
            "icon": "solarPanel.png",
            "children": []
        }, {
            "id": "3",
            "type": "electricMeter",
            "subtype": "electricMeter",
            "databaseId": "Pald201",
            "depth": "1",
            "icon": "electricMeter.png",
            "children": [{
                "id": "4",
                "type": "consumer",
                "subtype": "chargingStation",
                "databaseId": "loader42",
                "depth": "2",
                "icon": "chargingStation.png",
                "children": []
            }, {
                "id": "5",
                "type": "consumer",
                "subtype": "chargingStation",
                "databaseId": "charger42",
                "depth": "2",
                "icon": "chargingStation.png",
                "children": []
            }]
        }, {
            "id": "6",
            "type": "electricMeter",
            "subtype": "electricMeter",
            "databaseId": "JASD0213",
            "depth": "1",
            "icon": "electricMeter.png",
            "children": [{
                "id": "7",
                "type": "storage",
                "subtype": "battery",
                "databaseId": "battery123",
                "depth": "2",
                "icon": "battery.png",
                "children": []
            }]
        }, {
            "id": "8",
            "type": "producer",
            "subtype": "windmill",
            "databaseId": "Cloudy202",
            "depth": "1",
            "icon": "windmill.png",
            "children": []
        }, {
            "id": "9",
            "type": "producer",
            "subtype": "solarPanel",
            "databaseId": "sunnyDay3",
            "depth": "1",
            "icon": "solarPanel.png",
            "children": []
        }, {
            "id": "10",
            "type": "electricMeter",
            "subtype": "electricMeter",
            "databaseId": "ILikeCounting",
            "depth": "1",
            "icon": "electricMeter.png",
            "children": [{
                "id": "11",
                "type": "electricMeter",
                "subtype": "electricMeter",
                "databaseId": "1plus1is2",
                "depth": "2",
                "icon": "electricMeter.png",
                "children": [{
                    "id": "12",
                    "type": "producer",
                    "subtype": "windmill",
                    "databaseId": "spinningAround",
                    "depth": "3",
                    "icon": "windmill.png",
                    "children": []
                }, {
                    "id": "13",
                    "type": "storage",
                    "subtype": "battery",
                    "databaseId": "batman",
                    "depth": "3",
                    "icon": "battery.png",
                    "children": []
                }]
            }, {
                "id": "14",
                "type": "producer",
                "subtype": "solarPanel",
                "databaseId": "sunnysun",
                "depth": "2",
                 "icon": "solarPanel.png",
                "children": []
            }]
        }]
    };

    return text;
}

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

function tree(data, width, nodeDistanceX){
    const root = d3.hierarchy(data);
    // Set initial position of root node
    root.dx = nodeDistanceX;
    root.dy = width / (root.height + 1);
    return d3.tree().nodeSize([nodeDistanceX, root.dy])(root);
}

function parseHistorySVG(dataBaseIds){

    let margin = {top: 10, right: 30, bottom: 50, left: 100};
    let width = 1000 - margin.left - margin.right;
    let height = 500 - margin.top - margin.bottom;

    let svg = d3.select("#historySVG")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform",
            "translate(" + margin.left + "," + margin.right + ")");

    if(dataBaseIds.length == 0){ return; }
    let data = dataBaseIds.map(x => getHistoryData(x));
    let sumstat = d3.nest()
        .key(function(d){ return d.id })
        .entries(data.flat());
    console.log(sumstat);

    let x = d3.scaleTime()
        .domain(d3.extent(data, function(d){
            return (d["time"]);
        }))
        .range([0, width]);
    svg.append("g")
        .attr("transform", "translate(0," + height + ")")
        .call(d3.axisBottom(x));

    let y = d3.scaleLinear()
        .domain([0, d3.max(data, function(d){
            return +d["value"];
        })])
        .range([height, 0]);
    svg.append("g")
        .call(d3.axisLeft(y));

    let res = sumstat.map(function(d){ return d.key });
    let color = d3.scaleOrdinal()
        .domain(res)
        .range(["#FF0000"]);

    svg.selectAll(".line")
        .data(sumstat)
        .enter()
            .append("path")
            .datum(sumstat)
            .attr("fill", "none")
            .attr("stroke", function(d){ return color(d.key) })
            .attr("stroke-width", 1.5)
            .attr("d", function(d) {
                console.log(d);
                d3.line()
                    .x(function (d) { return x((d["time"])) })
                    .y(function (d) { return y(d["value"]) })
            });
}

var shownHistoryDbIds = [];
function parseHistorySVGHelper(d){
    if(shownHistoryDbIds.includes(d.data.databaseId)){ //already shown -> remove from graph
        shownHistoryDbIds.splice(shownHistoryDbIds.indexOf(d.data.databaseId), 1);
    } else { //not shown -> add to graph
        shownHistoryDbIds.push(d.data.databaseId);
    }
    parseHistorySVG(shownHistoryDbIds);
}

function parse() {
    let jsonObject = getData(); //TODO: Nur temporär als Hilfe.
    let typeDictionary = {"powerGrid": "Power Grid",
        "light": "Light",
        "solarPanel": "Solar Panel",
        "electricMeter": "Electric Meter",
        "windmill": "Windmill",
        "chargingStation": "Charging Station",
        "battery": "Battery"};

    let width = 1000;
    let height = 1000;

    let circleRadius = 18;
    let nodeDistanceX = 45;
    let padding = 10;


    const root = this.tree(jsonObject, width, nodeDistanceX);

    let x0 = Infinity;
    let x1 = -x0;
    root.each(d => {
        if(d.x > x1) x1 = d.x;
        if(d.x < x0) x0 = d.x;
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
    for(var i = 0; i < 14 ;i ++){
        let currentLink = linkList[i];
        if(currentLink.target){
            //Add new Links
            let distanceX = Math.abs((currentLink.target.x - currentLink.source.x)/2);
            let distanceY = Math.abs((currentLink.target.y - currentLink.source.y)/2);
            // Vorzeichen bei unterschiedlichen Richtungen
            if(Math.sign((currentLink.target.x - currentLink.source.x)) === 0){
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
            if(linkList.indexOf(firstLink) === -1) {
                linkListFinal.push(firstLink);
            }
            if(linkList.indexOf(secondLink) === -1) {
                linkListFinal.push(secondLink);
            }
            if(linkList.indexOf(thirdLink) === -1) {
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
        .attr("transform", d=> `translate(${d.y},${d.x})`)
        .attr("data-html","true")
        .attr("data-toggle","tooltip")
        .attr("data-placement","right")
        .attr("title",function(d){
            return "<strong>" + typeDictionary[d.data.subtype] + ":</strong>\n" + d.data.databaseId;

        })
        .on("click", function(d){
            parseHistorySVGHelper(d);
        });

    node.append("circle")
        // .attr("fill", d => d.children ? "#7da1b1" : "#0f1617")
        .attr("fill",function(d){
            return setNodeColor(d);
        })
        .attr("data-html","true")
        .attr("r", circleRadius);

    node.append("image")
        .attr("xlink:href", function(d) { return "icons/"+d.data.icon; }) //TODO: Wird vermutlich nur lokal funktionieren.
        .attr("x", "-12px")
        .attr("y", "-12px")
        .attr("width", "24px")
        .attr("height", "24px")
        .style("fill","#ffffff");

}

// This function sets the colors for the node subtypes.
// TODO: Eventuell ins JSON übertragen. Sollte Configurierbar sein.
function setNodeColor(d){
    var color;
    if(d.data.type === "powerGrid"){
        color = "#7a0606";
    }else if(d.data.type === "producer"){
        color = "#1abc9c";
    }else if(d.data.type === "electricMeter"){
        color = "#558dca";
    }else if(d.data.type === "consumer"){
        color = "#022449";
    }else if(d.data.type === "storage"){
        color = "#b8dce6";
    }
    return color;
}





parse();


