<script setup>
import { ref, reactive, onMounted, onUnmounted } from "vue";
import { userStore } from '@/js/store';
import TableHead from "@/components/TableHead.vue"
import { useRouter } from "vue-router";

const FETCH_INTERVAL_MS = 5000

var interval;

const { isSelectMode = false } = defineProps(['isSelectMode'])

const emit = defineEmits(['newAlbumClicked', 'albumSelected', 'updateAlbumSelected'])
const albums = ref([]);
const router = useRouter()
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = ref(10);
const errorMsg = ref("");
const fetchUserOwned = ref(false);

const sortInfoRef = reactive({
  id: '',
  dir: null,
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
    const response = await fetch(`/api/albums?${urlEncoded.toString()}`, { signal: controller.signal });

    if (response.ok) {
      const data = await response.json();
      albums.value = data.content;
      console.log(albums.value)
      totalPages.value = data.page.totalPages;
    } else if (response.status == 401) {
      userStore.logout()
      router.replace('login')
    } else {
      errorMsg.value = `Failed to fetch albums`;
    }
  } catch (error) {
    console.error('Error fetching albums:', error);
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

const onAlbumDelete = async (entry) => {
  const response = await fetch(`/api/albums/${entry.id}`, {
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
          <TableHead id='id' :sortInfo="sortInfoRef">ID</TableHead>
          <TableHead id='name' :sortInfo="sortInfoRef">Name</TableHead>
          <TableHead id='tracks' :sortInfo="sortInfoRef">Tracks</TableHead>
          <TableHead id='owner.username' :sortInfo="sortInfoRef">Owner Name</TableHead>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="album in albums" :key="album.id" @click.prevent="emit('updateAlbumSelected', album)">
          <td>{{ album.id }}</td>
          <td>{{ album.name }}</td>
          <td>{{ album.tracks }}</td>
          <td>{{ album.ownerName }}</td>
          <td>
            <button v-if="!isSelectMode && album.ownerId === userStore.userId" @click.stop="onAlbumDelete(album)">Delete</button>
            <button v-if="isSelectMode && album.ownerId === userStore.userId" @click.stop="emit('albumSelected', album)"> Select </button>
          </td>
        </tr>
      </tbody>
    </table>
    <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
    <span v-text="currentPage" />
    <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    <button @click="emit('newAlbumClicked')">New</button>
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
