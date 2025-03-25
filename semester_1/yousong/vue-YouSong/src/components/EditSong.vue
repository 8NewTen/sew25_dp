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
        />
        <span v-if="v$?.song?.title?.$dirty && v$?.song?.title?.required?.$invalid" class="text-danger">
          Title is required.
        </span>
        <span v-if="v$?.song?.title?.$dirty && v$?.song?.title?.maxLength?.$invalid" class="text-danger">
          Title cannot exceed 100 characters.
        </span>
      </div>
      <div class="mb-3">
        <label for="artist" class="form-label">Artist</label>
        <select id="artist" v-model="song.artist.id" class="form-control">
          <option value="">Select an artist</option>
          <option v-for="artist in availableArtists" :key="artist.id" :value="artist.id">
            {{ artist.name }}
          </option>
        </select>
        <span v-if="v$?.song?.artist?.$dirty && v$?.song?.artist?.required?.$invalid" class="text-danger">
          Artist is required.
        </span>
      </div>
      <div class="mb-3">
        <label for="genres" class="form-label">Genres</label>
        <span v-if="v$?.song?.genres?.$dirty && v$?.song?.genres?.required?.$invalid" class="text-danger">
          At least one genre is required.
        </span>
        <select id="genres" v-model="song.genres" class="form-control" multiple>
          <option v-for="genre in availableGenres" :key="genre" :value="genre">
            {{ genre }}
          </option>
        </select>
      </div>
      <div class="mb-3">
        <label for="length" class="form-label">Length (seconds)</label>
        <input
          type="number"
          id="length"
          v-model="song.length"
          class="form-control"
          placeholder="Enter song length"
          @blur="v$.song.length.$touch()"
        />
        <span v-if="v$?.song?.length?.$dirty && v$?.song?.length?.required?.$invalid" class="text-danger">
          Length is required.
        </span>
        <span v-if="v$?.song?.length?.$dirty && v$?.song?.length?.numeric?.$invalid" class="text-danger">
          Length must be a number.
        </span>
      </div>
      
      <!-- Music File Upload Field -->
      <div class="mb-3">
        <label for="musicFile" class="form-label">Music File (Optional - Leave empty to keep current file)</label>
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
      
      <div class="d-flex justify-content-between">
        <button type="submit" class="btn btn-primary" :disabled="isSubmitting">
          {{ isSubmitting ? 'Saving...' : 'Save' }}
        </button>
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
import useVuelidate from '@vuelidate/core';
import { required, maxLength, numeric } from '@vuelidate/validators';

export default {
  name: 'EditSong',
  data() {
    return {
      song: {
        title: '',
        artist: {
          id: '',
          name: ''
        },
        genres: [],
        length: '',
        musicData: null // Will only be set if uploading a new file
      },
      availableGenres: [], // To store the list of available genres
      availableArtists: [], // To store the list of available artists
      successMessage: '',
      errorMessage: '', // To store validation error messages
      uploadStatus: '',
      isSubmitting: false,
      v$: null 
    };
  },

  setup() {
    return { v$: useVuelidate() };
  },

  validations() {
    return {
      song: {
        title: { required, maxLength: maxLength(100) },
        artist: { required },
        genres: { required },
        length: { required, numeric }
        // Note: musicData is not required here since it's optional in edit mode
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
      
      // Check file size as mp3 is formatted as ASCII String (limit to ~15MB which is safely under the MEDIUMTEXT limit, that is 16MB)
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
      const reader = new FileReader(); //FileRead API
      reader.onload = (e) => {
        this.song.musicData = e.target.result; // Contains the data URL
      };
      reader.onerror = (e) => {
        this.uploadStatus = 'Error reading file.';
        this.song.musicData = null;
      };
      reader.readAsDataURL(file);
    },
    
    async fetchGenres() {
      try {
        const response = await SongService.getGenres();
        this.availableGenres = response;
        console.log("Fetched genres:", this.availableGenres);
      } catch (error) {
        console.error("Error fetching genres:", error);
      }
    },

    async fetchArtists() {
      try {
        const response = await SongService.getArtists();
        this.availableArtists = response;
        console.log("Fetched artists:", this.availableArtists);
      } catch (error) {
        console.error("Error fetching artists:", error);
        this.errorMessage = "Failed to load artists";
      }
    },

    fetchSong() {
      const songId = this.$route.params.id;
      SongService.getSongById(songId)
        .then((response) => {
          this.song = response;
          // Note: musicData won't be included in the response due to projection
        })
        .catch((error) => {
          console.error('Error fetching song:', error);
        });
    },
    
    submitForm() {
      console.log('Submitting song update');
      
      this.v$.$touch();

      if (this.v$.$invalid) {
        this.errorMessage = 'Please correct the validation errors.';
        return;
      }

      this.isSubmitting = true;

      const songId = this.$route.params.id;
      // Create a payload that might include musicData if a new file was uploaded
      const updatePayload = {
        title: this.song.title,
        artist: { id: this.song.artist.id },
        genres: this.song.genres,
        length: this.song.length
      };
      
      // Only include musicData if a new file was uploaded
      if (this.song.musicData) {
        updatePayload.musicData = this.song.musicData;
      }

      SongService.updateSong(songId, updatePayload)
        .then(() => {
          this.successMessage = 'Song updated successfully!';
          setTimeout(() => {
            this.$router.push({ name: 'Songs' });
          }, 2000);
        })
        .catch((error) => {
          console.error('Error response:', error.response);
          if (error.response && error.response.data) {
            const errorMessages = Object.values(error.response.data);
            this.errorMessage = errorMessages.join(', ');
          } else {
            this.errorMessage = 'An unexpected error occurred.';
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