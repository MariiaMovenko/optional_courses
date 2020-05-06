<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="h" uri="http://SummaryTaskMaria.movenko.nure.ua" %>

<c:set var="definedLanguage"
       value="${not empty param.language ? param.language : sessionScope.language}"
       scope="session"/>
<c:set var="language"
       value="${not empty definedLanguage ? definedLanguage : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="localization/message"/>
<head>
    <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/header.css"/>" rel="stylesheet">
    <title></title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary menu">
    <a class="navbar-brand" href="<c:url value="/main.html"/>">Optional Courses</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse">
        <ul class="navbar-nav mr-auto">
            <h:ifAuth path="/courses.html">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/courses.html"/>"><fmt:message key="all_courses"/></a>
                </li>
            </h:ifAuth>
            <h:ifAuth path="/student-courses.html">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/student_courses"/>"><fmt:message key="my_courses"/></a>
                </li>
            </h:ifAuth>
            <h:ifAuth path="/lector-courses.html">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/lector_courses"/>"><fmt:message key="my_courses"/></a>
                </li>
            </h:ifAuth>
            <h:ifAuth path="/create-course.html">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/create-course.html"/>"><fmt:message
                            key="create_course"/></a>
                </li>
            </h:ifAuth>
            <h:ifAuth path="/register-somebody.html">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/register-somebody.html"/>"><fmt:message
                            key="register_user"/></a>
                </li>
            </h:ifAuth>
            <h:ifAuth path="/add-theme.html">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/add-theme.html"/>"><fmt:message key="add_theme"/></a>
                </li>
            </h:ifAuth>
            <h:ifAuth path="/ban">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/ban"/>"><fmt:message key="ban_manager"/></a>
                </li>
            </h:ifAuth>
        </ul>
    </div>
    <div class="nav navbar-nav navbar-right">
        <div class="btn-group">
            <c:choose>
                <c:when test="${sessionScope.userInfo!=null && sessionScope.userInfo.avatarPath!=null}">
                    <img class="btn btn-primary dropdown-toggle account" data-toggle="dropdown" aria-haspopup="true"
                         aria-expanded="false" src="find_image" alt="avatar-image"/>
                </c:when>
                <c:otherwise>
                    <img class="btn btn-primary dropdown-toggle account" data-toggle="dropdown" aria-haspopup="true"
                         aria-expanded="false" src="<c:url value="/img/person.png"/>" alt="avatar-image"/>
                </c:otherwise>
            </c:choose>
            <div class="dropdown-menu dropdown-menu-right">
                <h:ifAuth path="/my_profile.html">
                    <a href="<c:url value="/my_profile.html"/>" class="dropdown-item" type="button"><fmt:message
                            key="my_account"/></a>
                    <form action="logout" method="post">
                        <button class="dropdown-item" type="submit"><fmt:message key="logout"/></button>
                    </form>
                </h:ifAuth>
                <c:if test="${sessionScope.userInfo == null}">
                    <a href="<c:url value="/login.html"/>" class="dropdown-item" type="button"><fmt:message
                            key="login"/></a>
                    <a href="<c:url value="/registration.html"/>" class="dropdown-item" type="button"><fmt:message
                            key="register"/></a>
                </c:if>
            </div>
        </div>
        <div class="btn-group">
            <button type="button" class="btn btn-success dropdown-toggle language" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <c:if test="${fn:split(language,'_')[0]== 'en' || fn:split(language,'_')[0]== ''}">
                    En
                </c:if>
                <c:if test="${fn:split(language,'_')[0] == 'ru'}">
                    Ru
                </c:if>
            </button>
            <div class="dropdown-menu">
                <a class="dropdown-item" onclick="setLocale('en');">EN</a>
                <a class="dropdown-item" onclick="setLocale('ru');">RU</a>
            </div>
        </div>
    </div>
</nav>
</body>
<script src="<c:url value="/js/locale.js"/>"></script>