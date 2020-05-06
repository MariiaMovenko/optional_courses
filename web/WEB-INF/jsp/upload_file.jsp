<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Login</title>
</head>
<body>
<menu:header/>
<form action="upload_file" enctype="multipart/form-data" method="POST" class="login-form">
    <c:if test="${param['upload_error'] != null}">
        <p class="err-message"><fmt:message key="${param['upload_error']}"/></p>
    </c:if>
    <div class="form-group">
        <h6><fmt:message key="upload_file_message"/></h6>
    <div class="custom-file">
        <input type="file" name="avatar" class="custom-file-input" id="customFile">
        <label class="custom-file-label" for="customFile"><fmt:message key="choose_file"/></label>
    </div>
        </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary upload-avatar"><fmt:message key="save"/></button>
    </div>
</form>

<%@include file="../jspf/footer.jspf" %>
<script src="<c:url value="/js/2.1.4.jquery.min.js"/>"></script>
</body>
</html>