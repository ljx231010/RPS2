<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.bootcss.com/semantic-ui/2.4.1/semantic.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/me.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/notiflix-3.2.5.min.css">
    <script src="${pageContext.request.contextPath}/static/js/notiflix-3.2.5.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/vis-network.min.js"></script>
    <script type="text/javascript" src="https://unpkg.com/vis-data@latest/peer/umd/vis-data.min.js"></script>
    <style>
        .msg {
            display: inline-block;
            color: green;
            background-size: 20px;
            padding-left: 30px;
        }

        .wrong {
            color: red;
        }

        .right {
            color: green;
        }

        li {
            text-align: left;
            font-size: 17px;
        }

        #mynetwork {
            width: 1130px;
            height: 400px;
            border: 1px solid lightgray;
        }

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
    <title>Reaction_Show</title>
</head>
<body class="bg1">
<%@include file="nav.jsp" %>
<div class="ui center aligned container" style="margin-top: 5em; width: 80%; ">
    <h3 class="ui header" id="path"><b>Current
        Pathway:&nbsp;&nbsp;${requestScope.get("path")}</b></h3>
    <h3 class="ui header">Current
        host organism:&nbsp;&nbsp;&ensp;${requestScope.get("curSpecies").getSpeciesName()}</h3>
    <input type="hidden" id="c1" value="${requestScope.get('cmap')}">
    <form id="searchForm" name="searchForm" action="#" method="get">
        <div class="ui accordion">
            <div class="title">
                <i class="dropdown icon"></i><b>Customize weights when recommending foreign enzyme</b>
            </div>
            <div class="content">
                <br>
                <label><b>Phylogenetic distance weights:&nbsp;</b></label>
                <input type="number" name="disWeight" id="disWeight" value="0.5" style="width: 80px;"
                       min="0.0" max=1 step="0.1" onblur="check('disWeight')"/>
                <p class="msg" id="disWeightMsg">Please enter a value between 0 and 1,or you can change nothing</p>
                <br/><br/><br>
                <label><b>Km Weights:&nbsp;</b></label>
                <input type="number" name="KMWeight" id="KMWeight" value="0.5" style="width: 80px;"
                       min="0.0" max=1 step="0.1" onblur="check('KMWeight')"/>
                <p class="msg" id="KMWeightMsg">Please enter a value between 0 and 1,or you can change nothing</p>
                <br/><br/><br>
            </div>
        </div>
        <table class="ui celled table" style="text-align: center;">
            <thead>
            <tr>
                <th>RId <span title="Reaction ID of KEGG" style="cursor: help;" data-toggle="tooltip"
                              data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Formula <span title="Reaction formula" style="cursor: help;" data-toggle="tooltip"
                                  data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>IER <span title="Is it an endogenous reaction?" style="cursor: help;" data-toggle="tooltip"
                              data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Enzyme <span title="EC Number" style="cursor: help;" data-toggle="tooltip"
                                 data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
                <th>Recommend<span
                        title="Click to suggest potential enzyme" style="cursor: help;"
                        data-toggle="tooltip"
                        data-placement="right">
			<sup style="color:black;"><i class="question circle outline icon" aria-hidden="true"></i></sup></span></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="reaction" items="${requestScope.get('speciesReactions')}" varStatus="kvStatus">
                <tr>
                    <td id="reaction${kvStatus.count}">
                        <a href="https://www.genome.jp/entry/${reaction.getRId()}"
                           target="_blank">${reaction.getRId()}</a>
                    </td>
                    <td id="td${kvStatus.count}">${reaction.getEquation()}</td>
                    <td>${reaction.getIfForeign()}</td>
                    <td>
                        <c:forEach var="s" items="${reaction.getEcNumber()}">
                            <a href="https://www.genome.jp/entry/ec:${s}" target="_blank">${s}</a>&nbsp;
                        </c:forEach>
                    </td>
                    <td>
                        <c:if test="${not reaction.getIfForeign()}">
                            <button class="ui button">
                                <a href="javascript:void(0)" style="text-decoration: none;"
                                   onclick="openUrl('${requestScope.get("curSpecies").getSpeciesId()}','${reaction.getRId()}')">RECOMMEND</a>
                            </button>
                            <input type="hidden" name="fr" value="${reaction.getRId()}"/>
                        </c:if>
                        <c:if test="${reaction.getIfForeign()}">
                            ---
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <br>
        <p style="text-align: left;font-size: 20px;font-weight: bold;">The following figure shows the current pathway, where:</p>
        <ul>
            <li><b>Ellipses</b> represent compounds,&nbsp; <b>arrows</b> with lines connecting ellipses represent
                reactions;
            </li>
            <br>
            <li><span style="color: red;font-weight: bold;">Red</span> ellipse represents the starting compound,&nbsp;
                <span style="color: orange;font-weight: bold;">orange</span> ellipse represents the target
                compound,&nbsp;<span style="color: deepskyblue;font-weight: bold;">blue</span> ellipses represent other
                compounds;
            </li>
            <br>
            <li><span style="color: green;font-weight: bold;">Green</span> edge represents endogenous reaction,&nbsp;
                <span style="color: purple;font-weight: bold;">purple</span> edge represents foreign reaction.
            </li>
        </ul>
        <div id="mynetwork" class="center"></div>
    </form>
    <input hidden="hidden" id="deadEndMessage" value="${requestScope.get("deadEndMessage")}">
    </input>
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
<script type="text/javascript">
    $(document).ready(function () {
        $('.ui.accordion').accordion({
            duration: 'click'
        });
    });
