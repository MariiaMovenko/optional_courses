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
        <button id="pendingCourses" class="nav-link"><fmt:message key="pending_courses"/></button>
    </li>
    <li class="nav-item">
        <button id="inProgressCourses" class="nav-link active"><fmt:message key="in_progress_courses"/></button>
    </li>
    <li class="nav-item">
        <button id="finishedCourses" class="nav-link"><fmt:message key="finished_courses"/></button>
    </li>
</ul>
<div class="tab-content">
    <div class="tab-pane fade my-courses" role="tabpanel" aria-labelledby="pendingCourses">
        <c:forEach var="course" items="${requestScope.courses.get('pending')}">
            <div class="card" style="width: 18rem;">
                <img class="card-img-top" src="${pageContext.request.contextPath}/img/courses3.png" alt="Card image cap">
                <div class="card-body">
                    <h5 class="card-title">${course.title}
                    </h5>
                    <p class="card-text">${course.theme}
                    </p>
                    <p class="card-text">
                        <fmt:message key="lector"/>: ${course.lector}<br>
                        <fmt:message key="start_date"/>: ${course.startDate}<br>
                        <fmt:message key="end_date"/>: ${course.endDate}<br>
                        <fmt:message key="duration"/>: ${course.duration}<br>
                        <fmt:message key="lecturer_email"/>: ${course.lectorEmail}<br>
                    </p>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="tab-pane my-courses fade show active" role="tabpanel" aria-labelledby="inProgressCourses">
        <c:forEach var="course" items="${requestScope.courses.get('inProgress')}">
            <div class="card" style="width: 18rem;">
                <img class="card-img-top" src="${pageContext.request.contextPath}/img/courses3.png" alt="Card image cap">
                <div class="card-body">
                    <h5 class="card-title">${course.title}
                    </h5>
                    <p class="card-text">${course.theme}
                    </p>
                    <p class="card-text">
                        <fmt:message key="lector"/>: ${course.lector}<br>
                        <fmt:message key="start_date"/>: ${course.startDate}<br>
                        <fmt:message key="end_date"/>: ${course.endDate}<br>
                        <fmt:message key="duration"/>: ${course.duration}<br>
                        <fmt:message key="lecturer_email"/>: ${course.lectorEmail}<br>
                    </p>
                </div>
            </div>
        </c:forEach>
    </div>
    <div class="tab-pane fade my-courses" role="tabpanel" aria-labelledby="finishedCourses">
        <c:forEach var="course" items="${requestScope.courses.get('finished')}">
            <div class="card" style="width: 18rem;">
                <img class="card-img-top" src="${pageContext.request.contextPath}/img/courses3.png" alt="Card image cap">
                <div class="card-body">
                    <h5 class="card-title">${course.getTitle()}
                    </h5>
                    <p class="card-text">${course.getTheme()}
                    </p>
                    <p class="card-text">
                        <fmt:message key="lector"/>: ${course.getLector()}<br>
                        <fmt:message key="start_date"/>: ${course.getStartDate()}<br>
                        <fmt:message key="end_date"/>: ${course.getEndDate()}<br>
                        <fmt:message key="duration"/>: ${course.getDuration()}<br>
                        <fmt:message key="lecturer_email"/>: ${course.lectorEmail}<br>
                        <fmt:message key="mark"/>: ${course.getMark()}
                    </p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<%@include file="../jspf/footer.jspf"%>
<script src="<c:url value="/js/2.1.4.jquery.min.js"/>"></script>
<script src="<c:url value="/js/dropdown.js"/>"></script>
<script src="<c:url value="/js/my-courses.js"/>"></script>
<script src="<c:url value="/js/coursesView.js"/>"></script>
<script src="<c:url value="/js/pagination.js"/>"></script>
<script src="<c:url value="/js/courses.js"/>"></script>
</body>
</html>