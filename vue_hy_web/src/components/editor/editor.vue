<template>
  <div>
      <mavon-editor
            ref="md"
            placeholder="请输入文档内容..."
            :boxShadow="false"
            style="z-index:1;border: 1px solid #d9d9d9;min-height:800px"
            v-model="text"
            :toolbars="toolbars"
            @imgAdd="$imgAdd"
            :navigation="true" 
            @change="change"
          />
  </div>
</template>

<script>
import {mavonEditor} from "mavon-editor";
import "mavon-editor/dist/css/index.css"
export default {
  name: "home",
  components: {
    mavonEditor:mavonEditor
  },
  data() {
    return {
       text:"",
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: false, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true // 预览
      }
    };
  },
  methods: {
    // 上传图片方法
    $imgAdd(pos, $file) {
            console.log(pos, $file);
      var formdata=new FormData();
      formdata.append("file",$file);

      let config = {
                    headers: {
                      "Content-Type": "multipart/form-data",
                    },
          }; //设置请求头!
      this.$http.post("http://121.199.16.65:8011/upload/uploadPicture",formdata,config).then(res=>{
        this.$refs.md.$img2Url(pos, res.data);
      })
    },
    //文章内容改变,传回给父页面
    change(value,render){
      //console.log(value)
      this.$emit("changeValue",value);
    }
  },
  props:{
    content:{
      type:String
    }
  }

};
</script>
<style scoped>
 .test{
   width: 1000px;
   height: 1500px;
   margin: 0 auto;
 }
 .v-note-navigation-wrapper transition{
   margin-right: 500px !important;
 }
</style>