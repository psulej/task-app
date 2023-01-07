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

            document.getElementById('listSize').innerHTML = `<div class="h5">[${res.currentPage + 1}/${lastPage + 1}]</div>`

            document.getElementById('tasksDiv').innerHTML = tableHtml

        })
}

function toTimestamp(strDate){
    var datum = Date.parse(strDate);
    return datum/1000;
}

function taskRow(task) {

    const taskId = task.id
    const dateTime = new Date(task.dateTime)

    const currentDateTimestamp = toTimestamp(new Date().toString());
    const dateTimeTimeStamp = toTimestamp(task.dateTime.toString())

    const oneDay = 24 * 60 * 60
    const numberOfDays = Math.round((dateTimeTimeStamp - currentDateTimestamp) / oneDay)

    const redFrame =`<div class="card mb-3 border border-danger" id="task2-${taskId}" style="justify-content: center">`
    const greenFrame =`<div class="card mb-3 border border-success" id="task2-${taskId}" style="justify-content: center">`
    const yellowFrame =`<div class="card mb-3 border border-warning" id="task2-${taskId}" style="justify-content: center">`
    const darkFrame =`<div class="card mb-3 border border-dark" id="task2-${taskId}" style="justify-content: center">`

    var div = ''

    if(numberOfDays >= 14) { div = greenFrame }
    else if(numberOfDays >= 7) { div = yellowFrame }
    else if(numberOfDays  >= 1) { div = redFrame }
    else { div = darkFrame }

    const tr = div+`<div class="card-body mb-3" id="task-${taskId}">
                    <h5 class="card-title">${task.title}</h5>
                    <p class="card-text">${task.content}</p>
                    <div class="position-absolute top-0 start-2 mt-2 mb-2 h6" id="leftCornerDate">
                    <p class="card-text">${dateTime.toLocaleString()}</p>
                    </div>
                <div class="d-grid gap-1 col-2 mx-auto">
                    <button type="button" onclick="openModal(${taskId})" class="btn btn-primary lg" data-bs-toggle="modal" data-bs-target="#editTaskModal">Edit task</button>	
                </div>
                <button onclick="deleteTask(${taskId})" class="bi bi-trash position-absolute top-0 end-0 btn btn-primary btn-lg btn btn-danger">\u2718</button>
              </div>
        </div>`

    return tr
}

function addTask() {
    const title = document.getElementById("title");
    const content = document.getElementById("content");
    const dateTime = document.getElementById("dateTime");

    document.getElementById("titleAlert").hidden = true;
    document.getElementById("contentAlert").hidden = true;
    document.getElementById("timeAlert").hidden = true;

    if(title.value === "") {
        document.getElementById("titleAlert").hidden = false;
    }
    if(content.value === "") {
        document.getElementById("contentAlert").hidden = false;
    }
    if(dateTime.value === "") {
        document.getElementById("timeAlert").hidden = false;
    }


    fetch(`http://localhost:8080/tasks`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({
            title: title.value,
            content: content.value,
            dateTime: dateTime.value
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
        .catch(task => {
            console.log("Form is incorrect")

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
    const editDate = document.getElementById('editTime').value

    // document.getElementById("editTitleAlert").hidden = true;
    // document.getElementById("editContentAlert").hidden = true;
    //
    // if(editTitle.value === "") {
    //     document.getElementById("editTitleAlert").hidden = false;
    // }
    // if(editContent.value === "") {
    //     document.getElementById("editContentAlert").hidden = false;
    // }


    fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: 'PUT',
        headers: getHeaders(),
        body: JSON.stringify({
            title: editTitle,
            content: editContent,
            dateTime: editDate
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
            row.innerHTML = taskRow(task)
            closeModal()
            fetchTasks()
        })
}

function closeModal() {
    const closeButton = document.querySelector("#editTaskModal .close")
    closeButton.click()
}

function openModal(taskId) {
    fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: 'GET',
        headers: getHeaders()
    })
        .then(res => res.json())
        .then(res => {

            const updateForm = document.getElementById("updateForm");

            //wyswietlanie value w formie
            updateForm.querySelector('#editTitle').value = res.title
            updateForm.querySelector('#editContent').value = res.content
            updateForm.querySelector('#editTime').value = res.dateTime
            updateForm.style.display = "block"

            const submitButton = updateForm.querySelector('button[type="submit"]')

            let newSubmitButton = submitButton.cloneNode(true);
            newSubmitButton.addEventListener("click", function() {
                updateTask(taskId)
            })
            submitButton.replaceWith(newSubmitButton); // usuniecie starych event listenerów
        })
}
