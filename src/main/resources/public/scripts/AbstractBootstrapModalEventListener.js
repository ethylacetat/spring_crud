export default class AbstractBootstrapModalEventListener {
    constructor() {
    }


    _handleShow(event) {
        // abstract
    }

    _handleShown(event) {
        // abstract
    }

    _handleHide(event) {
        // abstract
    }

    _handleHidden(event) {
        // abstract
    }

    handleEvent(event) {
        switch (event.type) {
            case 'show' :
                this._handleShow(event);
                break;
            case 'shown' :
                this._handleShown(event);
                break;
            case 'hide' :
                this._handleHide(event);
                break;
            case 'hidden' :
                this._handleHidden(event);
                break;
        }

    }

}