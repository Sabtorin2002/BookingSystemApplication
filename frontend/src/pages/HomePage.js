import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
  const [message, setMessage] = useState("Loading...");
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8081/home")
      .then((response) => response.text())
      .then((data) => setMessage(data))
      .catch((error) => setMessage("Error fetching message: " + error.message));
  }, []);

  const handleSignupClick = () => {
    navigate("/signup");
  };

  return (
      <div style={{ textAlign: "center", marginTop: "50px" }}>
        <h1>{message}</h1>
        <button
          onClick={() => navigate("/signup")}
          style={{ padding: "10px 20px", marginTop: "20px", cursor: "pointer" }}
        >
          Register
        </button>
        <br />
        <button
          onClick={() => navigate("/login")}
          style={{ padding: "10px 20px", marginTop: "10px", cursor: "pointer" }}
        >
          Log In
        </button>
      </div>
    );
  };

export default HomePage;
