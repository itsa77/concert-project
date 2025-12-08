import api from "./api";

const AuthService = {

    register(user) {
        return api.post("/auth/register", user);
    },

    login(username, password) {
        return api.post("/auth/login", { username, password })
            .then(response => {
                localStorage.setItem("token", response.data.token);
                localStorage.setItem("username", response.data.username);
                localStorage.setItem("userId", response.data.userId);
                return response.data;
            });
    },

    logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("username");
        localStorage.removeItem("userId");
    },

    isLoggedIn() {
        return !!localStorage.getItem("token");
    }
};

export default AuthService;