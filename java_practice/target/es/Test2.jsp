<%--
  Create By FangYan On 2017/11/08
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!doctype html>
<head>
    <meta charset=utf-8>
    <title>File Upload Progress Demo</title>
    <style>
        body { padding: 30px }
        form { display: block; margin: 20px auto; background: #eee; border-radius: 10px; padding: 15px }

        .progress { position:relative; width:400px; border: 1px solid #ddd; padding: 1px; border-radius: 3px; }
        .bar { background-color: #B4F5B4; width:0%; height:20px; border-radius: 3px; }
        .percent { position:absolute; display:inline-block; top:3px; left:48%; }
    </style>
</head>
<body>
<form action="upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="myfile" /><br>
    <input type="submit" value="Upload File to Server">
</form>

<div class="progress">
    <div class="bar"></div >
    <div class="percent">0%</div >
</div>

<div id="status"></div>

<script src="js/jquery-1.8.3.min.js"></script>
<script src="js/jquery.form.js"></script>
<script>

    $(function(){
        var bar = $('.bar');
        var percent = $('.percent');
        var status = $('#status');
        $('form').ajaxForm({
            beforeSerialize:function(){
//                alert("表单数据序列化前执行的操作！");
//                alert("表单数据序列化前执行的操作！");
                //$("#txt2").val("java");//如：改变元素的值
            },
            beforeSubmit:function(){
                //alert("表单提交前的操作");
                var filesize = $("input[type='file']")[0].files[0].size/1024/1024;
                if(filesize > 20){
                    alert("文件大小超过限制，最多10M");
                    return false;
                }
                //if($("#txt1").val()==""){return false;}//如：验证表单数据是否为空
            },
            beforeSend: function() {
                status.empty();
                var percentVal = '0%';
                bar.width(percentVal)
                percent.html(percentVal);
            },
            uploadProgress: function(event, position, total, percentComplete) {//上传的过程
                //position 已上传了多少
                //total 总大小
                //已上传的百分数
                var percentVal = percentComplete + '%';
                bar.width(percentVal)
                percent.html(percentVal);
                //console.log(percentVal, position, total);
            },
            success: function(data) {//成功
                var percentVal = '100%';
                bar.width(percentVal)
                percent.html(percentVal);
                alert(data);
            },
            /*error:function(err){//失败
                alert("表单提交异常！"+err.msg);
            },*/
            complete: function(xhr) {//完成
                status.html(xhr.responseText);
            }
        });

    });

</script>
