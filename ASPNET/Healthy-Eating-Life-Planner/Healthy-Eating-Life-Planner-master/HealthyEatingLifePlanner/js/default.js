/*
 * Name: default.js
 * Authors: Nick McRae
 *              Chris McManus
 *              Steve Pickering
 * Desc:    Holds all of the code required to make the front end work with the back end across the entire application
 */

//GLOBALS
var results;
var choices = [];
var resultSet = [];
var nutrientLevels = {};
var userDataObj = {};

//Desc:     Retrieves the user's input settings and saves them
function saveUserProfile() {
    
    var name = $('#userName').val();
    var age = $('#userAge').val();
    var weight = $('#userWeight').val();
    var height = $('#userHeight').val();
    var gender = $('#userGender').val();

    //validate
    if(name === "") {
        $('#profileError').text('Please enter your name.');
        $('#profileError').removeClass('hidden');
        return;
    }
    if (age === "") {
        $('#profileError').text('Please enter your age, ' + name + '.');
        $('#profileError').removeClass('hidden');
        return;
    }
    if (weight === "") {
        $('#profileError').text('Please enter your weight, ' + name + '.');
        $('#profileError').removeClass('hidden');
        return;
    }
    if (height === "") {
        $('#profileError').text('Please enter your height, ' + name + '.');
        $('#profileError').removeClass('hidden');
        return;
    }
    $('#profileError').addClass('hidden');

    //height should include " so that needs to be made into real characters
    if (height.indexOf('\"') > -1)
    {
        var index = height.indexOf('\"');
        //http://stackoverflow.com/questions/4364881/inserting-string-at-position-x-of-another-string
        height = [height.slice(0, index), '\\', height.slice(index)].join('');
    }

    //Persist data into backend
    var profileInfo = '{"name": "' + name + '",' +
                      '"age": "' + age + '",' +
                      '"weight": "' + weight + '",' +
                      '"height": "' + height + '",' +
                      '"gender": "' + gender + '"}';

    callAJAX("SaveClientData", profileInfo, "default.aspx/");
    return;
};

//Desc:     Used to make ajaxCalls to our server
function callAJAX(requestMethod, data, pageURL) {

    var pageMethod = pageURL + requestMethod;

    $.ajax({
        url: pageMethod,
        data: JSON.stringify({ request: data }),
        type: "POST",
        contentType: "application/json",
        dataType: "JSON",
        timeout: 600000,
        success: function (result) {
            ajaxCallback(result.d, requestMethod);
        },
        error: function (xhr, status) {
            console.log(status + " - " + xhr.responseText);
        }
    });
};

//Desc:     AjaxCallBack function - handles data returned from Ajax function
function ajaxCallback(serverResponse, requestMethod) {
    if (requestMethod === "ReadClientData") {
        populateUserData(JSON.parse(serverResponse));

        var path = window.location.href;
        var loc = path.substring(path.lastIndexOf('/') + 1);
        if (loc === "Nutrients.aspx" || loc === "Suggestions.aspx") {
            calculateNutrientLevels(userDataObj);
        }
    }      
    else if (requestMethod === "SearchTerm") {
        createRadioButtons(JSON.parse(serverResponse));
    }        
    else if (requestMethod === "ReadChoiceData") {        
            populateChoiceArray(JSON.parse(serverResponse));        
    }
    else if (requestMethod === "GetFood") {
        showFood(JSON.parse(serverResponse));
    }
};

//Desc:     Reads client data into the application when the page is loaded
function LoadClientInfo() {
    callAJAX("ReadClientData", "", "default.aspx/");
}


//Desc:     Populates the modal inputs with user data loaded from file
function populateUserData(userData) {

    $('#userName').val(userData.name);
    $('#userAge').val(userData.age);
    $('#userWeight').val(userData.weight);
    $('#userHeight').val(userData.height);
    $('#userGender').val(userData.gender);

    userDataObj.name = userData.name;
    userDataObj.age = userData.age;
    userDataObj.weight = userData.weight;
    userDataObj.height = userData.height;
    userDataObj.gender = userData.gender;
};

//Desc: Calls Fat Secret API with user search term to populate options for daily intake page
function search(caller) {
    $('#servingSize').val('');

    var searchTerm = $('#searchQuery').val();
    callAJAX("SearchTerm", searchTerm, caller);
};

