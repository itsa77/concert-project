
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService.jsx";

export default function RegisterView() {

    const [username, setUsername] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const navigate = useNavigate();

    function handleSubmit(e) {
        e.preventDefault();
        setError("");

        const user = {
            username,
            firstName,
            lastName,
            email,
            passwordHash: password   
        };

        AuthService.register(user)
            .then(() => navigate("/login"))
            .catch(() => setError("Registration failed. Try a different username/email."));
    }

    return (
        <div style={{ maxWidth: "300px", margin: "auto" }}>
            <h1>Register</h1>

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
                    type="text"
                    placeholder="First Name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    required
                />

                <input
                    type="text"
                    placeholder="Last Name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    required
                />

                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
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
                    Register
                </button>
            </form>

            <p>
                Already registered?{" "}
                <a href="/login">Login</a>
            </p>
        </div>
    );


}