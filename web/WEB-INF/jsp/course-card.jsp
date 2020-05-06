<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="h" uri="http://SummaryTaskMaria.movenko.nure.ua" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@include file="../jspf/locale.jspf" %>

<div class="card" style="width: 18rem;">
    <img class="card-img-top" src="${pageContext.request.contextPath}/img/courses3.png" alt="Card image cap">
    <div class="card-body">
        <h5 class="card-title"><%= request.getParameter("title") %>
        </h5>
        <a data-toggle="modal" data-target=".description<%=request.getParameter("cardNumber")%>" href="#"><fmt:message key="course_description"/></a>
        <div class="modal fade description<%=request.getParameter("cardNumber")%>" tabindex="-1" role="dialog"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><fmt:message key="course_description"/></h5>
                    </div>
                    <div class="modal-body">
                        <%= request.getParameter("description") %><br>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message
                                key="close"/></button>
                    </div>
                </div>
            </div>
        </div><br/>
        <p class="card-text"><%= request.getParameter("theme") %>
        </p>
        <p class="card-text">
            <% String lector = request.getParameter("lector");
                if (lector != null && !lector.equals("Not defined yet"))%>
            <fmt:message key="lector"/>: <%= lector %><br>
            <fmt:message key="start_date"/>: <%= request.getParameter("startDate") %><br>
            <fmt:message key="end_date"/>: <%= request.getParameter("endDate") %><br>
            <fmt:message key="duration"/>: <%= request.getParameter("duration") %><br>
            <fmt:message key="students_enrolled"/>: <%= request.getParameter("studentsEnrolled") %>
        </p>
        <c:if test="${'false'.equals(param['enrolled']) && !'FINISHED'.equals(param['status'])}">
            <h:ifAuth path="/enroll">
                <button class="btn btn-primary enroll-button" data-toggle="modal"
                        data-target=".subscribe-modal<%=request.getParameter("cardNumber")%>"><fmt:message
                        key="subscribe"/></button>
                <div class="modal fade subscribe-modal<%=request.getParameter("cardNumber")%>" tabindex="-1"
                     role="dialog" aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title"><fmt:message key="subscribe"/></h5>
                            </div>
                            <div class="modal-body">
                                <fmt:message key="subscribe_confirmation"/> "<%= request.getParameter("title") %>"<br>
                                <fmt:message key="sure"/>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message
                                        key="close"/></button>
                                <button type="button" data-coursetitle="<%=request.getParameter("title")%>"
                                        class="btn btn-primary confirm-subscribe"><fmt:message key="confirm"/></button>
                            </div>
                        </div>
                    </div>
                </div>
            </h:ifAuth>
        </c:if>
        <h:ifAuth path="/update_course"><a
                href="<c:url value="/course_info"/>?id=<%=request.getParameter("id")%>"
                class="btn btn-primary"><fmt:message key="edit"/></a>
        </h:ifAuth>
        <h:ifAuth path="/delete_course">
            <button class="btn btn-danger delete-course" data-toggle="modal"
                    data-target=".delete-modal<%=request.getParameter("cardNumber")%>"><fmt:message
                    key="delete"/></button>
            <div class="modal fade delete-modal<%=request.getParameter("cardNumber")%>" tabindex="-1" role="dialog"
                 aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><fmt:message key="delete"/></h5>
                        </div>
                        <div class="modal-body">
                            <fmt:message key="delete_confirmation"/> "<%= request.getParameter("title") %>"<br>
                            <fmt:message key="sure"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal"><fmt:message
                                    key="close"/></button>
                            <button type="button" data-coursetitle="<%=request.getParameter("title")%>"
                                    class="btn btn-primary confirm-delete"><fmt:message key="confirm"/></button>
                        </div>
                    </div>
                </div>
            </div>
        </h:ifAuth>
    </div>
</div>