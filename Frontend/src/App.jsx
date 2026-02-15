import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import LoginView from "./views/LoginView";
import RegisterView from "./views/RegisterView";
import UserDashboardView from "./views/UserDashboardView";
import ConcertsView from "./views/ConcertsView";
import AddConcertView from "./views/AddConcertView";
import FriendsView from "./views/FriendsView";
import ProtectedRoute from "./components/ProtectedRoute";
import "./App.css";

function RecentConcerts() {
  return <div>Recent Concerts</div>
}


function App() {
  
  return (
   <BrowserRouter>
    <Routes>
      {/* default route */}
      <Route path="/" element={<Navigate to="/dashboard" />} />
      
      {/* public routes */}
      <Route path="/login" element={<LoginView />} />
      <Route path="/register" element={<RegisterView />} />

      {/* protected routes */}
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <UserDashboardView />
          </ProtectedRoute>
        }
      >
        {/* These render inside UserDashboardView via <Outlet /> */}
        <Route index element={<RecentConcerts />} />
        <Route path="concerts" element={<ConcertsView />} />
        <Route path="friends" element={<FriendsView />} />
        <Route path="add" element={<AddConcertView />} />
      </Route>

      {/* catch-all */}
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
   </BrowserRouter>
  );
}

export default App;