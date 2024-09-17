<template>
    <div class="container">
        <h1>Create New Song</h1>
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
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
        <div v-if="successMessage" class="alert alert-success mt-3">{{ successMessage }}</div>
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
        }
    },
    methods: {
        submitForm() {
            console.log("Submitting song: ", this.song); // Log the song data to verify the form input
        
            SongsService.createSong(this.song).then(() => {
                this.successMessage = 'Song created successfully!';
                setTimeout(() => {
                    this.$router.push({ name: 'Songs' });
                }, 2000); // Redirect after 2 seconds
            }).catch((error) => {
                console.error("Error creating song:", error);
            });
        }
    }
}
</script>
