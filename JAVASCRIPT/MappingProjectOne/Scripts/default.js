//Author: Nick McRae
//Purpose: Script that allows for loading of map, adding of pins, and map optimization
//Revision history:
//Jan. 26/14: Initial implementation (got map working)
//Jan. 28/14: Got ajax working, returning JSON string
//Jan. 30/14: Got pins to display on map with a few bugs
//Feb. 1/14: Fixed bugs and added optimize, now should meet all specs, also checked for style and added comments
//Feb. 26/14: Created new UI overlay
//Feb. 27/14: Updated logic to allow user to search for either booze, bars, or buffets categories, filtered search results based on that criteria
//Feb. 28/14: Added info-boxes to pins, wrote code to print data about the pins in the right pane
//Mar. 16/14: Refactored the unnecessary construction of multiple functions that all do essentially the same thing into one large function that accomplishes the same thing as
//            the multiple functions. Also added responsive web design which includes adjustments for a smaller, but large screen, portrait mode, and landscape mode. This was
//            accomplished by creating multiple stylesheets, each with an associated media query. 

var map;
var factualJson;
var pinInfobox;

function boot() {
    Microsoft.Maps.loadModule('Microsoft.Maps.Overlays.Style', { callback: getMap });    
}

function getMap() {    
    map = new Microsoft.Maps.Map($gel("bingMap"), {
        credentials: getKey(),
        customizeOverlays: true,
        enableClickableLogo: true,
        enableSearchLogo: true,
        showDashboard: true,
        showBreadcrumb: true,
        showCopyright: true,
        zoom: 10,
        labelOverlay: Microsoft.Maps.LabelOverlay.hidden
    });        

    setGeoLocation();   

    window.onresize = resizeWin;
    resizeWin();
}

function optimizeMap() {    
    var locArray = [];
    var ltlng;
    var latLong = {};
    for (var i = 0; i < factualJson.response.data.length; ++i) {

        latLong['latitude'] = factualJson.response.data[i].latitude;
        latLong['longitude'] = factualJson.response.data[i].longitude;        
        
        ltlng = new Microsoft.Maps.Location(latLong['latitude'], latLong['longitude']);        
        locArray.push(ltlng);
    }

    var bestview = Microsoft.Maps.LocationRect.fromLocations(locArray);
    map.setView({ bounds: bestview });    
}

