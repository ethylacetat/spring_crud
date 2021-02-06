export default class AbstractSubmitEventListener {

    _handleSubmit(event) {
        // abstract
    }

    handleEvent(event) {
        if (event.type === 'submit') {
            this._handleSubmit(event);
        }
    }
}