<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.bootcss.com/semantic-ui/2.4.1/semantic.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/me.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/notiflix-3.2.5.min.css">
    <script src="${pageContext.request.contextPath}/static/js/clipboard.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/notiflix-3.2.5.min.js"></script>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico"/>
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
    <title>LinePath_Result</title>
</head>
<body class="bg1">
<nav class="ui inverted attached segment">
    <div class="ui container">
        <div class="ui inverted secondary menu">
            <h2 class="ui teal header item">RPS</h2>
            <a href="http://biolab.gxu.edu.cn/RPS/index.jsp" class="item"><i class="home icon"></i>Recommend</a>
            <a href="http://biolab.gxu.edu.cn/RPS/api_all.jsp" class="item"><i class="archive icon"></i>PathApi</a>
            <a href="http://biolab.gxu.edu.cn/RPS/html/introduction.html" class="item"><i class="idea icon"></i>Introduction</a>
            <a href="http://biolab.gxu.edu.cn/RPS/html/help.html" class="item"><i class="tags icon"></i>Help</a>
            <a href="http://biolab.gxu.edu.cn/RPS/html/download.html" class="item"><i
                    class="clone icon"></i>Download</a>
            <a href="http://biolab.gxu.edu.cn/RPS/html/contact us.html" class="item"><i class="info icon"></i>Contact
                us</a>
        </div>
    </div>
</nav>

<div class="ui center aligned container" style="margin-top: 5em; width: 80%; ">
    <h3 class="ui header">${requestScope.get("path")}</h3>
    <table class="ui celled table" style="text-align: center;">
        <thead>
        <tr>
            <th>Rank<span title="Path Ranking" style="cursor: help;" data-toggle="tooltip"
                          data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            <th>Pathway</th>
            <th>Copy<span title="Click Copy to copy the path of the current line" style="cursor: help;"
                          data-toggle="tooltip"
                          data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="path" items="${requestScope.get('results')}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td id="result${status.count}">${path}</td>
                <td>
                    <button class="ui button" data-clipboard-target="#result${status.count}">copy</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty requestScope.get('results')}">
        <p>No result is found in the current method. Please try another method</p>
    </c:if>

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
    var clipboard = new ClipboardJS('.ui button');

    clipboard.on('success', function (e) {
        console.log(e);
        console.info('Action:', e.action);
        console.info('Text:', e.text);
        console.info('Trigger:', e.trigger);
        copyResult();
    });

    clipboard.on('error', function (e) {
        console.log(e);
        console.error('Action:', e.action);
        console.error('Trigger:', e.trigger);
        copyFailure();
    });
</script>
<script>
    function copyResult() {
        Notiflix.Notify.success(
            'Copy Success', {
                timeout: 2000,
            },
        );
    }

    function copyFailure() {
        Notiflix.Notify.failure(
            'Copy failed due to browser settings, please copy manually', {
                timeout: 5000,
            },
        );
    }
</script>
</html>
