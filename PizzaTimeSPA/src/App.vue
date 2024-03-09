<script setup lang="ts">
import UserSignForm from "./components/UserSignForm.vue"
import PizzeriaSignForm from "./components/PizzeriaSignForm.vue"
import TopMenu  from "./components/TopMenu.vue"

</script>

<template>
    <div class="common-layout">
    <el-container>
      <el-header class="navbar">
        <TopMenu />
      </el-header>

      <el-main>
        <!-- Here goes visualizer -->
  <UserSignForm />
  <PizzeriaSignForm />
      </el-main>

      <el-footer>

      </el-footer>
    </el-container>
  </div>
  

</template>

<style scoped>

</style>
