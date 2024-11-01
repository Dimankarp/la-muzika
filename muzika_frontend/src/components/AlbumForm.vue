<script setup>
import { reactive, computed, ref, watch, defineProps, defineEmits } from 'vue';
import { userStore } from '@/js/store';

const { isNew, updateAlbum } = defineProps({
    isNew: {
        type: Boolean,
        default: () => (true)
    },
    updateAlbum: {
        type: Object,
        required: false,
        default: () => {
            const today = new Date().toISOString().split('T')[0]; // Get today's date in YYYY-MM-DD format
            return {
                name: 'Generic Album',
                tracks: 1,
            };
        }
    }
});

const emit = defineEmits(['albumFormClosed']);
const album = reactive({ ...updateAlbum });
const errors = reactive({
    name: '',
    tracks: '',
});

const canEdit = computed(() => {
    return (
        isNew || (album.ownerId === userStore.userId)
    );
});

const validate = () => {
    errors.name = album.name ? '' : 'Album name is required';
    errors.tracks = album.tracks > 0 ? '' : 'Album must contain at least one track';
};

const hasErrors = computed(() => {
    return Object.values(errors).some(error => error);
});

watch(album, validate, { deep: true });

const handleSubmit = async () => {
    validate();
    if (!hasErrors.value) {
        const method = isNew ? "POST" : "PUT";
        const response = await fetch("/api/albums", {
            method,
            credentials: "same-origin",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(album)
        });

        if (response.ok) {
            emit('albumFormClosed');
        } else {
            console.error("Failed to save album");
        }
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
        <h1>{{ isNew ? 'Create New Album' : 'Update Album' }}</h1>
        <form @submit.prevent="handleSubmit">
            <div>
                <label for="name">Album Name:</label>
                <input type="text" v-model="album.name" :disabled="!canEdit" />
                <span v-if="errors.name">{{ errors.name }}</span>
            </div>
            <div>
                <label for="tracks">Tracks:</label>
                <input type="number" v-model="album.tracks" :disabled="!canEdit" />
                <span v-if="errors.tracks">{{ errors.tracks }}</span>
            </div>
            <button type="button" @click.prevent="emit('albumFormClosed')">Back</button>
            <button type="submit" :disabled="!canEdit || hasErrors">{{ isNew ? 'Create' : 'Update' }}</button>
        </form>
    </div>
</template>
