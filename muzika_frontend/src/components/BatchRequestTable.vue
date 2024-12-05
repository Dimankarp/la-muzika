<script setup>
import { ref, reactive, onMounted, onUnmounted } from "vue";
import { userStore } from '@/js/store';
import TableHead from "@/components/TableHead.vue"
import { useRouter } from "vue-router";

const FETCH_INTERVAL_MS = 10000

var interval;


const requests = ref([]);
const router = useRouter()
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = ref(10);
const errorMsg = ref("");

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
    const response = await fetch(`/api/batches?${urlEncoded.toString()}`, { signal: controller.signal });

    if (response.ok) {
      const data = await response.json();
      requests.value = data.content;
      console.log(requests.value)
      totalPages.value = data.page.totalPages;
    } else if (response.status == 401) {
      userStore.logout()
      router.replace('login')
    } else {
      errorMsg.value = `Failed to fetch requests`;
    }
  } catch (error) {
    console.error('Error fetching requests:', error);
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

</script>

<template>

  <div>
    <span v-text="errorMsg" />
    <table>
      <thead>
        <tr>
          <TableHead id='id' :sortInfo="sortInfoRef">ID</TableHead>
          <TableHead id='creationDate' :sortInfo="sortInfoRef">Creation Date</TableHead>
          <TableHead id='owner.username' :sortInfo="sortInfoRef">Sender Name</TableHead>
          <TableHead id='status' :sortInfo="sortInfoRef">Status</TableHead>
          <TableHead id='addedCount' :sortInfo="sortInfoRef">Added Count</TableHead>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="request in requests" :key="request.id">
          <td>{{ request.id }}</td>
          <td>{{ request.creationDate }}</td>
          <td>{{ request.senderName }}</td>
          <td>{{ request.status }}</td>
          <td>{{ request.addedCount }}</td>
        </tr>
      </tbody>
    </table>
    <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
    <span v-text="currentPage" />
    <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
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
