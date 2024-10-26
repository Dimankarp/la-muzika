<script setup>
import { ref, reactive, onMounted, onUnmounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import ColorHash from 'color-hash';

const FETCH_INTERVAL_MS = 5000;

var interval;

const stageConfig = ref({
    width: window.innerWidth,
    height: window.innerHeight
});

const colorHash = new ColorHash();
const getColorByOwner = (ownerName) => {
    return colorHash.hex(ownerName);
};

const getCircleConfig = (band) => {
    console.log(band.id, band.coordX, band.coordY)
    return {
        x: band.coordX * scaleX.value,
        y: band.coordY * scaleY.value,
        onClick: () => handleCircleClick(band),
        radius: 10,
        fill: getColorByOwner(band.ownerName),
        stroke: 'black',
        strokeWidth: 0.5,
    };
};

const scaleX = computed(() => stageConfig.value.width / (1080))
const scaleY = computed(() => stageConfig.value.height / (1688))

const layerOffsetConfig = computed(() => {
    return {
        offsetX: -540 * scaleX,
        offsetY: -844 * scaleY,
    };
});

const router = useRouter();
const bands = ref([]);
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
            this.dir = 'asc';
        } else {
            switch (this.dir) {
                case 'asc':
                    this.dir = 'desc';
                    break;
                case null:
                    this.dir = 'asc';
                    break;
                default:
                    this.dir = null;
                    break;
            }
        }
        fetchData();
    }
});

var controller = new AbortController();
onMounted(() => {
    fetchData();
    interval = setInterval(async () => { await fetchData() }, FETCH_INTERVAL_MS);
});

onUnmounted(() => {
    controller.abort();
    clearInterval(interval);
});

const fetchData = async () => {
    controller.abort();
    controller = new AbortController();
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
        const response = await fetch(`/api/bands`, { signal: controller.signal });

        if (response.ok) {
            const data = await response.json();
            bands.value = data.content;
            totalPages.value = data.page.totalPages;
        } else if (response.status == 401) {
            userStore.logout();
            router.replace('login');
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
};

const handleCircleClick = (band) => {
    console.log(band.id)
}
</script>

<template>
    <div>
        <v-stage :config="stageConfig">
            <v-layer :config="layerOffsetConfig">
                <v-circle v-for="band in bands" :key="band.id" :config="getCircleConfig(band)" />
            </v-layer>
        </v-stage>
    </div>
</template>
