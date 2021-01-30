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
    if (inputValue === '') {
        document.getElementById("myInput")
            .setAttribute("placeholder", "You must write something!")
    } else {
        const response = await fetch('http://localhost:8080/job4j_to-do_list/todolist', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({action: "ADD", id: 0, description: inputValue, done: false})
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
        document.getElementById("doneTasksFilter").style.background = "#bbb";
        document.getElementById("allTasksFilter").style.background = "#bbb";
    } else {
        let allElem = document.querySelectorAll("li");
        allElem.forEach(function (item) {
            item.removeAttribute("style");
        });
    }
    if (eventId === "doneTasksFilter") {
        document.getElementById("doneTasksFilter").style.background = "#F9F9F9";
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
        document.getElementById("doneTasksFilter").style.background = "#bbb";
        document.getElementById("myTasksFilter").style.background = "#bbb";
    }
}

setTimeout(() => loadTasks(), 10);

async function loadTasks() {
    document.getElementById("allTasksFilter").style.background = "#F9F9F9";
    const response = await fetch('http://localhost:8080/job4j_to-do_list/todolist', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({action: "GET_ALL_TASKS", id: "", description: "", done: ""})
    });
    const arrayTasks = await response.json();
    for (let i = 0; i < arrayTasks.length; i++) {
        let li = document.createElement("li");
        let inputValue = arrayTasks[i].user.name + ": " + arrayTasks[i].description;
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
