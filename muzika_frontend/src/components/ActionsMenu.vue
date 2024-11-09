<script setup>
import { ref } from 'vue';
import { fromLocalDate } from '@/js/utils'

const { chosenBand } = defineProps(['chosenBand']);
const emit = defineEmits(['bandRequest']);
const sumOfAllAlbums = ref(null);
const albumsCount = ref(null);
const band = ref(chosenBand);
const establishmentDate = ref('');
const serverResponse = ref('');

const getSumOfAllAlbums = async () => {
    try {
        const response = await fetch('/api/actions/getSumOfAllAlbums', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        });
        const data = await response.json();
        sumOfAllAlbums.value = data;
        serverResponse.value = 'Sum of all albums fetched successfully';
    } catch (error) {
        console.error('Error fetching sum of all albums:', error);
        serverResponse.value = `Error fetching sum of all albums: ${error.message}`;
    }
};

const countAllWithAlbumsCount = async () => {
    if (!albumsCount.value) {
        serverResponse.value = 'Please enter a valid albums count';
        return;
    }
    try {
        const response = await fetch('/api/actions/countAllWithAlbumsCount', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(albumsCount.value),
        });
        const data = await response.json();
        serverResponse.value = `Count of bands with specified albums count: ${data}`;
    } catch (error) {
        serverResponse.value = `Error counting bands with albums count: ${error.message}`;
    }
};

const addSingle = async () => {
    if (!band.value || !band.value.id) {
        serverResponse.value = 'No band selected';
        return;
    }
    try {
        const response = await fetch('/api/actions/addSingle', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(band.value.id),
        });
        if (response.ok) {
            band.value = await response.json();
            serverResponse.value = 'Single added successfully';
        } else {
            const errorText = await response.text();
            serverResponse.value = `Failed to add single: ${errorText}`;
        }
    } catch (error) {
        serverResponse.value = `Error adding single: ${error.message}`;
    }
};

const removeMember = async () => {
    if (!band.value || !band.value.id) {
        serverResponse.value = 'No band selected';
        return;
    }
    try {
        const response = await fetch('/api/actions/removeMember', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(band.value.id),
        });
        if (response.ok) {
            band.value = await response.json();
            serverResponse.value = 'Member removed successfully';
        } else {
            const errorText = await response.text();
            serverResponse.value = `Failed to remove member: ${errorText}`;
        }
    } catch (error) {
        console.error('Error removing member:', error);
        serverResponse.value = `Error removing member: ${error.message}`;
    }
};

const removeByEstablishmentDate = async () => {
    if (!establishmentDate.value) {
        serverResponse.value = 'Please enter a valid establishment date';
        return;
    }
    try {
        const response = await fetch('/api/actions/removeByEstablishmentDate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(fromLocalDate(establishmentDate.value)),
        });
        if (response.ok) {
            if (await response.text()) {
                serverResponse.value = 'Band removed by establishment date';
            } else {
                serverResponse.value = 'No band suited criteria';
            }
        } else {
            const errorText = await response.text();
            serverResponse.value = `Error removing band by establishment date: ${errorText}`;
        }
    } catch (error) {
        serverResponse.value = `Error removing band by establishment date: ${error.message}`;
    }
};
</script>

<template>
    <div class="actions-view">
        <div class="action">
            <button @click="getSumOfAllAlbums">Get Sum of All Albums</button>
            <p v-if="sumOfAllAlbums !== null">Sum of All Albums: {{ sumOfAllAlbums }}</p>
        </div>
        <div class="action">
            <input type="number" v-model="albumsCount" placeholder="Enter Albums Count" />
            <button @click="countAllWithAlbumsCount">Count All With Albums Count</button>
        </div>
        <div class="action">
            <p v-if="band">Chosen Band Id: {{ band.id }}</p>
            <p v-if="band">Singles Count: {{ band.singlesCount }}</p>
            <p v-if="band">Members Count: {{ band.numberOfParticipants }}</p>
            <button @click="emit('bandRequested', band)">Select Band</button>
            <button @click="addSingle">Add Single</button>
            <button @click="removeMember">Remove Member</button>
        </div>
        <div class="action">
            <input type="datetime-local" v-model="establishmentDate" placeholder="Enter Establishment Date" />
            <button @click="removeByEstablishmentDate">Remove by Establishment Date</button>
        </div>
        <p v-if="serverResponse">{{ serverResponse }}</p>
    </div>
</template>

<style scoped>
.actions-view {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.action {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

button {
    margin-top: 10px;
}
</style>