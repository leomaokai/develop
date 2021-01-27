<template>
  <div>
    <el-form :rules="rules" ref="loginForm" :model="loginForm" class="loginContainer">
      <h3 class="loginTitle">系统登陆</h3>
      <el-form-item prop="username">
        <el-input type="text" auto-complete="false" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" auto-complete="false" v-model="loginForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input type="text" auto-complete="false" v-model="loginForm.code" placeholder="点击图片更换验证码"
                  style="width: 250px;margin-right: 5px"></el-input>
        <img :src="captchaUrl" @click="updateCaptcha">
      </el-form-item>
      <el-checkbox v-model="checked" class='loginRemember'>记住我</el-checkbox>
      <el-button type="primary" style="width:100%" @click="submitLogin">登录</el-button>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      captchaUrl: '/captcha?time='+new Date(),
      loginForm: {
        username: 'admin',
        password: '123',
        code: ''
      },
      checked: true,
      rules: {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}],
        code: [{required: true, message: '请输入验证码', trigger: 'blur'}]
      }
    }
  },
  methods: {
    updateCaptcha(){
      this.captchaUrl='/captcha?time='+new Date();
    },
    submitLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          alert('submit');
        } else {
          this.$message.error("错了")
          return false;
        }
      });
    }
  }
}
</script>

<style>
.loginContainer {
  /*边框*/
  border-radius: 15px;
  /*内边距*/
  background-clip: padding-box;
  margin: 100px auto;
  /*宽度*/
  width: 350px;
  /*内边距*/
  padding: 15px 35px 15px 35px;
  /*背景颜色*/
  background: #fff;
  /*边框*/
  border: 1px solid #eaeaea;
  /*阴影*/
  box-shadow: 0 0 25px #cac6c6;
}

.loginTitle {
  /*内边距*/
  margin: 10px auto 48px auto;
  /*居中*/
  text-align: center;
}

.loginRemember {
  text-align: left;
  margin: 0px 0px 15px 0px;
}

.el-form-item__content{
  display: flex;
  align-items: center;
}

</style>