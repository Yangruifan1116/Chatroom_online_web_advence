const form = document.querySelector("#loginForm");
const usernameInput = document.querySelector("#username");

form.addEventListener("submit", (e) => {
    const username = usernameInput.value.trim();

    if (!username) {
        e.preventDefault();
        alert("用户名不能为空，请重新输入");
        return;
    }

});