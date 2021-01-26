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
        requestServlet("CHANGE_DONE", selectLi, "", false);
    } else {
        elem.setAttribute("class", "checked");
        requestServlet("CHANGE_DONE", selectLi, "", true);
    }
}

function deleteTask(event) {
    let selectSpan = event.target.getAttribute('id');
    let selectLi = document.getElementById(selectSpan).parentElement.getAttribute('id');
    requestServlet("DELETE", selectLi);
    document.getElementById(selectSpan).parentElement.remove();
}


async function addNewTask() {
    let li = document.createElement("li");
    let inputValue = document.getElementById("myInput").value;
    if (inputValue === '') {
        document.getElementById("myInput")
            .setAttribute("placeholder", "You must write something!")
    } else {
        const response = await fetch('http://localhost:8080/job4j_to_do_list/todolist', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({action: "ADD", id: 0, description: inputValue, done: false})
        });
        const resp = await response.json();
        li.id = resp.id;
        let textTask = document.createTextNode(inputValue);
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

function hideTasks() {
    let status = document.getElementById("hide").firstChild.textContent;
    let allElemChecked = document.querySelectorAll("li.checked");
    if (status === "Done tasks") {
        allElemChecked.forEach(function (item) {
            item.style.display = "none";
        });
        document.getElementById("hide").innerText = "All tasks";
    } else {
        allElemChecked.forEach(function (item) {
            item.removeAttribute("style");
        });
        document.getElementById("hide").innerText = "Done tasks";
    }
}

setTimeout(() => loadTasks(), 10);

async function loadTasks() {
    const response = await fetch('http://localhost:8080/job4j_to_do_list/todolist', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({action: "GET_ALL_TASKS", id: "", description: "", done: ""})
    });
    const arrayTasks = await response.json();
    for (let i = 0; i < arrayTasks.length; i++) {
        let li = document.createElement("li");
        let inputValue = arrayTasks[i].description;
        li.id = arrayTasks[i].id;
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
    await fetch('http://localhost:8080/job4j_to_do_list/todolist', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({action: action, id: id, description: description, done: done})
    });
}
