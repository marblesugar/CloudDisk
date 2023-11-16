<template>
  <div style="width: 500px;margin: auto">
    <div>
      <h1>登录<i class="el-icon-s-home"></i></h1>
    </div>
    <el-form :model="LoginForm" ref="LoginForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="邮箱">
        <el-input v-model="LoginForm.email"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="LoginForm.password" @keyup.enter.native="submitForm()" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm()">立即登录</el-button>
        <el-button @click="resetForm('LoginForm')">重置</el-button>
        <el-link type="primary" :underline="false" @click="toRegister">前往注册</el-link>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import router from "@/router";

export default {
  name: "Login",
  data() {
    return {
      LoginForm: {
        email: '',
        password: ''
      },
    }
  },
  methods: {
    submitForm() {
      var patten = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
      if (!patten.test(this.LoginForm.email)){
        this.$message.warning("邮箱格式不正确")
        return;
      }
      this.$http.post('/user/login', this.LoginForm)
          .then(res => {
            console.log(res.data)
            if (res.data.code === 200){
              this.$message.success("登录成功！")
              localStorage.setItem("token", res.data.data.token)
              this.$router.push('/index')
            }else if (res.data.code === 400){
              this.$message.warning(res.data.message)
            }
          })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    toRegister(){
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>

</style>