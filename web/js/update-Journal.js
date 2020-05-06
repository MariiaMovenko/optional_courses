{
    $(document).ready(() => init());

    function init() {
        let successAlert = $(".alert-success");
        setTimeout(() => successAlert.attr("hidden", "hidden"), 3000);
    }

}