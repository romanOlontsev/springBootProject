updateForm.onsubmit = async (e) => {
    e.preventDefault();

    let response = await fetch('/data/users/' + userId, {
        headers: {"Content-Type": "application/json"},
        method: 'UPDATE',
        body: new FormData(updateForm)
    });

    let result = await response.json();
    if (result.message !== undefined) {
        alert(result.message);
    }
    window.location = "/console/users/" + userId;
};