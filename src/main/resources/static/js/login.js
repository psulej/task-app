function getHeaders() {
    const token = document.getElementsByName("_csrf")[0].getAttribute('content')
    const headerName = document.getElementsByName("_csrf_header")[0].getAttribute('content')
    const headers = {
        'Content-Type': 'application/json'
    }
    headers[headerName] = token
    return headers
}

function loginUser() {

    if (!validateLoginInputs()) {
        console.log('Form is invalid')
        return
    }

    const login = document.getElementById("login");
    const password = document.getElementById("password ");

    fetch(`http://localhost:8080/login`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({
            login: login.value,
            password: password.value,
        })
    })
        .then(() => {
            //window.location.replace("http://localhost:8080");
        })
}

function validateLoginInputs(){

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
        document.getElementById("login").value,
        /^[\w.-]{0,19}[0-9a-zA-Z]$/,
        document.getElementById('loginAlert')
    )

    const passwordValidation = new Validation(
        document.getElementById("password").value,
        /^[\w.-]{0,19}[0-9a-zA-Z]$/,
        document.getElementById('passwordAlert')
    )

    const validations = [
        loginValidation,passwordValidation
    ]

    let valid = true
    validations.forEach(validation => {
            if (validation.validate()) {
                validation.hideError()
                document.getElementById("loginInputsError").hidden = true;
            } else {
                validation.showError()
                valid = false
            }
            if(validation.getValue() === null || validation.getValue().length === 0){
                document.getElementById("loginInputsError").hidden = false;
            }
        }
    )

    return valid
}