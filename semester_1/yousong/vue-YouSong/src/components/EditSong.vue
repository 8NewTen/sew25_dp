<template>
    <div class="container">
        <h1>Edit Song</h1>
        <form @submit.prevent="submitForm">
            <div class="mb-3">
                <label for="title" class="form-label">Title</label>
                <input type="text" id="title" v-model="song.title" class="form-control" required />
            </div>
            <div class="mb-3">
                <label for="artist" class="form-label">Artist</label>
                <input type="text" id="artist" v-model="song.artist" class="form-control" required />
            </div>
            <div class="mb-3">
                <label for="genre" class="form-label">Genre</label>
                <input type="text" id="genre" v-model="song.genre" class="form-control" required />
            </div>
            <div class="mb-3">
                <label for="length" class="form-label">Length (seconds)</label>
                <input type="number" id="length" v-model="song.length" class="form-control" required />
            </div>
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
        <div v-if="successMessage" class="alert alert-success mt-3">{{ successMessage }}</div>
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
            successMessage: ''
        }
    },
    methods: {
        // Fetch the song details based on ID from the route parameter
        fetchSong() {
            const songId = this.$route.params.id;  // Song ID from the route
            SongService.getSongById(songId).then((response) => {
                this.song = response;
            }).catch((error) => {
                console.error("Error fetching song:", error);
            });
        },
        submitForm() {
            const songId = this.$route.params.id; // Song ID from the route
            SongService.updateSong(songId, this.song).then(() => {
                this.successMessage = 'Song updated successfully!';
                setTimeout(() => {
                    this.$router.push({ name: 'Songs' }); // Redirect to song list after success
                }, 2000); // Redirect after 2 seconds
            }).catch((error) => {
                console.error("Error updating song:", error);
            });
        }
    },
    created() {
        this.fetchSong(); // Fetch song data when component is created
    }
}
</script>

<style>
body {
    height: 100%;
}
</style>
