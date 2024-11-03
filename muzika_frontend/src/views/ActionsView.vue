<script setup>
import { ref } from 'vue';
import { fromLocalDate } from '@/js/utils'

const sumOfAllAlbums = ref(null);
const albumsCount = ref(null);
const bandId = ref(null);
const establishmentDate = ref('');

const getSumOfAllAlbums = async () => {
  try {
    const response = await fetch('/api/actions/getSumOfAllAlbums', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const data = await response.json();
    sumOfAllAlbums.value = data;
  } catch (error) {
    console.error('Error fetching sum of all albums:', error);
  }
};

const countAllWithAlbumsCount = async () => {
  try {
    const response = await fetch('/api/actions/countAllWithAlbumsCount', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(albumsCount.value),
    });
    const data = await response.json();
    console.log('Count of bands with specified albums count:', data);
  } catch (error) {
    console.error('Error counting bands with albums count:', error);
  }
};

const addSingle = async () => {
  try {
    await fetch('/api/actions/addSingle', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(bandId.value),
    });
    console.log('Single added successfully');
  } catch (error) {
    console.error('Error adding single:', error);
  }
};

const removeMember = async () => {
  try {
    await fetch('/api/actions/removeMember', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(bandId.value),
    });
    console.log('Member removed successfully');
  } catch (error) {
    console.error('Error removing member:', error);
  }
};

const removeByEstablishmentDate = async () => {

  const response = await fetch('/api/actions/removeByEstablishmentDate', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(fromLocalDate(establishmentDate.value)),
  });
  if (response.ok) {
    if(response.data){
      console.log('Band removed by establishment date:', await response.json());
    } else{
      console.log('No band suited criteria');
    }

  } else {
    console.error('Error removing band by establishment date:', await response.text());
  }

};
</script>

<template>
  <div class="actions-view">
    <div class="action">
      <button @click="getSumOfAllAlbums">Get Sum of All Albums</button>
      <p v-if="sumOfAllAlbums !== null">Sum of All Albums: {{ sumOfAllAlbums }}</p>
    </div>
    <div class="action">
      <input type="number" v-model="albumsCount" placeholder="Enter Albums Count" />
      <button @click="countAllWithAlbumsCount">Count All With Albums Count</button>
    </div>
    <div class="action">
      <input type="number" v-model="bandId" placeholder="Enter Band ID" />
      <button @click="addSingle">Add Single</button>
      <button @click="removeMember">Remove Member</button>
    </div>
    <div class="action">
      <input type="datetime-local" v-model="establishmentDate" placeholder="Enter Establishment Date" />
      <button @click="removeByEstablishmentDate">Remove by Establishment Date</button>
    </div>
  </div>
</template>

<style scoped>
.actions-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.action {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

button {
  margin-top: 10px;
}
</style>