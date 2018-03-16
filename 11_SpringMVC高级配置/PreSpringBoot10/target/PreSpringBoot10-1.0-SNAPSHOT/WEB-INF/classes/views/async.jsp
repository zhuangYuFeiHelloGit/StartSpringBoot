<%--
  Created by IntelliJ IDEA.
  User: zyf
  Date: 2018/3/6
  Time: 上午1:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>servlet async support</title>
    <script type="text/javascript" src="../assets/js/jquery-3.2.1.min.js"></script>
</head>
<body>
    <h1 id="result"></h1>
    <script type="text/javascript">
        deferred();
        function deferred() {
            $.get('defer',function (data) {
                $('#result').html(data);
                deferred();
            });
        }
    </script>
</body>
</html>
