<template>
    <el-card class="personCenter">
        <span slot="label"><i class="el-icon-user-solid"></i> 个人中心</span>
        <el-form label-position="left" :model="userInfo" label-width="100px" :rules="rules" ref="userInfo">
          <el-form-item label="用户头像" :label-width="labelWidth">
            <div class="imgBody" v-if="userInfo.avatar">
              <i class="el-icon-error inputClass" v-show="icon" @click="deletePhoto()" @mouseover="icon = true"></i>
              <img @mouseover="icon = true" @mouseout="icon = false" v-bind:src="userInfo.avatar" />
            </div>

            <div v-else class="uploadImgBody" @click="checkPhoto">
              <i class="el-icon-plus avatar-uploader-icon"></i>
            </div>
          </el-form-item>

          <el-form-item label="昵称" :label-width="labelWidth">
            <el-input v-model="userInfo.nickName" style="width: 50%"></el-input>
          </el-form-item>

          <el-form-item label="性别" :label-width="labelWidth" >
                <el-radio-group v-model="userInfo.gender">
                <el-radio label="0" >男</el-radio>
                <el-radio label="1">女</el-radio>
                </el-radio-group>
          </el-form-item>

          <el-form-item label="生日" :label-width="labelWidth">
            <el-date-picker
              v-model="userInfo.birthday"
              type="date"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>


          <el-form-item label="邮箱" :label-width="labelWidth" prop="email">
            <el-input v-model="userInfo.email" style="width: 100%"></el-input>
          </el-form-item>

          <el-form-item label="QQ号" :label-width="labelWidth" prop="qqNumber">
            <el-input v-model="userInfo.qqNumber" style="width: 100%"></el-input>
          </el-form-item>



          <el-form-item label="简介" :label-width="labelWidth">
            <el-input
              type="textarea"
              :autosize="{ minRows: 3, maxRows: 5}"
              placeholder="请输入内容"
              style="width: 100%"
              v-model="userInfo.summary">
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm">保 存</el-button>
            <el-button type="success"  @click="subOut">退 出</el-button>
          </el-form-item>

        </el-form>


          <!--头像裁剪-->
        <avatar-cropper
            v-show="imagecropperShow"
            :key="imagecropperKey"
            :width="50"
            :height="50"
            :url="uploadUrl"
            lang-type="zh"
            @close="close"
            @crop-upload-success="cropSuccess"
        />

    </el-card>
</template>
<script>
import AvatarCropper from '@/components/AvatarCropper'
import {getUserInfo,editUser} from '../../api/user'
import {logOut} from '../../api/user'
import {getToken,removeToken} from '../../utils/auth'
export default {
    data(){
        return{
            icon: false, //控制删除图标的显示
            labelWidth: "100px",
            genderDictList: [], //性别 字典列表
            rules: {
            qqNumber: [
                {pattern:  /[1-9]([0-9]{5,11})/, message: '请输入正确的QQ号码'},
            ],
            email: [
                {pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/, message: '请输入正确的邮箱'},
            ]
            },
            userInfoRules: {
                oldPwd: [
                    {required: true, message: '旧密码不能为空', trigger: 'blur'},
                    {min: 5, max: 20, message: '密码长度在5到20个字符'},
                ],
                newPwd: [
                    {required: true, message: '新密码不能为空', trigger: 'blur'},
                    {min: 5, max: 20, message: '密码长度在5到20个字符'},
                ],
                newPwd2: [
                    {required: true, message: '新密码不能为空', trigger: 'blur'},
                    {min: 5, max: 20, message: '密码长度在5到20个字符'},
                ]
            },
            userInfo: {
                uid:"",
                avatar:"",
                nickName:"",
                gender:0,
                birthday:"",
                label:"",
                qqNumber:"",
                summary:"",
            },
            imagecropperShow: false,
            imagecropperKey: 0,
            uploadUrl:  process.env.PICTURE_API+"/file/uploadPic",
        }
    },
    components:{
        AvatarCropper
    },
    created(){
        var param={token:getToken()}
        getUserInfo(param).then(res=>{
            this.userInfo=res.data

        })
    },
    methods:{
        subOut(){
            this.$router.push("/")
            this.$message.success("退出登录成功")
            setTimeout(()=>{
            removeToken()
            },500)
        },
        submitForm(){
            this.$refs.userInfo.validate((valid)=>{
                if(!valid){
                    this.$message.warning("格式错误")
                }else{

                    editUser(this.userInfo).then(res=>{
                        this.userInfo=res.data
                        this.$message.success("保存成功")
                    })
                }
            })
        },
        deletePhoto: function() {
            this.userInfo.avatar = "";
            this.icon = false;
      },
        close() {
            this.imagecropperShow = false
        },
        cropSuccess(resData) {
                this.imagecropperShow = false
                this.imagecropperKey = this.imagecropperKey + 1
                this.userInfo.avatar = resData
        },
            //弹出选择图片框
        checkPhoto() {
            this.imagecropperShow = true
        },
    }
}
</script>