function addPin() {

    map.entities.clear();

    var barsImg = "images/bars.jpg";
    var foodImg = "images/food.jpg";
    var liquorImg = "images/liquor.jpg";
    var imgContainer;
    var latLong = {};
    var name;
    var locArray = [];    
    var website;

    document.getElementById("spewOutput").innerHTML = "";

    for (var i = 0; i < factualJson.response.data.length; ++i) {

        var categoryFlag = false;

        if (typeof (factualJson.response.data[i].category_labels) == "undefined") {
            continue;
        }

        var booze = document.getElementById("boozeCheck").checked;
        var bars = document.getElementById("barsCheck").checked;
        var buffets = document.getElementById("buffetsCheck").checked;

        if (!booze && !bars && !buffets) {
            alert("Please check a category");
        }
        else if (booze && bars && buffets) {
            for (var k = 0; k < factualJson.response.data[i].category_labels.length; ++k) {
                for (var j = 0; j < factualJson.response.data[i].category_labels[k].length; ++j) {
                    if (factualJson.response.data[i].category_labels[k][j] == "Beer, Wine and Spirits"
                        || factualJson.response.data[i].category_labels[k][j] == "Buffets"
                        || factualJson.response.data[i].category_labels[k][j] == "Bars") {
                        categoryFlag = true;

                        if (factualJson.response.data[i].category_labels[k][j] == "Beer, Wine and Spirits") {
                            imgContainer = liquorImg;
                        }
                        else if (factualJson.response.data[i].category_labels[k][j] == "Buffets") {
                            imgContainer = foodImg;
                        }
                        else if (factualJson.response.data[i].category_labels[k][j] == "Bars") {
                            imgContainer = barsImg;
                        }
                    }
                }
            }
        }
        else if (booze && bars && !buffets) {
            for (var k = 0; k < factualJson.response.data[i].category_labels.length; ++k) {
                for (var j = 0; j < factualJson.response.data[i].category_labels[k].length; ++j) {
                    if (factualJson.response.data[i].category_labels[k][j] == "Beer, Wine and Spirits"
                        || factualJson.response.data[i].category_labels[k][j] == "Bars") {
                        categoryFlag = true;

                        if (factualJson.response.data[i].category_labels[k][j] == "Beer, Wine and Spirits") {
                            imgContainer = liquorImg;
                        }
                        else if (factualJson.response.data[i].category_labels[k][j] == "Bars") {
                            imgContainer = barsImg;
                        }
                    }
                }
            }
        }
        else if (!booze && bars && buffets) {
            for (var k = 0; k < factualJson.response.data[i].category_labels.length; ++k) {
                for (var j = 0; j < factualJson.response.data[i].category_labels[k].length; ++j) {
                    if (factualJson.response.data[i].category_labels[k][j] == "Bars"
                        || factualJson.response.data[i].category_labels[k][j] == "Buffets") {
                        categoryFlag = true;

                        if (factualJson.response.data[i].category_labels[k][j] == "Buffets") {
                            imgContainer = foodImg;
                        }
                        else if (factualJson.response.data[i].category_labels[k][j] == "Bars") {
                            imgContainer = barsImg;
                        }
                    }
                }
            }

        }
        else if (booze && !bars && buffets) {
            for (var k = 0; k < factualJson.response.data[i].category_labels.length; ++k) {
                for (var j = 0; j < factualJson.response.data[i].category_labels[k].length; ++j) {
                    if (factualJson.response.data[i].category_labels[k][j] == "Beer, Wine and Spirits"
                        || factualJson.response.data[i].category_labels[k][j] == "Buffets") {
                        categoryFlag = true;

                        if (factualJson.response.data[i].category_labels[k][j] == "Beer, Wine and Spirits") {
                            imgContainer = liquorImg;
                        }
                        else if (factualJson.response.data[i].category_labels[k][j] == "Buffets") {
                            imgContainer = foodImg;
                        }
                    }
                }
            }
        }
        else if (booze && !bars && !buffets) {
            for (var k = 0; k < factualJson.response.data[i].category_labels.length; ++k) {
                for (var j = 0; j < factualJson.response.data[i].category_labels[k].length; ++j) {
                    if (factualJson.response.data[i].category_labels[k][j] == "Beer, Wine and Spirits") {
                        categoryFlag = true;
                        imgContainer = liquorImg;
                    }
                }
            }
        }
        else if (!booze && bars && !buffets) {
            for (var k = 0; k < factualJson.response.data[i].category_labels.length; ++k) {
                for (var j = 0; j < factualJson.response.data[i].category_labels[k].length; ++j) {
                    if (factualJson.response.data[i].category_labels[k][j] == "Bars") {
                        categoryFlag = true;
                        imgContainer = barsImg;
                    }
                }
            }
        }
        else if (!booze && !bars && buffets) {
            for (var k = 0; k < factualJson.response.data[i].category_labels.length; ++k) {
                for (var j = 0; j < factualJson.response.data[i].category_labels[k].length; ++j) {
                    if (factualJson.response.data[i].category_labels[k][j] == "Buffets") {
                        categoryFlag = true;
                        imgContainer = foodImg;
                    }
                }
            }
        }

        if (!categoryFlag) {
            continue;
        }

        latLong['latitude'] = factualJson.response.data[i].latitude;
        latLong['longitude'] = factualJson.response.data[i].longitude;
        name = factualJson.response.data[i].name;
        var loc = new Microsoft.Maps.Location(latLong['latitude'], latLong['longitude']);

        locArray.push(loc);

        if (typeof factualJson.response.data[i].website != 'undefined') {
            website = factualJson.response.data[i].website;
        }

        var pin = new Microsoft.Maps.Pushpin(loc, {
            icon: imgContainer,
            anchor: new Microsoft.Maps.Point(8, 8),
            draggable: true,
            width: 48,
            height: 48
        });
        
        pin.title = name;
        pin.id = 'pin' + i;
        pin.description = website;
        Microsoft.Maps.Events.addHandler(pin, 'click', displayInfobox);
        map.entities.push(pin);

        printInfo(factualJson.response.data[i]);
    }

    if (locArray.length != 0) {
        var bestview = Microsoft.Maps.LocationRect.fromLocations(locArray);
        map.setView({ bounds: bestview });
    }
}

