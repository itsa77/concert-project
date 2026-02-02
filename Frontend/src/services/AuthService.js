
import { apiFetch, getToken } from "./api";

function setAuthSession({ token, username, userId, roles }) {
    localStorage.setItem("token", token);
    localStorage.setItem("username", username);
    localStorage.setItem("userId", String(userId));
    localStorage.setItem("roles", JSON.stringify(roles || []));
}

function clearAuthSession() {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("userId");
    localStorage.removeItem("roles");
}

async function handleJsonResponse(res) {
    
    const text = await res.text();
    let data = null;
    if (text) {
        try {
            data = JSON.parse(text);
        } catch {
            data = null;
        }
    }

    if (!res.ok) {
        const message = 
        (data && (data.message || data.error || data.detail)) ||
        text || `Request failed (${res.status})`;
        throw new Error(message);
    }
    return data;
}

const AuthService = {
    isLoggedIn() {
        return !!getToken();
    },

    getSession() {
        return {
            token: localStorage.getItem("token"),
            username: localStorage.getItem("username"),
            userId: localStorage.getItem("userId")
                ? Number(localStorage.getItem("userId"))
                : null,
            roles: JSON.parse(localStorage.getItem("roles") || "[]"),
        };
    },

    logout() {
        clearAuthSession();
    },

    async register({ username, firstName, lastName, email, password }) {
        // Backend registraion expects a User object; you were sending passwordHash as the raw password.
        // Keep matching current backend behavior:
        const payload = {
            username,
            firstName,
            lastName,
            email,
            passwordHash: password,
        };

        const res = await apiFetch("/auth/register", {
            method: "POST",
            body: JSON.stringify(payload),
        });
        // Backend likely returns text like "User registered successfully"    
        return handleJsonResponse(res);
    },

    async login({ username, password }) {
        const payload = {
            username,
            password, // login endpoint expects "password" (not passwordHash)
        };

        const res = await apiFetch("/auth/login", {
            method: "POST",
            body: JSON.stringify(payload),
        });

        const data = await handleJsonResponse(res);
        // expected: { token, username, userId, roles: [...] }
        setAuthSession(data);
        return data;
    },

    async getCurrentUser() {
        // Requires JWT; apiFetch auto-attaches Authorization: Bearer <token>
        const res = await apiFetch("/auth/me", { method: "GET" });
        return handleJsonResponse(res);
    },
};

export default AuthService;