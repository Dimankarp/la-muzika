<script setup>
import { KeepAlive, ref, shallowRef } from "vue";
import BandTable from "@/components/BandTable.vue";
import BandForm from "@/components/BandForm.vue";
import AlbumView from "@/views/AlbumView.vue";
import StudioView from "@/views/StudioView.vue";

const { isSelectMode = false, isBackAllowed=false,  startFromUpdateBand = null } = defineProps(['isSelectMode', 'isBackAllowed', 'startFromUpdateBand' ])

const component = shallowRef(BandTable)
const cachedBand = ref({})
const cachedIsNew = ref(false)
const props = ref({isSelectMode:isSelectMode, isBackAllowed: isBackAllowed})



const onNewBandClicked = () => {
  props.value.isNew = true;
  component.value = BandForm
}

const onBandSelected = (band) => {
  props.value = { isNew: false, updateBand: band }
  component.value = BandForm;
}


if(startFromUpdateBand){
  onBandSelected(startFromUpdateBand)
}

const onFormClosed = () => {
  props.value = {isSelectMode: isSelectMode, isBackAllowed:isBackAllowed};
  cachedBand.value = null;
  component.value = BandTable;
}

const onAlbumRequested = (band) => {
  cachedBand.value = band
  cachedIsNew.value = props.value.isNew
  props.value = { isSelectMode: true };
  component.value = AlbumView;
}

const onAlbumSelected = (album) => {

  cachedBand.value.bestAlbum = album;
  props.value = { isNew: cachedIsNew.value, updateBand: cachedBand.value }
  component.value = BandForm;
}

const onStudioRequested = (band) => {
  cachedBand.value = band
  cachedIsNew.value = props.value.isNew
  props.value = { isSelectMode: true };
  component.value = StudioView;
}

const onStudioSelected = (studio) => {
  cachedBand.value.studio = studio;
  props.value = { isNew: cachedIsNew.value, updateBand: cachedBand.value }
  component.value = BandForm;
}

const onBackPressed = ()=>{
  props.value = { isNew: cachedIsNew.value, updateBand: cachedBand.value }
  component.value = BandForm;
}

</script>

<template>
  <component v-bind="{ ...props }" :is="component" @newBandClicked="onNewBandClicked" @bandUpdateSelected="onBandSelected"
    @formClosed="onFormClosed" @albumSelected="onAlbumSelected" @albumRequested="onAlbumRequested" @studioSelected="onStudioSelected" @studioRequested="onStudioRequested" @backPressed="onBackPressed"/>
</template>