</script>
<script>
    function check(id) {
        var a = document.getElementById(id).value;
        var pId = id + 'Msg';
        console.log(pId);
        var msg = document.getElementById(pId);
        if (a < 0 || a > 1) {
            msg.className = 'msg wrong';
            msg.innerHTML = 'Error, please enter a value between 0 and 1';

        } else {
            msg.className = 'msg right'
            msg.innerHTML = 'Okay';
        }
    }

    function openUrl(speciesId, rId) {
        var disWeight = document.getElementById("disWeight").value;
        var KMWeight = document.getElementById("KMWeight").value;
        console.log(disWeight);
        console.log("disWeight:" + disWeight);
        console.log("KMWeight:" + KMWeight);
        if (disWeight > 1 || disWeight < 0 || KMWeight > 1 || KMWeight < 0) {
            Notiflix.Report.failure('Recommend Failure', 'There is a problem with the parameter value. Please check', 'Okay');
        } else {
            var url = "/RPS" + "/foreignEnzymeRecommend?speciesId=" + speciesId + "&reactionId=" + rId + "&disWeight=" + disWeight + "&KMWeight=" + KMWeight;
            window.open(url);
        }
    }

    function addHref() {
        var s = document.getElementById("deadEndMessage").value;
        console.log("deadEndMessage" + s);
        var messages = s.split("-");
        var arrays = new Array(messages.length);
        for (let i = 0; i < messages.length; i++) {
            var split = messages[i].split(";");
            let cId = split[0];
            let rId = split[1];
            let str = split[2];
            console.log(cId, rId, str);
            arrays[i] = [cId, rId, str];
        }
        console.log(arrays);
        var j = 1;
        while (j > 0) {
            var td = document.getElementById("td" + j);
            if (td === null) {
                break;
            } else {
                var reactionId = document.getElementById("reaction" + j).innerText;
                console.log("reactionId" + reactionId);
                var t = document.getElementById("td" + j).innerText;
                console.log(t);
                var array = t.split(" ");
                var arr1 = [];
                var pattern = new RegExp("C[0-9]{5}")
                for (let i = 0; i < array.length; i++) {
                    if (pattern.test(array[i])) {
                        let flag = false;
                        for (let k = 0; k < arrays.length; k++) {
                            console.log(1);
                            console.log(reactionId);
                            console.log(arrays[k][1]);
                            console.log(array[i]);
                            console.log(arrays[k][0]);
                            console.log(2);
                            if (reactionId === arrays[k][1] && array[i] === arrays[k][0]) {
                                flag = true;
                                arr1[i] = '<a style="background-color: #ffae19; text-decoration:none" target=_blank href="https://www.genome.jp/entry/' + array[i] +
                                    '" title="' + arrays[k][2] + '">' + array[i] + '</a>';
                                console.log("arr1[i]:" + arr1[i]);
                                console.log("1433223");
                                break;
                            }
                        }
                        if (!flag) {
                            arr1[i] = '<a href="https://www.genome.jp/entry/' + array[i] +
                                '" style="text-decoration: none;" target="_blank">' + array[i] + '</a>';
                        }
                    } else {
                        arr1[i] = array[i];
                    }
                }
                document.getElementById("td" + j).innerHTML = arr1.join(" ");
            }
            j++;
        }

    }

    window.onload = addHref();
