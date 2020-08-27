<template>
<div id="table" class="app-container calendar-list-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
      <el-button class="filter-item" type="primary" @click="handleAdd" icon="el-icon-edit" v-permission="'/picture/add'">添加</el-button>
      <el-button class="filter-item" type="primary" @click="handleReturn" icon="el-icon-s-promotion">返回分类</el-button>
      <el-button class= "button" type="primary"  @click="checkAll()" icon="el-icon-refresh">{{chooseTitle}}</el-button>
      <el-button class="filter-item" type="danger" @click="handleDeleteBatch" icon="el-icon-delete" v-permission="'/picture/delete'">删除选中</el-button>
      <el-button class="filter-item" type="success" @click="setCover" icon="el-icon-video-camera-solid" v-permission="'/picture/setCover'">设为封面</el-button>
    </div>


    <el-row>
      <el-col
        v-for="(picture, index) in tableData"
        :key="picture.uid"
        style="padding: 6px"
        :xs="24"
        :sm="12"
        :md="12"
        :lg="6"
        :xl="4"
      >

        <el-card
          :body-style="{ padding: '0px', textAlign: 'center' }"
          shadow="always"
        >
          <input style="position: absolute;z-index: 100;" type="checkbox" :id="picture.uid" :checked="pictureUids.indexOf(picture.uid)>=0" @click="checked(picture)">
          <el-image
            :src="picture.url"
            style="cursor:pointer"
            fit="scale-down"
            @click="showPicture(picture.url)"
          />
          <div @click="showPicture(picture.url)">
            <span class="media-title" v-if="picture.picName">{{picture.picName}}</span>
            <span class="media-title" v-else>图片 {{index + 1}}</span>
          </div>
          <div style="margin-bottom: 14px;">
            <el-button-group>
              <el-tooltip class="item" effect="dark" content="复制图片地址" placement="bottom-start" style="margin-right: 2px">
                <el-button
                  size="mini"
                  icon="el-icon-copy-document"
                  @click="copyUrl(picture.url)"
                />
              </el-tooltip>

              <el-tooltip class="item" effect="dark" content="复制Markdown格式图片地址" placement="bottom-start" style="margin-right: 2px">
                <el-button
                  type="primary"
                  size="mini"
                  icon="el-icon-document-copy"
                  @click="copyMarkdownUrl(picture.url, picture.url)"
                >
                </el-button>
              </el-tooltip>

              <el-tooltip class="item" effect="dark" content="裁剪图片" placement="bottom-start" style="margin-right: 2px" v-permission="'/picture/add'">
                <el-button
                  type="warning"
                  size="mini"
                  icon="el-icon-s-open"

                />
              </el-tooltip>

              <el-tooltip class="item" effect="dark" content="删除图片" placement="bottom-start" style="margin-right: 2px" v-permission="'/picture/delete'">
                <el-button
                  type="danger"
                  size="mini"
                  icon="el-icon-delete"
                  @click="handleDelete(picture)"
                />
              </el-tooltip>

            </el-button-group>
          </div>
        </el-card>

      </el-col>
    </el-row>

		<!--分页-->
    <div class="block">
        <el-pagination
          @current-change="handleCurrentChange"
          :current-page.sync="currentPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
    </div>

	  <!-- 添加或修改对话框 -->
		<el-dialog :title="title" :visible.sync="dialogFormVisible" width="40%"
  center>

       <el-upload
          class="upload-demo"
          drag
          action="http://localhost:8090"
          :auto-upload="false"
          :http-request="uploadFile"
          ref="upload"
          :before-upload="beforeAvatarUpload"
          list-type="picture"
          width="100%"
        >
          <i class="el-icon-upload" style="color:#0176FF"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">
            只能上传jpg/png/jpeg文件，且不超过4MB
          </div>
        </el-upload>
      <div class="uploadButton">
        <el-button class="submit" @click="upload" type="success" size="medium">上传<i class="el-icon-upload el-icon--right"></i></el-button>
      </div>
		</el-dialog>

    <el-dialog :visible.sync="dialogPictureVisible" fullscreen style="text-align: center">
      <img :src="dialogImageUrl" alt="">
    </el-dialog>

    <el-dialog :visible.sync="pictureCropperVisible" fullscreen>
      <PictureCropper v-if="reFresh" :modelSrc="checkedPicture.url" @cropperSuccess="cropperSuccess"></PictureCropper>
    </el-dialog>


  </div>
