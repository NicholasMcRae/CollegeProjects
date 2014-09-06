<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="default.aspx.cs" Inherits="MappingProjectOne._default" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <link rel="stylesheet" type="text/css" href="styles/default.css"/>
    <script type="text/javascript" src="scripts/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="scripts/key.js"></script>
    <script type="text/javascript" src="scripts/default.js"></script>  
    <script type="text/javascript" src="scripts/json2.js"></script>
    <script type="text/JavaScript" src="http://ecn.dev.virtualearth.net/mapcontrol/mapcontrol.ashx?v=7.0"></script>
</head>
<body onload="boot();">
    <div class="wrap">
        <form id="form1" runat="server">
            <div id="myForm" class="MyForm"> 
                <div class="title">
                    <p>Booze, Bars, and Buffets</p>
                </div>   
                <div class="functions">  
                    <ul>
                      <li>
                        <input value="Get Locations" onclick="loadLocations();" type="button"/> 
                      </li>
                      <li>
                        <input value="Center" onclick="centreMap()" type="button" />
                      </li>
                      <li>
                        <img src="images/liquor.jpg" alt=""/> Booze <input type="checkbox" value="booze" id="boozeCheck" />
                      </li>
                      <li>                
                        <img src="images/bars.jpg" alt=""/> Bars <input type="checkbox" value="bars" id="barsCheck" />
                      </li>
                      <li>
                        <img src="images/food.jpg" alt=""/> Buffets <input type="checkbox" value="buffets" id="buffetsCheck" />
                      </li>
                    </ul>  
                </div>      
            </div>
            <div id="bingMap" class="BingMap">
            </div> 
            <div class="output">
                <p id="spewOutput"></p>
            </div>       
        </form>
    </div>
</body>
</html>
