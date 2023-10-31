<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.bootcss.com/semantic-ui/2.4.1/semantic.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/me.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico"/>
    <style>
        table td {
            font-size: 17px;
            font-weight: bold;
            text-align: center;
        }

        table thead {
            font-size: 19px;
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<title>Species_Result</title>
<body class="bg1">
<%@include file="nav.jsp" %>
<div class="ui center aligned container" style="margin-top: 5em; width: 75%; ">
    <h3 class="ui header">Current Pathway:&nbsp;&nbsp;${requestScope.get('path')}</h3>
    <div class="field">
        <select class="ui dropdown" id="select1" onchange="topChange()">
            <option value="10">TOP 10</option>
            <option value="20">TOP 20</option>
            <option value="30">TOP 30</option>
            <option value="50" selected>TOP 50</option>
        </select>
    </div>
    <table class="ui celled table" id='t1' style="text-align: center;">
        <thead>
        <tr>
            <th>Rank <span title="Recommended candidate species" style="cursor: help;" data-toggle="tooltip"
                           data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            <th>Host organism <span title="Species score ranking" style="cursor: help;" data-toggle="tooltip"
                                    data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            <th>Score <span title="Species score" style="cursor: help;" data-toggle="tooltip"
                            data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            <th>Detail <span title="Click to view path details" style="cursor: help;" data-toggle="tooltip"
                             data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${requestScope.get('keyValues')==null}">
            <tr>
                <td colspan="4">${requestScope.get('message')}</td>
            </tr>
        </c:if>
        <c:if test="${requestScope.get('keyValues')!=null}">
            <c:forEach var="kv" items="${requestScope.get('keyValues')}" end="49" varStatus="kvStatus">
                <tr id="tr${kvStatus.count}">
                    <td>${kvStatus.count}</td>
                    <td>${requestScope.get(kv.getKey()).getSpeciesName()}</td>
                    <td>${kv.getValue()}</td>
                    <td>
                        <button class="ui button"><a
                                href="${pageContext.request.contextPath}/reaction/${kv.getKey()}/${requestScope.get('path')}"
                                target="_blank" style="text-decoration: none;">VIEW</a></button>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

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
</body>

<footer class="ui inverted vertical segment">
    <div class="ui center aligned container">
        <p class="" style="margin-bottom: 0px">©Guangxi University, Nanning, China</p>
        <p>
            College of computer and electronic information</p>
    </div>
</footer>
<script src="${pageContext.request.contextPath}/static/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/semantic.min.js"></script>
<script>
    $('.ui.dropdown').dropdown();

    function topChange() {
        var data = [];
        let num = document.getElementById("select1").value;
        console.log(num);
        for (let i = 1; i <= 70; i++) {
            data[i] = "tr" + i;
        }
        for (let i = 1; i <= 70; i++) {
            if (document.getElementById(data[i]) !== null) {
                document.getElementById(data[i]).hidden = false;
            }
        }
        for (let j = Number(num) + 1; j < data.length; j++) {
            if (document.getElementById(data[j]) !== null) {
                document.getElementById(data[j]).hidden = "hidden";
            }
        }
    }

</script>
</html>

