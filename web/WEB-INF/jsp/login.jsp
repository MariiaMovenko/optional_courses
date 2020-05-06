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
    <title>Login</title>
</head>
<body>
<%@include file="../jspf/locale.jspf" %>
<menu:header/>
<form action="login" method="POST" class="login-form">
    <div class="login-form-title">
        <h3>Optional Courses</h3>
        <h4><fmt:message key="login_to_continue"/>:</h4>


        <c:if test="${param['BANNED'] != null}">
            <p class="err-message"><fmt:message key="${param['BANNED']}"/></p>
        </c:if>
        <c:if test="${param['PENDING'] != null}">
            <p class="err-message"><fmt:message key="${param['PENDING']}"/></p>
        </c:if>
    </div>
    <div class="form-group">
        <c:if test="${param[Params.LOGIN_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.LOGIN_ERROR]}"/></p>
        </c:if>
        <label for="login-field"><fmt:message key="login"/></label>
        <input type="text" required class="form-control" id="login-field" name="login"
               placeholder="<fmt:message key="login"/>"
               pattern="${Params.LOGIN_PATTERN}" title="[A-Za-z0-9]">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.PASSWORD] != null}">
            <p class="err-message"><fmt:message key="${param[Params.PASSWORD]}"/></p>
        </c:if>
        <label for="password-field"><fmt:message key="password"/></label>
        <input type="password" required class="form-control" id="password-field" name="password"
               placeholder="<fmt:message key="password"/>"
               pattern="${Params.PASSWORD_PATTERN}" title="From four to ten characters [A-Za-z0-9]">
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary"><fmt:message key="login_button"/></button>
        <a href="<c:url value="/restore_password.html"/>" class="shift">
            <fmt:message key="restore_password"/>
        </a>
    </div>
</form>

<%@include file="../jspf/footer.jspf" %>
</body>
</html>