<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="h" uri="http://SummaryTaskMaria.movenko.nure.ua" %>
<%@ taglib prefix="menu" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ua.nure.movenko.summaryTask4.entities.User" %>
<%@include file="../jspf/locale.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/css/my-profile.css"/>" rel="stylesheet"/>
    <title>My profile</title>
</head>
<body>
<menu:header/>

<br/>
<% User thisUser = (User) session.getAttribute("userInfo"); %>
<div class="card">
    <c:if test="${sessionScope.userInfo.avatarPath == null}">
        <img src="${pageContext.request.contextPath}/img/my_account.png" alt="Person-Icon" class="person-img">
    </c:if>
    <c:if test="${sessionScope.userInfo.avatarPath != null}">
        <img src="find_image" alt="Person-Icon" class="person-img">
    </c:if>
    <a href="<c:url value="/upload_file.html"/>" ><fmt:message key="upload_avatar"/></a>
    <h2><%=thisUser.getLogin()%></h2>
    <p class="title"><%=thisUser.getRole() %>
    </p>
    <p><%=thisUser.getFirstName().toUpperCase()%> <%=thisUser.getLastName().toUpperCase()%>
    </p>
    <p class="title"><%=thisUser.getEmail() %>
    </p>
        <h:ifAuth path="/update_my_info">
           <a href="<c:url value="/update_my_info.html"/>"
                       class="my_profile_button"><fmt:message key="edit"/></a>
        </h:ifAuth>
        <h:ifAuth path="/change_password">
            <a href="<c:url value="/change_password.html"/>"
               class="my_profile_button">
                <fmt:message key="change_password"/>
            </a>
        </h:ifAuth>
</div>
<%@include file="../jspf/footer.jspf" %>
</body>
</html>