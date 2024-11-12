<template>
  <div class="container mt-5 p-4 bg-light rounded shadow-sm">
    <h1 class="text-center mb-4">Edit Song</h1>
    <form @submit.prevent="submitForm" class="needs-validation">
      <div class="mb-3">
        <label for="title" class="form-label">Title</label>
        <input
          type="text"
          id="title"
          v-model="song.title"
          class="form-control"
          placeholder="Enter song title"
          required
        />
      </div>
      <div class="mb-3">
        <label for="artist" class="form-label">Artist</label>
        <input
          type="text"
          id="artist"
          v-model="song.artist"
          class="form-control"
          placeholder="Enter artist name"
          required
        />
      </div>
      <div class="mb-3">
        <label for="genre" class="form-label">Genre</label>
        <input
          type="text"
          id="genre"
          v-model="song.genre"
          class="form-control"
          placeholder="Enter genre"
          required
        />
      </div>
      <div class="mb-3">
        <label for="length" class="form-label">Length (seconds)</label>
        <input
          type="number"
          id="length"
          v-model="song.length"
          class="form-control"
          placeholder="Enter song length"
          required
        />
      </div>
      <div class="d-flex justify-content-between">
        <button type="submit" class="btn btn-primary">Save</button>
        <button type="button" @click="goBack" class="btn btn-secondary ms-3">Return</button>
      </div>
    </form>

    <div v-if="errorMessage" class="alert alert-danger mt-3 text-center">
      {{ errorMessage }}
    </div>
    
    <div v-if="successMessage" class="alert alert-success mt-3 text-center">
      {{ successMessage }}
    </div>
  </div>
</template>

<script>
import SongService from '../services/SongService';

export default {
  name: 'EditSong',
  data() {
    return {
      song: {
        title: '',
        artist: '',
        genre: '',
        length: ''
      },
      successMessage: '',
      errorMessage: '' // To store validation error messages
    };
  },
  methods: {
    fetchSong() {
      const songId = this.$route.params.id;
      SongService.getSongById(songId)
        .then((response) => {
          this.song = response;
        })
        .catch((error) => {
          console.error('Error fetching song:', error);
        });
    },
    submitForm() {
      const songId = this.$route.params.id;
      SongService.updateSong(songId, this.song)
        .then(() => {
          this.successMessage = 'Song updated successfully!';
          setTimeout(() => {
            this.$router.push({ name: 'Songs' });
          }, 2000); // Redirect after 2 seconds
        })
        .catch((error) => {
          console.error('Error response:', error.response); // Log the full error response
          if (error.response && error.response.data) {
            // Display specific error messages from the backend
            const errorMessages = Object.values(error.response.data);
            this.errorMessage = errorMessages.join(', ');
          } else {
            this.errorMessage = 'An unexpected error occurred.';
          }
        });
    },
    goBack() {
      this.$router.push({ name: 'Songs' }); // Navigate back to Song.vue
    }
  },
  created() {
    this.fetchSong();
  }
};
</script>

<style scoped>
.container {
  max-width: 600px;
}
h1 {
  font-weight: bold;
  color: #343a40;
}
.form-label {
  font-weight: 600;
}
input::placeholder {
  font-style: italic;
}
button {
  width: 150px;
}
</style>
