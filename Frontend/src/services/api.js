

const API_BASE = import.meta.env.VITE_API_URL  || "http://localhost:8080";

export function getToken() {
    return localStorage.getItem("token");
}

export async function apiFetch(path, options = {}) {
    const token = getToken();

    const headers = {
        "Content-Type": "application/json",
        ...(options.headers || {}),
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };

    const res = await fetch(`${API_BASE}${path}`, {
        ...options,
        headers,
    });

    return res;
}