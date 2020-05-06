class Pagination {

    constructor(paginationElement, limit, coursesCount) {
        this._paginationElement = paginationElement;
        this._limit =limit;
        this._coursesCount = coursesCount;
    }

    clear() {
        this._paginationElement.empty();
    }

    show() {
        this._paginationElement.append(this._createPages());
    }

    _createPages() {
        let pages = "";
        let pageSize = Math.ceil(this._coursesCount /this._limit);
        for (let number = 1; number <= pageSize; number++) {
            pages += this._createPage(number, number === 1);
        }
        return pages;
    }

    _createPage(number, isFirst) {
        return `
            <li class="page-item ${isFirst ? 'active' : ''}"><a class="page-link">${number}</a></li>
        `;
    }

}