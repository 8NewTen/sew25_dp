<template>
  <div class="d-flex flex-column justify-content-center">
    <h1 class="text-center" style="text-align: center; margin: 1rem auto;">YouSong <br><!--!Relationships fehlt und validation noch nicht finished!--></h1>
    <input type="search" class="form-control" v-model="query" @input="searchSongs" placeholder="What do you want to listen to?" style="border: 2px solid #007bff;">
    <button class="btn btn-primary mb-3 mt-3" @click="navigateToCreateSong">Create New Song</button>
    
    <div class="table-responsive mt-3" v-if="songs.length > 0">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>Song Id</th>
            <th>Song Title</th>
            <th>Song Artist</th>
            <th>Song Genre</th>
            <th>Length (seconds)</th>
            <th>Edit or Delete</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="song in songs" :key="song.id">
            <td>{{ song.id }}</td>
            <td>{{ song.title }}</td>
            <td>{{ song.artist }}</td>
            <td>{{ song.genre }}</td>
            <td>{{ song.length }}</td>
            <td>
              <div class="d-flex align-items-center">
                <button class="btn btn-secondary me-2" @click="navigateToEditSong(song.id)">Edit</button>
                <button class="btn btn-danger" @click="confirmDelete(song.id)">Delete</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-else>
      <p class="text-center text-danger mt-3">No songs can be found. Please adjust your search.</p>
    </div>

    <!-- Pagination Section -->
    <div class="pagination-controls d-flex justify-content-between align-items-center mt-3">
      <div>
        <button class="btn btn-outline-secondary border border-primary mx-2" @click="fetchSongs(currentPage - 1)" :disabled="currentPage === 0">Previous</button>
        <button class="btn btn-outline-secondary border border-primary mx-2" @click="fetchSongs(currentPage + 1)" :disabled="currentPage >= totalPages - 1">Next</button>
      </div>
      <div class="pagination-info">
        <span>Page {{ currentPage + 1 }} of {{ totalPages }}</span>
      </div>
      <div>
        <button class="btn btn-outline-secondary border border-primary mx-2" @click="fetchSongs(0)" :disabled="currentPage === 0">First</button>
        <button class="btn btn-outline-secondary border border-primary mx-2" @click="fetchSongs(totalPages - 1)" :disabled="currentPage >= totalPages - 1">Last</button>
      </div>
    </div>
  </div>
</template>

<script>
import SongsService from '../services/SongService';
import { required, minLength, maxLength, numeric } from '@vuelidate/validators';
import useVuelidate from '@vuelidate/core';

export default {
  name: 'Songs',
  data() {
    return {
      query: '', // Default to an empty string
      songs: [],
      currentPage: 0,
      totalPages: 0,
      pageSize: 5, // Set the number of entries per page
    };
  },
  validations() {
    return {
      query: {
        required,
        minLength: minLength(1),
        maxLength: maxLength(100),
      },
      songs: {
        $each: {
          title: { required, minLength: minLength(1), maxLength: maxLength(100) },
          artist: { required, minLength: minLength(1), maxLength: maxLength(100) },
          genre: { required, minLength: minLength(1), maxLength: maxLength(100) },
          length: { required, numeric },
        },
      },
    };
  },
  methods: {
    // Automatically search when typing
    async searchSongs() {
      try {
        this.currentPage = 0; // Reset to first page on new search
        if (this.query) {
          const response = await SongsService.searchSongs(this.query, this.currentPage, this.pageSize);
          this.songs = response.content;
          this.totalPages = response.totalPages;
        } else {
          this.getSongs();
        }
      } catch (error) {
        console.error('No songs can be found. Please adjust your search.', error);
        this.songs = []; // Reset to an empty array if no songs are found
        this.totalPages = 0; // Reset total pages
      }
    },
    async getSongs() {
      try {
        const response = await SongsService.getSongs(this.currentPage, this.pageSize);
        this.songs = response.content;
        this.totalPages = response.totalPages;
      } catch (error) {
        console.error("Error fetching songs:", error);
      }
    },
    fetchSongs(page) {
      this.currentPage = page;
      this.getSongs();
    },
    navigateToCreateSong() {
      this.$router.push({ name: 'CreateSong' });
    },
    navigateToEditSong(songId) {
      this.$router.push({ name: 'EditSong', params: { id: songId } });
    },
    confirmDelete(songId) {
      if (confirm("Are you sure you want to delete this song?")) {
        this.deleteSong(songId);
      }
    },
    deleteSong(songId) {
      SongsService.deleteSong(songId)
        .then(() => {
          console.log(`Song with ID ${songId} deleted successfully.`);
          this.searchSongs(); // Refresh the song list after deletion
        })
        .catch(error => {
          console.error(`Error deleting song with ID ${songId}:`, error);
        });
    },
  },
  created() {
    this.getSongs(); // Fetch all songs when the component is created
  },
  mounted() {
    this.$v = useVuelidate(); // Initialize Vuelidate
  },
};
</script>

<style>
body {
  height: 100%;
}
</style>
