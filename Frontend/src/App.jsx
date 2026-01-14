import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import LoginView from "./views/LoginView";
import RegisterView from "./views/RegisterView";
import UserDashboardView from "./views/UserDashboardView";
import ConcertsView from "./views/ConcertsView";
import AddConcertView from "./views/AddConcertView";
import ProtectedRoute from "./components/ProtectedRoute";


function App() {
  
  return (
   <BrowserRouter>
    <Routes>
      {/* default route */}
      <Route path="/" element={<Navigate to="/login" />} />
      
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
      />

      <Route
        path="/concerts"
        element={
          <ProtectedRoute>
            <ConcertsView />
          </ProtectedRoute>
        } 
      />

      <Route
        path="/concerts/new"
        element={
          <ProtectedRoute>
            <AddConcertView />
          </ProtectedRoute>
        }
      />

      {/* catch-all */}
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
   </BrowserRouter>
  );
}

export default App;