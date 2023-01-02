
fetchTasks()

function fetchTasks() {

    let url = `http://localhost:8080/tasks`

    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })

        .then(res => res.json())
        .then(res => {
            console.log(res);

            let tasks = res;

            let tableHtml = ''
            for (let index in tasks) {
                const task = tasks[index]
                tableHtml += taskRow(task)
            }

            document.getElementById('tasksDiv').innerHTML = tableHtml
        })
}

function taskRow(task) {
    const taskId = task.id

    const tr =
        `<div class="card mb-3" id="task2-${taskId}" style="justify-content: center">
              <div class="card-body mb-3" id="task-${taskId}">
                    <h5 class="card-title">${task.title}</h5>
                    <p class="card-text">${task.content}</p>
                    <button onclick="deleteTask(${taskId})" class="btn btn-primary btn-lg btn btn-danger">Delete</button>
                <button type="button" onclick="openModal(${taskId})" class="btn btn-primary btn-lg btn btn" data-bs-toggle="modal" data-bs-target="#exampleModal">Edit</button>
              </div>
        </div>`

    return tr
}

function addTask() {
    const title = document.getElementById("title");
    const content = document.getElementById("content");

    fetch(`http://localhost:8080/tasks`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title: title.value,
            content: content.value
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { return Promise.reject(text) })
            } else {
                return response.json();
            }
        })
        .then(task => {
            if((task.title.length > 0) && (task.content.length > 0) ) {
                let taskTableBodyElement = document.getElementById('tasksDiv')
                let tableHtml = taskTableBodyElement.innerHTML + taskRow(task)
                taskTableBodyElement.innerHTML = tableHtml
            }
        })

}


function deleteTask(taskId) {

    fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: 'DELETE'
    })
        .then(() => {
            let row = document.getElementById(`task-${taskId}`)
            row.remove();
            let row2 = document.getElementById(`task2-${taskId}`)
            row2.style.visibility='hidden'
        })
}

function updateTask(taskId) {
    const editTitle = document.getElementById('editTitle').value
    const editContent = document.getElementById('editContent').value

    console.log('edit title: ')
    console.log(editTitle)


    fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title: editTitle,
            content: editContent
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { return Promise.reject(text) })
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
        headers: {
            'Content-Type': 'application/json'
        },
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



