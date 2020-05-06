$(document).ready(() => init());

function init() {
    $(".registration-form").on("submit", () => disable());
}

function disable() {
    $(".submit-registration").prop("disabled", true);
}