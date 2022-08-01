addForm.onsubmit = async (e) => {
    e.preventDefault();
    let response = await fetch('/data/users/', {
        method: 'POST',
        body: new FormData(addForm)
    });

    let result = await response.json();
    if (result.message !== undefined) {
        alert(result.message);
    }
    window.location = "/console/users/";
};