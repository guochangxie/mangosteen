
function saveProject() {
    $("#hf-projectName").validate("项目名称");
    $("#hf-codeBranch").validate("开发分支");
    $("#tomcatTable input").each(function (){
        if($(this).val()==""){
            alert("Tomcat信息不能为空！");
            $(this).focus();
            throw SyntaxError();
        }
    });


    var tomcatTableTrs=$("#tomcatTable tr");
    var tableData=[];
    for (i=1;i<tomcatTableTrs.length;i++){
        var serverIp=$(tomcatTableTrs[i]).find("[name='hf-ip']").val();
        var serverPort=$(tomcatTableTrs[i]).find("[name='hf-dumpPort']").val();
        var serverContainer=$(tomcatTableTrs[i]).find("[name='hf-tomcatName']").val();
        tableData.push({"serverIp":serverIp,"serverPort":serverPort,"serverContainer":serverContainer});

    }

    var projectName= $("#hf-projectName").val();
    var codeBranch= $("#hf-codeBranch").val();

    $.ajax({

        type: "POST",
        data:JSON.stringify({
            "projectName":projectName,
            "codeBranch":codeBranch,
            "projectConfig":tableData
        }),
        //方法所在页面和方法名
        url: "/mangosteen/project/saveProject/",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if(data=="SUCCESS"){
                alert("添加项目成功!");
            }
        },
        error: function (err) {
            alert("添加项目错误!");
        }
    });


}

function  toEexcuteProject(obj) {
    var projectName=$(obj).parent().parent().find("td").eq(0).text();
    $.StandardPost("/mangosteen/project/toExecuteProject",{'projectName':projectName});
}

function  tobuildHistory(obj) {
    var projectName=$(obj).parent().parent().find("td").eq(0).text();
    //window.location.href = "/mangosteen/project/toExecuteProject?projectName="+projectName;
    $.StandardPost("/mangosteen/project/tobuildHistory",{'projectName':projectName});
}

function addRow(obj) {

    var addContent = "<tr class=\"tr-shadow\">" +
        "<td>" +
        "<input  name=\"hf-ip\""+
        "class=\"form-control\">" +
        "</td>" +
        "<td>" +
        "<input  name=\"hf-tomcatName\"" +
        "class=\"form-control\">" +
        "</td>" +
        "<td>" +
        "<input  name=\"hf-dumpPort\""+
        "class=\"form-control\" onkeyup='this.value=this.value.replace(/\D/gi,\"\")'>"+
        "</td>" +
        "<td>" +
        "<button type=\"button\" class=\"btn btn-success btn-sm\" onclick=\"addRow(this)\">添加</button>" +"&nbsp"+
        "<button type=\"button\" class=\"btn btn-danger btn-sm\" onclick=\"delRow(this)\">删除</button>" +
        "</td>" +
        "</tr>";

    $(obj).parent().parent().after(addContent);
}

function delRow(obj) {
    //移除
    if($(obj).parent().parent().siblings().length==0){
        alert("不能删除最后一行!")
        return;
    }
    $(obj).parent().parent().remove();

}

function toReportDetail(obj) {
   var projectName=$(obj).parent().parent().find("input[name=projectName]").val();
    var reportPath=$(obj).parent().parent().find("input[name=reportPath]").val();
    var codeBranch=$(obj).parent().parent().find("td").eq(0).text();
    var serverIp=$(obj).parent().parent().find("td").eq(2).text();
    $.StandardPost("/mangosteen/project/reportDetail",{'projectName':projectName,'reportPath':reportPath,'codeBranch':codeBranch,'serverIp':serverIp});

}

function buildReport(obj) {

    $(obj).attr("disabled","true"); //设置变灰按钮
    $(obj).html("正在构建...");
    var buildReportForm=JSON.stringify($('#buildReportForm').serializeObject());
    $.StandardPost("/mangosteen/project/buildReport",{'buildReportForm':buildReportForm})

}
function buildIncrementReport(obj) {
    $(obj).attr("disabled","true"); //设置变灰按钮
    $(obj).html("正在构建...");
    var buildReportForm=JSON.stringify($('#buildReportForm').serializeObject());
    $.StandardPost("/mangosteen/project/buildReport",{'buildReportForm':buildReportForm,'Increment':true})

}
var websocket = null;
function openSocket() {

    if(websocket==null){
         websocket = new WebSocket('ws://10.10.129.212:8086/mangosteen/log');
         websocket.onmessage = function(event) {
            // 接收服务端的实时日志并添加到HTML页面中
            $("#filelog-container div").append(event.data);
            // 滚动条滚动到最低部
            $("#filelog-container").scrollTop($("#filelog-container div").height() - $("#filelog-container").height());
        };
    }



}

function closeSocket() {
    if (websocket != null) {
        websocket.close();
        websocket=null;
    }
}
function buildLog() {

    $("#filelog-container").dialog({
        modal:true,
        width:"50%",
        height: 450,
        modal: true,
        draggable:true,  //拖拽
        resizable:true,  //缩放
        open:function(){
            openSocket()
        },
        close:function(){
            closeSocket()
        }
    });

    $("#filelog-container").css({background: "#000",color: "#fff"});

}

function addProject() {
    window.location.href = "/mangosteen/project/toAddProject";

}

function isLogin() {

    var isSuccess=false;
    var params=$('#loginForm').serialize();
    $.ajax({
        type: "POST",
        data:params,
        async:false,
        url: "/mangosteen/project/isLogin",

        success: function (data) {
            if("SUCCESS"==data){
                 isSuccess=true;
            }
        }
    });

    if(isSuccess){
        window.location.href="/mangosteen/project/showProject";
        window.event.returnValue=false;

    }else {
        alert("用户名或密码错误");
    }

}

function resetCoverage(){
    var projectName=$("#hf-projectName").val();
    var ip=$("#hf-ip").val();

    $.ajax({
        type: "POST",
        data:{projectName:projectName,ip:ip},
        url: "/mangosteen/project/resetCoverage",

        success: function (data) {
            if("SUCCESS"==data){
                alert("覆盖率重置成功");

            }else { alert("覆盖率重置失败");
            }

        }
    });

}

function selectIPByProjectName() {
  var projectName=$("#hf-projectName").val();
    $.ajax({
        type: "POST",
        data:{projectName:projectName},
        async:false,
        url: "/mangosteen/project/queryServerIP",

        success: function (data) {
            $("#hf-ip").empty();
            $.each(data,function(index,item){
                var opt=$("<option value="+item.serverIp+">"+item.serverIp+"</option>")
                $("#hf-ip").append(opt)
            });
        }
    });
}


