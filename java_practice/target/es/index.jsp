<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<body>
<h2>Hello World!</h2>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script>
    $(function() {
        $.ajax({
            url:"http://localhost:8088/code/image",
            type:"get",
            headers:{
              deviceId:123456
            },
            xhrFields: {
                withCredentials: true
            },
            dataType:"json",
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
