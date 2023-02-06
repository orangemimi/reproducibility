<template>
  <div class="ZYM">
    <keep-alive>
      <editor v-model="content" :init="init" :disabled="disabled" ref="editor"></editor>
    </keep-alive>
  </div>
</template>

<script>
import tinymce from 'tinymce/tinymce.js';
import Editor from '@tinymce/tinymce-vue';
// import 'tinymce/skins/ui/oxide/skin.css'
import 'tinymce/themes/silver';

// import 'tinymce/icons/default/icons'
// import 'tinymce/themes/silver';
// import 'tinymce/themes/mobile/theme'
import 'tinymce/plugins/image';
import 'tinymce/plugins/media';
import 'tinymce/plugins/table';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/contextmenu';
import 'tinymce/plugins/wordcount';
import 'tinymce/plugins/colorpicker';
import 'tinymce/plugins/textcolor';
import 'tinymce/plugins/preview';
import 'tinymce/plugins/code';
import 'tinymce/plugins/link';
import 'tinymce/plugins/advlist';
import 'tinymce/plugins/codesample';
import 'tinymce/plugins/hr';
import 'tinymce/plugins/fullscreen';
import 'tinymce/plugins/textpattern';
import 'tinymce/plugins/searchreplace';
import 'tinymce/plugins/autolink';
import 'tinymce/plugins/directionality';
import 'tinymce/plugins/visualblocks';
import 'tinymce/plugins/visualchars';
import 'tinymce/plugins/template';
import 'tinymce/plugins/charmap';
import 'tinymce/plugins/nonbreaking';
import 'tinymce/plugins/insertdatetime';
import 'tinymce/plugins/imagetools';
import 'tinymce/plugins/autosave';
import 'tinymce/plugins/autoresize';
// 扩展插件
// import '../assets/tinymce/plugins/lineheight/plugin';
// import '../assets/tinymce/plugins/bdmap/plugin';

export default {
  components: {
    Editor
  },
  props: {
    value: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    plugins: {
      type: [String, Array],
      default:
        // 'preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media template code codesample table charmap hr nonbreaking insertdatetime advlist lists wordcount imagetools textpattern autosave bdmap autoresize lineheight',
        'preview searchreplace autolink directionality visualblocks visualchars fullscreen image link media template code codesample table charmap hr nonbreaking insertdatetime advlist lists wordcount imagetools textpattern autosave autoresize'
    },
    toolbar: {
      type: [String, Array],
      default:
        'code undo redo restoredraft | cut copy paste pastetext | forecolor backcolor bold italic underline strikethrough link codesample | alignleft aligncenter alignright alignjustify outdent indent lineheight formatpainter | \
    styleselect formatselect fontselect fontsizeselect | bullist numlist | blockquote subscript superscript removeformat | \
    table image media charmap hr pagebreak insertdatetime | bdmap fullscreen preview'
    }
  },
  data() {
    return {
      //初始化配置
      init: {
        language_url: '/tinymce/langs/zh_CN.js',
        language: 'zh_CN',
        skin_url: '/tinymce/skins/ui/oxide',
        height: 770,

        min_height: 770,
        max_height: 770,
        toolbar_mode: 'wrap',
        plugins: this.plugins,
        toolbar: this.toolbar,
        content_style: 'p {margin: 5px 0;}',
        fontsize_formats: '12px 14px 16px 18px 24px 36px 48px 56px 72px',
        font_formats:
          '微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;',
        branding: false
        //此处为图片上传处理函数，这个直接用了base64的图片形式上传图片，
        //如需ajax上传可参考https://www.tiny.cloud/docs/configure/file-image-upload/#images_upload_handler
        // images_upload_handler: (blobInfo, success, failure) => {
        //   const img = 'data:image/jpeg;base64,' + blobInfo.base64();
        //   success(img);
        // },
      },
      content: this.value,
      tinymceFlag: 1
    };
  },
  beforeDestroy() {
    console.log('beforeDestroy');
  },

  mounted() {
    console.log(this.value);
    console.log(this.disabled);
    tinymce.init({});
  },
  activated() {
    this.tinymceFlag++;
  },
  methods: {
    click() {
      console.log(this.$refs.editor);
    }
  },
  watch: {
    value(newValue) {
      this.content = newValue;
    },
    content(newValue) {
      this.$emit('input', newValue);
    }
  }
};
</script>
<style scoped lang="scss"></style>
