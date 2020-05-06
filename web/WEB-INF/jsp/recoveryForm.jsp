<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="menu" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ua.nure.movenko.summaryTask4.constants.Params" %>
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
<menu:header/>
<form action="restore_password" method="POST" class="login-form">
    <div class="login-form-title">
        <h3>Optional Courses</h3>
        <h6><fmt:message key="fill_form"/>:</h6>

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
        <input type="text" required class="form-control" id="login-field" name="login" placeholder="<fmt:message key="login"/>"
               pattern="${Params.LOGIN_PATTERN}" title="[A-Za-z0-9]">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.EMAIL_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.EMAIL_ERROR]}"/></p>
        </c:if>
        <label for="email-field"><fmt:message key="email"/></label>
        <input type="text" required class="form-control" id="email-field" name="email" placeholder="<fmt:message key="email"/>"
               pattern="${Params.EMAIL_PATTERN}" title="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary"><fmt:message key="restore_password"/></button>
        <a href="<c:url value="/login.html"/>" class="shift">
            <fmt:message key="back"/>
        </a>
    </div>
</form>
<%@include file="../jspf/footer.jspf"%>
</body>
</html>
