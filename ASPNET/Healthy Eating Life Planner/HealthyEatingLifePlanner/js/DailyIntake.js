/*var results;
var choices = [];
var resultSet = [];
MARKED FOR REMOVAL

    To do:
        - Need to take selection from radio button and save it to file
        - Figure out if we're going to display the daily intake on this page or another page.. might need to re-work some app verbs and layout


//function called from search button on page
function search() {
    var searchTerm = document.getElementById("searchQuery").value;
    searchAJAX("SearchTerm", searchTerm);
}

//web method call returning results with search term
function searchAJAX(requestMethod, term) {
    var pageMethod = "DailyIntake.aspx/" + requestMethod;

    $.ajax({
        url: pageMethod,
        data: JSON.stringify({ request: term }),
        type: "POST",
        contentType: "application/json",
        dataType: "JSON",
        timeout: 600000,
        success: function (result) {
            populateObject(result.d, requestMethod);
        },
        error: function (xhr, status) {
            alert(status + " - " + xhr.responseText);
        }
    });
};

//creates radio button layout with JSON result from web method
function populateObject(serverResponse, requestMethod) {
    var result = JSON.parse(serverResponse);    
    createRadioButtons(result);
    //alert(result);
};

//builds ratio button form on page
function createRadioButtons(results) {
    //maybe add some try catching in this function

    var radioDiv = document.getElementById("radioButtons");
    radioDiv.innerHTML = '<form method="post" action="self">';
    var food = results.foods.food;
    
    //magic
    resultSet = food;

    for (var i = 0; i < 20; i++) {        
        var radioHtml = '<input type="radio" id="' + i + '"  name="foodItem" ' + 'value="' + food[i].food_id + '"';
        radioHtml += '/>' + food[i].food_name + '<br/><hr />';
        radioDiv.innerHTML += radioHtml;
    }

    //Commented out while I was styling
    //radioDiv.innerHTML += '<br/><input type="text" name="grams" id="grams" />';
    //radioDiv.innerHTML += '<button onclick="addItem()">Add item</button>';

    radioDiv.innerHTML += '</form>';    
}

//preliminary button to add item
function addItem() {

    var g = document.getElementById("servingSize").value; //need to make sure this is an int value

    for (var i = 0; i < resultSet.length; i++) {
        if (document.getElementById(String(i)).checked) {

            var foodID = resultSet[i].food_id;
            var foodName = resultSet[i].food_name;
            choices.push({ "id": foodID, "name": foodName, "serving": g });
            break;
        }        
    }    
}
*/
