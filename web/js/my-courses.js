{
    $(document).ready(() => init());

    function init() {
        $(".courses-tabs").find(".nav-link").on("click", function() {
            let tabId = $(this).attr("id");
            $(".courses-tabs").find(".nav-link.active").toggleClass("active");
            $(this).toggleClass("active");
            let tabContent = $(".tab-content");
            let activeTab = tabContent.find(".tab-pane.active");
            activeTab.toggleClass("active");
            activeTab.toggleClass("show");
            let newActiveTab = tabContent.find("div[aria-labelledby='" + tabId + "']");
            newActiveTab.toggleClass("active");
            newActiveTab.toggleClass("show");
        });
    }

}