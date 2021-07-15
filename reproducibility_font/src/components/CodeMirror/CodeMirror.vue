<template>
  <div class="in-coder-panel">
    <div style="width:100%">
      <textarea ref="textarea"></textarea>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">

// https://github.com/zhangmhui/vue-CodeMirror

import _CodeMirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/cobalt.css'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/mode/xml/xml.js'
import 'codemirror/mode/clike/clike.js'
import 'codemirror/mode/markdown/markdown.js'
import 'codemirror/mode/python/python.js'
import 'codemirror/mode/r/r.js'
import 'codemirror/mode/shell/shell.js'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/mode/swift/swift.js'
import 'codemirror/mode/vue/vue.js'

// 尝试获取全局实例
const CodeMirror = window.CodeMirror || _CodeMirror

export default {
  name: 'in-coder',
  props: {
    value: String,
    language: {
      type: String,
      default: null
    },

    mode:{
        type:String
    }

  },
  data() {
    return {
      code: '',
      coder: null,
      options: {
        // 缩进格式
        tabSize: 2,
        // 主题，对应主题库 JS 需要提前引入
        theme: 'cobalt',
        // 显示行号
        lineNumbers: true,
        line: true,
        autoRefresh: true,
        matchBrackets: true,    //括号匹配
      },
       modes: [
        {
          value: 'clike',
          label: 'C语言'
        },
        {
          value: 'html',
          label: 'XML/HTML'
        },
        {
          value: 'x-java',
          label: 'Java'
        },
        {
          value: 'x-python',
          label: 'Python'
        },
        {
          value: 'x-sql',
          label: 'SQL'
        }
      ]

    }
  },
  mounted() {
    // 初始化
    this._initialize()
  },
  methods: {
    // 初始化
    _initialize() {
      this.coder = CodeMirror.fromTextArea(this.$refs.textarea, this.options)
      this.coder.setValue(this.value || this.code)

      this.coder.on('change', (coder) => {
        this.code = coder.getValue();


        if (this.$emit) {
          this.$emit('codeInput', this.code)
        }
      })

      if (this.language) {
        let modeObj = this._getLanguage(this.language)

        if (modeObj) {
          this.mode = modeObj.label
        }
        // this.changeMode( this.mode);
      }
    },
    // 获取当前语法类型
    _getLanguage(language) {
      return this.modes.find((mode) => {
        let currentLanguage = language.toLowerCase()
        let currentLabel = mode.label.toLowerCase()
        let currentValue = mode.value.toLowerCase()

        return currentLabel === currentLanguage || currentValue === currentLanguage
      })

    },

  }
}
</script>

<style scoped>
.in-coder-panel {
  flex-grow: 1;
  display: flex;
  position: relative;
  width: 100%;
  height: 100%;
}

.CodeMirror {
  flex-grow: 1;
  z-index: 1;
}

.CodeMirror-code {
  line-height: 19px;
}
</style>
