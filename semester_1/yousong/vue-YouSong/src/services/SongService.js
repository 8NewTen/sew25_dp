import axios from 'axios';

const API_URL = 'http://localhost:8080/api/songs';

class SongService {
    // Fetch all songs
    getSongs() {
        return axios.get(API_URL).then(response => response.data);
    }

    createSong(song) {
        return axios.post(API_URL, song)
            .then(response => response.data)
            .catch(error => {
                console.error("There was an error creating the song:", error);
                throw error; 
            });
    }

    updateSong(songId, song) {
        return axios.put(`${API_URL}/${songId}`, song)
            .then(response => response.data)
            .catch(error => {
                console.error("There was an error updating the song:", error);
                throw error;
            });
    }

    getSongById(songId) {
        return axios.get(`${API_URL}/${songId}`)
            .then(response => response.data)
            .catch(error => {
                console.error("There was an error fetching the song:", error);
                throw error;
            });
    }
}

export default new SongService();
