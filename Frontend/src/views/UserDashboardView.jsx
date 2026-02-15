import { NavLink, Outlet } from "react-router-dom";


export default function UserDashboardView() {
    const username = localStorage.getItem("username");

    return (
        <div className="dashboard-container">

            {/* Header */}
            <div className="dashboard-header">
                <h2>{username} - Rank</h2>
            </div>

            {/* Tabs */}
            <div className="dashboard-tabs">
                <NavLink to="/dashboard" end className={({ isActive }) => isActive ? "tab active" : "tab"}>
                    Dashboard
                </NavLink>

                <NavLink to="/dashboard/concerts" className={({ isActive }) => isActive ? "tab active" : "tab"}>
                    Concerts 
                </NavLink>

                <NavLink to="/dashboard/friends" className={({ isActive }) => isActive ? "tab active" : "tab"}>
                    Friends 
                </NavLink>

                <NavLink to="/dashboard/add" className={({ isActive }) => isActive ? "tab active" : "tab"}>
                    Add 
                </NavLink>
            </div>

            {/* Big Content Box */}
            <section className="dashboard-content">
                <Outlet />
            </section>

        </div>
    );
}