$(document).ready(() => init());

function init() {
    let defaultTheme = $(".theme-select").data("default");
    let themeDropdown = new Dropdown("/all_themes", $(".theme-select"));
    themeDropdown.appendToHtml(defaultTheme);
    let defaultLector = $(".lector-select").data("default");
    let lectorDropdown = new Dropdown("/get_lectors", $(".lector-select"));
    lectorDropdown.appendToHtml(defaultLector);
}