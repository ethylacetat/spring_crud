import AbstractSubmitEventListener from "./AbstractBootstrapModalEventListener.js";
import UserDataForm from "./UserDataForm.js";
import AbstractBootstrapModalEventListener from "./AbstractBootstrapModalEventListener.js";

export default class UserModal extends AbstractBootstrapModalEventListener {

    constructor(userData, HTMLModalRepresentation, actionCallback) {
        super();
        this._userData = userData;

        this._HTMLRepresentation = HTMLModalRepresentation;
        this._JQueryRepresentation = $(this._HTMLRepresentation);

        this._HTMLForm = HTMLModalRepresentation.querySelector('#userForm');
        this._HTMLTitle = HTMLModalRepresentation.querySelector('[name="modalTitle"]');

        this._actionCallback = actionCallback;

        this.title = '';
        this._isEnable = true;

        // Замыкаем что бы не терять контекст
        const submitClosure = this._actionCallback;
        const thisClosure = this;

        const callback = function (formData) {
            submitClosure(formData);
            thisClosure.hide();
        }

        this._dataForm = new UserDataForm(userData, this._HTMLForm, callback);
    }

    _handleHidden(event) {
        this._dataForm.dropEventSubscription();
    }


    render() {
        this._HTMLTitle.textContent = this.title;
        this._dataForm.render();
        this.show();
    }

    setEnable(isEnable) {
        this._isEnable = isEnable;
        this._dataForm.setEnable(isEnable);
    }

    setTitle(title) {
        this.title = title;
    }

    show() {
        this._JQueryRepresentation.one('show.bs.modal', this.handleEvent.bind(this));
        this._JQueryRepresentation.one('hidden.bs.modal',  this.handleEvent.bind(this));

        this._JQueryRepresentation.modal('show');
    }

    hide() {
        this._JQueryRepresentation.modal('hide');
    }

}