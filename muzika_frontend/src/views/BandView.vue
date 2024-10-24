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
        default: () => ({
            name: '',
            coordX: 0,
            coordY: 0,
            genre: '',
            numberOfParticipants: 0,
            singlesCount: 0,
            description: '',
            albumsCount: 0,
            ownerId: 0,
            ownerName: '',
            adminOpen: false,
            bestAlbumId: 0,
            studioId: 0
        })
    }
})

const emit = defineEmits(['viewClosed'])

const band = reactive({ ...updateBand});
const errors = reactive({
    name: '',
    coordX: '',
    coordY: '',
    genre: '',
    numberOfParticipants: '',
    singlesCount: '',
    description: '',
    albumsCount: '',
    ownerId: '',
    ownerName: '',
    adminOpen: '',
    bestAlbumId: '',
    studioId: ''
});

const canEdit = computed(() => {
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
    errors.description = band.description ? '' : 'Description is required';
    errors.albumsCount = band.albumsCount >= 0 ? '' : 'Albums count must be non-negative';
    errors.ownerId = band.ownerId > 0 ? '' : 'Owner ID must be greater than 0';
    errors.ownerName = band.ownerName ? '' : 'Owner name is required';
    errors.adminOpen = band.adminOpen !== null ? '' : 'Admin open status is required';
    errors.bestAlbumId = band.bestAlbumId >= 0 ? '' : 'Best album ID must be non-negative';
    errors.studioId = band.studioId >= 0 ? '' : 'Studio ID must be non-negative';
};

const hasErrors = computed(() => {
    return Object.values(errors).some(error => error);
});

watch(band, validate, { deep: true });

const handleSubmit = () => {
    validate();
    if (!hasErrors.value) {
        if (isNew) {
            // Logic to create a new band
            console.log('Creating new band:', band);
        } else {
            // Logic to update the existing band
            console.log('Updating band:', band);
        }
        emit('viewClosed')
    }
};
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
                <input type="text" v-model="band.genre" :disabled="!canEdit" />
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
                <label for="ownerId">Owner ID:</label>
                <input type="number" v-model="band.ownerId" :disabled="!canEdit" />
                <span v-if="errors.ownerId">{{ errors.ownerId }}</span>
            </div>
            <div>
                <label for="ownerName">Owner Name:</label>
                <input type="text" v-model="band.ownerName" :disabled="!canEdit" />
                <span v-if="errors.ownerName">{{ errors.ownerName }}</span>
            </div>
            <div>
                <label for="adminOpen">Admin Open:</label>
                <input type="checkbox" v-model="band.adminOpen" :disabled="!canEdit" />
                <span v-if="errors.adminOpen">{{ errors.adminOpen }}</span>
            </div>
            <div>
                <label for="bestAlbumId">Best Album ID:</label>
                <input type="number" v-model="band.bestAlbumId" :disabled="!canEdit" />
                <span v-if="errors.bestAlbumId">{{ errors.bestAlbumId }}</span>
            </div>
            <div>
                <label for="studioId">Studio ID:</label>
                <input type="number" v-model="band.studioId" :disabled="!canEdit" />
                <span v-if="errors.studioId">{{ errors.studioId }}</span>
            </div>
            <button type="button" @click.prevent="emit('viewClosed')">Back</button>
            <button type="submit" :disabled="!canEdit || hasErrors">{{ isNew ? 'Create' : 'Update' }}</button>
        </form>
    </div>
</template>