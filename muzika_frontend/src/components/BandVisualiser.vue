<script setup>
import { ref } from 'vue';
import { Stage, Layer, Circle } from 'vue-konva';
import ColorHash from 'color-hash';


const props = defineProps({
    bands: {
        type: Array,
        required: true
    }
});

const stageConfig = ref({
    width: window.innerWidth,
    height: window.innerHeight
});


const colorHash = new ColorHash();
const getColorByOwner = (ownerName) => {
    return colorHash.hex(ownerName);
};

const getCircleConfig = (band) => {
    return {
        x: band.x,
        y: band.y,
        onClick: () => handleCircleClick(band),
        radius: 20,
        fill: getColorByOwner(band.ownerName),
        stroke: 'black',
        strokeWidth: 2
    };
};
</script>

<template>
    <div>
        <v-stage :config="stageConfig">
            <v-layer>
                <v-circle
                    v-for="(band, index) in bands"
                    :key="band.id"
                    :config="getCircleConfig(band)"
                />
            </v-layer>
        </v-stage>
    </div>
</template>
