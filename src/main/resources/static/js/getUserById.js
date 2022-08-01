window.onload = async function getUserById() {
    const response = await fetch('/data/users/' + userId);
    if (response.ok) {
        await response.json().then(data => createUser(data)).catch(err => console.log(err));
    } else {
        alert("HTTP ERROR: " + response.status);
    }
}

const createUser = (data) => {
    let roles = getRoles();

    document.getElementById("getUser").innerHTML =
        "<tr>" +
        "<th>User Id" +
        "<th>Username" +
        "<th>User role" +
        "</tr>" +
        "<tr>" +
        "<td>" + data.id +
        "<td>" + data.username +
        "<td>" + roles;


    function getRoles() {
        let text = "<ul>";
        data.roles.forEach(addTag);
        text += "</ul>";
        return text;

        function addTag(value) {
            text += "<li>" + value + "</li>";
        }
    }
}