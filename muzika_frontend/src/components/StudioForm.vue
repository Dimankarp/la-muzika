<script setup>
import { reactive, computed, ref, watch, defineProps, defineEmits } from 'vue';
import { userStore } from '@/js/store';

const { isNew, updateStudio } = defineProps({
    isNew: {
        type: Boolean,
        default: () => (true)
    },
    updateStudio: {
        type: Object,
        required: false,
        default: () => {
            return {
                name: 'Generic Studio',
                address: 'Planet Earth',
            };
        }
    }
});

const emit = defineEmits(['studioFormClosed']);
const studio = reactive({ ...updateStudio });
const errors = reactive({
    name: '',
    address: '',
});

const canEdit = computed(() => {
    return (
        isNew || (studio.ownerId === userStore.userId)
    );
});

const validate = () => {
    errors.name = studio.name ? '' : 'Studio name is required';
};

const hasErrors = computed(() => {
    return Object.values(errors).some(error => error);
});

watch(studio, validate, { deep: true });

const handleSubmit = async () => {
    validate();
    if (!hasErrors.value) {
        const method = isNew ? "POST" : "PUT";
        const response = await fetch("/api/studios", {
            method,
            credentials: "same-origin",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(studio)
        });

        if (response.ok) {
            emit('studioFormClosed');
        } else {
            console.error("Failed to save studio");
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
        <h1>{{ isNew ? 'Create New Studio' : 'Update Studio' }}</h1>
        <form @submit.prevent="handleSubmit">
            <div>
                <label for="name">Studio Name:</label>
                <input type="text" v-model="studio.name" :disabled="!canEdit" />
                <span v-if="errors.name">{{ errors.name }}</span>
            </div>
            <div>
                <label for="address">Address:</label>
                <input type="text" v-model="studio.address" :disabled="!canEdit" />
                <span v-if="errors.address">{{ errors.address }}</span>
            </div>
            <button type="button" @click.prevent="emit('studioFormClosed')">Back</button>
            <button type="submit" :disabled="!canEdit || hasErrors">{{ isNew ? 'Create' : 'Update' }}</button>
        </form>
    </div>
</template>
