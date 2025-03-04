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
        <input
          type="text"
          id="artist"
          v-model="song.artist"
          class="form-control"
          placeholder="Enter artist name"
        />
        <span v-if="v$?.song?.artist?.$dirty && v$?.song?.artist?.required?.$invalid" class="text-danger">
          Artist is required.
        </span>
        <span v-if="v$?.song?.artist?.$dirty && v$?.song?.artist?.maxLength?.$invalid" class="text-danger">
          Artist cannot exceed 100 characters.
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
import useVuelidate from '@vuelidate/core';
import { required, maxLength, numeric } from '@vuelidate/validators';

export default {
  name: 'EditSong',
  data() {
    return {
      song: {
        title: '',
        artist: '',
        genres: [],
        length: ''
      },
      availableGenres: [], // To store the list of available genres
      successMessage: '',
      errorMessage: '', // To store validation error messages
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
        artist: { required, maxLength: maxLength(100) },
        genres: { required },
        length: { required, numeric }
      }
    };
  },

  methods: {
    async fetchGenres() {
      try {
        const response = await SongService.getGenres();
        this.availableGenres = response;
        console.log("Fetched genres:", this.availableGenres);
      } catch (error) {
        console.error("Error fetching genres:", error);
      }
    },
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
      console.log('Submitting song:', this.song);
      
      this.v$.$touch();

      if (this.v$.$invalid) {
        this.errorMessage = 'Please correct the validation errors.';
        return;
      }

      const songId = this.$route.params.id;
      SongService.updateSong(songId, this.song)
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
        });
    },
    goBack() {
      this.$router.push({ name: 'Songs' });
    }
  },

  mounted() {
    this.fetchGenres();
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
