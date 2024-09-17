import { createRouter, createWebHistory } from 'vue-router';
import Songs from '../components/Song.vue';
import CreateSong from '../components/CreateSong.vue';
import EditSong from '../components/EditSong.vue';

const routes = [
    { path: '/', name: 'Songs', component: Songs },
    { path: '/create-song', name: 'CreateSong', component: CreateSong },
    { path: '/edit-song/:id', name: 'EditSong', component: EditSong }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;
