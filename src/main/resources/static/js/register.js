
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

    if (!validateInputs()) {
        console.log('Form is invalid')
        return
    }

    const login = document.getElementById("registerLogin");
    const password = document.getElementById("registerPassword");
    const email = document.getElementById("registerEmail");

    // if(login.value === "") {
    //     document.getElementById("registerLoginAlert").hidden = false;
    // }
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

function validateInputs(){

    const Validation = function (value, regex, errorField) {
        this.value = value
        this.regex = regex
        this.errorField = errorField
    }

    Validation.prototype.validate = function() {
        return (this.value != null && this.value.length > 0) && this.regex.test(this.value)
    }
    Validation.prototype.showError = function() {
        this.errorField.hidden = false;
    }
    Validation.prototype.hideError = function () {
        this.errorField.hidden = true;
    }
    Validation.prototype.getValue = function () {
        return this.value;
    }

    const loginValidation = new Validation(
        document.getElementById("registerLogin").value,
        /^[\w.-]{0,19}[0-9a-zA-Z]$/,
        document.getElementById('registerLoginAlert')
    )

    const emailValidation = new Validation(
        document.getElementById("registerEmail").value,
        /^([a-zA-Z0-9])+([.a-zA-Z0-9_-])*@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-]+)+/,
        document.getElementById('registerEmailAlert')
    )

    const passwordValidation = new Validation(
        document.getElementById("registerPassword").value,
        /^[\w.-]{0,19}[0-9a-zA-Z]$/,
        document.getElementById('registerPasswordAlert')
    )

    const validations = [
        loginValidation,emailValidation,passwordValidation
    ]

    let valid = true
    validations.forEach(validation => {
            if (validation.validate()) {
                validation.hideError()
                document.getElementById("inputsError").hidden = true;
            } else {
                validation.showError()
                valid = false
            }
            if(validation.getValue() === null || validation.getValue().length === 0){
                // document.getElementById('inputsError').style.visibility = "visible"
                document.getElementById("inputsError").hidden = false;
            }
        }
    )

    return valid
}