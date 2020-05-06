{
    $(document).ready(() => init());

    function init() {
        $(".update-journal").on("click", () => updateJournal());
    }

    function updateJournal() {
        let siteUrl = "/" + window.location.pathname.split("/")[1];
        let courseTitle = $(".title").val();
        let params = {
            title: courseTitle
        };
        $(".mark").each(function() {
            params.set("mark", $(this).val());
        });
        $(".student-id").each(function() {
            params.set("student_id", $(this).val());
        });
        $.post(siteUrl + "/update_journal", params)
            .done(() => console.log("success"));
    }
}