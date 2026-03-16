import { useEffect, useState } from "react";
import { apiFetch } from "../services/api";

export default function ConcertsView() {
    const [concerts, setConcerts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [searchTerm, setSearchTerm] = useState("");
    const [activeFilter, setActiveFilter] =useState("all");


    useEffect(() => {
        async function loadConcerts() {
            try {
                setLoading(true);

                const res = await apiFetch("/concerts");
                const data = await res.json();

                setConcerts(data);
            } catch {
                setError("Failed to load concerts.");
            } finally {
                setLoading(false);
            }
        }

        loadConcerts();
    }, []);

    if (loading) return <p>Loading concerts...</p>;
    if (error) return <p>{error}</p>;

    return (
        <div className="concerts-page">

            {/* FILTER BAR */}

            <div className="concert-filters">
                <div className="filter-buttons">
                    <button 
                        className={activeFilter === "all" ? "active" : ""}
                        onClick={() => setActiveFilter("all")}
                    >
                        All
                    </button>

                    <button>Artists</button>
                    <button>Venues</button>
                    <button>Tours</button>
                    <button>Dates</button>

                    <button>Opening Acts</button>
                    <button>Festivals</button>
                    <button>States</button>
                </div>

                <input
                    type="text"
                    placeholder="Search concerts..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="concert-search"
                />

            </div>
        

            {/* CONCERT LIST */}

            <div className="concert-list">

                {concerts.map((concert) => (
                     <div className="concert-card" key={concert.concertId}>
                    
                        <div className="concert-card-left">
                        
                            <h3>
                                {concert.festivalName
                                ? `${concert.festivalName} - ${concert.venueName}`
                                : `${concert.artistName} - ${concert.venueName}`}
                            </h3>

                            {concert.tourName && (
                            <div className="concert-tour">({concert.tourName})</div>
                            )}

                            {concert.openingActNames?.length > 0 && (
                                <ul className="opening-acts">
                                    {concert.openingActNames.map((act) => (
                                    <li key={act}>{act}</li>
                                    ))}
                                </ul>
                            )}
                        </div>

                        <div className="concert-card-right">
                            <div className="concert-date">
                                {(() => {
                                    const [year, month, day] = concert.date.split("-");
                                    return `${month}/${day}/${year}`;
                                })()}
                            </div>
                            <div className="concert-location">
                            {concert.venueCity}, {concert.venueState}
                            </div>
                        </div>

                    </div>
                ))}
            </div>
        </div>

    );
}