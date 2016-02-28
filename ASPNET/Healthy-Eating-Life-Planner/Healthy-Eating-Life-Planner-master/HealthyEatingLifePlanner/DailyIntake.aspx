<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="DailyIntake.aspx.cs" Inherits="HealthyEatingLifePlanner.DailyIntake" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="css/sitecss.css" />
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/default.js"></script>
    <title>Daily Intake</title>
</head>
<body class="back-color" onbeforeunload="writeChoiceData()" onload="readChoiceData('DailyIntake.aspx/')"> 
    
    <!--navigation bar-->
    <nav class="navbar navbar-inverse" role="navigation">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" onclick="window.location.href='Default.aspx'">
                <img width="20" height="20" src="img\home_img.png" alt="profile picture" />
            </button>
            <a class="navbar-brand" href="#">Healthy Eating Life Planner</a>
        </div>
    </nav>

    <!--content-->
    <div class="contrainer">
        <h3 style="text-align: center">Daily Intake</h3>
        <div class="panel-group" id="accordion">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                            Search
                        </a>
                    </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in">
                    <div class="panel-body">
                        <div class="col-xs-7">
                            <input type="text" name="searchTerm" class="form-control"
                                id="searchQuery" placeholder="Type a food to begin"/>
                        </div>

                        <div class="col-xs-2">
                            <button type="button" class="btn-primary btn-sm" onclick="search('DailyIntake.aspx/')" id="searchBtn">
                                <img width="20" height="20" src="img\search_img.png" alt="add selected" />
                            </button>
                        </div>

                        <div class="col-xs-12">
                            <br />
                        </div>

                        <div class="col-xs-7">
                            <input type="text" name="servingSize" class="form-control"
                                id="servingSize" placeholder="How many servings?"/>
                        </div>
                        
                        <div class="col-xs-2">
                            <button type="button" class="btn-primary btn-sm" onclick="addItem()">
                                <img width="20" height="20" src="img\add_img.png" alt="add selected" />
                            </button>
                        </div>

                        <div class="col-xs-12">
                            <br />
                        </div>

                        <div class="col-xs-12 scroll-div" id="radioButtons" >

                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" id="logCollapse">
                            Your Log
                        </a>
                    </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse">
                    <div class="panel-body">
                      
                        <div class="col-xs-3 tblItemHead">
                            NAME
                        </div>
                        <div class="col-xs-4 tblItemHead">
                            SERVINGS
                        </div>
                        <div class="col-xs-5 tblItemHead">
                            CALORIES
                        </div>
                        <br />
                        <hr />

                        <div class="col-xs-12 scroll-div" id="logScroll">
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>   
</body>
<script>
    $('#searchQuery').on("keypress", function (e) {
        if (e.keyCode == 13) {
            $('#searchBtn').click();
        }
    });
</script>
</html>
