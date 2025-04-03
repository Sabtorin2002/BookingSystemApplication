import React, { useEffect, useState } from "react";
import axios from "axios";
import { useAuth } from "../pages/AuthContext";

const NotificationBell = () => {
  const { user } = useAuth();
  const [notifications, setNotifications] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);

  const fetchNotifications = async () => {
    try {
      const res = await axios.get(`http://localhost:8081/api/notifications/user/${user.id}/unread`);
      setNotifications(res.data);
    } catch (err) {
      console.error("❌ Failed to load notifications:", err);
    }
  };

  const markAsRead = async (id) => {
    try {
      await axios.put(`http://localhost:8081/api/notifications/${id}/read`);
      fetchNotifications(); // refresh
    } catch (err) {
      console.error("❌ Failed to mark as read:", err);
    }
  };

  useEffect(() => {
    if (user && user.id) {
      fetchNotifications();
    }
  }, [user]);

  useEffect(() => {
    if (user && user.id) {
      fetchNotifications();
      const interval = setInterval(fetchNotifications, 30000); // every 30s
      return () => clearInterval(interval); // cleanup
    }
  }, [user]);


  if (!user) return null;

  return (
    <div style={{ position: "absolute", top: 10, right: 20 }}>
      <div
        onClick={() => setShowDropdown((prev) => !prev)}
        style={{
          cursor: "pointer",
          backgroundColor: "#eee",
          padding: "8px 12px",
          borderRadius: "20px",
          fontWeight: "bold"
        }}
      >
        🔔 {notifications.length}
      </div>

      {showDropdown && (
        <div
          style={{
            position: "absolute",
            right: 0,
            marginTop: "5px",
            background: "#fff",
            border: "1px solid #ccc",
            borderRadius: "8px",
            width: "300px",
            boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
            zIndex: 1000,
            maxHeight: "300px",
            overflowY: "auto"
          }}
        >
          {notifications.length === 0 ? (
            <p style={{ padding: "10px" }}>No new notifications</p>
          ) : (
            notifications.map((n) => (
              <div key={n.id} style={{ padding: "10px", borderBottom: "1px solid #eee" }}>
                <p style={{ margin: 0 }}>{n.message}</p>
                <small style={{ color: "#888" }}>{n.createdAt}</small>
                <br />
                <button
                  onClick={() => markAsRead(n.id)}
                  style={{
                    marginTop: "5px",
                    fontSize: "12px",
                    padding: "3px 6px",
                    background: "#28a745",
                    color: "#fff",
                    border: "none",
                    borderRadius: "4px",
                    cursor: "pointer"
                  }}
                >
                  Mark as read
                </button>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default NotificationBell;
