getUserById();

async function getUserById() {
    const response = await fetch('/data/users/' + userId);
    if (response.ok) {
        await response.json().then(data => createUser(data)).catch(err => console.log(err));
    } else {
        alert("HTTP ERROR: " + response.status);
    }
}

const createUser = (data) => {
    document.getElementById("getUser").innerHTML =
        "<tr class='d-flex'>" +
        "<th style='height: 30%'>User Id" +
        "<th>Username" +
        "<th>User role";
}