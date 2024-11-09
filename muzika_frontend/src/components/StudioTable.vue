<script setup>
import { ref, reactive, onMounted, onUnmounted } from "vue";
import { userStore } from '@/js/store';
import TableHead from "@/components/TableHead.vue"
import { useRouter } from "vue-router";
import TableHeadWithFilter from "@/components/TableHeadWithFilter.vue";

const FETCH_INTERVAL_MS = 10000

var interval;

const { isSelectMode = false } = defineProps(['isSelectMode'])

const emit = defineEmits(['newStudioClicked', 'studioSelected', 'updateStudioSelected', 'backPressed'])
const studios = ref([]);
const router = useRouter()
const currentPage = ref(1);
const totalPages = ref(1);
const pageSize = ref(10);
const errorMsg = ref("");
const fetchUserOwned = ref(false);

const sortInfoRef = reactive({
  id: '',
  dir: null,
  name: '',
  address: '',
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
    if (sortInfoRef.address) {
      urlEncoded.append("address", sortInfoRef.address);
    }
    const response = await fetch(`/api/studios?${urlEncoded.toString()}`, { signal: controller.signal });

    if (response.ok) {
      const data = await response.json();
      studios.value = data.content;
      console.log(studios.value)
      totalPages.value = data.page.totalPages;
    } else if (response.status == 401) {
      userStore.logout()
      router.replace('login')
    } else {
      errorMsg.value = `Failed to fetch studios`;
    }
  } catch (error) {
    console.error('Error fetching studios:', error);
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

const onStudioDelete = async (entry) => {
  const response = await fetch(`/api/studios/${entry.id}`, {
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
          <TableHeadWithFilter id='name' :sortInfo="sortInfoRef">Name</TableHeadWithFilter>
          <TableHeadWithFilter id='address' :sortInfo="sortInfoRef">Address</TableHeadWithFilter>
          <TableHead id='owner.username' :sortInfo="sortInfoRef">Owner Name</TableHead>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="studio in studios" :key="studio.id" @click.prevent="emit('updateStudioSelected', studio)">
          <td>{{ studio.id }}</td>
          <td>{{ studio.name }}</td>
          <td>{{ studio.address }}</td>
          <td>{{ studio.ownerName }}</td>
          <td>
            <button v-if="!isSelectMode && studio.ownerId === userStore.userId" @click.stop="onStudioDelete(studio)">Delete</button>
            <button v-if="isSelectMode && studio.ownerId === userStore.userId" @click.stop="emit('studioSelected', studio)"> Select </button>
          </td>
        </tr>
      </tbody>
    </table>
    <button v-if="isSelectMode" @click="emit('backPressed')">Back</button>
    <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
    <span v-text="currentPage" />
    <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
    <button @click="emit('newStudioClicked')">New</button>
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
