import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { apiFetch } from "../services/api";

export default function AddConcertView() {
    const navigate = useNavigate();

    const [artistName, setArtistName] = useState("");
    const [venueName, setVenueName] = useState("");
    const [venueCity, setVenueCity] = useState("");
    const [venueState, setVenueState] = useState("");
    const [date, setDate] = useState("");
    const [startTime, setStartTime] = useState("");
    const [tourName, setTourName] = useState(""); 
    const [festivalName, setFestivalName] = useState("");
    const [openingActNames, setOpeningActNames] = useState([""]);
    const [error, setError] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    function handleOpeningActChange(index, value) {
        const updateActs = [...openingActNames];
        updateActs[index] = value;
        setOpeningActNames(updateActs);
    }

    function addOpeningActField() {
        setOpeningActNames([...openingActNames, ""]);
    }

    function removeOpeningActField(index) {
        const updatedActs = openingActNames.filter((_, i) => i !== index);
        setOpeningActNames(updatedActs.length > 0 ? updatedActs : [""]);
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");

        if (
            !artistName.trim() ||
            !venueName.trim() ||
            !venueCity.trim() ||
            !venueState.trim() ||
            !date
        ) {
            setError("Artist, venue name, city, state, and date are required.");
            return;
        }

        const concertData = {
            artistName: artistName.trim(),
            venueName: venueName.trim(),
            venueCity: venueCity.trim(),
            venueState: venueState.trim(),
            date,
            startTime: startTime ? startTime.trim() : null,
            tourName: tourName.trim() || null,
            festivalName: festivalName.trim() || null,
            openingActNames: openingActNames
                .map((name) => name.trim())
                .filter((name) => name.length > 0),
        };

        try {
            setIsSubmitting(true);

            await apiFetch("/concerts", {
                method: "POST",
                body: JSON.stringify(concertData),
            });

            navigate("/dashboard");
        } catch (err) {
            setError(err.message || "Failed to create concert.");
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <div className="add-concert-view">
            <h1>Add Concert</h1>

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Artist Name</label>
                    <input
                        type="text"
                        value={artistName}
                        onChange={(e) => setArtistName(e.target.value)}
                    />
                </div>

                <div>
                    <lable>Venue Name</lable>
                    <input
                        type="text"
                        value={venueName}
                        onChange={(e) => setVenueName(e.target.value)}
                    />
                </div>

                <div>
                    <lable>Venue City</lable>
                    <input
                        type="text"
                        value={venueCity}
                        onChange={(e) => setVenueCity(e.target.value)}
                    />
                </div>

                <div>
                    <label>Venue State</label>
                    <input
                        type="text"
                        value={venueState}
                        onChange={(e) => setVenueState(e.target.value)}
                    />
                </div>

                <div>
                    <lable>Date</lable>
                    <input
                        type="date"
                        value={date}
                        onChange={(e) => setDate(e.target.value)}
                    />
                </div>

                <div>
                    <lable>Start Time</lable>
                    <input
                        type="time"
                        value={startTime}
                        onChange={(e) => setStartTime(e.target.value)}
                    />
                </div>

                <div>
                    <lable>Tour Name</lable>
                    <input
                        type="text"
                        value={tourName}
                        onChange={(e) => setTourName(e.target.value)}
                    />
                </div>

                <div>
                    <lable>Festival Name</lable>
                    <input
                        type="text"
                        value={festivalName}
                        onChange={(e) => setFestivalName(e.target.value)}
                    />
                </div>

                <div>
                    <label>Opening Acts</label>

                    {openingActNames.map((act,index) => (
                        <div key={index}>
                            <input
                                type="text"
                                value={act}
                                onChange={(e) => handleOpeningActChange(index, e.target.value)}
                                placeholder="Opening act name"
                            />
                            <button type="button" onClick={() => removeOpeningActField(index)}>
                                remove
                            </button>
                        </div>
                    ))}

                    <button type="button" onClick={addOpeningActField}>
                        Add Opening Act
                    </button>
                </div>

                {error && <p>{error}</p>}

                <button type="submit" disabled={isSubmitting}>
                    {isSubmitting ? "Saving..." : "Create Concert"}
                </button>
            </form>
        </div>
    );
}