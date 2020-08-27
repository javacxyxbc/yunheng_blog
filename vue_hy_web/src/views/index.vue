<!--首页-->
<template>

  <div class="indexFa">
      <div class="left">
        <div class="leftTop">
          <h2>hy Blog</h2>
          <h5 >just do it</h5>
        </div>
        <!--导航栏-->
        <div class="leftBar">
            <el-menu
                  default-active="indexView"
                  class="el-menu-vertical-demo"
                  router
                  >
                  <el-menu-item index="indexView">
                    <i class="el-icon-menu"></i>
                    <span slot="title">首页</span>
                  </el-menu-item>
                  <el-menu-item index="archived">
                    <i class="el-icon-edit-outline"></i>
                    <span slot="title">归档</span>
                  </el-menu-item>
                  <el-menu-item index="sort" >
                    <i class="el-icon-loading"></i>
                    <span slot="title">分类</span>
                  </el-menu-item>
                  <el-menu-item index="indexLabel" >
                    <i class="el-icon-collection-tag"></i>
                    <span slot="title">标签</span>
                  </el-menu-item>
                  <el-menu-item index="aboutMe">
                    <i class="el-icon-paperclip"></i>
                    <span slot="title">关于</span>
                  </el-menu-item>
                  <el-menu-item index="messageBoard">
                    <i class="el-icon-edit"></i>
                    <span slot="title">留言</span>
                  </el-menu-item>
                  <el-menu-item index="personCenter">
                    <i class="el-icon-user"></i>
                    <span slot="title">中心</span>
                  </el-menu-item>
                </el-menu>
        </div>
        <!--底部汇总-->
        <div class="leftButtom">
            <BottomView @showSearch="turnSearchState"></BottomView>
        </div>
      </div>
      <!--中部隔离层-->
      <div class="mid"></div>
      <!--文章概要预览-->
      <div class="right">
        <router-view class="route" ></router-view>
      </div>

      <!-- 博客搜索栏 -->
      <el-dialog
          :visible.sync="searchVisible"
          width="50%"
          :before-close="turnSearchState">
          <searchBar  @closeSearch="turnSearchState"></searchBar>
      </el-dialog>
  </div>

</template>

<script>
export default {
  data(){
    return{
      searchVisible:null,
    }

  },
  components:{
     BottomView:resolve=>require(['../components/index/bottom-view'],resolve),
     searchBar:resolve=>require(['../components/index/search'],resolve)
   },
   methods:{
     turnSearchState(){
       this.searchVisible=!this.searchVisible
     }
   },
   mounted(){
   }
}
</script>

<style lang="less" scoped>
    .indexFa{

      width: 1100px;
      height: 100%;
      margin: 0 auto;
      display: flex;
      /deep/.el-dialog__header{
        padding: 0;
        display: none;
      }
      .left{
        flex: 4;
        display: flex;
        flex-direction: column;
        margin-right: 20px;
        height:640px ;
        .leftTop{
          color: white;
          flex: 2;
          background-color: black;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          h5{
            color: #DDDDDD;
          }
        }
        .leftBar{
           flex: 7;
           background: white;
           /deep/.el-menu-item{
             height: 48px;
           }
        }
        .leftButtom{
          margin-top: 10px;
          background-color: white;
           flex:4;
        }
      }
      .right{
        flex:12;
        .route{
             min-height: 640px;
             background-color: white;
             width: 85%;
        }
      }
}

</style>
