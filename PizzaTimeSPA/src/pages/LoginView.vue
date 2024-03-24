<script setup lang="ts">
import { ref } from 'vue';
import UserSignForm from '../components/UserSignForm.vue';
import PizzeriaSignForm from '../components/PizzeriaSignForm.vue';
const selected = ref('1');

</script>


<template>
     <el-container>
        <el-header>
            <span>Login as: </span> <br/>
            <el-radio-group v-model="selected">
                <el-radio label="1">User</el-radio>
                <el-radio label="2">Pizzeria</el-radio>
            </el-radio-group>

      </el-header>

      <el-main>
        <UserSignForm  v-if="selected === '1'" />
        <PizzeriaSignForm v-if="selected === '2'" />
      </el-main>

      <el-footer>
        footer
      </el-footer>
    </el-container>
</template>

<style scoped>

</style>
