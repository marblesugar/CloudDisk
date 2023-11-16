<template>
    <div style="width: 500px;margin: auto">
      <div>
        <h1>注册<i class="el-icon-s-custom"></i></h1>
      </div>
      <el-form :model="LoginForm" :rules="rules" ref="registerForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="LoginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="LoginForm.email"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="LoginForm.password" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('registerForm')">立即注册</el-button>
          <el-button @click="resetForm('registerForm')">重置</el-button>
          <el-link type="primary" :underline="false" @click="toLogin">前往登录</el-link>
        </el-form-item>
      </el-form>
    </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    var validatePassword = (rule, value, callback) =>{
      const regex = /^[a-zA-Z]\w{5,17}$/
      if (!value) {
        return callback(new Error('密码不能为空'));
      }
      setTimeout(() => {
        if (!regex.test(value)) {
          callback(new Error("以字母开头，长度在6~18之间，只能包含字母、数字和下划线"));
        } else {
            callback();
        }
      }, 500)
    }
    return {
      LoginForm: {
        username: '',
        email: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入您的用户名', trigger: 'blur' },
          { min: 2, max: 9, message: '长度在 2 到 9 个字符', trigger: 'blur' }
        ],
        email: [
          { type:'email', required: true, message: '请输入您的邮箱', trigger: 'blur' },
        ],
        password: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$http.post('/user/register', this.LoginForm)
              .then(res => {
                console.log(res.data)
                if (res.data.code === 200){
                  this.$message.success("请前往邮箱进行激活！")
                  setTimeout(()=>{
                    this.$message.info("正在跳转登陆页面...")
                    this.$router.push('/login')
                  },3000)
                }else {
                  this.$message.warning(res.data.message)
                }
              })
        } else {
          this.$message.warning("请填写正确信息！")
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    toLogin(){
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>

</style>