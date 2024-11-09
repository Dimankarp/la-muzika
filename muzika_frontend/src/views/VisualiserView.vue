<script setup>
import { ref, shallowRef } from "vue";
import BandVisualiser from "@/components/BandVisualiser.vue";
import BandView from "@/views/BandView.vue";

const component = shallowRef(BandVisualiser)
const props = ref({})
const onCircleClick = (band) => {
  props.value.startFromUpdateBand = band;
  component.value = BandView
}

const onBackPressed = ()=>{
  props.value = {}
  component.value = BandVisualiser;
}

</script>

<template>
  <component v-bind="{ ...props }" :is="component" @circleClicked="onCircleClick" @backPressed="onBackPressed" @formClosed="onBackPressed"/>
</template>
