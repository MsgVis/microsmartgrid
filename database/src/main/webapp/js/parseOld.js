/*
 * Just a temporary helper for the Json File
 * We need a new structure anyway
*/
function getData(){
    let text = '{' +
        '"producer": [' +
        '{' +
        '"id": "0",' +
        '"type": "solar",' +
        '"power": "3"' +
        '}, {' +
        '"id": "2",' +
        '"type": "wind",' +
        '"power": "0"' +
        '}' +
        '],' +
        '"storage": [' +
        '{' +
        '"id": "1",' +
        '"type": "battery",' +
        '"capacity": "50",' +
        '"level": "20",' +
        '"loading": "0.8"' +
        '}' +
        '],' +
        '"consumer": [' +
        '{' +
        '"id": "3",' +
        '"type": "charging station",' +
        '"in use": "false",' +
        '"power": "0"' +
        '}, {' +
        '"id": "4",' +
        '"type": "charging station",' +
        '"in use": "true",' +
        '"power": "2"' +
        '}, {' +
        '"id": "5",' +
        '"type": "light",' +
        '"in use": "true",' +
        '"power": "0.2"' +
        '}' +
        ']' +
        '}';

    return text;
}

/*
 * Loads the data from the Json in Tables.
 * First try to test the use of JSON Files for data transfer.
 */
function parse(){
    //TODO: Get Data from a real JSON file. This just uses the JSON object above atm.
    let jsonObject = JSON.parse(getData());


    //TODO: basicly it's the same code three time. Maybe find a way to get the 3 table in the right slot without using the code 3 times.
    //Producer Table
    let table = document.getElementById("liveProducerTable");
    let tBody = document.createElement("tbody");
    let firstRow = document.createElement("tr");

    for(let key in jsonObject["producer"][0]){
        let cell = document.createElement("th");
        let cellText = document.createTextNode(key);
        cell.appendChild(cellText);
        firstRow.appendChild(cell);
    }
    tBody.appendChild(firstRow);

    for(let i = 0; i < jsonObject["producer"].length; i++){
        let row = document.createElement("tr");
        for(let key in jsonObject["producer"][i]){
            let cell = document.createElement("td");
            let cellText = document.createTextNode(jsonObject["producer"][i][key]);
            cell.appendChild(cellText);
            row.appendChild(cell);
        }

        tBody.appendChild(row);
    }
    table.appendChild(tBody);

    //Storage Table
    table = document.getElementById("liveStorageTable");
    tBody = document.createElement("tbody");
    firstRow = document.createElement("tr");

    for(let key in jsonObject["storage"][0]){
        let cell = document.createElement("th");
        let cellText = document.createTextNode(key);
        cell.appendChild(cellText);
        firstRow.appendChild(cell);
    }
    tBody.appendChild(firstRow);

    for(let i = 0; i < jsonObject["storage"].length; i++){
        let row = document.createElement("tr");
        for(let key in jsonObject["storage"][i]){
            let cell = document.createElement("td");
            let cellText = document.createTextNode(jsonObject["storage"][i][key]);
            cell.appendChild(cellText);
            row.appendChild(cell);
        }

        tBody.appendChild(row);
    }
    table.appendChild(tBody);

    //Consumer Table
    table = document.getElementById("liveConsumerTable");
    tBody = document.createElement("tbody");
    firstRow = document.createElement("tr");

    for(let key in jsonObject["consumer"][0]){
        let cell = document.createElement("th");
        let cellText = document.createTextNode(key);
        cell.appendChild(cellText);
        firstRow.appendChild(cell);
    }
    tBody.appendChild(firstRow);

    for(let i = 0; i < jsonObject["consumer"].length; i++){
        let row = document.createElement("tr");
        for(let key in jsonObject["consumer"][i]){
            let cell = document.createElement("td");
            let cellText = document.createTextNode(jsonObject["consumer"][i][key]);
            cell.appendChild(cellText);
            row.appendChild(cell);
        }

        tBody.appendChild(row);
    }
    table.appendChild(tBody);
}

/*
 * Loads the data in a SVG. Just a test to get used to D3.
 */
function parseGraph(){
    let jsonObject = JSON.parse(getData());

    // Basic Values for the SVG.
    let width = 800;
    let height = 400;

    let circleRadius = 10;
    let padding = 10;

    // Setup SVG
    let svg = d3.select("#liveSVG").attr("width", width).attr("height", height);

    // Take data for producer and calculate the spacing between the Circles.
    let producer = jsonObject["producer"];
    let producerScale = (height/producer.length > 50 ? 50 : height/producer.length);

    // Creating the Circles from the Data.
    svg
        .selectAll(".producerCircle")
        .data(producer)
        .enter()
        .append("circle")
        .attr("class", "producerCircle")
        .attr("cx", function(value, index){
            return (circleRadius*2);
        })
        .attr("cy", function(value, index){
            return (index * (producerScale + padding) + circleRadius + padding);
        })
        .attr("r", circleRadius)
        .attr("fill", "#00BB00");

    let storage = jsonObject["storage"];
    let storageScale = (width/storage.length > 50 ? 50 : width/storage.length);
    let storageMiddle = width/2 - ((storage.length - 1) * (storageScale + padding)/2);

    svg
        .selectAll(".storageCircle")
        .data(storage)
        .enter()
        .append("circle")
        .attr("class", "storageCircle")
        .attr("cx", function(value, index){
            return (index * (storageScale + padding) + storageMiddle);
        })
        .attr("cy", function(value, index){
            return (height - circleRadius*2);
        })
        .attr("r", circleRadius)
        .attr("fill", "#0000BB");

    let consumer = jsonObject["consumer"];
    let consumerScale = (height/consumer.length > 50 ? 50 : height/consumer.length);

    svg
        .selectAll(".consumerCircle")
        .data(consumer)
        .enter()
        .append("circle")
        .attr("class", "consumerCircle")
        .attr("cx", function(value, index){
            return (width - circleRadius*2);
        })
        .attr("cy", function(value, index){
            return (index * (consumerScale + padding) + circleRadius + padding);
        })
        .attr("r", circleRadius)
        .attr("fill", "#BB0000");


    // "Network Dot"
    svg.append("circle")
        .attr("class", "gridNodeCircle")
        .attr("cx", width/2)
        .attr("cy", circleRadius*2)
        .attr("r", circleRadius)
        .attr("fill", "#000000");
}



//TODO: no checks for entries. JSON-File could be empty.

// Loading Code as extra File, to avoid Errors when switching to the Livepage without reloading the Website.
// Needed because of "Singlepage" style.
parse();
parseGraph();