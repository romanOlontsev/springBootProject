updateForm.onsubmit = async (e) => {
    const rolesArray = [];
    Array.from(document.querySelectorAll('input[type=checkbox]:checked'))
        .map(item => rolesArray.push(item.value));
    const userData = Object.fromEntries(new FormData(e.target).entries());
    userData.roles = rolesArray;

    console.log(userData);

    let response = await fetch('/data/users/' + userId, {
        headers: {"Content-Type": "application/json"},
        method: 'PATCH',
        body: JSON.stringify(userData)
    });

    let result = await response.json();
    if (result.message !== undefined) {
        alert(result.message);
    }
    window.location = "/console/users/" + userId;
}