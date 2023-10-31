<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title data-react-helmet="true">405</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error.css">
</head>
<body>
<div id="root">
    <div class="box-404-wrap">
        <div class="box">
            <div class="d-flex flex-column align-items-center">
                <div class="text-wrap">
                    <h1 data-t="500" class="h1">405</h1>
                </div>
                <div class="text-center mt-2">Request method 'GET' not supported.</div>
                <div class="mt-4"><a href="${pageContext.request.contextPath}/index.jsp" role="button" tabindex="0" class="btn btn-primary">home</a></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
