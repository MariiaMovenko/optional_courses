<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ua.nure.movenko.summaryTask4.constants.Params" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="menu" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="../jspf/locale.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/forms.css"/>" rel="stylesheet">
    <title>Registration</title>
</head>
<body>
<menu:header/>
<form action="registration" method="post" class="registration-form">
    <div class="login-form-title">
        <h3><fmt:message key="registration"/></h3>
    </div>
    <div class="form-group">
        <label for="firstNameField"><fmt:message key="first_name"/></label>
        <input type="text" minlength="2" maxlength="45" required class="form-control"
               id="firstNameField" placeholder="<fmt:message key="first_name"/>" name="first_name">
    </div>
    <div class="form-group">
        <label for="LastNameField"><fmt:message key="last_name"/></label>
        <input type="text" minlength="2" maxlength="45" required class="form-control"
               id="lastNameField" placeholder="<fmt:message key="last_name"/>" name="last_name">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.LOGIN_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.LOGIN_ERROR]}"/></p>
        </c:if>
        <label for="loginField"><fmt:message key="login"/></label>
        <input type="text" minlength="3" maxlength="20" required class="form-control"
               id="loginField" placeholder="<fmt:message key="login"/>" name="login"
               pattern="${Params.LOGIN_PATTERN}" title="^[A-Za-z0-9]{3,20}$">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.EMAIL_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.EMAIL_ERROR]}"/></p>
        </c:if>
        <label for="EmailField"><fmt:message key="email"/></label>
        <input type="text" maxlength="45" required class="form-control"
               id="EmailField" placeholder="<fmt:message key="email"/>" name="email"
               pattern="${Params.EMAIL_PATTERN}" title="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
    </div>

    <div class="form-group">
        <c:if test="${param[Params.PASSWORD] != null}">
            <p class="err-message"><fmt:message key="${param[Params.PASSWORD]}"/></p>
        </c:if>
        <label for="passwordField"><fmt:message key="password"/></label>
        <input type="password" minlength="4" maxlength="10" required class="form-control"
               id="passwordField" placeholder="<fmt:message key="password"/>" name="password"
               pattern="^[A-Za-z0-9]{4,10}$"
               title="From four to ten characters [A-Za-z0-9]">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.CONFIRM_PASSWORD] != null}">
            <p class="err-message"><fmt:message key="${param[Params.CONFIRM_PASSWORD]}"/></p>
        </c:if>
        <label for="confirmPassword"><fmt:message key="repeatedPassword"/></label>
        <input type="password" minlength="4" maxlength="10" required class="form-control"
               id="confirmPassword" placeholder="<fmt:message key="repeatedPassword"/>" name="repeatedPassword">
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary submit-registration"><fmt:message key="register"/></button>
    </div>
</form>
<%@include file="../jspf/footer.jspf" %>
<script src="<c:url value="/js/registration.js"/>"></script>
</body>
</html>