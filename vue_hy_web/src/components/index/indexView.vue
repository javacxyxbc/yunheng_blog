<template>
    <div class="indexViewFa">
        <div class="indexView" v-for="(item,index) in dataList" :key="index">
            <!--头部预览区域-->
            <div class="topic">
                <div class="topicTop">
                    {{item.title}}
                </div>
                <div class="topicButtom">
                    <div>发表于 <i>{{item.createTime}}</i> </div>
                    <div>分类与 <i>{{item.sortName}}</i></div>
                    <div style="borderRight:None"><i>{{item.clickCount}}+</i> 次浏览量</div>
                </div>
            </div>
            <!--图片区域-->
            <div class="midImg">
                <el-image 
                    style="width: 100%; height: 100%"
                    :src="item.url" 
                    :preview-src-list="changeStringToArr(item.url)">
                </el-image>
            </div>
            <!--底部按钮-->
            <div class="readMore">
                <button @click="gotoArticle(item.uid)">阅读全文 >></button>
                <div class="divider"></div>
            </div>
        </div>
      <!-- 分页 -->
        <div class="page">
                <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-size="pageSize"
                    layout="total, prev, pager, next, jumper"
                    :total="total"
                >
                </el-pagination>
        </div>
    </div>
</template>

<script>
import {getBlogByTime} from "../../api/index"
import {getArticleByTagUid} from '../../api/tag'
import {getArticleByBlogSortUid} from '../../api/classify'
export default {
data(){
    return{
        dataList:null,
        currentPage:1,
        pageSize:5,
        total:0,
    }
},
created(){
    var type=JSON.parse(sessionStorage.getItem("listType"));
    var param={};
    param.currentPage=this.currentPage;
    param.pageSize=this.pageSize;
    if(type==null){
        //1.说明是首页
            getBlogByTime(param).then(res=>{
                this.currentPage=res.data.current
                this.pageSize=res.data.size;
                this.total=res.data.total;
                this.dataList=res.data.records
            })
    }else{
        //2.取出type
        var listType=type.type;
        if(listType=="tag"){
            //如果是标签列表
            param.tagUid=type.uid;
            getArticleByTagUid(param).then(res=>{ 
                this.currentPage=res.data.current
                this.pageSize=res.data.size;
                this.total=res.data.total;
                this.dataList=res.data.records
            })
        }else if(listType=="sort"){
            //如果是分类列表
            param.blogSortUid=type.uid;
            getArticleByBlogSortUid(param).then(res=>{
                this.currentPage=res.data.current
                this.pageSize=res.data.size;
                this.total=res.data.total;
                this.dataList=res.data.records
            })
        }
    }
},
methods:{
    gotoArticle(uid){
        var info={};
        info.uid=uid;
        sessionStorage.setItem("articleInfo",JSON.stringify(info))
        console.log(info)
        this.$router.push("/article");
    },
    handleCurrentChange(e){
            var param={};
            param.currentPage=e;
            param.pageSize=this.pageSize;
            getBlogByTime(param).then(res=>{
                this.currentPage=res.data.current
                this.pageSize=res.data.size;
                this.total=res.data.total;
                this.dataList=res.data.records
            })
    },
    changeStringToArr(e){
        return e.split(',')
    }
  },
  destroyed(){
      sessionStorage.removeItem("listType")
  }
}
</script>

<style lang="less" scoped>
.indexViewFa{
   display: flex;
   flex-direction: column;
   align-items: center;
   padding-bottom: 20px;
    .indexView{
        width: 90%;
        box-sizing: border-box;
        padding: 0 30px;
        min-height: 700px;
        background-color: white;
        display: flex;
        flex-direction: column;
        .topic{
            flex: 3;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            .topicTop{
                font-weight: 800;
                font-size: 25px;
                margin-bottom: 10px;
            }
            .topicButtom{
                color: #a6b3cd;
                font-size: 14px;
                display: flex;
                div{
                    padding: 0 10px;
                    border-right: 1px solid #a6b3cd ;
                }
            }
        }
        .midImg{
            flex: 5;
        }
        .readMore{
            flex: 2;
            display: flex;
            flex-direction: column;
            align-items: center;
            .divider{
                width: 10%;
                height: 2px;
                background-color: #DDDDDD;
                margin-top: 100px;
            }
            button{
                width: 100px;
                height: 30px;
                border: 1px solid #555555;
                color: #555555;
                background-color: white;
                cursor: pointer;   
            }
        }
    }
}

</style>