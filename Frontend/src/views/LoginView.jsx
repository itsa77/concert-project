import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService";

export default function LoginView() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();
    const location = useLocation();

    const from = location.state?.from?.pathname || "/dashboard";

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");

        try {
            await AuthService.login({ username, password });
            navigate(from, { replace: true });
        } catch (err) {
            setError(err?.message || "Login failed");
        }
    }

    return (
       <div>
        <h1>Login</h1>

        {error && <p style={{ color: "red" }}>{error}</p>}
        
        <form onSubmit={handleSubmit}>
            <div>
                <label>Username</label>
                <input
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    autoComplete="username"
                />
            </div>

            <div>
                <lable>Password</lable>
                <input
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    type="password"
                    autoColmplete="current-password"
                />
            </div>

            <button type="submit">Log In</button>
        </form>
        
       </div>
    );
}