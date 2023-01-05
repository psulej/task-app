
var myModal = document.getElementById('myModal')
var myInput = document.getElementById('myInput')

function getHeaders() {
    const token = document.getElementsByName("_csrf")[0].getAttribute('content')
    const headerName = document.getElementsByName("_csrf_header")[0].getAttribute('content')
    const headers = {
        'Content-Type': 'application/json'
    }
    headers[headerName] = token
    return headers
}

function registerUser() {

    const login = document.getElementById("registerLogin");
    const password = document.getElementById("registerPassword");
    const email = document.getElementById("registerEmail");

    if(login.value === "") {
        document.getElementById("registerLoginAlert").hidden = false;
    }
    // if(password.value === "") {
    //     document.getElementById("registerPasswordAlert").hidden = false;
    // }
    // if(email.value === "") {
    //     document.getElementById("registerEmailAlert").hidden = false;
    // }

    fetch(`http://localhost:8080/registration`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({
            login: login.value,
            password: password.value,
            email: email.value
        })
    })
        .then(() => {
            window.location.replace("http://localhost:8080/login");
        })
}