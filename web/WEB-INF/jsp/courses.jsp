<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="menu" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="h" uri="http://SummaryTaskMaria.movenko.nure.ua" %>
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
<div class="alert alert-success" hidden role="alert">
    Subscribed successfully!
</div>
<div class="alert alert-danger" hidden role="alert">
    Error during subscribe!
</div>
<div class="courses">
    <div class="accordion" id="accordionExample">
        <div class="card">
            <div class="card-header" id="headingTwo">
                <h5 class="mb-0">
                    <button class="btn btn-link collapsed" type="button" data-toggle="collapse"
                            data-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                        <fmt:message key="sort"/>
                    </button>
                </h5>
            </div>
            <div id="collapseTwo" class="collapse" data-operation="sort" aria-labelledby="headingTwo"
                 data-parent="#accordionExample">
                <div class="card-body sort">
                    <h3><fmt:message key="criteria"/></h3>
                    <div class="btn-group btn-group-toggle" data-toggle="buttons">
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="criteria" value="title_dictionary" autocomplete="off" checked>
                            <fmt:message key="Title"/>
                        </label>
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="criteria" value="duration" autocomplete="off"> <fmt:message key="duration"/>
                        </label>
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="criteria" value="students_count" autocomplete="off">  <fmt:message key="group_size"/>
                        </label>
                    </div>
                    <hr>

                    <h3><fmt:message key="order"/></h3>
                    <div class="btn-group btn-group-toggle" data-toggle="buttons">
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="order" value="ASC" autocomplete="off" checked> <fmt:message key="ascending"/>
                        </label>
                        <label class="btn btn-outline-primary">
                            <input type="radio" name="order" value="DESC" autocomplete="off"> <fmt:message key="descending"/>
                        </label>
                    </div>
                    <hr>

                    <div class="form-group submit-sort">
                        <button type="submit" class="btn btn-primary sort-cards"><fmt:message key="sort"/></button>
                    </div>
                </div>
            </div>
        </div>
        <div class="card">
            <div class="card-header" id="headingThree">
                <h5 class="mb-0">
                    <button class="btn btn-link collapsed" type="button" data-toggle="collapse"
                            data-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                        <fmt:message key="filter"/>
                    </button>
                </h5>
            </div>
            <div id="collapseThree" class="collapse" data-operation="filter" aria-labelledby="headingThree"
                 data-parent="#accordionExample">
                <div class="card-body filter">
                    <label for="theme"><fmt:message key="theme"/></label>
                    <select class="form-control theme-filter" id="theme">
                    </select>
                    <hr>
                    <label for="lector"><fmt:message key="lector"/></label>
                    <select class="form-control lector-filter" id="lector">
                    </select>
                    <hr>
                    <div class="form-group submit-filter">
                        <button type="submit" class="btn btn-primary filter-cards"><fmt:message key="filter"/></button>
                    </div>
                </div>
            </div>
        </div>
        <div class="card-body limit">
            <p><fmt:message key="show_on_page"/></p>
            <div class="btn-group btn-group-toggle" data-toggle="buttons">
                <label class="btn btn-outline-primary ${ (sessionScope.limit == 3 || sessionScope.limit == null) ? 'active' : ''}">
                    <input type="radio" name="limit" value="3" autocomplete="on"> 3
                </label>
                <label class="btn btn-outline-primary ${sessionScope.limit == 6 ? 'active' : ''}">
                    <input type="radio" name="limit" value="6" autocomplete="off"> 6
                </label>
                <label class="btn btn-outline-primary ${sessionScope.limit == 9 ? 'active' : ''}">
                    <input type="radio" name="limit" value="9" autocomplete="off"> 9
                </label>
                <button type="submit" class="btn btn-primary submit-limit"><fmt:message key="save"/></button>
            </div>

        </div>
    </div>

    <div class="courses-cards" data-studentsenrolled="<fmt:message key="students_enrolled"/>"
         data-subscribe="<h:ifAuth path="/enroll"/>"
         data-lector="<fmt:message key="lector"/>" data-startDate="<fmt:message key="start_date"/>"
         data-endDate="<fmt:message key="end_date"/>" data-duration="<fmt:message key="duration"/>"
         data-days="<fmt:message key="days"/>">
    </div>
</div>
<div class="text-center">
    <nav class="pagination-container">
        <ul class="pagination justify-content-center">
        </ul>
    </nav>
</div>

<%@include file="../jspf/footer.jspf" %>
<script src="<c:url value="/js/2.1.4.jquery.min.js"/>"></script>
<script src="<c:url value="/js/dropdown.js"/>"></script>
<script src="<c:url value="/js/coursesView.js"/>"></script>
<script src="<c:url value="/js/pagination.js"/>"></script>
<script src="<c:url value="/js/courses.js"/>"></script>
</body>
</html>