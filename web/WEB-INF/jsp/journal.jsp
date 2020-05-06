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
<div class="alert alert-success" ${sessionScope.get("updated").equalsIgnoreCase("updated") ? " ": "hidden"} role="alert">
    <fmt:message key="changes_saved"/>
    ${sessionScope.remove("updated")}
</div>
<form action="update_journal" method="post">
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col"><fmt:message key="last_name"/></th>
            <th scope="col"><fmt:message key="first_name"/></th>
            <th scope="col">Email</th>
            <th scope="col"><fmt:message key="current_mark"/></th>
            <th scope="col"><fmt:message key="edit_mark"/><br/><fmt:message key="changes_available"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="raw" items="${requestScope.journal.studentsMarks.entrySet()}">
            <tr>
                <td>${raw.key.lastName}</td>
                <td>${raw.key.firstName}</td>
                <td>${raw.key.email}</td>
                <td>${raw.getValue()}</td>
                <td><label><fmt:message key="mark"/></label>
                    <input type="hidden" name="student_id" value="${raw.key.id}"/>
                    <input ${"FINISHED".equalsIgnoreCase(requestScope.journal.courseInfo.status) ? "" : "disabled" }
                            type="text" name="mark" value="${raw.value}"></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <input type="hidden" name="course_id" value="${requestScope.journal.courseInfo.id}"/>
    <input ${"FINISHED".equalsIgnoreCase(requestScope.journal.courseInfo.status) ? "" : "disabled"} type="submit"
                                                                                                    class="btn btn-primary update-journal"
                                                                                                    value="<fmt:message key="save"/>"/>


</form>

<%@include file="../jspf/footer.jspf" %>
<script src="<c:url value="/js/2.1.4.jquery.min.js"/>"></script>
<script src="<c:url value="/js/dropdown.js"/>"></script>
<script src="<c:url value="/js/ban-manager.js"/>"></script>
<script src="<c:url value="/js/my-courses.js"/>"></script>
<script src="<c:url value="/js/coursesView.js"/>"></script>
<script src="<c:url value="/js/pagination.js"/>"></script>
<script src="<c:url value="/js/courses.js"/>"></script>
<script src="<c:url value="/js/update-Journal.js"/>"></script>
</body>
</html>