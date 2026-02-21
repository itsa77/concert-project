import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { apiFetch } from "../services/api";

export default function AddConcertView() {
    const navigate = useNavigate();

    const [artist, setArtist] = useState("");
    const [venue, setVenue] = useState("");
    const [city, setCity] = useState("");
    const [date, setDate] = useState("");
    
    
    
    return 
}