let page = 0
let size = 5

fetchTasks()

function loadNextPage() {
    page += 1
    fetchTasks()
}

function loadPreviousPage() {
    page -= 1
    fetchTasks()
}

function loadPage(number) {
    page = number
    fetchTasks()
}

function getHeaders() {
    const token = document.getElementsByName("_csrf")[0].getAttribute('content')
    const headerName = document.getElementsByName("_csrf_header")[0].getAttribute('content')
    const headers = {
        'Content-Type': 'application/json'
    }
    headers[headerName] = token
    return headers
}

function fetchTasks() {
    let url = `http://localhost:8080/tasks?page=${page}&size=${size}`

    fetch(url, {
        method: 'GET',
        headers: getHeaders(),
    })

        .then(res => res.json())
        .then(res => {
            console.log(res);

            let tasks = res.items;
            let tableHtml = ''
            for (let index in tasks) {
                const task = tasks[index]
                tableHtml += taskRow(task)
            }

            const previousPageButton = document.getElementById('previousPageButton');
            const nextPageButton = document.getElementById('nextPageButton');

            const lastPage = res.totalPages - 1;

            if (page === lastPage) {
                nextPageButton.disabled = true;
            }
            if (page !== lastPage) {
                nextPageButton.disabled = false;
            }
            if (tasks.length === 0) {
                nextPageButton.disabled = true;
                previousPageButton.disabled = true;
            }
            previousPageButton.disabled = page === 0;

            document.getElementById('listSize').innerHTML = `[${res.currentPage + 1}/${lastPage + 1}]`

            document.getElementById('tasksDiv').innerHTML = tableHtml

        })
}

function taskRow(task) {
    const taskId = task.id

    const tr = `<div class="card mb-3" id="task2-${taskId}" style="justify-content: center">
              <div class="card-body mb-3" id="task-${taskId}">
                    <h5 class="card-title">${task.title}</h5>
                    <p class="card-text">${task.content}</p>
                    <div class="position-absolute top-0 start-2 mt-2 mb-2"><p class="card-text">22.05.2023 &nbsp; 15:17</p></div>
                <div class="d-grid gap-1 col-2 mx-auto">
                    <button type="button" onclick="openModal(${taskId})" class="btn btn-primary lg" data-bs-toggle="modal" data-bs-target="#exampleModal">Edit task</button>	
                </div>
                <button onclick="deleteTask(${taskId})" class="bi bi-trash position-absolute top-0 end-0 btn btn-primary btn-lg btn btn-danger">\u2718</button>
              </div>
        </div>`

    return tr
}

function addTask() {
    const title = document.getElementById("title");
    const content = document.getElementById("content");

    const datetime = document.getElementById("datetime");
    console.log("datetime: ",datetime)

    fetch(`http://localhost:8080/tasks`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({
            title: title.value,
            content: content.value
        })
    })
        .then(task => {
            if (!task.ok) {
                return task.text().then(text => {
                    return Promise.reject(text)
                })
            } else {
                return task.json();
            }
        })
        .then(task => {
            if ((task.title.length > 0) && (task.content.length > 0)) {
                let taskTableBodyElement = document.getElementById('tasksDiv')
                let tableHtml = taskTableBodyElement.innerHTML + taskRow(task)
                taskTableBodyElement.innerHTML = tableHtml
                fetchTasks()
            }
        })

}


function deleteTask(taskId) {

    fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: 'DELETE',
        headers: getHeaders()
    })
        .then(() => {
            let row = document.getElementById(`task-${taskId}`)
            row.remove();
            let row2 = document.getElementById(`task2-${taskId}`)
            row2.style.visibility = 'hidden'
            fetchTasks()
        })
}

function updateTask(taskId) {
    const editTitle = document.getElementById('editTitle').value
    const editContent = document.getElementById('editContent').value

    console.log('edit title: ')
    console.log(editTitle)


    fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: 'PUT',
        headers: getHeaders(),
        body: JSON.stringify({
            title: editTitle,
            content: editContent
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    return Promise.reject(text)
                })
            } else {
                return response.json();
            }
        })
        .then(task => {
            // wyswietlanie
            const row = document.getElementById(`task-${taskId}`)
            row.querySelector('.title').innerHTML = task.title
            row.querySelector('.content').innerHTML = task.content
        })
}

function openModal(taskId) {
    fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: 'GET',
        headers: getHeaders()
    })
        .then(res => res.json())
        .then(res => {

            console.log(res)
            const updateForm = document.getElementById("updateForm");

            console.log('title')
            console.log(res.title)

            //wyswietlanie value w formie
            updateForm.querySelector('#editTitle').value = res.title
            updateForm.querySelector('#editContent').value = res.content
            updateForm.style.display = "block"

            const submitButton = updateForm.querySelector('button[type="submit"]')

            let newSubmitButton = submitButton.cloneNode(true);
            newSubmitButton.addEventListener("click", function() {
                updateTask(taskId)
            })
            submitButton.replaceWith(newSubmitButton); // usuniecie starych event listenerów
        })
}


var myModal = document.getElementById('myModal')
var myInput = document.getElementById('myInput')