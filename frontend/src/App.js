import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom"; //
import HomePage from "./pages/HomePage"; //
import Signup from "./pages/SignUp"; //
import LoginPage from "./pages/LoginPage";
import BookingPage from "./pages/BookingPage";
import MyBookingsPage from "./pages/MyBookingsPage";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/home" />} /> {/* ✅ Redirect "/" to "/home" */}
        <Route path="/home" element={<HomePage />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/booking" element={<BookingPage />} />
        <Route path="/my-bookings" element={<MyBookingsPage />} />
        <Route path="*" element={<h1>404 Not Found</h1>} />
      </Routes>
    </Router>
  );
}

export default App;
