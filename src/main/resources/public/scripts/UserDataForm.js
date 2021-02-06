import AbstractSubmitEventListener from "./AbstractSubmitEventListener.js";

export default class UserDataForm extends AbstractSubmitEventListener {
    constructor(userData, HTMLModalRepresentation, submitActionCallback) {
        super();
        this._userData = userData;
        this._HTMLRepresentation = HTMLModalRepresentation;

        this._isEnable = true;

        this._inputFirstName = HTMLModalRepresentation.querySelector('input[name="firstName"]');
        this._inputSecondName = HTMLModalRepresentation.querySelector('input[name="secondName"]');
        this._inputEmail = HTMLModalRepresentation.querySelector('input[name="email"]');
        this._inputPassword = HTMLModalRepresentation.querySelector('input[name="password"]');

        this._checkboxInputs = HTMLModalRepresentation.querySelectorAll('input[type="checkbox"]');

        // TODO: delete
        this._HTMLSubmitBtn = HTMLModalRepresentation.querySelector('[name="submitButton"]');

        this._submitActionCallback = submitActionCallback;
    }

    _handleSubmit(event) {
        event.preventDefault();

        const formData = new FormData();
        formData.append(this._inputFirstName.name, this._inputFirstName.value);
        formData.append(this._inputSecondName.name, this._inputSecondName.value);
        formData.append(this._inputEmail.name, this._inputEmail.value);
        formData.append(this._inputPassword.name, this._inputPassword.value);

        this._checkboxInputs.forEach(checkbox => {
            if (checkbox.checked) {
                formData.append(checkbox.name, checkbox.value);
            }
        });

        this.dropEventSubscription();

        this._submitActionCallback(formData);
    }

    render() {
        this._clearInputs();

        if (this._userData) {
            this._inputFirstName.value = this._userData.firstName;
            this._inputSecondName.value = this._userData.secondName;
            this._inputEmail.value = this._userData.email;

            this._userData.roles.forEach(role => {
                const checkbox = this._HTMLRepresentation.querySelector('#' + role.authority);
                if (checkbox) {
                    checkbox.checked = 'true'
                }
            });
        }

        this._isEnable ? this._enableInputs() : this._disableInputs();

        this._HTMLRepresentation.addEventListener('submit', this);
    }

    setEnable(isEnable) {
        this._isEnable = isEnable;
    }

    _enableInputs() {
        this._inputFirstName.removeAttribute('readonly');
        this._inputSecondName.removeAttribute('readonly');
        this._inputEmail.removeAttribute('readonly');
        this._inputPassword.removeAttribute('readonly');

        this._checkboxInputs.forEach(checkbox => checkbox.removeAttribute('disabled'));
    }

    _disableInputs() {
        this._inputFirstName.setAttribute('readonly', '');
        this._inputSecondName.setAttribute('readonly', '');
        this._inputEmail.setAttribute('readonly', '');
        this._inputPassword.setAttribute('readonly', '');

        this._checkboxInputs.forEach(checkbox => checkbox.setAttribute('disabled', ''));
    }

    _clearInputs() {
        this._inputFirstName.value = '';
        this._inputSecondName.value = '';
        this._inputEmail.value = '';
        this._inputPassword.value = '';

        this._checkboxInputs.forEach(checkbox => checkbox.checked = false);
    }

    dropEventSubscription() {
        this._HTMLRepresentation.removeEventListener('submit', this);
    }

}