</script>
<script>
    function createHTMLTitle(html) {
        var element = document.createElement("div");
        element.innerHTML = html;
        return element;
    }

    var cJsonData = {};
    var path = document.getElementById("path").innerText.split(":")[1].trim();
    $.post({
        url: "${pageContext.request.contextPath}/compoundIdToName",
        data: {'path': path},
        async: false,
        success: function (data, status) {
            console.log(status)
            console.log(data);
            cJsonData = JSON.parse(data);
        }
    });

    var rJsonData = {};
    $.post({
        url: "${pageContext.request.contextPath}/reactionIdToName",
        data: {'path': path},
        async: false,
        success: function (data, status) {
            console.log(status)
            console.log(data);
            rJsonData = JSON.parse(data);
        }
    });

    var fReaction = [];
    var nodes = document.getElementsByName("fr");
    for (let i = 0; i < nodes.length; i++)
        fReaction[i] = nodes[i].value;
    console.log("fReaction" + fReaction);
    var patternCompound = new RegExp("C[0-9]{5}");
    var patternReaction = new RegExp("R[0-9]{5}");
    var arr1 = path.split("->");
    var compounds = [];
    var reactions = [];
    for (let i = 0, j = 0, k = 0; i < arr1.length; i++) {
        if (patternCompound.test(arr1[i])) {
            compounds[j++] = arr1[i];
        }
        if (patternReaction.test(arr1[i])) {
            reactions[k++] = arr1[i];
        }
    }
    console.log(compounds);
    console.log(reactions);
    console.log(cJsonData);
    var nodesData1 = [];
    for (let i = 0; i < compounds.length; i++) {
        console.log(compounds[i]);
        if (i === 0) {
            nodesData1[i] = {
                id: i,
                label: compounds[i],
                shape: "ellipse",
                color: {
                    border: '#ff0000',
                    background: '#ff0000',
                    highlight: {
                        border: '#ff0000',
                        background: '#ff0000'
                    },
                    hover: {
                        border: '#ff0000',
                        background: '#ff0000'
                    }
                },
                title: createHTMLTitle(cJsonData[compounds[i]] + '<br><img src="https://www.genome.jp/Fig/compound/' + compounds[i] + '.gif" width="170px" height="170px"/>'),
            };
        } else if (i === compounds.length - 1) {
            nodesData1[i] = {
                id: i,
                label: compounds[i],
                shape: "ellipse",
                color: {
                    border: 'orange',
                    background: 'orange',
                    highlight: {
                        border: 'orange',
                        background: 'orange'
                    },
                    hover: {
                        border: 'orange',
                        background: 'orange'
                    }
                },
                title: createHTMLTitle(cJsonData[compounds[i]] + '<br><img src="https://www.genome.jp/Fig/compound/' + compounds[i] + '.gif" width="170px" height="170px"/>'),
            };
        } else {
            console.log(cJsonData[compounds[i]]);
            nodesData1[i] = {
                id: i,
                label: compounds[i],
                shape: "ellipse",
                title: createHTMLTitle(cJsonData[compounds[i]] + '<br><img src="https://www.genome.jp/Fig/compound/' + compounds[i] + '.gif" width="170px" height="170px"/>'),
            };
        }
    }
    var edgesData1 = [];
    for (let i = 0; i < reactions.length; i++) {
        if (fReaction.includes(reactions[i])) {
            edgesData1[i] = {
                from: i,
                to: i + 1,
                label: reactions[i],
                color: {
                    color: 'purple',
                    highlight: 'purple',
                },
                title: rJsonData[reactions[i]],
            };
        } else {
            edgesData1[i] = {
                from: i,
                to: i + 1,
                label: reactions[i],
                title: rJsonData[reactions[i]],
            };
        }

    }

    var container = document.getElementById('mynetwork');

    var data = {
        nodes: new vis.DataSet(nodesData1),
        edges: new vis.DataSet(edgesData1)
    };

    var options = {
        nodes: {
            shape: 'ellipse',
            font: {
                color: '#000',
                size: 16,
            },
            borderWidth: 1,
            borderWidthSelected: 3,
            color: {
                border: '#2B7CE9',
                background: '#97C2FC',
                hover: {
                    border: 'purple',
                    background: '#D2E5FF'
                }
            },
            shadow: true,
            fixed: false,
            scaling: {
                min: 10,
                max: 5,
                label: true
            },
        },
        edges: {
            arrows: {
                to: {
                    enabled: true,
                    type: "arrow",
                }
            },
            length: 80,
            color: {
                color: '#00ff00',
                highlight: '#00ff00',
                inherit: 'from',
                opacity: 1.0
            },
            smooth: {
                enabled: true,
                type: "curvedCW",
                roundness: 0.2
            },
            hoverWidth: 2,
        },
        interaction: {
            hover: true,
            hoverConnectedEdges: true,
        },
        layout: {
            improvedLayout: true,
            hierarchical: {
                direction: "LR",
                sortMethod: 'directed',
                shakeTowards: 'roots',
            }
        },
    };
    var network = new vis.Network(container, data, options);
</script>
</html>
