class Dropdown {

    constructor(action, dropdownElement) {
        this._url = "/" + window.location.pathname.split("/")[1] + action;
        this._dropdownElement = dropdownElement;
    }

    appendToHtml(defaultValue) {
        this._dropdownElement.empty();
        $.get(this._url, {})
            .done(response => this._responseToList(response, defaultValue));
    }

    _responseToList(response, defaultValue) {
        this._dropdownElement.append(this._jsonToListItem(null));
        for (let i = 0; i < response.length; i++) {
            let themeJson = response[i];
            this._dropdownElement.append(this._jsonToListItem(themeJson, defaultValue));
        }
    }

    _jsonToListItem(json, defaultValue) {
        let selected = "";
        if (json && json.itemTitle === defaultValue) {
            selected = "selected";
        }
        return `
            <option ${selected}>${json ? json.itemTitle : ""}</option>
        `;
    }

}