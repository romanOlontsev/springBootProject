getUsers();

async function getUsers() {
    const response = await fetch('/data/users');
    if (response.ok) {
        await response.json().then(data => createTable(data)).catch(err => console.log(err));
    } else {
        alert("HTTP ERROR: " + response.status);
    }
}

const createTable = (data) => {
    const tableData = data;
    const headerData = Object.keys(tableData[0]);
    const table = document.getElementById("table");
    const tr = (table).insertRow(-1);
    for (let i = 0; i < headerData.length; i++) {
        const th = document.createElement('th');
        th.innerHTML = headerData[i];
        tr.appendChild(th)
    }

    for (let i = 0; i < tableData.length; i++) {
        const tr = table.insertRow(-1);
        const obj = tableData[i];
        let href = "document.location = '" + "/console/users/" + data[i].id + "\'";
        tr.setAttribute("onclick", href);
        for (let key in obj) {
            const td = document.createElement('td');
            td.innerHTML = obj[key];
            tr.appendChild(td);
        }
    }
}