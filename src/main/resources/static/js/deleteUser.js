async function deleteUser() {
    if (confirm("Are you sure?")) {
        await fetch('/data/users/' + userId, {
            method: 'DELETE',
        })
        window.location = "/console/users/";
    }
}