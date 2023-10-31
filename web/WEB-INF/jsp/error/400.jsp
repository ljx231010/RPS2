<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<html>
<head>
    <title data-react-helmet="true">400</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error.css">
</head>
<body>
<div id="root">
    <div class="box-404-wrap">
        <div class="box">
            <div class="d-flex flex-column align-items-center">
                <div class="text-wrap">
                    <h1 data-t="400" class="h1">400</h1>
                </div>
                <div class="text-center mt-2">Due to perceived client error (such as malformed request syntax, invalid request information frames, or virtual request routing), the server is unable or will not process the current request.</div>
                <div class="mt-4"><a href="${pageContext.request.contextPath}/index.jsp" role="button" tabindex="0" class="btn btn-primary">home</a></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