function centreMap() {
    setGeoLocation();
}

function displayInfobox(e) {

    pinInfobox = new Microsoft.Maps.Infobox(e.target.getLocation(),
    {
        title: e.target.title,
        description: e.target.description,
        visible: true,
        offset: new Microsoft.Maps.Point(0, 15)
    });
    pinInfobox.setLocation(e.target.getLocation());
    map.entities.push(pinInfobox);
}

function displayName(e) {

    document.getElementById("spewOutput").innerHTML = "";

    if (this.target.id  != -1) {        
        document.getElementById("spewOutput").innerHTML = this.target.title;
    }
}

function printInfo(obj) {

    if (typeof obj.name != "undefined") {
        document.getElementById("spewOutput").innerHTML += obj.name;
    }

    document.getElementById("spewOutput").innerHTML += "</br>";

    if (typeof obj.website != "undefined") {
        document.getElementById("spewOutput").innerHTML += "<a href=" + obj.website +">" + obj.website + "</a>";
    }

    document.getElementById("spewOutput").innerHTML += "</br>";

    if (typeof obj.address != "undefined") {
        document.getElementById("spewOutput").innerHTML += obj.address;
    }

    document.getElementById("spewOutput").innerHTML += "</br>";

    if (typeof obj.tel != "undefined") {
        document.getElementById("spewOutput").innerHTML += obj.tel;
    }

    document.getElementById("spewOutput").innerHTML += "</br></br>";
}

// *************************************************************
// Set or reset the map position based on the user's Geolocation
function setGeoLocation() {
    // Check for geolocation support
    if (navigator.geolocation) {
        // Use method getCurrentPosition to get coordinates
        navigator.geolocation.getCurrentPosition(function (position) {
            // Access them accordingly
            map.setView({ zoom: 15, center: new Microsoft.Maps.Location(position.coords.latitude, position.coords.longitude) });
        });
    }
    else {
        // set to centre of North America
        map.setView({ zoom: 4, center: new Microsoft.Maps.Location(47.23591995239258, -93.52752685546875) });
    }
}

// ************************************************
// event handler sets the geo location for the user
function manualLocation() {
    var geoLocationProvider = new Microsoft.Maps.GeoLocationProvider(map);
    geoLocationProvider.getCurrentPosition();
}

// ************************************
// remove all the pushpins from the map
function clearMap() {
    for (var i = map.entities.getLength() - 1; i >= 0; i--) {
        var pushpin = map.entities.get(i);
        if (pushpin instanceof Microsoft.Maps.Pushpin)
            map.entities.removeAt(i);
    }
}

// *************************************************
// resize the map container when the browser resizes
function resizeWin() {
    var bingWidth = window.innerWidth;

    $gel("bingMap").style.width = bingWidth + "px";
    $gel("bingMap").style.height = window.innerHeight + "px";

    return;
}

// **************************************************
// support function wraps the document.getElementById
// property to reduce the code size
function $gel(docElem) {
    return document.getElementById(docElem);
}

// ********************************************
// load the product object by retrieving a JSON
// string from the server and convert (eval)
function loadLocations() {
    var latlong = map.getCenter();
    var lt = latlong.latitude;
    var lng = latlong.longitude;

    callAJAX("fetchFromFactual", lt, lng);
}

// *********************
// Main AJAX entry point
function callAJAX(requestMethod, lt, lng) {
    var pageMethod = "default.aspx/" + requestMethod;   

    $.ajax({
        url: pageMethod,   
        data: JSON.stringify({ lat: lt, lon: lng }),// parameter map as JSON 
        type: "POST", // data has to be POSTed  
        contentType: "application/json", // posting JSON content      
        dataType: "JSON",  // type of data is JSON (must be upper case!)  
        timeout: 600000,    // AJAX timeout  
        success: function (result) {
            ajaxCallback(result.d);
        },
        error: function (xhr, status) {
            alert(status + " - " + xhr.responseText);
        }
    });
}

function ajaxCallback(serverResponse) {
    if (serverResponse !== "loadLocations") {
        factualJson = JSON.parse(serverResponse);
        //alert(serverResponse);
        addPin();        
    }
    else
        alert("error");
}