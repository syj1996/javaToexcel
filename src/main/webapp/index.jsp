<html>
<%@page pageEncoding="UTF-8" %>
<%@page contentType="text/html; UTF-8" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="bootstrap.css" rel="stylesheet">
</head>
    <script src="bootstrap.js"></script>
    <script src="jQuery.js"></script>
    <script src="xlsx.js"></script>
    <script src="FileSaver.js"></script>
    <script src="download.js"></script>
</head>
<body>
<button onclick="action1()" id="mybtn">获取</button>
<script>
    function action1(){
        $.getJSON("httpServlet",{},function(data){
            if(data!=null)
                console.log(data);
                downloadExl(data);
        });
    }
</script>
</body>
</html>
