import React, { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../pages/AuthContext";
import NotificationBell from "../pages/NotificationBell"

const BookingPage = () => {
  const { user } = useAuth();

  const [roomId, setRoomId] = useState("");
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [rooms, setRooms] = useState([]);
  const [availability, setAvailability] = useState({});
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const roomRes = await axios.get("http://localhost:8081/api/rooms");
        setRooms(roomRes.data);
      } catch (err) {
        console.error("Error fetching rooms:", err);
      }
    };

    fetchRooms();
  }, []);

  useEffect(() => {
    console.log("🧠 AuthContext user =", user);
  }, [user]);


  const checkAvailability = async () => {
    if (!startTime || !endTime) return;

    try {
      const availabilityMap = {};

      const response = await axios.get("http://localhost:8081/api/bookings"); // ✅ all bookings
      const allBookings = response.data;

      for (let room of rooms) {
        const overlapping = allBookings.filter(
          booking =>
            booking.roomName === room.name &&
            !(new Date(endTime) <= new Date(booking.startTime) || new Date(startTime) >= new Date(booking.endTime))
        );

        availabilityMap[room.id] = overlapping.length === 0;
      }

      setAvailability(availabilityMap);
    } catch (err) {
      console.error("Error checking availability:", err);
    }
  };


  const handleBooking = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post("http://localhost:8081/api/bookings", {
        roomId: Number(roomId),
        userId: user.id,
        startTime,
        endTime
      });

      setMessage("✅ Booking successful!");
      checkAvailability(); // Refresh availability
    } catch (error) {
      console.error("Booking failed:", error);
      setMessage(`❌ Booking failed: ${error.response?.data || "Unknown error"}`);
    }

    console.log("Booking request sent with:", {
      roomId: Number(roomId),
      userId: user.id,
      startTime,
      endTime
    });

  };

  return (
    <div style={{ textAlign: "center", padding: "50px" }}>
      <NotificationBell/>
      <h1>Welcome to the Booking Page</h1>
      {user ? (
        <>
          <h2>Hello {user.fullName}!</h2>
          <button
            onClick={() => window.location.href = "/my-bookings"}
            style={{
              marginBottom: "20px",
              padding: "8px 20px",
              background: "#007bff",
              color: "#fff",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer"
            }}
          >
            👤 My Profile / Bookings
          </button>


          <form onSubmit={handleBooking} style={{ maxWidth: "400px", margin: "auto" }}>
            <div>
              <label>Start Time: </label>
              <input
                type="datetime-local"
                value={startTime}
                onChange={(e) => {
                  setStartTime(e.target.value);
                  checkAvailability();
                }}
                required
              />
            </div>
            <div>
              <label>End Time: </label>
              <input
                type="datetime-local"
                value={endTime}
                onChange={(e) => {
                  setEndTime(e.target.value);
                  checkAvailability();
                }}
                required
              />
            </div>
            <div>
              <label>Select Room: </label>
              <select value={roomId} onChange={(e) => setRoomId(e.target.value)} required>
                <option value="">-- Choose a room --</option>
                {rooms.map((room) => (
                  <option key={room.id} value={room.id}>
                    {room.name}
                  </option>
                ))}
              </select>
            </div>
            <button type="submit" style={{ marginTop: "15px" }}>Book Room</button>
          </form>

          {message && <p style={{ marginTop: "20px", fontWeight: "bold" }}>{message}</p>}

          <h3 style={{ marginTop: "50px" }}>📋 Room List with Availability:</h3>
          <table style={{ margin: "auto", marginTop: "10px", borderCollapse: "collapse" }}>
            <thead>
              <tr>
                <th style={{ padding: "8px", borderBottom: "1px solid #ccc" }}>Room</th>
                <th style={{ padding: "8px", borderBottom: "1px solid #ccc" }}>Available</th>
              </tr>
            </thead>
            <tbody>
              {rooms.map((room) => (
                <tr key={room.id}>
                  <td style={{ padding: "8px" }}>{room.name}</td>
                  <td style={{ padding: "8px" }}>
                    {availability[room.id] === undefined ? "N/A" : availability[room.id] ? "✅" : "❌"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      ) : (
        <p>Loading user info...</p>
      )}
    </div>
  );
};

export default BookingPage;
