<%@page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@page import="java.util.List" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.bootcss.com/semantic-ui/2.4.1/semantic.min.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico"/>
    <link rel="stylesheet" href="./static/css/me.css">
    <title>Atomic cluster tracing</title>

    <style type="text/css">
        .m-text-font {
            font-size: 1em;
        }

        .button {
            background-color: transparent;
            border: none;
            color: black;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
        }

        .button:hover {
            background-color: transparent;
            border: 2px solid #e7e7e7;
            color: black;
        }

        table {
            border-collapse: collapse;
            margin: 0 auto;
            text-align: center;
        }

        table td, table th {
            border: 1px solid #cad9ea;
            color: #666;
            height: 30px;
        }

        table thead th {
            background-color: #CCE8EB;
            width: 100px;
        }

        table tr:nth-child(odd) {
            background: #fff;
        }

        table tr:nth-child(even) {
            background: #F5FAFA;
        }

        hr {
            width: 80%;
            margin: 0 auto;
            border: 0;
            height: 0;
            border-top: 1px solid rgba(0, 0, 0, 0.1);
            border-bottom: 1px solid rgba(255, 255, 255, 0.3);
        }

        div {
            width: 75%;
            margin: 0 auto;
        }
    </style>

</head>
<body class="bg1">
<%@include file="nav.jsp" %>
<div class="ui container" id="mainDiv" style="margin-top: 5em; width: 70%;">
    <h3 class="ui header" style="text-align: left;">
        <i class="paper plane icon"></i>Pathway Search
    </h3>
    <div class="ui fluid segment SEARCH m-text-font attached"
         style="width: 100%">

        <form class="ui form" id="myForm" action="${pageContext.request.contextPath}/bpapi">
            <div class="field">
                <h4 class="ui header">KEGG ID for Start Compound:</h4>
                <div class="ui input ">
                    <input name="substrate" id="substrate" type="text"
                           placeholder="For example: C00022">
                </div>
                <p id="start_info"></p>
            </div>
            <div class="field">
                <h4 class="ui header">KEGG ID for Target Compound:</h4>
                <div class="ui input ">
                    <input name="product" id="product" type="text"
                           placeholder="For example: C00183">
                </div>
                <p id="end_info"></p>
            </div>
            <div class="field">
                <h4 class="ui header">Number of the minimal atom groups:</h4>
                <div class="ui input ">
                    <input name="minatomic" id="minatomic" type="text"
                           placeholder="For example: 2">
                </div>
                <p id="atom_info"></p>
            </div>
            <button class="ui teal basic center aligned button formsubmit"
                    id="formsubmit" onclick="submit_form()" type="button"
                    value="Submit">Submit
            </button>
        </form>
    </div>
    <h3 class="ui header">
        <i class="bell outline icon"></i>Parameters for running
    </h3>
    <table class="ui celled table" style="font-size: 16px;text-align: center">
        <thead>
        <colgroup>
            <col style="width: 10%;" />
            <col style="width: 30%;" />
            <col style="width: 60%;" />
        </colgroup>
        <tr>
            <th></th>
            <th>Option</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>1</td>
            <td>Start Compound</td>
            <td>Start compound in KEGG compound ID</td>
        </tr>
        <tr>
            <td>2</td>
            <td>Target Compound</td>
            <td>Target compound in KEGG compound ID</td>
        </tr>
        <tr>
            <td>3</td>
            <td>Number Of The Minimal Atom Groups</td>
            <td>The number of the minimal atom groups transferred in the reactions of the pathway.</td>
        </tr>
        </tbody>
    </table>
</div>
<br>
<br>

<div data-role="content" style="display:none;" id="formResponse">
    <br />
    <br />
    <br />
    <p style="font-size: 24px;">The program is running, please wait...</p>
    <p style="font-size: 24px;">Your inputs are as follow:</p>
    <ul>
        <li><span style="font-size: 20px;">Start compound&nbsp;:&nbsp;</span><span style="font-weight: bold;" id="yourStart"></span></li>
        <br><li><span style="font-size: 20px;">Target compound&nbsp;:&nbsp;</span><span style="font-weight: bold;" id="yourTarget"></span></li>
        <br><li><span style="font-size: 20px;">Number of the minimal atom groups&nbsp;:&nbsp;</span><span style="font-weight: bold;" id="yourAtomNum"></span>
    </ul>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
</div>
</body>
<br>
<br>
<br>

<footer class="ui inverted vertical segment">
    <div class="ui center aligned container">
        <p class="" style="margin-bottom: 0px">©Guangxi University,
            Nanning, China</p>
        <p>College of computer and electronic information</p>
    </div>
</footer>
<script src="${pageContext.request.contextPath}/static/js/jquery.min.js" type="text/javascript"></script>
<script
        src="${pageContext.request.contextPath}/static/js/semantic.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $('.wechat').popup({
        popup: $('.wechat-qr'),
        position: "right center"
    });
</script>
<script type="text/javascript">

    function jump(url) {
        window.location.href = "ShowServlet?path=" + url;
    }


    function submit_form() {
        var start = document.getElementById("substrate").value;
        console.log(start);
        var end = document.getElementById("product").value;
        var minatom = document.getElementById("minatomic").value;
        var start_info = document.getElementById("start_info");
        var end_info = document.getElementById("end_info");
        var minatom_info = document.getElementById("atom_info");
        var reg = new RegExp("^\s*(\S+)\s*$");

        var patternCompound = new RegExp("C[0-9]{5}");
        var n = Number(minatom);

        var check = true;
        if (!patternCompound.test(start)) {
            check = false;
            start_info.setAttribute("style", 'color:red');
            start_info.innerHTML = "The substrate should satisify the standard KEGG ID, for example, C00011 in KEGG represents Carbon dioxide.See more detail in https://www.kegg.jp/";
        } else {
            start_info.innerHTML = "";
            document.getElementById("substrate").value = start.replace(/^\s+|\s+$/g, '');
        }
        if (!patternCompound.test(end)) {
            check = false;
            end_info.innerHTML = "The product should satisify the standard KEGG ID, for example, C00011 in KEGG represents Carbon dioxide.See more detail in https://www.kegg.jp/";
        } else {
            end_info.innerHTML = "";
            document.getElementById("substrate").value = start.replace(/^\s+|\s+$/g, '');
        }
        if (isNaN(n)) {
            check = false;
            minatom_info.innerHTML = "being smaller than 5,minimum atomic groups is number.";
        } else {
            minatom_info.innerHTML = "";
        }
        if (check) {
            document.getElementById("myForm").submit();
            document.getElementById("formsubmit").disabled = "disabled";
            afterSubmit();
        }
    }

    function afterSubmit() {
        document.getElementById('yourStart').innerText = document.getElementById('substrate').value;
        document.getElementById('yourTarget').innerText = document.getElementById('product').value;
        document.getElementById('yourAtomNum').innerText = document.getElementById('minatomic').value;
        document.getElementById('formResponse').style.removeProperty('display');
        document.getElementById('mainDiv').style.display = 'none';
    }


</script>
</html>
