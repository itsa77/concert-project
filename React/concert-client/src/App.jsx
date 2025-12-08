import { BrowserRouter, Routes, Route } from "react-router-dom";

import LoginView from "./views/LoginView";
import RegisterView from "./views/RegisterView.jsx";
import DashboardView from "./views/DashboardView";   // placeholder for now

function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/login" element={<LoginView />} />
        <Route path="/register" element={<RegisterView />} />

        {/* After login redirect */}
        <Route path="/dashboard" element={<DashboardView />} />

        {/* Default route */}
        <Route path="*" element={<LoginView />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
