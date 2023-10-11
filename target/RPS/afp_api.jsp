
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.bootcss.com/semantic-ui/2.4.1/semantic.min.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico"/>
    <link rel="stylesheet" href="./static/css/me.css">
    <title>AFP</title>
</head>
<body class="bg1">
<%@include file="nav.jsp" %>
<div class="ui container" id="mainDiv" style="margin-top: 5em; width: 70%;">
    <div class="ui segment" style="font-size:20px;">
        <p style="font-weight:700;text-align:center">AFP(Atom-group Flux Pathfinder)</p>
        <p>AFP incorporates the movements of atom group into the reaction stoichiometry
            to construct MILP model to search the pathways containing atom group exchange
            in the reactions and adapts the stoichiometric MILP model to provide the options
            of searching pathways from an arbitrary or given compound to a target compound.
            For a given target compound, our method AFP identifies the metabolic pathways
            that convert the user-defined or arbitrary starting compound to the target compound
            via three main steps: We first use the reactions that transfer atom groups between
            substrates and products to construct atom group transfer network. Then, we combine
            the reaction stoichiometry with the atom group exchanged within reactions to construct
            a MILP model based on the atom group transfer network. Finally, we solve the objective
            function of the constructed MILP model to find the linear pathways that starting from the
            user-defined or arbitrary compound to the target compound by tracking the motion of
            atom groups in the built atom group transfer network.
        </p>
    </div>
    <h3 class="ui header" style="text-align: left;">
        <i class="paper plane icon"></i>Pathway Search
    </h3>
    <div class="ui attached segment">
        <form class="ui form" action="${pageContext.request.contextPath}/afpapi" id="conf_form" method="get"
              style="font-size:20px;">
            <div class="field">
                <h4 class="ui header">KEGG ID for Start Compound:</h4>
                <div class="ui input ">
                    <input name="startCompound" id="startCompound" type="text" onblur="checkStart()"
                           placeholder="For example: C00022 or arbitrary">
                </div>
                <p id="start_info"></p>
            </div>
            <div class="field">
                <h4 class="ui header">KEGG ID for Target Compound:</h4>
                <div class="ui input ">
                    <input name="targetCompound" id="targetCompound" type="text" onblur="checkEnd()"
                           placeholder="For example: C00183">
                </div>
                <p id="end_info"></p>
            </div>
            <div class="field">
                <h4 class="ui header">Number Of The Minimal Atom Groups:</h4>
                <div class="ui input ">
                    <input name="minatomic" id="minatomic" type="text" onblur="checkAtomNum()"
                           placeholder="For example: 2">
                </div>
                <p id="atom_info"></p>
            </div>

            <div class="field">
                <h4 class="ui header">Number of Resulting Pathways:</h4>
                <div class="ui input ">
                    <input name="solutionNumber" id="solutionNumber" type="text" onblur="checkResultPathNum()"
                           placeholder="For example: 10">
                </div>
                <p id="solu_info"></p>
            </div>

            <div class="field">
                <h4 class="ui header">Time Limit (second) :</h4>
                <div class="ui input ">
                    <input name="timeLimit" id="timeLimit" type="text" onblur="checkLimitTime()"
                           placeholder="For example: 200">
                </div>
                <p id="time_info"></p>
            </div>

            <div class="field">
                <h4 class="ui header">Number of the visualized resulting pathways:</h4>
                <div class="ui input ">
                    <input name="drawNpathways" id="drawNpathways" type="text" onblur="checkDrawPathNum()"
                           placeholder="For example: 10">
                </div>
                <p id="path_info"></p>
            </div>

            <h4 class="ui header">Searching Strategy:</h4>
            <div class="inline fields" id="d1">
                <div class="field">
                    <input type="hidden" name="alpha" id="alpha">
                    <div class="ui checkbox" style="font-size:20px;">
                        <input type="radio"  name="searchingStrategy" value="conserved">
                        <label fer="overlapping">conserved</label>
                    </div>

                    <div class="field">
                        <div class="ui checkbox" style="font-size:20px;">
                            <input type="radio" id="searchingStrategy" name="searchingStrategy" value="non-conserved"
                                   checked>
                            <label fer="default">default(non-conserved)</label>
                        </div>
                    </div>

                </div>
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
        <table class="ui celled table" style="font-size: 16px">
            <thead>
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
            <tr>
                <td>4</td>
                <td>Number of Resulting Pathways</td>
                <td>Number of Resulting Pathways</td>
            </tr>
            <tr>
                <td>5</td>
                <td>Time Limit</td>
                <td>Time limit for searching pathways (in seconds)</td>
            </tr>
            <tr>
                <td>6</td>
                <td>Number of the visualized resulting pathways</td>
                <td>Number of the visualized resulting pathways</td>
            </tr>
            <tr>
                <td>7</td>
                <td>Searching Strategy</td>
                <td>The searching strategies for tracking atom group can be chosen: non-conserved, conserved, default.
                    The non-conserved/default strategy means that AFP searches the pathways containing non-conserved
                    atom group exchange in the reactions.
                    The conserved strategy means that AFP searches the pathways transferring conserved atom groups from
                    the start to target compounds.
                </td>
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
        <br><br><li><span style="font-size: 20px;">Number of resulting pathways&nbsp;:&nbsp;</span><span style="font-weight: bold;" id="yourResultPathNum"></span>
        <br><br><li><span style="font-size: 20px;">Time limit (second)&nbsp;:&nbsp;</span><span style="font-weight: bold;" id="yourLimitTime"></span>
        <br><br><li><span style="font-size: 20px;">Number of the visualized resulting pathways&nbsp;:&nbsp;</span><span style="font-weight: bold;" id="yourDrawPathNum"></span>
        <br><br><li><span style="font-size: 20px;">Searching strategy&nbsp;:&nbsp;</span><span style="font-weight: bold;" id="yourStrategy"></span>
    </ul>
    <br><br /><br />
    <br><br /><br />
    <br><br /><br />
    <br><br /><br />
