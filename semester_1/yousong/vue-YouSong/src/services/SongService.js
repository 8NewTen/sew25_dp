import axios from 'axios';

const API_URL = 'http://localhost:8080/api/songs';

class SongService {
  // Fetch all songs with pagination
  getSongs(page = 0, size = 5) {
    return axios.get(`${API_URL}?page=${page}&size=${size}`)
      .then(response => response.data) // Adjust to return the response data
      .catch(error => {
        console.error("Error fetching songs:", error);
        throw error; 
      });
  } 

  // Search for songs with pagination
  searchSongs(query, page = 0, size = 5) {
    return axios.get(`${API_URL}?query=${query}&page=${page}&size=${size}`)
      .then(response => response.data) // Adjust to return the response data
      .catch(error => {
        console.error("Error searching for songs:", error);
        throw error; 
      });
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

  deleteSong(songId) {
    return axios
      .delete(`${API_URL}/${songId}`)
      .then(response => {
        console.log(`Song with ID ${songId} deleted successfully.`);
        return response.data; // Ensure to return response data for chaining or handling
      })
      .catch(error => {
        console.error(`Error deleting song with ID ${songId}:`, error);
        throw error;
      });
  }
}

export default new SongService();
