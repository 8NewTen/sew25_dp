import axios from 'axios';

const API_URL = 'http://localhost:8080/api/songs';
const GENRES_URL = 'http://localhost:8080/api/genres';
const ARTISTS_URL = 'http://localhost:8080/api/artists';

class SongService {
  // Fetch all songs with pagination and optional genre filter
  getSongs(page = 0, size = 5, selectedGenres = []) {
    const genresQuery = selectedGenres.length > 0 ? `&genres=${selectedGenres.join(',')}` : '';
    return axios.get(`${API_URL}?page=${page}&size=${size}${genresQuery}`)
      .then(response => response.data) // Adjust to return the response data
      .catch(error => {
        console.error("Error fetching songs:", error);
        throw error; 
      });
  } 

  // Search for songs with pagination, query, and optional genre filter
  searchSongs(query, page = 0, size = 5, selectedGenres = []) {
    const genresQuery = selectedGenres.length > 0 ? `&genres=${selectedGenres.join(',')}` : '';
    return axios.get(`${API_URL}?query=${query}&page=${page}&size=${size}${genresQuery}`)
      .then(response => response.data) // Adjust to return the response data
      .catch(error => {
        console.error("Error searching for songs:", error);
        throw error; 
      });
  }

  createSong(song) {
    return axios.post(API_URL, { 
      title: song.title,
      artist: { id: song.artist }, // Send artist as an object with id field
      genres: Array.isArray(song.genres) ? song.genres : [], 
      length: song.length
    })
    .then(response => response.data)
    .catch(error => {
      if (error.response && error.response.data) {
        throw error.response.data;
      } else {
        console.error("There was an error creating the song:", error);
        throw error; 
      }
    });
  }

    updateSong(songId, song, etag) {
        return axios.put(`${API_URL}/songs/${songId}`, song, {
            headers: etag ? { 'If-Match': etag } : {}
        })
            .then(response => response.data)
            .catch(error => {
                if (error.response) {
                    if (error.response.status === 412) {
                        console.error("Version mismatch! The song has been updated elsewhere.");
                        throw new Error("Version mismatch: The song has been modified by someone else.");
                    }
                    throw error.response.data;
                } else {
                    console.error("Error updating song:", error);
                    throw error;
                }
            });
    }


    getSongById(songId) {
        return axios.get(`${API_URL}/${songId}`)
            .catch(error => {
                console.error("There was an error fetching the song:", error);
                throw error;
            });
    }

  // Fetcht den song mit der ID und den Musikdaten
  getSongWithMusicData(songId) {
    return axios.get(`${API_URL}/${songId}/play`)
      .then(response => response.data)
      .catch(error => {
        console.error("Error fetching song with music data:", error);
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

  getGenres() {
    return axios.get(GENRES_URL)
      .then(response => response.data) // Return the list of genres
      .catch(error => {
        console.error("Error fetching genres:", error);
        throw error;
      });
  }

  getArtists() {
    return axios.get(ARTISTS_URL)
      .then(response => response.data)
      .catch(error => {
        console.error("Error fetching artists:", error);
        throw error;
      });
  }
}

export default new SongService();
