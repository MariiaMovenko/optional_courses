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
    <title>Edit my Info</title>
</head>
<body>
<menu:header/>
<form action="update_my_info" method="post" class="update-user-form">
    <div class="form-group">
        <label for="firstNameField"><fmt:message key="first_name"/></label>
        <input type="text" minlength="2" maxlength="45" required class="form-control"
               value="${sessionScope.userInfo.firstName}"
               id="firstNameField" placeholder="<fmt:message key="first_name"/>" name="first_name">
    </div>
    <div class="form-group">
        <label for="LastNameField"><fmt:message key="last_name"/></label>
        <input type="text" minlength="2" maxlength="45" required class="form-control"
               value="${sessionScope.userInfo.lastName}"
               id="lastNameField" placeholder="<fmt:message key="last_name"/>" name="last_name">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.LOGIN_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.LOGIN_ERROR]}"/></p>
        </c:if>
        <label for="loginField"><fmt:message key="login"/></label>
        <input type="text" minlength="3" maxlength="20" required class="form-control"
               value="${sessionScope.userInfo.login}"
               id="loginField" placeholder="<fmt:message key="login"/>" name="login"
               pattern="${Params.LOGIN_PATTERN}" title="^[A-Za-z0-9]{3,20}$">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.EMAIL_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.EMAIL_ERROR]}"/></p>
        </c:if>
        <label for="EmailField"><fmt:message key="email"/></label>
        <input type="text" maxlength="45" required class="form-control"
               value="${sessionScope.userInfo.email}"
               id="EmailField" placeholder="<fmt:message key="email"/>" name="email"
               pattern="${Params.EMAIL_PATTERN}" title="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$">
    </div>

    <div class="form-group">
        <button type="submit" class="btn btn-primary"><fmt:message key="update"/></button>
    </div>
</form>
<%@include file="../jspf/footer.jspf" %>
<script src="<c:url value="/js/2.1.4.jquery.min.js"/>"></script>
</body>
</html>