<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>405 Method Not Allowed Error</title>
    <link href="<c:url value="/css/error.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
</head>
<body class="body">
<h1 class="h000" >Whoops...It is 405 ERROR!</h1>
<p class="p">The method received in the request-line is known by the origin server
    but not supported by the target resource. Why don't You add  ".html" to your request?<br><br>
    With love, <br>
    Optional Courses' team!
</p>
<a href="main.html" type="button" class="btn btn-dark" style="margin: .2em 2rem;">  To main page</a><br>
</body>
</html>