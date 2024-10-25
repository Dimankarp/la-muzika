<script setup>
import { ref, shallowRef } from "vue";
import AlbumTable from "@/components/AlbumTable.vue";
import AlbumForm from "@/components/AlbumForm.vue";

const { isSelectMode = false } = defineProps(['isSelectMode'])

const component = shallowRef(AlbumTable)
const props = ref({isSelectMode: isSelectMode})
const onNewAlbumClicked = () => {
  props.value.isNew = true;
  component.value = AlbumForm
}

const onUpdateAlbumSelected = (album) => {
  props.value = { isNew: false, updateAlbum: album }
  component.value = AlbumForm;
}

const onFormClosed = () => {
  console.log("AlbumView")
  props.value = {isSelectMode: isSelectMode};
  component.value = AlbumTable;
}





</script>

<template>
  <component v-bind="{ ...props }" :is="component" @newAlbumClicked="onNewAlbumClicked" @updateAlbumSelected="onUpdateAlbumSelected"
    @albumFormClosed="onFormClosed" @albumSelected="onAlbumSelected" />


</template>
