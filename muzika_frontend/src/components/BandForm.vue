<script setup>
import { reactive, computed, ref, watch, defineProps, defineEmits } from 'vue';
import { userStore } from '@/js/store';

const { isNew, updateBand } = defineProps({
    isNew: {
        type: Boolean,
        default: () => (true)
    },
    updateBand: {
        type: Object,
        required: false,
        default: () => {
            const today = new Date().toISOString().split('T')[0]; // Get today's date in YYYY-MM-DD format
            return {
                name: 'Generic Band',
                coordX: 0,
                coordY: 0,
                genre: 'ROCK',
                numberOfParticipants: 1,
                singlesCount: 1,
                description: 'A band',
                albumsCount: 1,
                adminOpen: false,
                establishmentDate: today,
                album:undefined,
                studio:undefined
            };
        }
    }
});

const emit = defineEmits(['formClosed', 'albumRequested']);
console.log(updateBand.coordX);
const band = reactive({ ...updateBand });
const errors = reactive({
    name: '',
    coordX: '',
    coordY: '',
    genre: '',
    numberOfParticipants: '',
    singlesCount: '',
    description: '',
    albumsCount: '',
    adminOpen: '',
    establishmentDate: ''
});

const canEdit = computed(() => {
    console.log(`Band ownerid: ${band.ownerId}`)
    console.log(`User id: ${userStore.userId}`)
    return (
        isNew || (band.ownerId === userStore.userId || (band.adminOpen && userStore.isAdmin))
    );
});

const validate = () => {
    errors.name = band.name ? '' : 'Band name is required';
    errors.coordX = band.coordX !== null ? '' : 'Coordinate X is required';
    errors.coordY = band.coordY !== null ? '' : 'Coordinate Y is required';
    errors.genre = band.genre ? '' : 'Genre is required';
    errors.numberOfParticipants = band.numberOfParticipants > 0 ? '' : 'Number of participants must be greater than 0';
    errors.singlesCount = band.singlesCount >= 0 ? '' : 'Singles count must be non-negative';
    errors.albumsCount = band.albumsCount >= 0 ? '' : 'Albums count must be non-negative';
    errors.adminOpen = band.adminOpen !== null ? '' : 'Admin open status is required';
    errors.establishmentDate = band.establishmentDate ? '' : 'Establishment date is required';
};

const hasErrors = computed(() => {
    return Object.values(errors).some(error => error);
});

watch(band, validate, { deep: true });

const handleSubmit = async () => {
    validate();
    if (!hasErrors.value) {
        if (isNew) {
            const d2 = new Date();
            band.establishmentDate = d2.toJSON();
            const response = await fetch("/api/bands", {
                method: "POST",
                credentials: "same-origin",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(band)
            });

        } else {
            const d2 = new Date();
            band.establishmentDate = d2.toJSON();
            const response = await fetch("/api/bands", {
                method: "PUT",
                credentials: "same-origin",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(band)
            });
        }
        emit('formClosed');
    }
};

const onAlbumRequested = ()=>{
    emit('albumRequested', {...band})
}

const onStudioRequested = ()=>{
    emit('studioRequested', {...band})
}
</script>

<style scoped>
span {
    color: red;
    font-size: 0.8em;
}
</style>

<template>
    <div>
        <h1>{{ isNew ? 'Create New Band' : 'Update Band' }}</h1>
        <form @submit.prevent="handleSubmit">
            <div>
                <label for="name">Band Name:</label>
                <input type="text" v-model="band.name" :disabled="!canEdit" />
                <span v-if="errors.name">{{ errors.name }}</span>
            </div>
            <div>
                <label for="coordX">Coordinate X:</label>
                <input type="number" v-model="band.coordX" :disabled="!canEdit" />
                <span v-if="errors.coordX">{{ errors.coordX }}</span>
            </div>
            <div>
                <label for="coordY">Coordinate Y:</label>
                <input type="number" v-model="band.coordY" :disabled="!canEdit" />
                <span v-if="errors.coordY">{{ errors.coordY }}</span>
            </div>
            <div>
                <label for="genre">Genre:</label>
                <select v-model="band.genre" :disabled="!canEdit">
                    <option value="ROCK">ROCK</option>
                    <option value="BLUES">BLUES</option>
                    <option value="MATH_ROCK">MATH_ROCK</option>
                    <option value="PUNK_ROCK">PUNK_ROCK</option>
                </select>
                <span v-if="errors.genre">{{ errors.genre }}</span>
            </div>
            <div>
                <label for="numberOfParticipants">Number of Participants:</label>
                <input type="number" v-model="band.numberOfParticipants" :disabled="!canEdit" />
                <span v-if="errors.numberOfParticipants">{{ errors.numberOfParticipants }}</span>
            </div>
            <div>
                <label for="singlesCount">Singles Count:</label>
                <input type="number" v-model="band.singlesCount" :disabled="!canEdit" />
                <span v-if="errors.singlesCount">{{ errors.singlesCount }}</span>
            </div>
            <div>
                <label for="description">Description:</label>
                <input type="text" v-model="band.description" :disabled="!canEdit" />
                <span v-if="errors.description">{{ errors.description }}</span>
            </div>
            <div>
                <label for="albumsCount">Albums Count:</label>
                <input type="number" v-model="band.albumsCount" :disabled="!canEdit" />
                <span v-if="errors.albumsCount">{{ errors.albumsCount }}</span>
            </div>
            <div>
                <label for="adminOpen">Admin Open:</label>
                <input type="checkbox" v-model="band.adminOpen" :disabled="!canEdit" />
                <span v-if="errors.adminOpen">{{ errors.adminOpen }}</span>
            </div>
            <div>
                <label for="studioId">Studio ID:</label>
                <input type="number" v-model="band.studioId" :disabled="!canEdit" />
                <span v-if="errors.studioId">{{ errors.studioId }}</span>
            </div>
            <div>
                <label for="establishmentDate">Establishment Date:</label>
                <input type="date" v-model="band.establishmentDate" :disabled="!canEdit" />
                <span v-if="errors.establishmentDate">{{ errors.establishmentDate }}</span>
            </div>
            <div>
                <h2>Album</h2>
                <button type="button" @click.prevent="onAlbumRequested">Request Album</button>
                <div v-if="band.album">
                    <div>
                        <label for="albumId">Id:</label>
                        <input type="test" v-model="band.album.id" :disabled="true" />
                    </div>
                    <div>
                        <label for="albumName">Album Name:</label>
                        <input type="text" v-model="band.album.name" :disabled="true" />
                    </div>
                </div>
            </div>
            <div>
                <h2>Studio</h2>
                <button type="button" @click.prevent="onStudioRequested">Request Studio</button>
                <div v-if="band.studio">
                    <div>
                        <label for="studioId">Id:</label>
                        <input type="test" v-model="band.studio.id" :disabled="true" />
                    </div>
                    <div>
                        <label for="studioName">Studio Name:</label>
                        <input type="text" v-model="band.studio.name" :disabled="true" />
                    </div>
                </div>
            </div>
            <button type="button" @click.prevent="emit('formClosed')">Back</button>
            <button type="submit" :disabled="!canEdit || hasErrors">{{ isNew ? 'Create' : 'Update' }}</button>
        </form>
    </div>
</template>