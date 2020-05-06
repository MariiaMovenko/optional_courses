<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="menu" tagdir="/WEB-INF/tags" %>
<%@ page import="ua.nure.movenko.summaryTask4.constants.Params" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@include file="../jspf/locale.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/forms.css"/>" rel="stylesheet">
    <title>Theme creation page</title>
</head>
<body>
<menu:header/>
<form action="add_theme" method="post" class="registration-form">
    <h4><fmt:message key="new_theme"/>:</h4>
    <div class="form-group">
        <c:if test="${param[Params.EN_THEME_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.EN_THEME_ERROR]}"/></p>
        </c:if>
        <label for="en_title"><fmt:message key="en_title"/></label>
        <input type="text" minlength="2" maxlength="45" required class="form-control"
               id="en_title" placeholder="<fmt:message key="en_title"/>"
               name="en_theme">

    </div>
    <div class="form-group">
        <c:if test="${param[Params.RU_THEME_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.RU_THEME_ERROR]}"/></p>
        </c:if>
        <label for="ru_title"><fmt:message key="ru_title"/></label>
        <input type="text" minlength="2" maxlength="45" required class="form-control"
               id="ru_title" placeholder="<fmt:message key="ru_title"/>"
               name="ru_theme">
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary"><fmt:message key="add_theme"/></button>
    </div>
</form>
<%@include file="../jspf/footer.jspf"%>
<script src="<c:url value="/js/2.1.4.jquery.min.js"/>"></script>
</body>
</html>