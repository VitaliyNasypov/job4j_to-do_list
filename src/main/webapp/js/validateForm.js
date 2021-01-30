async function validateSignUp() {
    let checkValidateSignUp = validateLogin();
    let name = document.getElementById("name").value;
    let password = document.getElementById("password").value;
    let repeatPassword = document.getElementById("repeatPassword").value;
    if (name === '') {
        document.getElementById("resultCheckName").innerHTML = ' (Empty Name)';
        checkValidateSignUp = false;
    } else {
        document.getElementById("resultCheckEmail").innerHTML = '';
    }
    if (repeatPassword === '' || repeatPassword !== password) {
        document.getElementById("resultCheckRepeatPassword").innerHTML = ' (Passwords do not match)';
        checkValidateSignUp = false;
    } else {
        document.getElementById("resultCheckRepeatPassword").innerHTML = '';
    }
    return checkValidateSignUp;
}

async function validateLogin() {
    let checkValidateLogin = true;
    const regEmail = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    if (email === '' || !regEmail.test(email)) {
        document.getElementById("resultCheckEmail").innerHTML = ' (Invalid Email)';
        checkValidateLogin = false;
    } else {
        document.getElementById("resultCheckEmail").innerHTML = '';
    }
    if (password === '') {
        document.getElementById("resultCheckPassword").innerHTML = ' (Empty Password)';
        checkValidateLogin = false;
    } else {
        document.getElementById("resultCheckPassword").innerHTML = '';
    }
    return checkValidateLogin;
}