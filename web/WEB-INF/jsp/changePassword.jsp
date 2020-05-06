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
    <title>Change Password</title>
</head>
<body>
<menu:header/>
<form action="change_password" method="post" class="changingPassword-form">
        <div class="form-group">
            <c:if test="${param[Params.CURRENT_PASSWORD] != null}">
    <p class="err-message"><fmt:message key="${param[Params.CURRENT_PASSWORD]}"/></p>
    </c:if>
    <label for="passwordField"><fmt:message key="current_password"/></label>
    <input type="password" minlength="4" maxlength="10" required class="form-control"
           id="passwordField" placeholder="<fmt:message key="password"/>" name="current_password"
           pattern="^[A-Za-z0-9]{4,10}$"
           title="From four to ten characters [A-Za-z0-9]">
    </div>
    <c:if test="${param[Params.NEW_PASSWORD] != null}">
        <p class="err-message"><fmt:message key="${param[Params.NEW_PASSWORD]}"/></p>
    </c:if>
    <div class="form-group">
        <label for="newPassword"><fmt:message key="new_password"/></label>
        <input type="password" minlength="4" maxlength="10" required class="form-control"
               id="newPassword" placeholder="<fmt:message key="new_password"/>" name="new_password">
    </div>
    <div class="form-group">
        <label for="repeatPassword"><fmt:message key="repeatedPassword"/></label>
        <input type="password" minlength="4" maxlength="10" required class="form-control"
               id="repeatPassword" placeholder="<fmt:message key="repeatedPassword"/>" name="repeatedPassword">
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary"><fmt:message key="change_password"/></button>
    </div>
</form>
<%@include file="../jspf/footer.jspf"%>
</body>
</html>