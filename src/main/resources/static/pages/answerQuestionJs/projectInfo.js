/**
 * Created by Amy on 2018/8/7.
 */
$(function() {
    isLoginFun();
    header();
    $("#ctl01_lblUserName").text(getCookie('userName'));
    getProjectInfo();

});

// 查看项目详细信息
function getProjectInfo() {
    var projectId = getCookie('projectId');

    var url = '/queryProjectList';
    var data = {
        "id": projectId
    };
    commonAjaxPost(true, url, data, getProjectInfoSuccess);

}

// 查看项目详细信息成功
function getProjectInfoSuccess(result) {
    console.log(result)
    if (result.code == "666") {
        var projectInfo = result.data[0];
        var questionList = result.data[0].questionList;
        $("#projectNameSpan").text(projectInfo.projectName);
        $("#createTimeSpan").text(timeFormat(projectInfo.createDate));
        $("#adminSpan").text(projectInfo.createdBy);
        $("#projectContentSpan").text(projectInfo.projectContent);

        var text = "";

        if (questionList.length) {
            for (var i = 0; i < questionList.length; i++) {
                text += "<tr>"
                text += "    <td style=\"text-align: center;color: #333333\" colspan=\"1\">" + (i + 1) + "</td>";
                text += "    <td style=\"text-align: center;color: #333333\" colspan=\"1\">" + questionList[i].questionName + "</td>";
                if (questionList[i].releaseTime == null) {
                    text += "    <td style=\"text-align: center;color: #333333\" colspan=\"1\">" + "暂未发布" + "</td>"; //发布时间
                } else {
                    text += "    <td style=\"text-align: center;color: #333333\" colspan=\"1\">" + timeFormat(questionList[i].releaseTime) + "</td>"; //发布时间
                }
                text += "    <td style=\"text-align: center;color: #333333\" colspan=\"1\">" +
                    "<a href=\"javascript:void(0)\" onclick=\"editQuest(" + "'" + questionList[i].id + "'," + "'" + questionList[i].questionName + "'," + "'" + questionList[i].questionContent + "'," + "'" + questionList[i].endTime + "'," + "'" + questionList[i].creationDate + "'," + "'" + questionList[i].dataId + "'" + ")\">" +
                    "编辑" +
                    "</a>" + '&nbsp;&nbsp;|&nbsp;&nbsp;' +
                    "<a href=\"javascript:void(0)\" onclick=\"sendQuest(" + "'" + questionList[i].id + "'," + "'" + questionList[i].questionName + "'," + "'" + questionList[i].questionContent + "'," + "'" + questionList[i].endTime + "'," + "'" + questionList[i].creationDate + "'," + "'" + questionList[i].dataId + "'" + ")\">" +
                    "发布" +
                    "</a>" + '&nbsp;&nbsp;|&nbsp;&nbsp;' +
                    "<a href=\"javascript:void(0)\" onclick=\"designQuest(" + "'" + questionList[i].id + "'," + "'" + questionList[i].questionName + "'," + "'" + questionList[i].questionContent + "'," + "'" + questionList[i].endTime + "'," + "'" + questionList[i].creationDate + "'," + "'" + questionList[i].dataId + "'" + ")\">" +
                    "设计" +
                    "</a>" +'&nbsp;&nbsp;|&nbsp;&nbsp;' +
                    "<a href=\"javascript:void(0)\" onclick=\"stopQuest(" + "'" + questionList[i].id + "'," + "'" + questionList[i].questionName + "'," + "'" + questionList[i].questionContent + "'," + "'" + questionList[i].endTime + "'," + "'" + questionList[i].creationDate + "'," + "'" + questionList[i].dataId + "'" + ")\">" +
                    "停止" +
                    "</a>" +          
                    "</td>";
                text += "</tr>"
            }
        } else {
            text += "<tr>";
            text += "    <td style=\"text-align: center;color: #d9534f\" colspan=\"4\">暂无调查问卷</td>";
            text += "</tr>";
        }

        $("#questTableBody").empty();
        $("#questTableBody").append(text)

    } else if (result.code == "333") {
        layer.msg(result.message, { icon: 2 });
        setTimeout(function() {
            window.location.href = 'login.html';
        }, 1000)
    } else {
        layer.msg(result.message, { icon: 2 })
    }
}



