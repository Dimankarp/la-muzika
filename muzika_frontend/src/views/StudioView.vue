<script setup>
import { ref, shallowRef } from "vue";
import StudioTable from "@/components/StudioTable.vue";
import StudioForm from "@/components/StudioForm.vue";

const { isSelectMode = false } = defineProps(['isSelectMode'])

const component = shallowRef(StudioTable)
const props = ref({isSelectMode: isSelectMode})
const onNewStudioClicked = () => {
  props.value.isNew = true;
  component.value = StudioForm
}

const onUpdateStudioSelected = (studio) => {
  props.value = { isNew: false, updateStudio: studio }
  component.value = StudioForm;
}

const onFormClosed = () => {
  props.value = {isSelectMode: isSelectMode};
  component.value = StudioTable;
}





</script>

<template>
  <component v-bind="{ ...props }" :is="component" @newStudioClicked="onNewStudioClicked" @updateStudioSelected="onUpdateStudioSelected"
    @studioFormClosed="onFormClosed" @studioSelected="onStudioSelected" />


</template>
