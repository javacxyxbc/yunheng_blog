<!--标签页面-->
<template>
  <div class="indexLabel">
 
          <h3>文章标签</h3>
          <div class="labels">
            <div class="label" v-for="(item,index) in tagList" :key="index"
             style="background-color: #ecf5ff; border-color: #d9ecff;"
             @click="gotoTag(item.uid)"
             >
                {{item.content}}
            </div>
          </div> 

  </div>
</template>

<script>
import {getTagList} from '../../api/tag'
export default {
    data() {
      return {
        total:0,
        tagList:[],
      };
    },
    created(){
      getTagList().then(res=>{
        this.total=res.data.total;
        this.tagList=res.data.records;
      })
    },
    methods:{
      gotoTag(uid){
         var type={};
         type.uid=uid;
         type.type="tag";
         sessionStorage.setItem("listType",JSON.stringify(type));
         this.$router.push("/indexView")
      }
    }
  }
</script>

<style lang="less" scoped>
.indexLabel{
    width: 90%;
    box-sizing: border-box;
    padding: 0 30px;
    min-height: 900px;
    padding-top: 20px;
    h3{
        text-align: center;
        margin-bottom: 25px;
    }
   .labels{
       display: flex;
       flex-wrap: wrap;
       justify-content: center;

      
   }
   .label{
       margin-bottom: 15px;
    
    display: inline-block;
    margin-right: 15px;
    height: 40px;
    padding: 0 10px;
    line-height: 40px;
    font-size: 15px;
    border-width: 1px;
    border-style: solid;
    cursor: pointer;
    border-radius: 4px;
    box-sizing: border-box;
    white-space: nowrap;
   }
   
}


</style>