<template>
  <div class="IndexButtom">
      <div class="buttomTop">
          <div class=".img">
            <!-- 图片区域 -->
            <el-image
                  style="width: 60px; height: 60px;border: 1px solid #dddddd;"
                  :src="logoUrl"
                  :preview-src-list="logoUrl.split(',')">
            </el-image>
          </div>
          <div style="color:#B5B5B5;fontSize:13px">做一个自律的人</div>
      </div>
      <div class="buttomMid">
          <div class="buttomMidChild">
              <div class="buttomMidNum">{{collect.blogCount}}</div>
              <div class="buttomMidView">文章</div>
          </div>
          <div class="buttomMidChild">
              <div class="buttomMidNum">{{collect.tagCount}}</div>
              <div class="buttomMidView">标签</div>
          </div>
          <div class="buttomMidChild">
              <div class="buttomMidNum">{{collect.sortCount}}</div>
              <div class="buttomMidView">分类</div>
          </div>
      </div>
      <div class="buttomBu" @click="blurSearch">
          <i class="el-icon-position"></i>
          search
      </div>
  </div>
</template>

<script>
import {getCollect} from '../../api/index'
export default {
  data(){
    return{
      collect:{
        blogCount:0,
        sortCount:0,
        tagCount:0,
      },
      logoUrl:'http://124.70.200.174:81/img/logo.jpg'
    }
  },
   methods:{
       blurSearch(){
           this.$emit("showSearch")
       }
   },
   created(){
     getCollect().then(res=>{
       console.log(res.data);
       this.collect=res.data
     })
   }
}
</script>

<style lang="less" scoped>
.IndexButtom{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    padding-top: 20px;
    padding-bottom: 10px;
    padding-right: 30px;
    padding-left: 30px;
    box-sizing: border-box;
    .buttomTop{
      flex: 2;
      padding-top: 5px;
      box-sizing: border-box;
      display: flex;
      justify-content: space-around;
      align-items: top;
      img{
          width: 60px ;
          height:60px;
          border: 1px solid #dddddd;

      }
    }
    .buttomMid{
        flex: 2;
        display: flex;
        box-sizing: border-box;
        padding-top: 5px;
        padding-bottom: 5px;
         .buttomMidChild{
              flex: 1;
              display: flex;
              flex-direction: column;
              align-items: center;
              border-right: 1px solid #dddddd;
              .buttomMidView{
                  color: #B5B5B5;
                  font-size: 13px;
              }
              .buttomMidNum{
                  font-weight: 800;
              }

          }
          .buttomMidChild:nth-child(3){
              border-right: none;
          }

    }
    .buttomBu{
        cursor: pointer;
        flex: 1;
        border-top: 1px solid #dddddd;
        border-bottom: 1px solid #dddddd;
        text-align: center;
        padding: 10px ;
        box-sizing: border-box;
        color: orange;
    }
}
</style>
