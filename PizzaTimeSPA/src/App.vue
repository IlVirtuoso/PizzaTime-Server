<script setup lang="ts">
import UserSignForm from "./components/UserSignForm.vue"
import PizzeriaSignForm from "./components/PizzeriaSignForm.vue"
import TopMenu  from "./components/TopMenu.vue"

</script>

<template>
  <TopMenu />
  <div>
    <a href="https://vitejs.dev" target="_blank">
      <img src="/vite.svg" class="logo" alt="Vite logo" />
    </a>
    <a href="https://vuejs.org/" target="_blank">
      <img src="./assets/vue.svg" class="logo vue" alt="Vue logo" />
    </a>
  </div>
  <UserSignForm />
  <PizzeriaSignForm />
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
  transition: filter 300ms;
}
.logo:hover {
  filter: drop-shadow(0 0 2em #8183a6aa);
}
.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>./components/Login.vue./components/UserSignForm.vue
