import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "./AuthContext"; // ✅ Make sure path is correct
import NotificationBell from "../pages/NotificationBell"

const MyBookingsPage = () => {
  const { user } = useAuth(); // ✅ Step 1: This is where user comes from
  const [bookings, setBookings] = useState([]);

  // ✅ Step 2: Log the user to check what's inside
  useEffect(() => {
    console.log("👤 Fetched user from context:", user);
  }, [user]);

  // ✅ Step 3: Fetch bookings if user is defined
  useEffect(() => {
    if (!user || !user.id) return;

    const fetchBookings = async () => {
      try {
        const response = await axios.get(`http://localhost:8081/api/bookings/user/${user.id}`);
        console.log("📦 Bookings fetched:", response.data);
        setBookings(response.data);
      } catch (error) {
        console.error("❌ Error fetching bookings:", error);
      }
    };

    fetchBookings();
  }, [user]);

  // ✅ Step 3: Handle loading or missing user
  if (!user) {
    return <p>🔄 Loading user context... (or no user is logged in)</p>;
  }

  return (
    <div style={{ padding: "40px", textAlign: "center" }}>
      <NotificationBell/>
      <h1>📅 My Bookings</h1>

      {bookings.length === 0 ? (
        <p>📭 You have no bookings yet.</p>
      ) : (
        <table style={{ margin: "auto", marginTop: "20px", borderCollapse: "collapse" }}>
          <thead>
            <tr>
              <th style={{ padding: "8px", borderBottom: "1px solid #ccc" }}>Room</th>
              <th style={{ padding: "8px", borderBottom: "1px solid #ccc" }}>Start Time</th>
              <th style={{ padding: "8px", borderBottom: "1px solid #ccc" }}>End Time</th>
              <th style={{ padding: "8px", borderBottom: "1px solid #ccc" }}>Status</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map((booking) => (
              <tr key={booking.id}>
                <td style={{ padding: "8px" }}>{booking.roomName}</td>
                <td style={{ padding: "8px" }}>{booking.startTime}</td>
                <td style={{ padding: "8px" }}>{booking.endTime}</td>
                <td style={{ padding: "8px" }}>{booking.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default MyBookingsPage;
