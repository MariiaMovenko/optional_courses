<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="menu" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../jspf/locale.jspf" %>

<html>
<head>
    <title>Success registration</title>
    <link href="<c:url value="/css/success_registration.css"/>" rel="stylesheet">
</head>
<body>
<menu:header/>
<h4 class="h4">
    <br/>
    <fmt:message key="success_registration1"/><br/>
    <fmt:message key="success_registration2"/><br/>

</h4>
<div>
    <IMG class="displayed" src="<c:url value="/img/ok.png"/>" alt="Ok-smile">
</div>
<%@include file="../jspf/footer.jspf" %>
</body>
</html>