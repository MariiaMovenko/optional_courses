<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="menu" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@include file="../jspf/locale.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Optional Courses</title>
    <link href="<c:url value="/css/courses.css"/>" rel="stylesheet">
</head>
<body>
<menu:header/>
<ul class="nav nav-tabs courses-tabs">
    <li class="nav-item">
        <button id="ban" class="nav-link active"><fmt:message key="ban"/></button>
    </li>
    <li class="nav-item">
        <button id="unban" class="nav-link"><fmt:message key="unban"/></button>
    </li>
</ul>
<div class="tab-content">
    <div class="tab-pane fade show active" role="tabpanel" aria-labelledby="ban">
        <form action="ban_manager" method="post">
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col"><fmt:message key="login"/></th>
                    <th scope="col"><fmt:message key="first_name"/></th>
                    <th scope="col"><fmt:message key="last_name"/></th>
                    <th scope="col">Email</th>
                    <th scope="col"><fmt:message key="role"/></th>
                    <th scope="col"><fmt:message key="status"/></th>
                    <th scope="col"><fmt:message key="select"/></th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${requestScope.users.get('active')}">
                        <tr>
                            <td class="login-column">${user.getLogin()}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.email}</td>
                            <td>${user.role}</td>
                            <td>${user.status}</td>
                            <td><input class="ban-checkbox" type="checkbox"/></td>
                            <input type="hidden" name="login"/>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <input type="hidden" name="status" value="BANNED"/>
            <input type="submit" value="<fmt:message key="ban"/>" class="btn btn-primary ban-button"/>
        </form>
    </div>
    <div class="tab-pane fade" role="tabpanel" aria-labelledby="unban">
        <form action="ban_manager" method="post">
            <table class="table">
                <thead class="thead-dark">
                <tr>
                    <th scope="col"><fmt:message key="login"/></th>
                    <th scope="col"><fmt:message key="first_name"/></th>
                    <th scope="col"><fmt:message key="last_name"/></th>
                    <th scope="col">Email</th>
                    <th scope="col"><fmt:message key="role"/></th>
                    <th scope="col"><fmt:message key="status"/></th>
                    <th scope="col"><fmt:message key="select"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${requestScope.users.get('banned')}">
                    <tr>
                        <td class="login-column">${user.getLogin()}</td>
                        <td>${user.getFirstName()}</td>
                        <td>${user.getLastName()}</td>
                        <td>${user.getEmail()}</td>
                        <td>${user.getRole()}</td>
                        <td>${user.getStatus()}</td>
                        <td><input class="ban-checkbox" type="checkbox"/></td>
                        <input type="hidden" name="login"/>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <input type="hidden" name="status" value="ACTIVE"/>
            <input type="submit" value="<fmt:message key="unban"/>" class="btn btn-primary ban-button"/>
        </form>
    </div>
</div>
<%@include file="../jspf/footer.jspf"%>
<script src="<c:url value="/js/2.1.4.jquery.min.js"/>"></script>
<script src="<c:url value="/js/dropdown.js"/>"></script>
<script src="<c:url value="/js/ban-manager.js"/>"></script>
<script src="<c:url value="/js/my-courses.js"/>"></script>
<script src="<c:url value="/js/coursesView.js"/>"></script>
<script src="<c:url value="/js/pagination.js"/>"></script>
<script src="<c:url value="/js/courses.js"/>"></script>
</body>
</html>