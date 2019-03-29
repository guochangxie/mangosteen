/**
 * 校验文本是否为空
 * tips：提示信息
 * 使用方法：$("#id").validate("提示文本");
 * @itmyhome
 */
$.fn.validate = function(tips){

    if($(this).val() == "" || $.trim($(this).val()).length == 0){
        alert(tips + "不能为空！");
        $(this).focus();
        throw SyntaxError();
    }
}
/**
 * 扩展jQuery动态创建表单，post提交数据后页面可以跳转
 * url：要跳转的页面
 * args：后台传递参数,{arg0:'arg0',arg1:'arg1'}
 * 用法为：$.StandardPost('url/path/req',{arg0:'arg0',arg1:'arg1'});
 */
$.extend({
    StandardPost:function(url,args){
        var body = $(document.body),
            form = $("<form method='post'></form>"),
            input;
        form.attr({"action":url});
        $.each(args,function(key,value){
            input = $("<input type='hidden'>");
            input.attr({"name":key});
            input.val(value);
            form.append(input);
        });

        form.appendTo(document.body);
        form.submit();
        document.body.removeChild(form[0]);
    }
});


$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};



function changeFrameHeight(){
    var ifm= document.getElementById("reportIframe");
    ifm.height=document.documentElement.clientHeight;
    ifm.weight=document.documentElement.clientWidth;

}


window.onresize=function(){
    changeFrameHeight();

}


