<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Suggestions.aspx.cs" Inherits="HealthyEatingLifePlanner.Suggestions" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server"> 
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="css/sitecss.css" />
    <script type="text/javascript" src="js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="js/default.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <title>Suggestions</title>
</head>
<body class="back-color" onload="readChoiceData('Suggestions.aspx/')">
    
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
        <h3 style="text-align: center">Suggestions</h3>
        <div class="panel-group" id="accordion">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
                            Your Levels
                        </a>
                    </h4>
                </div>
                <div id="collapseOne" class="panel-collapse collapse in">
                    <div class="panel-body" id="levelsDiv">

                    </div>
                </div>
            </div>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" id="suggestCollapse">
                            Suggestions
                        </a>
                    </h4>
                </div>
                <div id="collapseTwo" class="panel-collapse collapse">
                    <div class="panel-body">
                        <div class="col-xs-12 scroll-div" id="radioButtons" >

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>  
</body>
</html>
