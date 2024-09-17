<template>
    <div class="d-flex flex-column justify-content-center"> 
        <h1 class="text-center">YouSong</h1>
        <button class="btn btn-primary mb-3" @click="navigateToCreateSong">Create New Song</button>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Song Id</th>
                        <th>Song Title</th>
                        <th>Song Artist</th>
                        <th>Song Genre</th>
                        <th>Song Length in seconds</th>
                        <th>Edit Song</th>
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
                            <button class="btn btn-secondary" @click="navigateToEditSong(song.id)">Edit</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import SongsService from '../services/SongService';

export default {
    name: 'Songs',
    data() {
        return {
            songs: []
        };
    },
    methods: {
        getSongs() {
            SongsService.getSongs().then((response) => {
                this.songs = response;
            }).catch((error) => {
                console.error("Error fetching songs:", error);
            });
        },
        navigateToCreateSong() {
            this.$router.push({ name: 'CreateSong' });
        },
        navigateToEditSong(songId){
            this.$router.push({ name: 'EditSong', params: { id: songId } });
        }
    },
    created() {
        this.getSongs();
    }
}
</script>

<style>
body {
    height: 100%;
}
</style>