//Desc:     builds ratio button form on page
function createRadioButtons(results) {
    //add try catching in this function

    var radioDiv = $('#radioButtons');
    var radioHtml = '<form method="post" action="self">';
    var food = results.foods.food;

    //magic
    resultSet = food;
    if (window.location.href.indexOf('DailyIntake.aspx') >= 0)
        for (var i = 0; i < 20; i++) {
            radioHtml += '<input type="radio" id="' + i + '"  name="foodItem" ' + 'value="' + food[i].food_id + '"';
            radioHtml += '/>' + food[i].food_name + '<br/><hr />';
        }
    else if ((window.location.href.indexOf('Suggestions.aspx') >= 0))
        for (var i = 0; i < food.length; i++) {
            radioHtml += '<p>' + food[i].food_name;
            radioHtml += '<p/>' + '<hr />';
        }
    else
        for (var i = 0; i < 20; i++) {
            radioHtml += '<input type="radio" id="' + i + '"  name="foodItem" ' + 'value="'
                            + food[i].food_id + '" onclick="displayFoodInformation();"';
            radioHtml += '/>' + food[i].food_name + '<hr />';
        }

    radioHtml += '</form>';
    radioDiv.html(radioHtml);
};

//Desc:     add item on daily intake page
function addItem() {

    var g = $('#servingSize').val(); //need to make sure this is an int value

    for (var i = 0; i < resultSet.length; i++) {
        if ($('#' + String(i)).prop('checked')) {
            /*Add all the data here!*/
            var foodID = resultSet[i].food_id;
            var foodName = resultSet[i].food_name;
            //Get food description details
            var food = {};
            parseFoodDesc(resultSet[i].food_description, food);

            choices.push({
                "id": foodID,
                "name": foodName,
                "serving": g,
                "calories": food.calories,
                "fat": food.fat,
                "carbs": food.carbs,
                "protein": food.protein,
                "servingSize": food.servingSize
            });

            break;
        }
    }

    //Update log display
    displayLogInformation(true);

};

//Desc:     Parses the data from the food_description returned by FatSecret
function parseFoodDesc(str, obj) {

    obj.servingSize = str.substring(str.indexOf(" "), str.indexOf("-")).trim();
    str = str.substring(str.indexOf("Calories"));
    obj.calories = str.substring(str.indexOf(" "), str.indexOf("|")).trim();
    str = str.substring(str.indexOf("Fat"));
    obj.fat = str.substring(str.indexOf(" "), str.indexOf("|")).trim();
    str = str.substring(str.indexOf("Carbs"));
    obj.carbs = str.substring(str.indexOf(" "), str.indexOf("|")).trim();
    str = str.substring(str.indexOf("Protein"));
    obj.protein = str.substring(str.indexOf(" ")).trim();
};

//Desc:     displays information about the selected food item and prints it to an html table within the page
function displayFoodInformation() {

    var food = {};
    var desc;

    for (var i = 0; i < resultSet.length; i++) {
        if ($("#" + i).prop('checked')) {
            food.foodName = resultSet[i].food_name;
            desc = resultSet[i].food_description;
            break;
        }
    }

    parseFoodDesc(desc, food);

    var table = $('#tblFoodInfo');
    var tableMarkup = "<thead><tr><th>Name</th><th>" + food.foodName + "</th></tr></thead>";
    tableMarkup += "<tbody><tr><td>Serving Size</td><td>" + food.servingSize + "</td></tr>";
    tableMarkup += "<tr><td>Calories</td><td>" + food.calories + "</td></tr>";
    tableMarkup += "<tr><td>Fat</td><td>" + food.fat + "</td></tr>";
    tableMarkup += "<tr><td>Carbs</td><td>" + food.carbs + "</td></tr>";
    tableMarkup += "<tr><td>Protein</td><td>" + food.protein + "</td></tr></tbody>";
    table.html(tableMarkup);

    $('#infoCollapse').click();

};

//writes user choice data via Daily Intake beforeunload
function writeChoiceData() {
    
    var userChoices = {};
    
    for (var i = 0; i < choices.length; i++) {
        userChoices['"' + i + '"'] = choices[i];
    }

    var choicesString = JSON.stringify(userChoices);

    callAJAX("SaveChoiceData", choicesString, "DailyIntake.aspx/");
    return;
};

//Desc:     read data from file
function readChoiceData( page ) {
    callAJAX("ReadChoiceData", "", page);
    return;
};

