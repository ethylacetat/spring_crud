import PagePagination from "./PagePagination.js";
import UserModal from "./UserModal.js";

export default class UserPage {

    constructor(pageData, HTMLRepresentation, loadUserPageCallback) {
        this._pageData = pageData;
        this._pageHTML = HTMLRepresentation;
        this._pagination = new PagePagination(pageData, HTMLRepresentation, loadUserPageCallback);
        this._loadUserPageCallback = loadUserPageCallback;
    }

    setPageData(pageData) {
        this._pageData = pageData;
    }

    render() {
        this._renderTable();
        this._pagination.render();
    }

    _renderTable(){
        const tbody = this._pageHTML.querySelector('tbody');
        tbody.innerHTML = '';

        const HTMLModal = document.querySelector('#userModal');

        // Замыкаем чтобы не терять контекст
        const pageNumber = this._pageData.pageNumber;
        const loadUserPageCallback = this._loadUserPageCallback;

        this._pageData.paginatedContent.forEach((user, index) => {
            const row = tbody.insertRow(index);
            row.insertCell(0).innerText = user.id;
            row.insertCell(1).innerText = user.firstName;
            row.insertCell(2).innerText = user.secondName;
            row.insertCell(3).innerText = user.email;

            // Edit button
            // ================================
            const editButtonHTML = createEditButton(user);

            const submitEditCallback = function (formData) {
                fetch(`/admin/users/${user.id}`, {
                    method: 'put',
                    credentials: 'same-origin',
                    body: formData,
                }).then(response => {
                    if (response.ok) {
                        loadUserPageCallback(`/admin/users?page=${pageNumber}`);
                    }
                });
            }

            const editModal = new UserModal(user, HTMLModal, submitEditCallback);
            editModal.setEnable(true);
            editModal.setTitle("Редактирование пользователя");

            editButtonHTML.addEventListener('click', function (event) {
                editModal.render();
            });

            row.insertCell(4).insertAdjacentElement('afterbegin', editButtonHTML);
            // ================================

            // Delete button
            // ================================
            const deleteButtonHTML = createDeleteButton(user);

            const submitDeleteCallback = function (formData) {
                fetch(`/admin/users/${user.id}`, {
                    method: 'DELETE',
                    credentials: 'same-origin'
                }).then(response => {
                    if (response.ok) {
                        loadUserPageCallback(`/admin/users?page=${pageNumber}`);
                    }
                });
            }

            const deleteModal = new UserModal(user, HTMLModal, submitDeleteCallback);
            deleteModal.setEnable(false);
            deleteModal.setTitle("Удаление пользователя");

            deleteButtonHTML.addEventListener('click', function (event) {
                deleteModal.render();

            });

            row.insertCell(5).insertAdjacentElement('afterbegin', deleteButtonHTML);
            // ================================
        })

    }
}

// TODO: Убрать и переделать
function createDeleteButton(userData) {
    const deleteButtonHTML = document.createElement('button');

    deleteButtonHTML.name = 'delete';
    deleteButtonHTML.classList.add('btn');
    deleteButtonHTML.classList.add('btn-danger');
    deleteButtonHTML.classList.add('py-0');
    deleteButtonHTML.dataset.toggle = 'modal';
    deleteButtonHTML.dataset.targe = '#deleteUserModal'
    deleteButtonHTML.dataset.id = userData.id;
    deleteButtonHTML.type = 'button'
    deleteButtonHTML.textContent = 'Delete'

    return deleteButtonHTML;
}

// TODO: Убрать и переделать
function createEditButton(userData) {
    const editButtonHTML = document.createElement('button');

    editButtonHTML.name = 'edit';
    editButtonHTML.classList.add('btn');
    editButtonHTML.classList.add('btn-info');
    editButtonHTML.classList.add('py-0');
    editButtonHTML.dataset.toggle = 'modal';
    editButtonHTML.dataset.targe = '#editUserModal'
    editButtonHTML.dataset.id = userData.id;
    editButtonHTML.type = 'button'
    editButtonHTML.textContent = 'Edit'

    return editButtonHTML;
}