//编辑问卷
function editQuest(id, name, content, endTime, creationDate, dataId) {
    var data = {
        "id": id
    };
    commonAjaxPost(true, '/selectQuestionnaireStatus', data, function(result) {
        if (result.code == "666") {
            if (result.data != "5") {
                layer.msg('问卷已发布，不可修改', { icon: 2 });
            } else if (result.data == "5") {
                deleteCookie("questionId");
                deleteCookie("questionName");
                deleteCookie("questionContent");
                deleteCookie("endTime");
                console.log(dataId)
                setCookie("questionId", id);
                setCookie("questionName", name);
                setCookie("questionContent", content);
                setCookie("endTime", endTime);
                setCookie("creationDate", creationDate);
                setCookie("dataId", dataId);
                window.location.href = 'editQuestionnaire.html'
            }
        }
        if (result.code == "666") {
            if (result.data == "1") {
                if ($("#operationAll" + m + n).children("a:first-child").text() == '开启') {
                    judgeIfChangeStatus(m, n);
                }
                layer.msg('问卷运行中，不可修改', { icon: 2 });
            } else

            if (result.data != "1") {
                commonAjaxPost(true, '/selectQuestSendStatus', { id: id }, function(result) {
                    //发送过问卷
                    if (result.code == "40003") {
                        setCookie("ifEditQuestType", "false");
                    } else if (result.code == "666") { //未发送过问卷
                        setCookie("ifEditQuestType", "true");
                    }
                });
                deleteCookie("questionId");
                deleteCookie("questionName");
                deleteCookie("questionContent");
                deleteCookie("endTime");
                setCookie("questionId", id);
                setCookie("questionName", name);
                setCookie("questionContent", content);
                setCookie("endTime", endTime);
                setCookie("creationDate", creationDate);
                setCookie("dataId", dataId);
                window.location.href = 'editQuestionnaire.html'
            }
        } else if (result.code == "333") {
            layer.msg(result.message, { icon: 2 });
            setTimeout(function() {
                window.location.href = 'login.html';
            }, 1000)
        } else {
            layer.msg(result.message, { icon: 2 })
        }
    });
}

//发布问卷
function sendQuest(id, name, content, endTime, creationDate, dataId) {
    deleteCookie("questionId");
    deleteCookie("questionName");
    deleteCookie("questionContent");
    deleteCookie("endTime");
    setCookie("questionId", id);
    setCookie("nameOfQuestionnaire", name);
    setCookie("questionContent", content);
    setCookie("endTime", endTime);
    setCookie("creationDate", creationDate);
    setCookie("dataId", dataId);
    window.location.href = 'sendQuestionnaire.html'
}
//设计问卷
function designQuest(id, name, content, endTime, creationDate, dataId) {
    deleteCookie("QuestionId");
    deleteCookie("questionName");
    deleteCookie("questionContent");
    deleteCookie("endTime");
    setCookie("QuestionId", id);
    setCookie("nameOfQuestionnaire", name);
    setCookie("questionContent", content);
    setCookie("endTime", endTime);
    setCookie("creationDate", creationDate);
    setCookie("dataId", dataId);
    window.location.href ='designQuestionnaire.html'+'?qId='+id;
}
//停止问卷
function stopQuest(id, name, content, endTime, creationDate, dataId) {
    var data = {
        "id": id,
        "questionStop":4
        };
    commonAjaxPost(true, '/modifyQuestionnaireStatus', data, function(result) {
        if (result.code == "666") {
                layer.msg('问卷已停止', { icon: 1 });
                setTimeout(function() {
                window.location.href = 'projectInfo.html';
            }, 1000)
        } else if (result.code == "333") {
            layer.msg(result.message, { icon: 2 });
            setTimeout(function() {
                window.location.href = 'login.html';
            }, 1000)
        } else {
            layer.msg(result.message, { icon: 2 })
        }
    });
}
