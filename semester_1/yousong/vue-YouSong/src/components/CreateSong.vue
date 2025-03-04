<template>
  <div class="container mt-5 p-4 bg-light rounded shadow-sm">
    <h1 class="text-center mb-4">Create New Song</h1>
    <form @submit.prevent="submitForm" class="needs-validation">
      <!-- Title Field with Validation -->
      <div class="mb-3">
        <label for="title" class="form-label">Title</label>
        <span v-if="v$?.song?.title?.$dirty && v$?.song?.title?.required?.$invalid" class="text-danger">
          Title is required.
        </span>
        <span v-if="v$?.song?.title?.$dirty && v$?.song?.title?.maxLength?.$invalid" class="text-danger">
          Title cannot exceed 100 characters.
        </span>
        <input
          type="text"
          id="title"
          v-model="song.title"
          class="form-control"
          placeholder="Enter song title"
        />
      </div>

      <!-- Artist Field with Validation -->
      <div class="mb-3">
        <label for="artist" class="form-label">Artist</label>
        <span v-if="v$?.song?.artist?.$dirty && v$?.song?.artist?.required?.$invalid" class="text-danger">
          Artist is required.
        </span>
        <span v-if="v$?.song?.artist?.$dirty && v$?.song?.artist?.maxLength?.$invalid" class="text-danger">
          Artist cannot exceed 100 characters.
        </span>
        <input
          type="text"
          id="artist"
          v-model="song.artist"
          class="form-control"
          placeholder="Enter artist name"
        />
      </div>

      <!-- Genres Field with Validation -->
      <div class="mb-3">
        <label for="genres" class="form-label">Genres</label>
        <span v-if="v$?.song?.genres?.$dirty && v$?.song?.genres?.required?.$invalid" class="text-danger">
          At least one genre is required.
        </span>
        <select
          id="genres"
          v-model="song.genres"
          class="form-control"
          multiple>
          <option v-for="genre in availableGenres" :key="genre" :value="genre">
            {{ genre }}
          </option>
        </select>
      </div>

      <!-- Length Field with Validation -->
      <div class="mb-3">
        <label for="length" class="form-label">Length (seconds)</label>
        <span v-if="v$?.song?.length?.$error" class="text-danger">
          Length is required.
        </span>
        <span v-if="v$?.song?.length?.$dirty && v$?.song?.length?.numeric?.$invalid" class="text-danger">
          Length must be a number.
        </span>
        <input
          type="number"
          id="length"
          v-model="song.length"
          class="form-control"
          placeholder="Enter length of the song"
          @blur=" v$.song.length.$touch()"
        />
      </div>

      <!-- Buttons -->
      <div class="d-flex justify-content-between">
        <button type="submit" class="btn btn-primary">Submit</button>
        <button type="button" @click="goBack" class="btn btn-secondary ms-3">Return</button>
      </div>
    </form>

    <!-- Error and Success Messages -->
    <div v-if="errorMessage" class="alert alert-danger mt-3 text-center">
      {{ errorMessage }}
    </div>

    <div v-if="successMessage" class="alert alert-success mt-3 text-center">
      {{ successMessage }}
    </div>
  </div>
</template>

<script>
import SongsService from '../services/SongService';
import useVuelidate from '@vuelidate/core';
import { required, maxLength, numeric } from '@vuelidate/validators';

export default {
  name: 'CreateSong',
  data() {
    return {
      song: {
        title: '',
        artist: '',
        genres: [],
        length: ''
      },
      availableGenres: [],
      successMessage: '',
      errorMessage: '', // To store validation error messages
      v$: null // Hier speichern wir die Vuelidate-Instanz
    };
  },

  setup() {
    return { v$: useVuelidate() }
  },

  validations () {
      return {
        song: {
          title: { required, maxLength: maxLength(100) },
          artist: { required, maxLength: maxLength(100) },
          genres: { required },
          length: { required, numeric }
        }
      };
  },

  methods: {

    
    // Fetch genres from API
    async fetchGenres() {
      try {
        const response = await SongsService.getGenres();
        this.availableGenres = response;
        console.log("Fetched genres:", this.availableGenres);
      } catch (error) {
        console.error("Error fetching genres:", error);
      }
    },

    // Submit the form with validation
    submitForm() {
      console.log('Submitting song:', this.song); // Log the song data to verify the form input

      // Trigger Validation
      this.v$.$touch();

      // Check for validation errors
      if (this.v$.$invalid) {
        this.errorMessage = 'Please correct the validation errors.';
        return;
      }

      // Call Backend Service
      SongsService.createSong(this.song)
        .then(() => {
          this.successMessage = 'Song created successfully!';
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

    // Navigate back
    goBack() {
      this.$router.push({ name: 'Songs' }); // Navigate back to Song.vue page
    }
  },

  mounted() {
    this.fetchGenres();
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