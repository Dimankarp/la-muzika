<script setup>
import { ref, reactive, onActivated, onDeactivated, onMounted, onUnmounted } from "vue";
import { userStore } from '@/js/store';
import { useRouter } from "vue-router";
import TableHead from "@/components/TableHead.vue"
import TableHeadWithFilter from "@/components/TableHeadWithFilter.vue"

const FETCH_INTERVAL_MS = 10000

var interval;

const emit = defineEmits(['newBandClicked', 'bandSelected'])
const router = useRouter()
const bands = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = ref(10);
const errorMsg = ref("");
const fetchUserOwned = ref(false);

const sortInfoRef = reactive({
  id: '',
  dir: null,
  name: '',
  description: '',
  sortChange(id) {
    if (id != this.id) {
      this.id = id;
      this.dir = 'asc'
    } else {
      switch (this.dir) {
        case 'asc':
          this.dir = 'desc'
          break;
        case null:
          this.dir = 'asc'
          break;
        default:
          this.dir = null;
          break;
      }
    }
    fetchData();
  },
  updateFilter(name, val) {
    this[name] = val;
    fetchData();
  }

})

var controller = new AbortController();
onMounted(() => {
  fetchData();
  console.log("Interval")
  interval = setInterval(async () => { await fetchData() }, FETCH_INTERVAL_MS)
})

onUnmounted(() => {
  controller.abort()
  clearInterval(interval)
})

const fetchData = async () => {
  controller.abort()
  controller = new AbortController()
  try {

    const urlEncoded = new URLSearchParams({
      page: currentPage.value - 1,
      size: pageSize.value,
    });
    if (sortInfoRef.id !== null && sortInfoRef.dir !== null) {
      urlEncoded.append("sort", `${sortInfoRef.id},${sortInfoRef.dir}`);
    }
    if (fetchUserOwned.value) {
      urlEncoded.append("owner", userStore.username);
    }
    if (sortInfoRef.name) {
      urlEncoded.append("name", sortInfoRef.name);
    }
    if (sortInfoRef.description) {
      urlEncoded.append("description", sortInfoRef.description);
    }
    const response = await fetch(`/api/bands?${urlEncoded.toString()}`, { signal: controller.signal });

    if (response.ok) {
      const data = await response.json();
      bands.value = data.content;
      totalPages.value = data.page.totalPages;
    } else if (response.status == 401) {
      userStore.logout()
      router.replace('login')
    } else {
      errorMsg.value = `Failed to fetch bands`;
    }
  } catch (error) {
    console.error('Error fetching bands:', error);
  }
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
    fetchData();
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    fetchData();
  }
}

const bandDeleted = async (entry) => {
  const response = await fetch(`/api/bands/${entry.id}`, {
    method: "DELETE"
  });
  if (response.ok) {
    await fetchData();
  } else {
    console.log("Failed to delete because of " + response.body);
  }

}

</script>

<template>

  <div>
    <span v-text="errorMsg" />
    <label>
      <input type="checkbox" v-model="fetchUserOwned" @change="fetchData" />
      User owned
    </label>
    <table>
      <thead>
        <tr>
          <TableHeadWithFilter id='name' :sortInfo="sortInfoRef">Name</TableHeadWithFilter>
          <TableHead id='coordinates.x' :sortInfo="sortInfoRef">Coord X</TableHead>
          <TableHead id='coordinates.y' :sortInfo="sortInfoRef">Coord Y</TableHead>
          <TableHead id='creationDate' :sortInfo="sortInfoRef">Creation Date</TableHead>
          <TableHead id='genre' :sortInfo="sortInfoRef">Genre</TableHead>
          <TableHead id='numberOfParticipants' :sortInfo="sortInfoRef">Number of Participants</TableHead>
          <TableHead id='singlesCount' :sortInfo="sortInfoRef">Singles Count</TableHead>
          <TableHeadWithFilter id='description' :sortInfo="sortInfoRef">Description</TableHeadWithFilter>
          <TableHead id='albumsCount' :sortInfo="sortInfoRef">Albums Count</TableHead>
          <TableHead id='establishmentDate' :sortInfo="sortInfoRef">Establishment Date</TableHead>
          <TableHead id='owner.username' :sortInfo="sortInfoRef">Owner Name</TableHead>
          <TableHead id='bestAlbum.id' :sortInfo="sortInfoRef">Best Album</TableHead>
          <TableHead id='studio.id' :sortInfo="sortInfoRef">Studio</TableHead>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="band in bands" :key="band.id" @click.prevent="emit('bandSelected', band)">
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
          <td>
            <button v-if="band.ownerId === userStore.userId" @click.stop="bandDeleted(band)">Delete</button>
          </td>
        </tr>
      </tbody>
    </table>
    <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
    <span v-text="currentPage" />
    <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    <button @click="emit('newBandClicked')">New</button>
  </div>
</template>


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
