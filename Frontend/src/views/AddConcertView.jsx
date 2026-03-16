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

    const US_STATES = [
  { code: "AL", name: "Alabama" }, 
  { code: "AK", name: "Alaska" }, 
  { code: "AZ", name: "Arizona" }, 
  { code: "AR", name: "Arkansas" },
  { code: "CA", name: "California" }, 
  { code: "CO", name: "Colorado" },
  { code: "CT", name: "Connecticut" }, 
  { code: "DE", name: "Delaware" },
  { code: "FL", name: "Florida" }, 
  { code: "GA", name: "Georgia" },
  { code: "HI", name: "Hawaii" }, 
  { code: "ID", name: "Idaho" },
  { code: "IL", name: "Illinois" }, 
  { code: "IN", name: "Indiana" },
  { code: "IA", name: "Iowa" }, 
  { code: "KS", name: "Kansas" },
  { code: "KY", name: "Kentucky" }, 
  { code: "LA", name: "Louisiana" },
  { code: "ME", name: "Maine" },
  { code: "MD", name: "Maryland" },
  { code: "MA", name: "Massachusetts" },
  { code: "MI", name: "Michigan" },
  { code: "MN", name: "Minnesota" },
  { code: "MS", name: "Mississippi" },
  { code: "MO", name: "Missouri" },
  { code: "MT", name: "Montana" },
  { code: "NE", name: "Nebraska" },
  { code: "NV", name: "Nevada" },
  { code: "NH", name: "New Hampshire" },
  { code: "NJ", name: "New Jersey" },
  { code: "NM", name: "New Mexico" },
  { code: "NY", name: "New York" },
  { code: "NC", name: "North Carolina" },
  { code: "ND", name: "North Dakota" },
  { code: "OH", name: "Ohio" },
  { code: "OK", name: "Oklahoma" },
  { code: "OR", name: "Oregon" },
  { code: "PA", name: "Pennsylvania" },
  { code: "RI", name: "Rhode Island" },
  { code: "SC", name: "South Carolina" },
  { code: "SD", name: "South Dakota" },
  { code: "TN", name: "Tennessee" },
  { code: "TX", name: "Texas" },
  { code: "UT", name: "Utah" },
  { code: "VT", name: "Vermont" },
  { code: "VA", name: "Virginia" },
  { code: "WA", name: "Washington" },
  { code: "WV", name: "West Virginia" },
  { code: "WI", name: "Wisconsin" },
  { code: "WY", name: "Wyoming" }
];

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
                    <label>Main Artist Name</label>
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
                    <label htmlFor="VenueState">Venue State</label>
                    <select
                        id="venueState"
                        value={venueState}
                        onChange={(e) => setVenueState(e.target.value)}
                    >
                        <option value="">Select state</option>
                        {US_STATES.map((state) => (
                            <option key={state.code} value={state.name}>
                                {state.code} - {state.name}
                            </option>
                        ))}
                    </select>
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