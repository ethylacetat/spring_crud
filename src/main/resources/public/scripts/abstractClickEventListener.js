export default class AbstractClickEventListener {

    _handleClick(event) {
        // abstract
    }

    handleEvent(event) {
        if (event.type === 'click') {
            this._handleClick(event);
        }
    }
}