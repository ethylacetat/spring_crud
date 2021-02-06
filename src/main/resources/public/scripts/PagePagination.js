import AbstractClickEventListener from "./abstractClickEventListener.js";

export default class PagePagination extends AbstractClickEventListener {
    constructor(pageData, HTMLRepresentation, loadUserPageCallback) {
        super();
        this._loadUserPageCallback = loadUserPageCallback;
        this._pageData = pageData;
        this._paginationHTML = HTMLRepresentation;

        this._prevHTML = this._paginationHTML.querySelector('#prev');
        this._nextHTML = this._paginationHTML.querySelector('#next');
    }

    render() {
        this._renderRefs();
        this._renderPageNumber();
    }

    _renderPageNumber() {
        const numberHTML = this._paginationHTML.querySelector('#page');
        numberHTML.textContent = this._pageData.pageNumber;
    }

    _renderRefs() {
        // prev ref
        // ================================
        const prevRef = this._prevHTML.querySelector('#prevRef');
        const prevClassList = this._prevHTML.classList;

        if (this._pageData.hasPrevPage) {
            prevClassList.remove('disabled');
            prevRef.addEventListener('click', this);
        } else {
            if (!prevClassList.contains('disabled')) {
                prevClassList.add('disabled');
            }
        }
        // ================================

        // next ref
        // ================================
        const nextRef = this._nextHTML.querySelector('#nextRef');
        const nextClassList = this._nextHTML.classList;

        if (this._pageData.hasNextPage) {
            nextClassList.remove('disabled');
            nextRef.addEventListener('click', this);
        } else {
            if (!nextClassList.contains('disabled')) {
                nextClassList.add('disabled');
            }
        }
        // ================================
    }

    _handleClick(event) {
        this._prevHTML.querySelector('#prevRef').removeEventListener('click', this);
        this._nextHTML.querySelector('#nextRef').removeEventListener('click', this);

        if (event.currentTarget.id === 'prevRef') {
            this._loadUserPageCallback(`/admin/users?page=${this._pageData.pageNumber - 1}`);
        }

        if (event.currentTarget.id === 'nextRef') {
            this._loadUserPageCallback(`/admin/users?page=${this._pageData.pageNumber + 1}`);
        }
    }
}