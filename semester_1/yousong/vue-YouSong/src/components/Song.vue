<template>
    <div class="container">
        <h1 class="text-center"> YouSong </h1>
        <table class="table table-stripped">
            <thead>
                <tr>
                    <th> Song id</th>
                    <th> Song title</th>
                    <th> Song artist</th>
                    <th> Song genre</th>
                    <th> Song length in seconds</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="song in songs" v-bind:key="song.id">
                    <td>{{ song.id }}</td>
                    <td>{{ song.title }}</td>
                    <td>{{ song.artist }}</td>
                    <td>{{ song.genre }}</td>
                    <td>{{ song.length }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import SongsService from '../services/SongService';
    export default{
        name: 'Songs',
        data(){
            return{
                songs: []
            }
        },
        methods:{
            getSongs(){
                SongsService.getSongs().then((response) =>{
                    this.songs = response;
                }).catch((error) => {
                    console.error("Error fetching songs:", error);
                });
            }
        },
        created(){
            this.getSongs();
        }
    }
</script>