//Desc:     populate choice array
function populateChoiceArray(choiceData) {

    //populating array with choice objects containing basic food info
    for (var x in choiceData) {
        choices.push(choiceData[x]);
    }

    //detecting which page the user is on so we can better direct our spaghetti code :p
    var path = window.location.href;
    var loc = path.substring(path.lastIndexOf('/') + 1);

    //If page is DailyIntake display log information, if page is nutrient levels, calculate nutrient levels
    if (loc === "DailyIntake.aspx")
        displayLogInformation(true);
    else if (loc === "Nutrients.aspx" || loc === "Suggestions.aspx") {
        callAJAX("ReadClientData", "", loc + "/");
    }
        
};


//Desc:     Create the html for the food log
//Note:     Using flag to trigger the click on the collapsePanel at the end since there are 
//              instances where we don't want that to run (ie if it's already open)
function displayLogInformation(flag) {

    var html = "";

    for (var i = 0; i < choices.length; ++i) {
        var calories = choices[i].calories;//Edit this value 
        html += '<div class="col-xs-4">' + choices[i].name + '</div>';
        html += '<div class="col-xs-3 text-right">' + choices[i].serving + '</div>';
        html += '<div class="col-xs-5 text-right">' + 
        parseFloat(choices[i].serving) * parseFloat(calories.substring(0, calories.indexOf('kcal')));
        html += '<button type="button" class="btn-danger btn-xs" id="removeItem' + i + '" onclick="removeFromList(this.id)">';
        html += '<img width="20" height="20" src="img/delete_img.png" alt="remove selected" />';
        html += '</button></div>';
        //Styling hack
        var foodNameLength = choices[i].name.length;
        while (foodNameLength > 10) {
            html += '<br />';
            foodNameLength -= 10;
        }
        html += '<br /><hr />';
    }

    $('#logScroll').html(html);

    //for the demo sake
    if (choices.length <= 0)
        flag = !flag;
    if( flag )
        $('#logCollapse').click();

};

//Desc:     removes an item from the log display and log array
function removeFromList(id) {
    
    //remove element from choices array
    id = id.substring( 10, id.length );
    choices.splice(id, 1);

    //update log
    displayLogInformation(false);

};

//Desc: Parses daily intake log and calculates percentage consumed of each macromolecule for nutrients and suggestions page
//Final functionality for this page not completed yet
function calculateNutrientLevels(userInfo) {

    nutrientLevels.calories = 0;
    nutrientLevels.fat = 0;
    nutrientLevels.carbs = 0;
    nutrientLevels.protein = 0;
    var html = "";

    for (var i = 0; i < choices.length; i++) {
        var serving = parseInt(choices[i].serving);
        var calories = serving * extractInt(choices[i].calories, "k");
        var fat = serving * extractFloat(choices[i].fat, "g");
        var carbs = serving * extractFloat(choices[i].carbs, "g");
        var protein = serving * extractFloat(choices[i].protein, "g");

        nutrientLevels.calories += calories;
        nutrientLevels.fat += fat;
        nutrientLevels.carbs += carbs;
        nutrientLevels.protein += protein;
    }    

    /*
    var proteinPercent = (nutrientLevels.protein / (parseInt(userInfo.weight) * 0.8)) * 100;
    var fatPercent = ((nutrientLevels.fat * 9) / (nutrientLevels.calories * 0.1)) * 100;
    var carbsPercent = (nutrientLevels.carbs / 130.0) * 100;
    var caloriesPercent = (nutrientLevels.calories / 2000.0) * 100;*/

    //Replaced with something less voodoo magic-like
    var proteinNeed = 7.0 * 50;
    var carbNeed = 7.0 * 310;
    var fatNeed = 7.0 * 70;
    var calorieNeed;
    if( userInfo.gender === 'Male' )
        calorieNeed = 7.0 * 2700;
    else
        calorieNeed = 7.0 * 2100;

    var proteinPercent = (nutrientLevels.protein / proteinNeed) * 100.0;
    var fatPercent = (nutrientLevels.fat / fatNeed) * 100.0;
    var carbsPercent = (nutrientLevels.carbs / carbNeed) * 100.0;
    var caloriesPercent = (nutrientLevels.calories / calorieNeed) * 100.0;

    //================
    html += '<table class="table table-striped"><thead><tr><th>Attribute</th><th class="text-right">You</th>';
    html += '<th class="text-right">Need</th><th class="text-right">Percentage</th></tr></thead><tr>';
    html += '<td>Carbs</td><td class="text-right">' + nutrientLevels.carbs.toFixed(2) + 'g</td>';
    html += '<td class="text-right">' + carbNeed + 'g</td>';
    html += '<td class="text-right">' + carbsPercent.toFixed(2) + '%</td></tr><tr><td>Fat</td>'
    html += '<td class="text-right">' + nutrientLevels.fat.toFixed(2) + 'g</td><td class="text-right">' + fatNeed + 'g</td>';
    html += '<td class="text-right">' + fatPercent.toFixed(2) + '%</td></tr><tr><td>Protein</td>';
    html += '<td class="text-right">' + nutrientLevels.protein.toFixed(2) + 'g</td><td class="text-right">' + proteinNeed + 'g</td>';
    html += '<td class="text-right">' + proteinPercent.toFixed(2) + '%</td></tr><tr><th>Calories</th>';
    html += '<th class="text-right">' + nutrientLevels.calories.toFixed(2) + 'k</th><th class="text-right">' + calorieNeed + 'k</th>';
    html += '<th class="text-right">' + caloriesPercent.toFixed(2) + '%</th></tr></table>';
    $('#levelsDiv').html(html);    

    var path = window.location.href;
    var loc = path.substring(path.lastIndexOf('/') + 1);

    if (loc === "Suggestions.aspx") {
        showFoodChoices(proteinPercent, fatPercent, carbsPercent, caloriesPercent);
    }
}

