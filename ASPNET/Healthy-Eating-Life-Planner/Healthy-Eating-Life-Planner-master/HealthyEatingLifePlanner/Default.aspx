<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="HealthyEatingLifePlanner.Default" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Healthy Eating Life Planner</title>
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="css/sitecss.css" />
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/default.js"></script>
</head>
<body class="back-color" onload="LoadClientInfo()">
    <!--navigation bar-->
    <nav class="navbar navbar-inverse" role="navigation">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="modal" data-target="#myModal">
                <img width="20" height="20" src="img\profile_img.png" alt="profile picture" />
            </button>
            <a class="navbar-brand" href="#">Healthy Eating Life Planner</a>
        </div>
    </nav>

    <!--modal-->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span>
                        <span class="sr-only">Close</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">Profile</h4>
                </div>
                <div class="modal-body">
                    <label>Preferred Name</label>
                    <input id="userName" type="text" class="form-control" />
                    <label>Age</label>
                    <input id="userAge" type="text" class="form-control" />
                    <label>Weight</label>
                    <input id="userWeight" type="text" class="form-control" />
                    <label>Height</label>
                    <input id="userHeight" type="text" class="form-control" />
                    <label>Gender</label>
                    <select id="userGender" class="form-control">
                        <option>Male</option>
                        <option>Female</option>
                    </select>
                    <span id='profileError' class="hidden error-msg">Please enter a name</span>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" onclick="saveUserProfile()">Save profile</button>
                </div>
            </div>
        </div>
    </div>

    <!--content-->
    <div class="contrainer">
        <h3 style="text-align: center">Welcome!</h3>
        <div class="panel-group" id="accordion">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                            Daily Intake
                        </a>
                    </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in">
                    <div class="panel-body">
                        <div class="col-xs-9">
                            Monitor your daily eating habits by logging what you eat.
                        </div>
                        <div class="col-xs-1">
                            <button type="button" class="btn-primary btn-lg" onclick="window.location.href='DailyIntake.aspx'">Start</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
                            Nutrient Levels
                        </a>
                    </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse">
                    <div class="panel-body">
                        <div class="col-xs-9">
                            See how well you are meeting your daily recommended levels of vitamins and nutrients.
                        </div>
                        <div class="col-xs-1">
                            <button type="button" class="btn-primary btn-lg" onclick="window.location.href='Nutrients.aspx'">Start</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
                            Food Information
                        </a>
                    </h4>
                </div>
                <div id="collapseThree" class="panel-collapse collapse">
                    <div class="panel-body">
                        <div class="col-xs-9">
                            Look up the nutritional information of your favourite foods.
                        </div>
                        <div class="col-xs-1">
                            <button type="button" class="btn-primary btn-lg" onclick="window.location.href='Information.aspx'">Start</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseFour">
                            Food Suggestions
                        </a>
                    </h4>
                </div>
                <div id="collapseFour" class="panel-collapse collapse">
                    <div class="panel-body">
                        <div class="col-xs-9">
                            Can't decide what to eat? Click here for suggestions.
                        </div>
                        <div class="col-xs-1">
                            <button type="button" class="btn-primary btn-lg" onclick="window.location.href='Suggestions.aspx'">Start</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>