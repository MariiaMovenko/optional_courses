<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>401 Unauthorized Error</title>
    <link href="<c:url value="/css/error.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
</head>
<body class="body">
<h1 class="h000">Whoops...It is 401 ERROR!</h1>
<p class="p"> We are so sorry, but you have no right to access requested page..
    Please make sure that supplied credentials are correct.<br>
    <br>
    With love, <br>
    Optional Courses' team!
</p>
<a href="main.html" type="button" class="btn btn-dark" style="margin: .2em 2rem;"> To main page</a>
<a href="login.html" type="button" class="btn btn-dark">Login</a><br>
</body>
</html>