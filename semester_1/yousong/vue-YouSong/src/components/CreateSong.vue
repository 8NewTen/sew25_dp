<template>
    <div class="container mt-5 p-4 bg-light rounded shadow-sm">
      <h1 class="text-center mb-4">Create New Song</h1>
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
            placeholder="Enter length of the song"
            required
          />
        </div>
        <div class="d-flex justify-content-between">
          <button type="submit" class="btn btn-primary">Submit</button>
          <button type="button" @click="goBack" class="btn btn-secondary ms-3">Return</button>
        </div>
      </form>
      <div v-if="successMessage" class="alert alert-success mt-3 text-center">
        {{ successMessage }}
      </div>
    </div>
  </template>
  
  <script>
  import SongsService from '../services/SongService';
  
  export default {
    name: 'CreateSong',
    data() {
      return {
        song: {
          title: '',
          artist: '',
          genre: '',
          length: ''
        },
        successMessage: ''
      };
    },
    methods: {
      submitForm() {
        console.log('Submitting song: ', this.song); // Log the song data to verify the form input
  
        SongsService.createSong(this.song)
          .then(() => {
            this.successMessage = 'Song created successfully!';
            setTimeout(() => {
              this.$router.push({ name: 'Songs' });
            }, 2000); // Redirect after 2 seconds
          })
          .catch((error) => {
            console.error('Error creating song:', error);
          });
      },
      goBack() {
        this.$router.push({ name: 'Songs' }); // Navigate back to Song.vue page
      }
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
  