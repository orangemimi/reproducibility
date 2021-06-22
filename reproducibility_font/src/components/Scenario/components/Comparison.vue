<template>
  <div class="main_content">
    <el-col :span="22" :offset="1">
      <!-- <el-col :span="12"> -->
      {{ taskInfoInit }}
      <!-- <tinymce ref="editor" v-model="taskInfoInit.content" @saveNote="saveNote" :height="600" /> -->

      <!-- </el-col> -->
      <!-- <el-col :span="12">
        <div class="preview">
          <span>Preview</span>
          <div class="content" v-html="taskInfoInit.content" :height="600" />
        </div>
      </el-col> -->
      <!-- <Button type="success" @click="createTool('toolInfo')" class="create">Create</Button> -->
    </el-col>
  </div>
</template>
<script>
// import { uploadResourcePicture } from '@/api/request';
// import tinymce from '_com/Tinymce/Tinymce';
export default {
  props: {
    taskInfoInit: {
      type: Object
    }
  },

  // components: {
  //   tinymce
  // },

  data() {
    return {
      selectModelRoute: this.modelRoute,
      selectModel: this.modelItem,
      selectModelName: '',
      selectModelDes: '',
      selectModelUrl: '',
      toolInfo: {
        toolImg: '',
        content: ''
      },
      userId: 'testUserId',
      inputToolTag: '',
      visible: false,
      //表示图片
      image: ''
    };
  },

  methods: {
    handleView() {
      this.visible = true;
    },
    handleRemove() {
      this.image = '';
      this.toolInfo.toolImg = '';
    },
    uploadPhoto(e) {
      // 利用fileReader对象获取file
      var file = e.target.files[0];
      var filesize = file.size;
      // 2,621,440   2M
      if (filesize > 21014400) {
        // 图片大于2MB,gif动图较大
        this.$Message.error('size > 20MB');
      } else {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = async e => {
          // 读取到的图片base64 数据编码 将此编码字符串传给后台即可
          let formData = new FormData();
          formData.append('pictureFile', file);

          // let fileName = await uploadResourcePicture(formData);

          // this.toolInfo.toolImg = url;
          // this.image = e.target.result;
          // this.$('#choosePicture').val('');
          this.axios
            .post('/resources/picture', formData)
            .then(res => {
              if (res.data != 'Fail') {
                let url = `http://${window.location.host}/r/${this.userId}/projectPicture/${res.data}`;
                console.log(url);
                this.toolInfo.toolImg = url;
                // this.selectedTool.toolImg = res.data;
                this.image = e.target.result;
                // eslint-disable-next-line no-undef
                // $('#choosePicture').val('');
              } else {
                this.$Message.error('upload picture Fail!');
              }
            })
            .catch();
        };
      }
    },
    saveNote(value) {
      console.log(value);
    }
  },
  updated() {
    // eslint-disable-next-line no-undef
    $('.content')
      .find('code')
      .css('background-color', 'rgb(187, 76, 76)');
  }
};
</script>
<style lang="scss" scoped>
.main_content {
  // /deep/ pre {
  //   background-color: #f5f2f0;
  //   padding: 1em;
  //   margin: 0.5em 0;
  //   overflow: auto;
  //   width: calc(34vw);
  // }
  // /deep/ code {
  //   font-family: Consolas, Monaco, 'Andale Mono', 'Ubuntu Mono', monospace; //coding family
  //   direction: ltr;
  //   text-align: left;
  //   line-height: 1.5;
  //   font-size: 14px;
  // }
  // .preview {
  //   border: 0 solid #c5c5c5;
  //   background-color: #fff;
  //   border-width: 1px;
  //   min-height: 737px;
  //   margin: 0 2px;
  //   // width: 100%;
  // }
}
</style>
