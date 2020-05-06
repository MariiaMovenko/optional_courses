{
    $(document).ready(() => init());

    function init() {
        let coursesView = new CoursesView($(".courses-cards"));
        let siteUrl = "/" + window.location.pathname.split("/")[1];
        loadCourseCards(coursesView, siteUrl,1);
        $(".sort-cards").on("click", () => reloadCards(coursesView, siteUrl));
        let themeDropdown = new Dropdown("/all_themes", $(".theme-filter"));
        themeDropdown.appendToHtml();
        let lectorDropdown = new Dropdown("/get_lectors", $(".lector-filter"));
        lectorDropdown.appendToHtml();
        $(".filter-cards").on("click", () => reloadCards(coursesView, siteUrl));
        $(".submit-limit").on("click", () => reloadCards(coursesView, siteUrl));
    }

    function reloadCards(coursesView, siteUrl) {
        let limit = $(".limit").find("label.active").find("input").val();
        getCards(coursesView, siteUrl, limit, 1);
        $.get(siteUrl + "/courses_count", {})
            .done(response => appendPagination(response, limit, coursesView, siteUrl));
    }

    function getCards(coursesView, siteUrl, limit, page) {
        let operation = $(".collapse.show").data("operation");
        let sortForm = $(".sort");
        let criteria = sortForm.find("label.active").find("input[name='criteria']").val();
        let order = sortForm.find("label.active").find("input[name='order']").val();
        let themeFilter = $(".theme-filter").val();
        let lectorFilter = $(".lector-filter").val();
        let params = {
            operation: operation,
            criteria: criteria,
            order: order,
            theme_title: themeFilter,
            lector_name: lectorFilter,
            limit: limit,
            page: page
        };
        $.get(siteUrl + "/show_courses", params)
            .done(response => appendCourses(coursesView, response, siteUrl));
    }

    function loadCourseCards(coursesView, siteUrl, page) {
        let limit = $(".limit").find("label.active").find("input").val();
        let params = {
            limit: limit,
            page: page
        };
        $.get(siteUrl + "/show_courses", params)
            .done(response => appendCourses(coursesView, response, siteUrl));
        $.get(siteUrl + "/courses_count", {})
            .done(response => appendPagination(response, limit, coursesView, siteUrl));
    }

    function appendCourses(coursesView, response, siteUrl) {
        coursesView.clear();
        loadNextCard(response, 0, coursesView, null, siteUrl);
    }

    function loadNextCard(cards, cardNumber, coursesView, cardResponse, siteUrl) {
        let isLastCourse = cardNumber === cards.length;
        if (cardNumber >= 0 && cardResponse) {
            appendCourse(coursesView, cardResponse, siteUrl, isLastCourse);
        }
        let courseJson = cards[cardNumber];
        let params = {
            id: courseJson.id,
            status: courseJson.status,
            enrolled: courseJson.enrolled,
            enTitle: courseJson.enTitle,
            ruTitle: courseJson.ruTitle,
            title: courseJson.title,
            theme: courseJson.theme,
            lector: courseJson.lector,
            startDate: courseJson.startDate,
            endDate: courseJson.endDate,
            duration: courseJson.duration,
            description: courseJson.description,
            studentsEnrolled: courseJson.studentsEnrolled,
            cardNumber: cardNumber
        };
        $.get(siteUrl + "/course-card.html", params)
            .done(response => loadNextCard(cards, cardNumber + 1, coursesView, response, siteUrl, isLastCourse));
    }

    function appendPagination(response, limit, coursesView, siteUrl) {
        let paginationElement = $(".pagination");
        let pagination = new Pagination(paginationElement, limit, response.count);
        pagination.clear();
        pagination.show();
        $(".page-item").each(function () {
            $(this).on("click", function () {
                let pageButton = $(this);
                let pageNumber = pageButton.find(".page-link").text();
                getCards(coursesView, siteUrl, limit, pageNumber);
                $(".page-item.active").toggleClass("active");
                $(this).toggleClass("active");
            });
        });
    }

    function appendCourse(coursesView, response, siteUrl, isLastCourse) {
        coursesView.appendCourse(response);
        if (isLastCourse) {
            $(".confirm-subscribe").on("click", event => subscribeListener(event, siteUrl));
            $(".confirm-delete").on("click", event => deleteListener(event, siteUrl, coursesView));
        }
    }

    function subscribeListener(event, siteUrl) {
        let title = $(event.target).data("coursetitle");
        let params = {
            title: title
        };
        $.post(siteUrl + "/enroll", params)
            .done(() => disableSubscribeButton($(event.target)))
            .fail(() => alertSubscribeFailed($(event.target)));
    }

    function alertSubscribeFailed(subscribeButton) {
        let dialog = subscribeButton.parents(".modal");
        dialog.find("button[data-dismiss='modal']").click();
        let failedAlert = $(".alert-success");
        failedAlert.removeAttr("hidden");
        setTimeout(() => failedAlert.attr("hidden", "hidden"), 3000);
    }

    function disableSubscribeButton(subscribeButton) {
        let dialog = subscribeButton.parents(".modal");
        dialog.find("button[data-dismiss='modal']").click();
        let enrolButton = dialog.prev(".enroll-button");
        enrolButton.attr("disabled", "disabled");
        enrolButton.hide();
        let successAlert = $(".alert-success");
        successAlert.removeAttr("hidden");
        setTimeout(() => successAlert.attr("hidden", "hidden"), 3000);
    }

    function deleteListener(event, siteUrl, coursesView) {
        let title = $(event.target).data("coursetitle");
        let params = {
            title: title
        };
        $.post(siteUrl + "/delete_course", params)
            .done(() => confirmDelete(coursesView, siteUrl, $(event.target)))
            .fail(() => alertSubscribeFailed($(event.target)));
    }

    function confirmDelete(coursesView, siteUrl, deleteButton) {
        let dialog = deleteButton.parents(".modal");
        dialog.find("button[data-dismiss='modal']").click();
        let limit = $(".limit").find("label.active").find("input").val();
        getCards(coursesView, siteUrl, limit, 1);
    }

}