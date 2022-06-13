/**
 * Created by Amy on 2018/8/7.
 */

var projectName = getCookie('projectName');
var projectContent = getCookie('projectContent');
var projectId = getCookie('projectId');
// //console.log(projectName, projectContent, projectId);

var flag = true;

$(function() {
    isLoginFun();
    header();
    $("#ctl01_lblUserName").text(getCookie('userName'));
    $("#projectName").val(projectName);
    $("#inputIntro").val(projectContent);
});

//点击“保存修改”，编辑项目
function modifyProject() {
    var projectNameInt = $("#projectName").val();
    var projectContentInt = $("#inputIntro").val();
    editProjectRight(projectNameInt, projectContentInt);
}

function editProjectRight(projectNameInt, projectContentInt) {

    if (flag == true) {
        if (projectNameInt.trim() == '') {
            layer.msg('请完整填写项目名称')
        } else if (projectContentInt.trim() == '') {
            layer.msg('请完整填写项目描述')
        } else {
            var userName = getCookie("userName");
            var url = '/queryAllProjectName';
            var da = {}
            commonAjaxPost(false, url, da, function(result) {
                console.log(result)
                if (result.code == "666") {
                    if (result.data.length) {
                        for (var i = 0; i < result.data.length; i++) {
                            if (projectNameInt == result.data[i].projectName) {
                                layer.msg('该项目名称重复，请重新命名', { icon: 2 });
                                break;
                            }
                            if (i == result.data.length - 1) {
                                var url = '/modifyProjectInfo';
                                var data = {
                                    "id": projectId,
                                    "projectName": projectNameInt,
                                    "projectContent": projectContentInt

                                };
                                commonAjaxPost(true, url, data, function(result) {
                                    if (result.code == '666') {
                                        layer.msg('修改成功');
                                        setTimeout(function() {
                                            window.location.href = "myQuestionnaires.html"
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
                        }
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
    }
}