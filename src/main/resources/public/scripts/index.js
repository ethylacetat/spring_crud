import UserPage from "./UserPage.js";
import UserDataForm from "./UserDataForm.js";

const userCreationsFormHTML = document.querySelector('#userCreationsForm');

const pageHTMLRepresentation = document.querySelector('#nav-user-table');

let userCreationsForm;

(function init(){
    loadUserPage('/admin/users');
    initUserDataForm();
})()

function loadUserPage(url) {
    fetch(url, {
        credentials: 'same-origin'
    })
        .then(response => response.json())
        .then(data => new UserPage(data, pageHTMLRepresentation, loadUserPage))
        .then(userPage => userPage.render());
}

function initUserDataForm() {
    const submitCallback = function (formData) {
        fetch(`/admin/users`, {
            method: 'post',
            credentials: 'same-origin',
            body: formData,
        }).then(response => {
            prepareCreationsForm();
            if (response.ok) {
                loadUserPage('/admin/users');
            }
        });
    }

    userCreationsForm = new UserDataForm(null, userCreationsFormHTML, submitCallback);
    prepareCreationsForm();
}

function prepareCreationsForm() {
    userCreationsForm.render();
}
