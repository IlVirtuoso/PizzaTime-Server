<script setup lang="ts">
import { ref } from 'vue';

const signForm = ref();

const piva = ref('');
const password = ref('');
const confirm_password = ref('');

function signup() {
    console.log("piva is ", piva , " and password is ", password, " and confirm password is ", confirm_password);
}

</script>


<template>
<el-form ref="signForm" :model="signForm" label-width="auto" style="max-width: 600px">
    <el-form-item label="PIVA" prop="piva">
        <el-input v-model="piva"></el-input>
    </el-form-item>
    <el-form-item label="Password" prop="password">
        <el-input type="password" v-model="password"></el-input>
    </el-form-item>
    <el-form-item label="Confirm" prop="confirm_password" >
        <el-input type="password" v-model="confirm_password"></el-input>
    </el-form-item>
    <el-button @click="signup">Sign Up</el-button>
</el-form>
</template>

<style scoped>
.read-the-docs {
    color: #888;
}
</style>
