<template>
  <div class="search">
      <!-- 搜索框 -->
     <div class="inputBar">
         <span class="searchIcon" @click="search">
             <svg t="1598181610661" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2443" width="18" height="18"><path d="M1004.79998 1004.799969c-25.599999 25.599999-63.999998 25.599999-89.599997 0l-198.399994-198.399994c-172.799995 127.999996-409.599987 121.599996-569.599982-25.599999-179.199994-166.399995-191.999994-460.799986-19.199999-639.99998 172.799995-179.199994 460.799986-185.599994 639.99998-6.4 159.999995 159.999995 172.799995 409.599987 38.399999 582.399982l198.399994 198.399994C1030.399979 940.799971 1030.399979 979.199969 1004.79998 1004.799969zM703.999989 262.399992c-127.999996-179.199994-390.399988-179.199994-518.399984 0-83.199997 108.799997-83.199997 268.799992 0 377.599988 127.999996 179.199994 390.399988 179.199994 518.399984 0C787.199987 524.799984 787.199987 371.199988 703.999989 262.399992z" p-id="2444" fill="#44d7b6"></path></svg>
         </span>
         
         <input class="input" v-model="keyWord" @keyup.enter="search">

         <span class="closeIcon" @click="close">
              <svg t="1598182423630" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4046" width="18" height="18"><path d="M578.382 512.5L861.58 795.696c18.745 18.745 18.745 49.137 0 67.883-18.746 18.745-49.138 18.745-67.883 0L510.5 580.382 227.304 863.58c-18.745 18.745-49.137 18.745-67.883 0-18.745-18.746-18.745-49.138 0-67.883L442.618 512.5 159.42 229.304c-18.745-18.745-18.745-49.137 0-67.883 18.746-18.745 49.138-18.745 67.883 0L510.5 444.618 793.696 161.42c18.745-18.745 49.137-18.745 67.883 0 18.745 18.746 18.745 49.138 0 67.883L578.382 512.5z" p-id="4047" fill="#5bdcbf"></path></svg>
         </span>

     </div>
      <el-divider></el-divider>

    <!-- tips -->
     <div class="tips" v-show="searchList.length==0">
           点击回车键,或者左侧搜索按钮,输入关键字,即可进行搜索
      </div>

      <!-- 结果列表 -->
      <div class="searchList">
        <el-scrollbar style="height:100%">
          <div class="result" 
          @click="gotoArticle(item.uid)"
          v-for="(item,index) in searchList" :key="index">
              <div class="title">{{item.title}}</div>
              <div class="content">
                  <div v-html="item.all"></div>
              </div>
          </div>
        </el-scrollbar>  
      </div>

 
  </div>
</template>

<script>
import {searchBlog} from '../../api/search'
export default {
  data(){
      return{
          keyWord:"",
          searchList:[],
      }
  },
  methods:{
      search(){
          var param={keyWord:this.keyWord}
          searchBlog(param).then(res=>{
              var list=res.data.blogList
              if(list==undefined){
                  this.searchList=[]
              }else{
                  this.searchList=list
              }
          })
      },
    gotoArticle(uid){
        var info={};
        info.uid=uid;
        sessionStorage.setItem("articleInfo",JSON.stringify(info))
        console.log(info)
        this.$router.push("/article");
    },
    close(){
        this.$emit("closeSearch")
    }
  }
}
</script>

<style lang="less" scoped>
   .search{
       .inputBar{
           display: flex;
           justify-content: space-between;
           align-items: center;
           .input{
               flex: 9;
               box-sizing: border-box;
               padding-right: 10px;
               color: #7a7a7a;
               border: none;
               outline: none;
               font-family: sans-serif;
           }
           .searchIcon{
               cursor: pointer;
               flex: 1;

           }
           .closeIcon{
               cursor: pointer;
           }
       }
       .searchList{
                height:400px;
                
                overflow-x: hidden;
                /deep/.el-scrollbar__wrap{
                overflow-x: hidden;
                }
                /deep/.el-scrollbar__bar{
                width: 3px;
                }
          .result{
              max-height: 100px;
              overflow: hidden;
              padding: 20px 10px;
              .title{
                  font-weight: bold;
                  font-size: 16px;
                  font-family: Verdana, Geneva, Tahoma, sans-serif;
                  margin-bottom: 10px;
              }
              .content{
                  font-size: 13px;
                  color: #7e8482;
              }
          }
          .result:hover{
              cursor: pointer;
              background-color: #ecfbf7;
          }   
       }
   }
</style>