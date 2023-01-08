function registerUser() {

    // validacja po stronie

    if (!validateInputs()) {
        console.log('Form is invalid')
        return
    }

    const login = document.getElementById("registerLogin");
    const password = document.getElementById("registerPassword");
    const email = document.getElementById("registerEmail");

    fetch(`http://localhost:8080/registration`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({
            login: login.value,
            password: password.value,
            email: email.value
        })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    return Promise.reject(text)
                })
            } else {
                window.location.replace("http://localhost:8080/login?register");
            }
        })

        .catch(errorText => {
            const errors = JSON.parse(errorText).errors
            if (errors) {
                if (errors.indexOf('EMAIL_EXISTS') !== -1) {
                    console.log('Email exists, try different one :(')
                    document.getElementById("existsEmailAlert").hidden = false;
                }
                if (errors.indexOf('LOGIN_EXISTS') !== -1) {
                    console.log('Login exists, try different one :(')
                    document.getElementById("existsLoginAlert").hidden = false;
                }
            }
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
        /[A-Za-z0-9]{3,14}/,
        document.getElementById('registerLoginAlert')
    )

    const emailValidation = new Validation(
        document.getElementById("registerEmail").value,
        /^(.+)@(\S+)$/,
        document.getElementById('registerEmailAlert')
    )

    const passwordValidation = new Validation(
        document.getElementById("registerPassword").value,
        /[A-Za-z0-9]{5,14}/,
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
                document.getElementById("inputsError").hidden = false;
            }
        }
    )

    return valid
}