</template>

<script>
import {
  getPictureList,
  addPicture,
  editPicture,
  deletePicture,
  setCover
} from "@/api/picture";
import { getToken } from '@/utils/auth'

import PictureCropper from '@/components/PictureCropper'

import { formatData } from "@/utils/webUtils";

import { Loading } from "element-ui";

export default {
  components: {
    PictureCropper
  },
  data() {
    return {
      imgsArr: [],
      dialogImageUrl: "", //图片显示地址
      checkedPicture: {}, // 单选的图片
      pictureCropperVisible: false, // 裁剪图片框是否显示
      dialogPictureVisible: false,
      tableData: [],
      uploadPictureHost: null,
      fileList: [],
      pictureSortUid: "",
      pictureUids: [], //图片uid集合
      pictureUploadList: [], //图片上传列表
      chooseTitle: "全选",
      isCheckedAll: false, //是否全选
      fileUids: "", //上传时候的文件uid
      form: {
        uid: null,
        fileUid: null,
        picName: null,
        pictureSortUid: null
      },
      count: 0, //计数器，用于记录上传次数
      loading: true,
      currentPage: 1,
      pageSize: 18,
      total: null,
      title: "增加图片",
      dialogFormVisible: false,
      keyword: "",
      reFresh: true, //是否刷新组件
    };
  },
  watch: {
    checkedPicture(val) {
      this.reFresh= false
    }
  },
  created() {
    //传递过来的pictureSordUid
    this.pictureSortUid = this.$route.query.pictureSortUid;

    // 获取图片列表
    this.pictureList()



    //其它数据
    this.otherData = {
      source: "picture",
      userUid: "uid00000000000000000000000000000000",
      adminUid: "uid00000000000000000000000000000000",
      projectName: "blog",
      sortName: "admin",
      token: getToken()
    };
  },
  methods: {
     beforeAvatarUpload(file) {
      const isLt = file.size / 1024 / 1024 < 4;
      const testmsg = /^image\/(jpeg|png|jpg)$/.test(file.type);
      if (!isLt) {
        this.$message.error("上传头像图片大小不能超过 4MB!");
        return;
      }
      if (!testmsg) {
        this.$message.error("上传图片格式不对!");
        return;
      }
      return isLt && testmsg;
    },
       uploadFile(file) {
      this.imgsArr.push(file.file); //往一个数组中添加图片数据
    },
    upload() {
      var formdate = new FormData();
      formdate.append("sortUid",this.pictureSortUid);
      this.$refs.upload.submit();
      if (this.imgsArr.length > 0) {
        this.imgsArr.forEach((img, index) => {
          formdate.append(`file`, img);
         // console.log(img);
        });
      } else {
        this.$message.error("请先上传图片");
        return;
      }
      // this.$http.post( process.env.ADMIN_API+"/picture/add1",formdate,config).then(res=>{
      //   console.log(res.data);
      //   this.$message.success(res.data.code);
      // })
      addPicture(formdate).then(res=>{
        console.log(res.data);
         this.$message.success(res.data)
         this.pictureList();
      })
    },
    pictureList: function() {
      var params = {};
      params.keyword = this.keyword
      params.sortUid = this.pictureSortUid
      params.pageSize = this.pageSize
      params.currentPage = this.currentPage
      getPictureList(params).then(response => {
        if (response.code == "success") {
          console.log(response.data);
          this.tableData = response.data.records;
          this.currentPage = response.data.current;
          this.pageSize = response.data.size;
          this.total = response.data.total;
        } else {
          console.log('fail')
          this.$message({
            type: "error",
            message: response.data
          });
        }
      });
    },
    getFormObject: function() {
      var formObject = {
        uid: null,
        fileUid: null,
        picName: null,
        pictureSortUid: null
      };
      return formObject;
    },
    showPicture: function(url) {
      this.dialogPictureVisible = true
      this.dialogImageUrl = url
    },
    copyUrl(url) {
      this.$commonUtil.copyText(url)
      this.$commonUtil.message.success('复制链接到剪切板成功')
    },
    copyMarkdownUrl(name, url) {
      const text = '![' + name + '](' + url + ')'
      this.$commonUtil.copyText(text)
      this.$commonUtil.message.success('复制Markdown格式链接到剪切板成功')
    },
    //点击单选
    checked: function(data) {
      let idIndex = this.pictureUids.indexOf(data.uid);
      if (idIndex >= 0) {
        //选过了
        this.pictureUids.splice(idIndex, 1);
      } else {
        this.pictureUids.push(data.uid);
      }
    },
    checkAll: function() {
      //如果是全选
      if (this.isCheckedAll) {
        this.pictureUids = [];
        this.isCheckedAll = false;
        this.chooseTitle = "全选";
      } else {
        this.pictureUids = [];
        this.tableData.forEach(function(picture) {
          this.pictureUids.push(picture.uid);
        }, this);
        this.isCheckedAll = true;
        this.chooseTitle = "取消全选";
      }
    },
    handleDelete: function(picture) {
      this.pictureUids = [picture.uid]
      this.handleDeleteBatch()
    },
    handleDeleteBatch: function() {
      if (this.pictureUids.length <= 0) {
        this.$message({
          type: "error",
          message: "请先选中图片！"
        });
        return;
      }

      this.$confirm("是否删除选中图片？, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {};
          params.uid = this.pictureUids.join(","); //将数组变成,组成
          deletePicture(params).then(response => {
            if (response.code == "success") {
              this.$message({
                type: "success",
                message: response.data
              });

              // 清空选中的列表
              this.pictureUids = []
              this.checkedPicture = []

              this.pictureList();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    setCover: function() {
      if (this.pictureUids.length != 1) {
        this.$message({
          type: "error",
          message: "选择一张图片设为封面图！"
        });
        return;
      }

      this.$confirm("是否将该图片设为封面？, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          let params = {};
          params.uid = this.pictureUids[0]
          params.sortUid = this.pictureSortUid
          console.log(params);
          setCover(params).then(response => {
            this.$message({
              type: "success",
              message: response.data
            });
            this.pictureUids = [];
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });

    },
    handleCropper: function(picture) {
      this.checkedPicture = picture;
      setTimeout(() => {

        this.pictureCropperVisible = true;
        this.reFresh = true;

      }, 10)
    },
    // 裁剪成功后的回调
    cropperSuccess: function(picture) {
      this.pictureCropperVisible = false;
      var checkedPicture = this.checkedPicture
      checkedPicture.fileUid =  picture.uid
      let params = {};
      params.uid = checkedPicture.uid
      params.fileUid = checkedPicture.fileUid
      params.picName = checkedPicture.picName
      params.pictureSortUid = checkedPicture.pictureSortUid
      editPicture(params).then(response => {
        if (response.code == "success") {
          this.$message({
            type: "success",
            message: response.data
          });
          this.pictureList();
        } else {
          this.$message({
            type: "error",
            message: response.data
          });
        }
      });

      // 清空选中的列表
      this.pictureUids = []
      this.checkedPicture = []

    },
    handleReturn: function() {
      this.$router.push({
        path: "pictureSort",
        query: {}
      });
    },
    //改变页码
    handleCurrentChange(val) {
      var that = this;
      this.currentPage = val; //改变当前所指向的页数
      this.pictureList();
    },
    handleAdd: function() {
      this.dialogFormVisible = true;
    },
    handlePreview: function() {

    },
    handleRemove: function() {

    },
    submitNormalUpload: function() {
      console.log();
      this.$refs.upload.submit();
    },
    fileSuccess: function(response, file, fileList) {
            console.log(response.data)
      }

  }
};
</script>

<style scoped>
  .media-title {
    color: #8492a6;
    font-size: 14px;
    padding: 14px;
    display: inline-block;
    white-space: nowrap;
    width: 60%;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .el-image {
    width: 100%;
    height: 160px;
  }
  .uploadButton{
    margin-top: 20px;

  }
</style>
