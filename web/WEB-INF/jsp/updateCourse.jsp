<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="menu" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ua.nure.movenko.summaryTask4.constants.Params" %>
<%@include file="../jspf/locale.jspf" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/forms.css"/>" rel="stylesheet">
    <title>Optional Courses</title>
</head>
<body>
<menu:header/>
<form action="update_course" method="POST" class="login-form">
    <input type="hidden" name="id" value="${param.id}"/>
    <div class="login-form-title">
        <h3><fmt:message key="update_course"/></h3>
    </div>
    <div class="form-group">
        <c:if test="${param[Params.EN_COURSE_TITLE_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.EN_COURSE_TITLE_ERROR]}"/></p>
        </c:if>
        <label for="en-title"><fmt:message key="en_title"/></label>
        <input type="text" required class="form-control" id="en-title" name="title_en"
               placeholder="<fmt:message key="en_title"/>" value="${requestScope.CourseInfo.titleDictionary.en}"
               pattern="${Params.TITLE_PATTERN}"
               title="<fmt:message key="title_format"/>">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.RU_COURSE_TITLE_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.RU_COURSE_TITLE_ERROR]}"/></p>
        </c:if>
        <label for="ru-title"><fmt:message key="ru_title"/></label>
        <input type="text" required class="form-control" id="ru-title"
               value="${requestScope.CourseInfo.titleDictionary.ru}"
               name="title_ru" placeholder="<fmt:message key="ru_title"/>" pattern="${Params.TITLE_PATTERN}"
               title="<fmt:message key="title_format"/>">
    </div>
    <div class="form-group">
        <label for="theme"><fmt:message key="theme"/></label>
        <select required class="form-control theme-select"
                data-default="${language.equals("en") ? requestScope.CourseInfo.theme.themeTitleEn : requestScope.CourseInfo.theme.themeTitleRu}"
                id="theme" name="theme_title">
        </select>
    </div>
    <div class=" form-group">
        <label for="lector"><fmt:message key="lector"/></label>
        <select required
                data-default="${requestScope.CourseInfo.lector.firstName} ${requestScope.CourseInfo.lector.lastName}"
                class="form-control lector-select"
                id="lector" name="lector_name">
        </select>
    </div>
    <div class="form-group">
        <c:if test="${param[Params.START_DATE_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.START_DATE_ERROR]}"/></p>
        </c:if>
        <label for="start-date"><fmt:message key="start_date"/></label>
        <input type="text" required class="form-control" id="start-date" name="start_date"
               placeholder="<fmt:message key="date_format"/>" pattern="${Params.DATE_PATTERN}"
               value="${requestScope.CourseInfo.startDate}" title="<fmt:message key="date_format"/>">
    </div>
    <div class="form-group">
        <c:if test="${param[Params.FINISH_DATE_ERROR] != null}">
            <p class="err-message"><fmt:message key="${param[Params.FINISH_DATE_ERROR]}"/></p>
        </c:if>
        <label for="end-date"><fmt:message key="end_date"/></label>
        <input type="text" required class="form-control" id="end-date" name="finish_date"
               placeholder="<fmt:message key="date_format"/>" pattern="${Params.DATE_PATTERN}"
               value="${requestScope.CourseInfo.finishDate}" title="<fmt:message key="date_format"/>">
    </div>
    <div class="form-group">
        <label for="description"><fmt:message key="course_description"/></label>
        <textarea type="text" required class="form-control" id="description" name="description">
            ${requestScope.CourseInfo.description}
        </textarea>
    </div>
    <div class="form-group">
        <button type="submit" class="btn btn-primary"><fmt:message key="update"/></button>
    </div>
</form>
<%@include file="../jspf/footer.jspf" %>
<script src="<c:url value="/js/2.1.4.jquery.min.js"/>"></script>
<script src="<c:url value="/js/dropdown.js"/>"></script>
<script src="<c:url value="/js/create_course.js"/>"></script>
</body>
</html>