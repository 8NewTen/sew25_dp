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
        <select id="artist" v-model="song.artist" class="form-control">
          <option value="">Select an artist</option>
          <option v-for="artist in availableArtists" :key="artist.id" :value="artist.id">
            {{ artist.name }}
          </option>
        </select>
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
          v-model.number="song.length"
          class="form-control"
          placeholder="Enter length of the song"
          @blur="v$.song.length.$touch()"
        />
      </div>

      <!-- Music File Upload Field -->
      <div class="mb-3">
        <label for="musicFile" class="form-label">Music File</label>
        <span v-if="v$?.song?.musicData?.$dirty && v$?.song?.musicData?.required?.$invalid" class="text-danger">
          Music file is required.
        </span>
        <input
          type="file"
          id="musicFile"
          @change="handleFileUpload"
          class="form-control"
          accept="audio/*"
        />
        <div v-if="uploadStatus" class="mt-2" :class="{'text-success': uploadStatus === 'File selected', 'text-danger': uploadStatus !== 'File selected'}">
          {{ uploadStatus }}
        </div>
      </div>

      <!-- Buttons -->
      <div class="d-flex justify-content-between">
        <button type="submit" class="btn btn-primary" :disabled="isSubmitting">
          {{ isSubmitting ? 'Submitting...' : 'Submit' }}
        </button>
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
        length: null, // Changed from empty string to null for better numeric handling
        musicData: null // To store the music file as data URL
      },
      availableGenres: [],
      availableArtists: [],
      successMessage: '',
      errorMessage: '',
      uploadStatus: '',
      isSubmitting: false,
      v$: null
    };
  },

  setup() {
    return { v$: useVuelidate() }
  },

  validations() {
    return {
      song: {
        title: { required, maxLength: maxLength(100) },
        artist: { required }, // Only validate required, no maxLength for ID
        genres: { required },
        length: { required, numeric },
        musicData: { required }
      }
    };
  },

  methods: {
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (!file) {
        this.uploadStatus = '';
        this.song.musicData = null;
        return;
      }
      
      // Check file size as mp3 is formatted as ASCII String (limit to ~15MB which is safely under the MEDIUMTEXT limit)
      if (file.size > 15 * 1024 * 1024) {
        this.uploadStatus = 'File is too large. Please select a file smaller than 15MB.';
        this.song.musicData = null;
        return;
      }

      // Check if it's an audio file
      if (!file.type.startsWith('audio/')) {
        this.uploadStatus = 'Please select a valid audio file.';
        this.song.musicData = null;
        return;
      }

      this.uploadStatus = 'File selected';
      
      // Convert file to data URL
      const reader = new FileReader();
      reader.onload = (e) => {
        this.song.musicData = e.target.result; // This will be a data URL
      };
      reader.onerror = (e) => {
        this.uploadStatus = 'Error reading file.';
        this.song.musicData = null;
      };
      reader.readAsDataURL(file);
    },

    async fetchArtists() {
      try {
        const response = await SongsService.getArtists();
        this.availableArtists = response;
        console.log("Fetched artists:", this.availableArtists);
      } catch (error) {
        console.error("Error fetching artists:", error);
        this.errorMessage = "Failed to load artists";
      }
    },

    async fetchGenres() {
      try {
        const response = await SongsService.getGenres();
        this.availableGenres = response;
        console.log("Fetched genres:", this.availableGenres);
      } catch (error) {
        console.error("Error fetching genres:", error);
        this.errorMessage = "Failed to load genres";
      }
    },

    submitForm() {
      console.log('Submitting song');
      
      // Clear previous messages
      this.successMessage = '';
      this.errorMessage = '';

      // Trigger validation
      this.v$.$touch();

      // Check for validation errors
      if (this.v$.$invalid) {
        this.errorMessage = 'Please correct the validation errors.';
        return;
      }

      // Convert length to number if it's not already
      if (typeof this.song.length === 'string') {
        this.song.length = parseInt(this.song.length, 10);
      }

      this.isSubmitting = true;

      // Call Backend Service
      SongsService.createSong(this.song)
        .then(() => {
          this.successMessage = 'Song created successfully!';
          setTimeout(() => {
            this.$router.push({ name: 'Songs' });
          }, 2000);
        })
        .catch((error) => {
          console.error('Error response:', error);
          
          if (error.response && error.response.data) {
            // Display specific error messages from the backend
            if (typeof error.response.data === 'object') {
              const errorMessages = Object.values(error.response.data).join(', ');
              this.errorMessage = errorMessages;
            } else {
              this.errorMessage = error.response.data.toString();
            }
          } else {
            this.errorMessage = 'An unexpected error occurred. Please try again.';
          }
        })
        .finally(() => {
          this.isSubmitting = false;
        });
    },

    goBack() {
      this.$router.push({ name: 'Songs' });
    }
  },

  mounted() {
    this.fetchGenres();
    this.fetchArtists();
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