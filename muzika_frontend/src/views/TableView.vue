<script setup>
import { ref, onMounted, onUnmounted, reactive } from "vue";
import { userStore } from '@/js/store';
import BandTable from "@/components/BandTable.vue";
import { logout } from "@/js/auth";

const { entryType, isDeleteAllowed, shouldEmitEntry } = defineProps({
  entryType: {
    validator(value, props) {
      return ['bands', 'albums', 'studios'].includes(value)
    }
  },
  isDeleteAllowed: {
    type: Boolean,
    default() {
      return true;
    }
  },
  shouldEmitEntry: {
    type: Boolean,
    default() {
      return false;
    }
  }
})

const FETCH_INTERVAL_MS = 5000
var interval;

const entries = ref([]);
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
  //interval = setInterval(async () => { await fetchData() }, FETCH_INTERVAL_MS)
})

onUnmounted(() => {
  controller.abort()
 // clearInterval(interval)
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
    const response = await fetch(`/api/${entryType}?${urlEncoded.toString()}`, { signal: controller.signal });

    if (response.ok) {
      const data = await response.json();
      entries.value = data.content;
      totalPages.value = data.page.totalPages;
    } else if (response.status == 401) {
      logout()
    } else {
      errorMsg.value = `Failed to fetch ${entryType}`;
    }
  } catch (error) {
    console.error('Error fetching entries:', error);
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
  <span v-text="errorMsg" />
  <div>
    <BandTable :bands="entries" :sortInfoRef="sortInfoRef" />
    <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
    <span v-text="currentPage" />
    <button @click="nextPage" :disabled="currentPage === totalPages">Next</button>
  </div>
</template>
