function transformToQueryParams(params) {
    var parameters = "?";
    $.each(params, function (index, value) {
        parameters += index + "=" + encodeURI(value) + "&"
    });
    parameters = parameters.substr(0, parameters.length - 1);
    return parameters;
}

function getSearchParameters() {
    var prmstr = window.location.search.substr(1);
    return prmstr != null && prmstr != "" ? transformToAssocArray(prmstr) : {};
}

function transformToAssocArray(prmstr) {
    var params = {};
    var prmarr = prmstr.split("&");
    for (var i = 0; i < prmarr.length; i++) {
        var tmparr = prmarr[i].split("=");
        params[tmparr[0]] = tmparr[1];
    }
    return params;
}

function setLocale(locale){
    var params = getSearchParameters();
    params["language"] = locale;
    window.location = window.location.href.replace(/[\?#].*|$/, transformToQueryParams(params));
}