<%--
  Create By FangYan On 2018/01/02
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>获取token</h2>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script>
    $(function() {
        $.ajax({
            url:"http://localhost:8088/oauth/token",
            type:"post",
            headers:{
                Authorization:"Basic c3VueXVhbjpzdW55dWFu",
                deviceId:"123456"
            },
            xhrFields: {
                withCredentials: true
            },
            dataType:"json",
            data:{
                grant_type:"password",
                username:"peter",
                password:"123456",
                scope:"all",
                imageCode:"5877"
            },
            success:function (data) {
                alert(data.result);
            },
            error:function () {
                alert("失败啦");
            }
        })
    })
</script>
</body>
</html>
