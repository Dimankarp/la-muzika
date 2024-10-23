<script setup>
import TableHead from './TableHead.vue';

const emit = defineEmits(['selectedEntry'])
const {bands, sortInfoRef} = defineProps(["bands", "deleteAllowed", "selectAllowed", "sortInfoRef"])

</script>

<style scoped>
table {
    width: 100%;
    border-collapse: collapse;
}

th,
td {
    border: 1px solid #ddd;
    padding: 8px;
}

th {
    background-color: #f2f2f2;
    text-align: left;
    width: 150px;
}

tbody tr:hover {
    background-color: beige;
}

button {
    margin: 10px;
}
</style>

<template>
    <table>
        <thead>
            <tr>
                <TableHead id='name' :sortInfo="sortInfoRef">Name</TableHead>
                <TableHead id='coordinates.x' :sortInfo="sortInfoRef">Coord X</TableHead>
                <TableHead id='coordinates.y' :sortInfo="sortInfoRef">Coord Y</TableHead>
                <TableHead id='creationDate' :sortInfo="sortInfoRef">Creation Date</TableHead>
                <TableHead id='genre' :sortInfo="sortInfoRef">Genre</TableHead>
                <TableHead id='numberOfParticipants' :sortInfo="sortInfoRef">Number of Participants</TableHead>
                <TableHead id='singlesCount' :sortInfo="sortInfoRef">Singles Count</TableHead>
                <TableHead id='description' :sortInfo="sortInfoRef">Description</TableHead>
                <TableHead id='albumsCount' :sortInfo="sortInfoRef">Albums Count</TableHead>
                <TableHead id='establishmentDate' :sortInfo="sortInfoRef">Establishment Date</TableHead>
                <TableHead id='owner.username' :sortInfo="sortInfoRef">Owner Name</TableHead>
                <TableHead id='bestAlbum.id' :sortInfo="sortInfoRef">Best Album</TableHead>
                <TableHead id='studio.id' :sortInfo="sortInfoRef">Studio</TableHead>
            </tr>
        </thead>
        <tbody>
            <tr v-for="band in bands" :key="band.id">
                <td>{{ band.name }}</td>
                <td>{{ band.coordX }}</td>
                <td>{{ band.coordY }}</td>
                <td>{{ band.creationDate }}</td>
                <td>{{ band.genre }}</td>
                <td>{{ band.numberOfParticipants }}</td>
                <td>{{ band.singlesCount }}</td>
                <td>{{ band.description }}</td>
                <td>{{ band.albumsCount }}</td>
                <td>{{ band.establishmentDate }}</td>
                <td>{{ band.ownerName }}</td>
                <td>{{ band.bestAlbum !== null ? band.bestAlbum.name : "-" }}</td>
                <td>{{ band.studio !== null ? band.studio.name : "-" }}</td>
            </tr>
        </tbody>
    </table>
</template>
