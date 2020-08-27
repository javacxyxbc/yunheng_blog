<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >
      <h3 class="title">云衡博客</h3>
      <el-form-item prop="username">
        <span class="svg-container svg-container_login">
          <svg-icon icon-class="user"/>
        </span>
        <el-input
          v-model="loginForm.username"
          ref="userNameInput"
          name="username"
          type="text"
          auto-complete="on"
          placeholder="用户名/手机号/邮箱"
          @keyup.enter.native="handleLogin"
        />
      </el-form-item>
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password"/>
        </span>
        <el-input
          :type="pwdType"
          v-model="loginForm.password"
          name="password"
          auto-complete="on"
          placeholder="password"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon icon-class="eye"/>
        </span>
      </el-form-item>
      <el-form-item >
         <el-button
          :loading="loading"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >登 录</el-button>

      </el-form-item>

      <el-form-item >
         <el-button
          :loading="registerLoading"
          type="success"
          style="width:100%;"
          @click.native.prevent="handleRegister"
        >注 册</el-button>

      </el-form-item>
    </el-form>

    <!--引入粒子特效-->
    <vue-particles
      color="#fff"
      :particleOpacity="0.7"
      :particlesNumber="60"
      shapeType="circle"
      :particleSize="4"
      linesColor="#fff"
      :linesWidth="1"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="2"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push"
      class="lizi"
    >
    </vue-particles>
  </div>
</template>

<script>
import { isvalidUsername } from "@/utils/validate";
import {Login,Register} from '../api/login'
import {setToken} from '../utils/auth'
export default {
  name: "Login",
  data() {
    const validateUsername = (rule, value, callback) => {
      callback();
      // if (!isvalidUsername(value)) {
      //   callback(new Error('请输入正确的用户名'))
      // } else {
      //   callback()
      // }
    };
    const validatePass = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error("密码不能小于5位"));
      } else {
        callback();
      }
    };
    return {
      loginForm: {
        username: "",
        password: ""
      },
      loginRules: {
       username: [
            {required: true, message: '用户名不能为空', trigger: 'blur'},
            {min: 5, max: 20, message: '长度在5到20个字符'},
           ],
        password: [{ required: true, trigger: "blur", validator: validatePass }]
      },
      loading: false,
      registerLoading:false,
      pwdType: "password",
      redirect: undefined,
    };
  },
  mounted() {
    // mounted钩子函数，dom已经渲染完毕，可以直接获取到dom对象进行聚焦
    this.$refs.userNameInput.focus()
  },
  components:{

  },
  methods: {
    inputFocus: function() {
      this.$nextTick(x => {
        this.$refs.userNameInput.focus()
      })
    },
    showPwd() {
      if (this.pwdType === "password") {
        this.pwdType = "";
      } else {
        this.pwdType = "password";
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          var param={}
          param.userName=this.loginForm.username;
          param.passWord=this.loginForm.password;
          Login(param).then(res=>{
            if(res.code=="success"){
              this.$message.success("登录成功")
              var token=res.data.token
              var userUid=res.data.userUid
              sessionStorage.setItem("userUid",userUid);
              setToken(token)
              this.$router.push("/")
              this.loading=false;
            }else{
              this.$message.error(res.data)
              this.loading=false;
            }
          })
        } else {
         this.$message.error("格式错误")
          return false;
        }
      });
    },
    handleRegister(){
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.registerLoading = true;
          var param={}
          param.userName=this.loginForm.username;
          param.passWord=this.loginForm.password;
          Register(param).then(res=>{
            if(res.code=="success"){
               this.registerLoading=false;

               this.$message.success("注册成功,请再次登录")
            }else{
              this.$message.error(res.data)
              this.registerLoading=false;
            }
          })
        }  else {
         this.$message.error("格式错误")
          return false;
        }
      });
    }
  }
};
</script>

<style  lang="less">


/* reset element-ui css */

.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: #eee;
      height: 47px;
      &:-webkit-autofill {
        -webkit-box-shadow: 0 0 0px 1000px #2d3a4b inset !important;
        -webkit-text-fill-color: #fff !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style  lang="less" scoped>
.buttons{
  display: flex;
  justify-content: space-between;
}
.login-container {
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: #2d3a4b;
  .login-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 520px;
    max-width: 100%;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }
  .svg-container {
    padding: 6px 5px 6px 15px;
    color: #889aa4;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
    &_login {
      font-size: 20px;
    }
  }
  .title {
    font-size: 26px;
    font-weight: 400;
    color: #eee;
    margin: 0px auto 40px auto;
    text-align: center;
    font-weight: bold;
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: #889aa4;
    cursor: pointer;
    user-select: none;
  }
}
</style>
