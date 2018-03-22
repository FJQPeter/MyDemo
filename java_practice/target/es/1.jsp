<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
        <h1>图片:</h1>
        <script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
        <script>
            $(function() {
                $.ajax({
                    url:"http://localhost:8088/code/image",
                    type:"get",
                    headers:{
                        deviceId: "123456"
                    },
                    xhrFields: {
                        withCredentials: "true"
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