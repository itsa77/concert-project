import { useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService.jsx";

export default function LoginView() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    function handleSubmit(e) {
        e.preventDefault();
        setError("");

        AuthService.login(username, password)
            .then(() => {
                navigate("/dashboard");
            })
            .catch(() => {
                setError("Invalid username or password.");
            });
    }

    return (
        <div style={{ maxWidth: "300px", margin: "auto" }}>
            <h1>Login</h1>

            {error && <p style={{ color: "red" }}>{error}</p>}

            <form onSubmit={handleSubmit}>

                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <button type="submit">
                    Login
                </button>
            </form>

            <p>
                Don't have an account?{" "}
                <a href="/register">Register</a>
            </p>
        </div>
    );
}