</div>
</body>
<footer class="ui inverted vertical segment">
    <div class="ui center aligned container">
        <p class="" style="margin-bottom: 0px">©Guangxi University,
            Nanning, China</p>
        <p>College of computer and electronic information</p>
    </div>
</footer>
<script>
    function checkStart() {
        var start = document.getElementById('startCompound').value;
        var patternCompound = new RegExp("C[0-9]{5}");
        start_info = document.getElementById('start_info');
        if (!patternCompound.test(start)) {
            start_info.setAttribute("style", 'color:red');
            start_info.innerHTML = "The substrate should satisfy the standard KEGG ID, for example, C00011 in KEGG represents Carbon dioxide.See more detail in https://www.kegg.jp/";
            return false;
        } else {
            start_info.innerHTML = "";
            return true;
        }
    }

    function checkEnd() {
        var end = document.getElementById('targetCompound').value;
        var patternCompound = new RegExp("C[0-9]{5}");
        end_info = document.getElementById('end_info');
        if (!patternCompound.test(end)) {
            end_info.setAttribute("style", 'color:red');
            end_info.innerHTML = "The target should satisfy the standard KEGG ID, for example, C00011 in KEGG represents Carbon dioxide.See more detail in https://www.kegg.jp/";
            return false;
        } else {
            end_info.innerHTML = "";
            return true;
        }
    }

    function checkAtomNum() {
        var num = document.getElementById('minatomic').value;
        var atom_info = document.getElementById('atom_info');
        if (isNaN(parseInt(num)) || (parseInt(num) !== parseFloat(num))) {
            atom_info.className = 'msg wrong';
            atom_info.innerHTML = 'Please enter the value as an integer!';
            return false;
        } else {
            atom_info.innerHTML = "";
            return true;
        }
    }

    function checkResultPathNum() {
        var num = document.getElementById('solutionNumber').value;
        var solu_info = document.getElementById('solu_info');
        if (isNaN(parseInt(num)) || (parseInt(num) !== parseFloat(num))) {
            solu_info.className = 'msg wrong';
            solu_info.innerHTML = 'Please enter the value as an integer!';
            return false;
        } else {
            solu_info.innerHTML = "";
            return true;
        }
    }

    function checkLimitTime() {
        var num = document.getElementById('timeLimit').value;
        var time_info = document.getElementById('time_info');
        if (isNaN(parseInt(num)) || (parseInt(num) !== parseFloat(num))) {
            time_info.className = 'msg wrong';
            time_info.innerHTML = 'Please enter the value as an integer!';
            return false;
        } else {
            time_info.innerHTML = "";
            return true;
        }
    }

    function checkDrawPathNum() {
        var num = document.getElementById('drawNpathways').value;
        var path_info = document.getElementById('path_info');
        if (isNaN(parseInt(num)) || (parseInt(num) !== parseFloat(num))) {
            path_info.className = 'msg wrong';
            path_info.innerHTML = 'Please enter the value as an integer!';
            return false;
        } else {
            path_info.innerHTML = "";
            return true;
        }
    }

    function submit_form() {
        var startFlag = checkStart();
        var endFlag = checkEnd();
        var k1 = checkAtomNum();
        var k2 = checkResultPathNum();
        var k3 = checkLimitTime();
        var k4 = checkDrawPathNum();
        if (startFlag === true && endFlag === true && k1 === true && k2 === true && k3 === true && k4 === true) {
            document.getElementById("conf_form").submit();
            document.getElementById("formsubmit").disabled = "disabled";
            console.log(111);
            afterSubmit();
            console.log(222);
        }
    }

    function afterSubmit() {
        console.log("aftersubmit");
        document.getElementById('yourStart').innerText = document.getElementById('startCompound').value;
        document.getElementById('yourTarget').innerText = document.getElementById('targetCompound').value;
        document.getElementById('yourAtomNum').innerText = document.getElementById('minatomic').value;
        document.getElementById('yourResultPathNum').innerText = document.getElementById('solutionNumber').value;
        document.getElementById('yourLimitTime').innerText = document.getElementById('timeLimit').value;
        document.getElementById('yourDrawPathNum').innerText = document.getElementById('drawNpathways').value;
        document.getElementById('yourStrategy').innerText = getBoxValue();
        document.getElementById('formResponse').style.removeProperty('display');
        document.getElementById('mainDiv').style.display = 'none';
    }


    function getBoxValue(){
        var obj = document.getElementsByName("searchingStrategy");
        for(var i=0; i<obj.length; i ++){
            if(obj[i].checked){
                return obj[i].value;
            }
        }
    }
</script>
<script src="${pageContext.request.contextPath}/static/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/semantic.min.js"></script>
</html>
