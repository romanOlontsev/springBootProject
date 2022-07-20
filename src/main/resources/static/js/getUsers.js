async function loadUsers() {
    const response = await fetch('/data/users');
    const users = await response.json();
    console.log(users);
}
loadUsers();