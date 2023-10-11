<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <!-- <meta charset="UTF-8"> -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.bootcss.com/semantic-ui/2.4.1/semantic.min.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico"/>
    <link rel="stylesheet" href="./static/css/me.css">
    <style>
        table td {
            font-size: 15px;
            font-weight: bold;
            text-align: center;
        }

        table thead {
            font-size: 16px;
            font-weight: bold;
            text-align: center;
        }
    </style>
    <title>API-all</title>
</head>
<body class="bg1">
<nav class="ui inverted attached segment">
    <div class="ui container">
        <div class="ui inverted secondary menu">
            <h2 class="ui teal header item">RPS</h2>
            <a href="${pageContext.request.contextPath}/index.jsp" class="item"><i class="home icon"></i>Recommend</a>
            <a href="#" class="active item"><i class="archive icon"></i>PathApi</a>
            <a href="${pageContext.request.contextPath}/html/introduction.html" class="item"><i class="idea icon"></i>Introduction</a>
            <a href="${pageContext.request.contextPath}/html/help.html" class="item"><i class="tags icon"></i>Help</a>
            <a href="${pageContext.request.contextPath}/html/download.html" class="item"><i class="clone icon"></i>Download</a>
            <a href="${pageContext.request.contextPath}/html/contact us.html" class="item"><i class="info icon"></i>Contact
                us</a>
        </div>
    </div>
</nav>
<br>
<h2 class="ui header" style="text-align: center;">Some pathway search methods for use before using RPS</h2>
<div class="ui center aligned container" style="margin-top: 4em;width: 75%">
    <table class="ui celled table" style="text-align: center">
        <thead>
        <tr>
            <th>Method <span title="Path search method" style="cursor: help;" data-toggle="tooltip" data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            <th>Introduction <span title="Method introduction" style="cursor: help;" data-toggle="tooltip"
                                   data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            <th>Literature <span title="Literature corresponding to the method" style="cursor: help;" data-toggle="tooltip"
                                 data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            <th>Search <span title="Literature corresponding to the method" style="cursor: help;" data-toggle="tooltip"
                             data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Atomic Group Tracking</td>
            <td>Atomic Group Tracking</td>
            <td><a href="https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0168725" target="_blank">A Method for Finding
                Metabolic Pathways Using Atomic Group Tracking</a></td>
            <td>
                <button class="ui button" onclick="window.open('bp_api.jsp')">Search</button>
            </td>
        </tr>
        <tr>
            <td>YenK</td>
            <td>K-shortest loop free path algorithm</td>
            <td><a href="https://pubsonline.informs.org/doi/abs/10.1287/mnsc.17.11.712" target="_blank">Finding the K Shortest Loopless
                Paths in a Network</a></td>
            <td>
                <button class="ui button" onclick="window.open('YenK_api.jsp')">Search</button>
            </td>
        </tr>
        <tr>
            <td>AFP</td>
            <td>Atom-group Flux Pathfinder</td>
            <td>Engineer</td>
            <td>
                <button class="ui button" onclick="window.open('afp_api.jsp')">Search</button>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
</body>
<footer class="ui inverted vertical segment">
    <div class="ui center aligned container">
        <p class="" style="margin-bottom: 0px">©Guangxi University,
            Nanning, China</p>
        <p>College of computer and electronic information</p>
    </div>
</footer>
<script>
</script>
</html>
