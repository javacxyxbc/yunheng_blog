<template>
  <div class="comment">
    <div class="commentBox">
      <span class="right">
      <textarea
      id="textpanel" class="textArea" placeholder="既然来了，那就留下些什么吧~" v-model="value"  @input="vaildCount"></textarea>
      </span>
    </div>
    <div class="bottom">
      <!-- <el-button class="submit p2" type="primary"  @click="handleSubmit">发送评论</el-button> -->
      <button @click="handleSubmit" class="submitButton">发送</button>

      <div class="emoji-panel-btn p2" @click="showEmojiPanel">
        <img src="../../../assets/img/face_logo.png" />
      </div>
      <span class="allowFont">还能输入{{count}}个字符</span>

      <emoji-panel class="emojiPanel" @emojiClick="appendEmoji" v-if="isShowEmojiPanel"></emoji-panel>

    </div>
  </div>
</template>

<script>
  import {dateFormat} from '../../../utils/webUtils'
  import  EmojiPanel from "@/components/EmojiPanel/EmojiPanel.vue";
  export default {
    name: 'CommentBox',
    components: {
      EmojiPanel,
    },
    data() {
      return {
        comments: [],
        submitting: false,
        value: '',
        user: {},
        count: 1024,
        isShowEmojiPanel: false, // 是否显示表情面板
       text:"",
      toolbars: {
      }
      }
    },
    mounted() {
      // 页面加载的时候调用监听
      this.hideEmojiPanel()
    },
    watch:{
      // value(e){
      //   this.$emit("change",this.value)
      // }
    },
    methods: {
      //拿到vuex中的写的方法
      vaildCount: function() {
        var count = 1024 - this.value.length;
        if(count <= 0) {
          this.count = 0
        } else {
          this.count = count;
        }
      },
      handleSubmit() {
        if(this.value =="") {
          this.$notify.error({
            title: '警告',
            message: '评论内容不能为空哦~',
            offset: 100
          });
          return;
        }
        // 替换表情
        let content = this.value.replace(/:.*?:/g, this.emoji);
        this.value = '';
        this.count = 1024;
        this.$emit("submit", content)
      },
      emoji(word) {
        // 生成html
        const type = word.substring(1, word.length - 1);
        return `<span class="emoji-item-common emoji-${type} emoji-size-small"></span>`;
      },
      handleCancle() {
        this.value = '';
        this.count = 1024;
        this.isShowEmojiPanel = false
        if(this.toInfo) {
          this.$emit("cancel-box", this.toInfo.commentUid)
        }
        this.hideEmojiPanel()
      },
      showEmojiPanel() {
        this.isShowEmojiPanel = !this.isShowEmojiPanel;
      },
      hideEmojiPanel() {
        this.isShowEmojiPanel = false
      },
      appendEmoji(text) {
        // const el = document.getElementById("textpanel");
        this.value = this.value + ":" + text + ":";
      }
    },
  };
</script>


<style lang="less" scoped>
  @import "../../../assets/css/emoji.css";
  .comment{
    display: flex;
    flex-direction: column;
    box-sizing: border-box;
    padding-right: 10px;
    .commentBox{
      width: 100%;
      height: 80px;

    }
  textarea::-webkit-input-placeholder {
    padding: 10px;
    box-sizing: border-box;
    color: #909399;
  }
  .commentBox  textarea {
    border-radius: 5px;
    padding: 15px;
    box-sizing: border-box;
    outline: none;
    color: #606266;
    resize: none;
    width: 100%;
    height: 100%;
  }
  .bottom{
    margin-top: 10px;
    display: flex;
    flex-direction: row-reverse;
    align-items: center;
    .submitButton{
      background-color: #44d7b6;
      border: none;
      color: white;
      box-sizing: border-box;
      padding: 7px 12px;
      border-radius: 5px;
      font-size: 13px;
      cursor: pointer;
    }
    .allowFont{
      font-size: 14px;
      color: #909399;
    }
  }
}



  .emoji-panel-btn img{
    height: 20px;
    margin:0 5px;
    width: 20px;
  }
  .emoji-panel-btn:hover {
    cursor: pointer;
    opacity: 0.8;
  }
  .emoji-item-common {
    background: url("../../../assets/img/emoji_sprite.png");
    display: inline-block;
  }
  .emoji-item-common:hover {
     cursor: pointer;
   }
  .emoji-size-small {
    // 表情大小
    zoom: 0.3;
  }
  .emoji-size-large {
    zoom: 0.5; // emojipanel表情大小
    margin: 4px;
  }
</style>
