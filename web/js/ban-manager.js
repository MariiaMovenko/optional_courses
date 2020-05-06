{
    $(document).ready(() => init());

    function init() {
        $(".ban-checkbox").on("click", function() {
            if ($(this).prop("checked")) {
                let login = $($(this).parent("td").siblings(".login-column")[0]).html();
                $($(this).parent("td").siblings("input[name='login']")[0]).val(login);
            } else {
                $($(this).parent("td").siblings("input[name='login']")[0]).val("");
            }
        });
    }

}