//Functionality not complete
function showFoodChoices(proteinPercent, fatPercent, carbsPercent, caloriesPercent) {

    var protein = false;
    var fat = false;
    var carbs = false;
    var calories = false;

    if (proteinPercent < 80.0) {
        protein = true;
    }
    if (fatPercent < 80.0) {
        fat = true;
    }
    if (carbsPercent < 80.0) {
        carbs = true;
    }

    var food = [];

    if(protein) {
        food.push({ food_id: "Eggs", food_name: "Eggs" });
        food.push({ food_id: "Chicken", food_name: "Chicken" });              
        food.push({ food_id: "Nuts", food_name: "Nuts" });               
        food.push({ food_id: "Soy", food_name: "Soy" });
        food.push({ food_id: "Turkey", food_name: "Turkey" });
        food.push({ food_id: "Tuna", food_name: "Tuna" });
        food.push({ food_id: "Salmon", food_name: "Salmon" });
        food.push({ food_id: "Cheese", food_name: "Cheese" });
        food.push({ food_id: "Pork Loin", food_name: "Pork Loin" });
        food.push({ food_id: "Lean Beef", food_name: "Lean Beef" });
        food.push({ food_id: "Tofu", food_name: "Tofu" });
        food.push({ food_id: "Yogurt", food_name: "Yogurt" });
    }

    if(fat) {
        food.push({ food_id: "Butter", food_name: "Butter" });
        food.push({ food_id: "Ground Beef", food_name: "Ground Beef" });
        food.push({ food_id: "Refined Sugar", food_name: "Refined Sugar" });
        food.push({ food_id: "Advocados", food_name: "Advocados" });
        food.push({ food_id: "Eggs", food_name: "Eggs" });
        food.push({ food_id: "Nuts", food_name: "Nuts" });
        food.push({ food_id: "Cheese", food_name: "Cheese" });
    }

    if (carbs) {
        food.push({ food_id: "Pasta", food_name: "Pasta" });
        food.push({ food_id: "Bread", food_name: "Bread" });
        food.push({ food_id: "Potatoes", food_name: "Potatoes" });
        food.push({ food_id: "Dried Fruit", food_name: "Dried Fruit" });
        food.push({ food_id: "Banana", food_name: "Banana" });
        food.push({ food_id: "Granola", food_name: "Granola" });
    }

    if (!(carbs || fat || protein)) {
        food.push({ food_id: "nothing", food_name: "You're doing a Great job! Keep up the good work! :)" });
    }

    var foods = { food: food };
    var res = { foods: foods };
    createRadioButtons(res);

    $('#suggestCollapse').click();
}

//Desc:     gets an integer from the supplied parameters
function extractInt(molecule, character) {
    var n = molecule.indexOf(character);
    var substr = molecule.substring(-1, n);
    return parseInt(substr);
}

//Desc:     gets a float from the supplied parameters
function extractFloat(molecule, char) {
    var n = molecule.indexOf(char);
    var substr = molecule.substring(-1, n);
    return parseFloat(substr);
}