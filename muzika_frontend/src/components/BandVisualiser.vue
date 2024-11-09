<script setup>
import { ref, reactive, onMounted, onUnmounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import ColorHash from 'color-hash';

const FETCH_INTERVAL_MS = 5000;

var interval;
const emit = defineEmits(['circleClicked'])

const stageConfig = ref({
    width: window.innerWidth,
    height: window.innerHeight
});


const colorHash = new ColorHash();
const getColorByOwner = (ownerName) => {
    return colorHash.hex(ownerName);
};

const scaleX = computed(() => stageConfig.value.width / (560 + 560));
const scaleY = computed(() => stageConfig.value.height / (874 + 874));
const handleCircleClick = (band) => {
    emit('circleClicked', band)
}

const getCircleConfig = (band) => {
    return {
        x: (band.coordX * scaleX.value) + stageConfig.value.width / 2,
        y: (band.coordY * scaleY.value) + stageConfig.value.height / 2,
        onClick: () => handleCircleClick(band),
        radius: 10,
        fill: getColorByOwner(band.ownerName),
        stroke: 'black',
        strokeWidth: 0.5,
    };
};


const layerOffsetConfig = computed(() => {
    return {
        offsetX: 0,
        offsetY: 0,
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




const tooltip = reactive({
    visible: false,
    band: null,
    x: 0,
    y: 0
});

const showTooltip = (band, event) => {

    tooltip.visible = true;
    tooltip.band = band;
    tooltip.x = event.clientX;
    tooltip.y = event.clientY;
    console.log(tooltip)
};

const hideTooltip = () => {
    tooltip.visible = false;
};

const tooltipStyle = computed(() => ({
    position: 'absolute',
    top: `${tooltip.y}px`,
    left: `${tooltip.x}px`,
    backgroundColor: 'white',
    border: '1px solid black',
    padding: '5px',
    pointerEvents: 'none'
}));

</script>


<template>
    <div class="stage">
        <div v-if="tooltip.visible" :style="tooltipStyle">
            <p>ID: {{ tooltip.band.id }}</p>
            <p>Owner: {{ tooltip.band.ownerName }}</p>
            <p>Coordinates: ({{ tooltip.band.coordX }}, {{ tooltip.band.coordY }})</p>
        </div>
        <v-stage :config="stageConfig">
            <v-layer :config="layerOffsetConfig">
                <v-circle v-for="band in bands" :key="band.id" :config="getCircleConfig(band)"
                    @mouseover="showTooltip(band, $event)" @mouseout="hideTooltip" />
            </v-layer>
        </v-stage>
    </div>
</template>

<style scoped>
.tooltip {
    z-index: 0;
}
.stage {
    height: 600;
    width: 600;
}
</style>