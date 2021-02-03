function eventUl(event) {
    let eventId = event.target.getAttribute('id');
    if (/s\d+/.test(eventId)) {
        deleteTask(event);
    } else {
        selectLi(event);
    }
}

function selectLi(event) {
    let selectLi = event.target.getAttribute('id');
    let elem = document.getElementById(selectLi);
    if (elem.getAttribute("class") === "checked") {
        elem.setAttribute("class", "");
        requestServlet("CHANGE_DONE", selectLi.split(".", 2)[1], "", false);
    } else {
        elem.setAttribute("class", "checked");
        requestServlet("CHANGE_DONE", selectLi.split(".", 2)[1], "", true);
    }
}

function deleteTask(event) {
    let selectSpan = event.target.getAttribute('id');
    let selectLi = document.getElementById(selectSpan).parentElement.getAttribute('id');
    requestServlet("DELETE", selectLi.split(".", 2)[1]);
    document.getElementById(selectSpan).parentElement.remove();
}

async function addNewTask() {
    let li = document.createElement("li");
    let inputValue = document.getElementById("myInput").value;
    let arrayCheckbox = document.getElementsByClassName('checkbox');
    let activeCheckbox = [];
    for (let i = 0; i < arrayCheckbox.length; i++) {
        if (arrayCheckbox[i].checked) {
            let activeId = Number(arrayCheckbox[i].getAttribute("id").split("c", 2)[1]);
            if (activeCheckbox.length === 0) {
                activeCheckbox[0] = activeId;
            } else {
                activeCheckbox[activeCheckbox.length - 1 + 1] = activeId;
            }
        }
    }
    if (inputValue === '') {
        document.getElementById("myInput")
            .setAttribute("placeholder", "You must write something!")
    } else if (activeCheckbox.length === 0) {
        document.getElementById("myInput")
            .setAttribute("placeholder", "You must check category!")
    } else {
        let jsonCheckbox = JSON.stringify(activeCheckbox);
        const response = await fetch('http://localhost:8080/job4j_to-do_list/todolist', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({action: "ADD", id: 0, description: inputValue, done: false, checkbox: jsonCheckbox})
        });
        const resp = await response.json();
        li.id = resp.user.id + "." + resp.id;
        let textTask = document.createTextNode(resp.user.name + ": " + inputValue);
        li.appendChild(textTask);
        document.getElementById("myUL").appendChild(li);
        document.getElementById("myInput").value = "";
        document.getElementById("myInput")
            .setAttribute("placeholder", "Title...")
    }
    addCloseButton(li);
}

function addCloseButton(li) {
    let span = document.createElement("SPAN");
    let iconClose = document.createTextNode("\u00D7");
    span.className = "close";
    span.id = "s" + li.getAttribute("id");
    span.appendChild(iconClose);
    li.appendChild(span);
}

async function filterTasks(event) {
    let eventId = event.target.getAttribute('id');
    let allElemChecked = document.querySelectorAll("li.checked");
    if (eventId === "myTasksFilter") {
        document.getElementById("myTasksFilter").style.background = "#F9F9F9";
        const response = await fetch('http://localhost:8080/job4j_to-do_list/todolist', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({action: "USER", id: "", description: "", done: ""})
        });
        const json = await response.json();
        let userId = json.id;
        let allElem = document.querySelectorAll("li");
        allElemChecked.forEach(function (item) {
            item.removeAttribute("style");
        });
        allElem.forEach(function (item) {
            if (Number(userId) !== Number(item.getAttribute("id").split(".")[0])) {
                item.style.display = "none";
            }
        });
        document.getElementById("activeTasksFilter").style.background = "#bbb";
        document.getElementById("allTasksFilter").style.background = "#bbb";
    } else {
        let allElem = document.querySelectorAll("li");
        allElem.forEach(function (item) {
            item.removeAttribute("style");
        });
    }
    if (eventId === "activeTasksFilter") {
        document.getElementById("activeTasksFilter").style.background = "#F9F9F9";
        allElemChecked.forEach(function (item) {
            item.style.display = "none";
        });
        document.getElementById("allTasksFilter").style.background = "#bbb";
        document.getElementById("myTasksFilter").style.background = "#bbb";
    }
    if (eventId === "allTasksFilter") {
        document.getElementById("allTasksFilter").style.background = "#F9F9F9";
        allElemChecked.forEach(function (item) {
            item.removeAttribute("style");
        });
        document.getElementById("activeTasksFilter").style.background = "#bbb";
        document.getElementById("myTasksFilter").style.background = "#bbb";
    }
}

setTimeout(() => loadCategories(), 10);

async function loadCategories() {
    const response = await fetch('http://localhost:8080/job4j_to-do_list/todolist', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({action: "GET_ALL_CATEGORIES", id: "", description: "", done: ""})
    });
    const arrayCategories = await response.json();
    for (let i = 0; i < arrayCategories.length; i++) {
        let input = document.createElement("input");
        input.type = "checkbox";
        input.value = arrayCategories[i].name;
        input.id = "c" + arrayCategories[i].id;
        input.setAttribute("class", "checkbox");
        let label = document.createElement('label')
        label.appendChild(document.createTextNode(arrayCategories[i].name));
        label.appendChild(input);
        document.getElementById("checkbox").appendChild(label);
    }
    loadTasks()
}

async function loadTasks() {
    document.getElementById("allTasksFilter").style.background = "#F9F9F9";
    const responseTasks = await fetch('http://localhost:8080/job4j_to-do_list/todolist', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({action: "GET_ALL_TASKS", id: "", description: "", done: ""})
    });
    const arrayTasks = await responseTasks.json();
    for (let i = 0; i < arrayTasks.length; i++) {
        let category = "Category: ";
        arrayTasks[i].categories.forEach((element) => {
            category = category + element.name + ", ";
        })
        category = category.substring(0, category.length - 2) + " |"
        let li = document.createElement("li");
        let inputValue = category + " Author: " + arrayTasks[i].user.name + " | Task: " + arrayTasks[i].description;
        li.id = arrayTasks[i].user.id + "." + arrayTasks[i].id;
        let textTask = document.createTextNode(inputValue);
        li.appendChild(textTask);
        document.getElementById("myUL").appendChild(li);
        addCloseButton(li);
        if (arrayTasks[i].done) {
            li.setAttribute("class", "checked");
        }
    }
}

async function requestServlet(action, id, description, done) {
    await fetch('http://localhost:8080/job4j_to-do_list/todolist', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({action: action, id: id, description: description, done: done})
    });
}
