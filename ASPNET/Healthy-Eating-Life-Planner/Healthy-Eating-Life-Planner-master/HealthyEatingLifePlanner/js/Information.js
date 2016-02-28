/*

MARKED FOR REMOVAL
Pulled some search functions into this script for the information page. Need to turn the server response into data
to be displayed on the page for user's perusal.
*/

/*function searchAJAX(requestMethod, term) {
    var pageMethod = "Information.aspx/" + requestMethod;

    $.ajax({
        url: pageMethod,
        data: JSON.stringify({ request: term }),
        type: "POST",
        contentType: "application/json",
        dataType: "JSON",
        timeout: 600000,
        success: function (result) {
            showAlert(result.d, requestMethod);
        },
        error: function (xhr, status) {
            alert(status + " - " + xhr.responseText);
        }
    });
};

function showAlert(serverResponse, requestMethod) {
    var result = serverResponse;    
    alert(result);
};

function search() {
    var searchTerm = document.getElementById("searchQuery").value;
    searchAJAX("SearchTerm", searchTerm);
}
*/