<%--
  Created by IntelliJ IDEA.
  User: zyf
  Date: 2018/3/6
  Time: 上午12:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>SSE Demo</title>
    <script type="text/javascript" src="../assets/js/jquery-3.2.1.min.js"></script>
</head>
<body>
    <div id="msgFromPush">

    </div>

    <script type="text/javascript">
        if(!!window.EventSource){//该对象，只有新式浏览器才有，该对象就是SSE的客户端
            var source = new EventSource('push');
            s = '';
            //添加监听获取服务器传递过来的消息
            source.addEventListener('message',function (e) {
                s+=e.data+"<br/>";
                $("#msgFromPush").html(s);
            })

            source.addEventListener('open',function (e) {
                console.log("连接打开.");
            },false);

            source.addEventListener('error',function (e) {
                if(e.readyState == EventSource.CLOSED){
                    console.log("连接关闭");
                }else {
                    console.log(e.readyState);
                }
            },false)
        }else {
            console.log("sorry，你的浏览器有点旧");
        }
    </script>
</body>
</html>
