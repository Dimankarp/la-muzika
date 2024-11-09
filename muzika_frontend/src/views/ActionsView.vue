<script setup>
import { KeepAlive, ref, shallowRef } from "vue";
import ActionsMenu from "@/components/ActionsMenu.vue";
import BandView from "@/views/BandView.vue";
import AlbumView from "@/views/AlbumView.vue";
import StudioView from "@/views/StudioView.vue";
import BandTable from "../components/BandTable.vue";

const component = shallowRef(ActionsMenu)
const chosenBand= ref({})
const props = ref({})

const onBandRequested = (band) => {
  chosenBand.value = band
  props.value = { isSelectMode: true };
  component.value = BandView;
}
const onBandSelected = (band) => {
  chosenBand.value = band
  props.value = { chosenBand: chosenBand.value }
  component.value = ActionsMenu;
}

const onBackPressed = ()=>{
  props.value = { chosenBand: chosenBand.value }
  component.value = ActionsMenu;
}

</script>

<template>
  <component v-bind="{ ...props }" :is="component" 
    @bandRequested="onBandRequested" @bandSelected="onBandSelected" @backPressed="onBackPressed"/>
</template>
