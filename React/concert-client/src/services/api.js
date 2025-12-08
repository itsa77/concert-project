import axios from "axios";

const BASE_URL = "http://localhost:8080";

function getHeaders() {
    const token = localStorage.getItem("token");
    return token
        ? { Authorization: `Bearer ${token}` }
        : {};
}

function get(url) {
    return axios.get(BASE_URL + url, { headers: getHeaders() });
}

function post(url, data) {
    return axios.post(BASE_URL + url, data, { headers: getHeaders() });
}

function put(url, data) {
    return axios.put(BASE_URL + url, data, { headers: getHeaders() });
}

function del(url) {
    return axios.delete(BASE_URL + url, { headers: getHeaders() });
}

export default {
    get,
    post,
    put,
    delete: del
};