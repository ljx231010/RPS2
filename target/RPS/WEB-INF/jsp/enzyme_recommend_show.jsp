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
            font-size: 16px;
            font-weight: bold;
            text-align: center;
        }

        table thead {
            font-size: 17px;
            font-weight: bold;
            text-align: center;
        }
    </style>
    <title>Enzyme_Recommend_Show</title>
</head>
<body class="bg1">
<%@include file="nav.jsp" %>

<div class="ui aligned container" style="margin-top: 5em; width: 80%; ">
    <h2 class="ui header" style="text-align: center">
        Recommend foreign enzymes for foreign reaction <b>${requestScope.get("reactionId")}</b> in <b>${requestScope.get("speciesName")}</b></h2>
    <br>
    <p style="margin-left: 35%;">
        <font size="5px">Filter:&nbsp;&nbsp;</font>
    </p>
    <div class="field" style="text-align: center;margin-top: -1.1cm;">
        <select class="ui dropdown" id="select1" onchange="func2()">
            <option value="1">only phylogenetic distance</option>
            <option value="2">only Km</option>
            <option value="3" selected="selected">combination(default)</option>
        </select>
    </div>
    <table class="ui celled table" id="table1" hidden="hidden" style="text-align: center;">
            <thead>
            <tr>
                <th>Rank <span title="Enzyme score ranking" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Enzyme <span title="EC Number of recommended enzyme" style="cursor: help;"
                                           data-toggle="tooltip"
                                           data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Source Organism <span title="Source organism of recommended enzyme" style="cursor: help;"
                                         data-toggle="tooltip"
                                         data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Distance <span title="Phylogenetic distance" style="cursor: help;" data-toggle="tooltip"
                                   data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>View <span title="Related links" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="enzyme" items="${requestScope.get('distanceEnzymes')}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${enzyme.getEcNumber()}</td>
                    <td>${enzyme.getSourceOrganismName()}</td>
                    <td>${enzyme.getDistance()}</td>
                    <td>
                        <c:if test="${enzyme.getUrls()==null}">
                            -
                        </c:if>
                        <c:forEach var="url" items="${enzyme.getUrls()}">
                            <a href="${url.getWebSite()}" target="_blank">${url.getName()}</a>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty requestScope.get('distanceEnzymes')}">
                <tr>
                    <td colspan="5">No Results</td>
                </tr>
            </c:if>
            </tbody>
        </table>
        <table class="ui celled table" id="table2" hidden="hidden" style="text-align: center;">
            <thead>
            <tr>
                <th>Rank <span title="Enzyme score ranking" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Enzyme <span title="EC Number of recommended enzyme" style="cursor: help;"
                                           data-toggle="tooltip"
                                           data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Source Organism <span title="Source organism of recommended enzyme" style="cursor: help;"
                                         data-toggle="tooltip"
                                         data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Km<span title="KM values" style="cursor: help;" data-toggle="tooltip" data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>View <span title="Related links" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="enzyme" items="${requestScope.get('dynamicsEnzymes')}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${enzyme.getEcNumber()}</td>
                    <td>${enzyme.getSourceOrganismName()}</td>
                    <td>${enzyme.getKm()}</td>
                    <td>
                        <c:if test="${enzyme.getUrls()==null}">
                            -
                        </c:if>
                        <c:forEach var="url" items="${enzyme.getUrls()}">
                            <a href="${url.getWebSite()}" target="_blank">${url.getName()}</a>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty requestScope.get('dynamicsEnzymes')}">
                <tr>
                    <td colspan="5">No Results</td>
                </tr>
            </c:if>
            </tbody>
        </table>
        <table class="ui celled table" id="table3" style="text-align: center;">
            <thead>
            <tr>
                <th>Rank <span title="Enzyme score ranking" style="cursor: help;" data-toggle="tooltip"
                               data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Enzyme <span title="EC Number of recommended enzyme" style="cursor: help;"
                                           data-toggle="tooltip"
                                           data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                    <th>Source Organism<span title="Source organism of recommended enzyme" style="cursor: help;"
                                         data-toggle="tooltip"
                                         data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Score <span title="Enzyme score" style="cursor: help;" data-toggle="tooltip" data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>View <span title="Related links" style="cursor: help;" data-toggle="tooltip" data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="enzyme" items="${requestScope.get('collect')}" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${enzyme.getEcNumber()}</td>
                    <td>
                        <c:if test="${enzyme.getSourceOrganismId()!=null}">
                            ${enzyme.getSourceOrganismName()}
                        </c:if>
                        <c:if test="${enzyme.getSourceOrganismId()==null}">
                            ${enzyme.getSourceOrganismTaxName()}
                        </c:if>
                    </td>
                    <td>${enzyme.getScore()}</td>
                    <td>
                        <c:if test="${enzyme.getUrls()==null}">
                            -
                        </c:if>
                        <c:forEach var="url" items="${enzyme.getUrls()}">
                            <a href="${url.getWebSite()}" target="_blank">${url.getName()}</a>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty requestScope.get('collect')}">
                <tr>
                    <td colspan="5">No Results</td>
                </tr>
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
</script>
<script>
    function func2() {
        console.log("func2");
        var value1 = document.getElementById("select1").value;
        var value = parseInt(value1);
        console.log(value);
        if (value === 1) {
            console.log("value===1");
            document.getElementById("table1").removeAttribute("hidden");
            document.getElementById("table2").hidden = "hidden";
            document.getElementById("table3").hidden = "hidden";
        } else if (value === 2) {
            console.log("value===2");
            document.getElementById("table2").removeAttribute("hidden");
            document.getElementById("table1").hidden = "hidden";
            document.getElementById("table3").hidden = "hidden";
        } else if (value === 3) {
            console.log("value===3");
            document.getElementById("table3").removeAttribute("hidden");
            document.getElementById("table1").hidden = "hidden";
            document.getElementById("table2").hidden = "hidden";
        }
    }
</script>
</html>

