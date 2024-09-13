import axios from "axios";

const SONG_API_BASE_URL = 'http//localhost:8080/api/songs';

class SongService {
    async getSongs() {
        return  await fetch("http://localhost:8080/api/songs").then(response => response.json());
    }
}

export default new SongService()