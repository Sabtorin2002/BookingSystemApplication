import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../pages/AuthContext";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const { setUser} = useAuth();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8081/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const userDetailsRes = await fetch(`http://localhost:8081/api/users/${email}`);
        const user = await userDetailsRes.json();

        setUser(user);
        setMessage("Login successful!");
        navigate("/booking");
      } else {
        const errorText = await response.text();
        setMessage(`Login failed: ${errorText}`);
      }
    } catch (error) {
      setMessage("Server error. Try again later.");
    }
  };

  return (
    <div style={{ textAlign: "center", padding: "40px" }}>
      <h2>Log In</h2>
      <form onSubmit={handleLogin}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <br />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <br />
        <button type="submit" style={{ marginTop: "10px", padding: "8px 16px" }}>
          Log In
        </button>
      </form>
      <p>{message}</p>
    </div>
  );
};

export default LoginPage;
