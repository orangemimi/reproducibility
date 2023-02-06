<!--  -->
<template>
  <div class="main">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="Name">
        <el-input v-model="form.name" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="Description">
        <el-input v-model="form.description" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="Token">
        <el-input v-model="form.token" size="mini"></el-input>
      </el-form-item>
      <el-form-item label="Dependency List">
        <data-service-table :currentTable="form.dependencyItemList" :currentType="'dependency'" @addItem="addItem"></data-service-table>
      </el-form-item>
      <el-form-item label="Input List">
        <data-service-table :currentTable="form.inputItemList" :currentType="'input'" @addItem="addItem"></data-service-table>
      </el-form-item>
      <el-form-item label="Parameter List">
        <data-service-table :currentTable="form.parameteItemrList" :currentType="'parameter'" @addItem="addItem"></data-service-table>
      </el-form-item>
      <el-form-item label="Output List">
        <data-service-table :currentTable="form.outputItemList" :currentType="'output'" @addItem="addItem"></data-service-table>
      </el-form-item>
      <el-form-item label="Type">
        <el-select class="code-mode-select" v-model="mode" @change="changeMode">
          <el-option v-for="mode in modes" :key="mode.value" :label="mode.label" :value="mode.value"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="Code">
        <code-mirror :mode="mode" @codeInput="codeInput" />
      </el-form-item>
    </el-form>
    <div class="footer">
      <el-button @click="submit" class="btn">Submit</el-button>
    </div>
    <!-- <pre v-highlight><code>const s = new Date().toString()</code></pre> -->
    <!-- <> -->
  </div>
</template>

<script>
import codeMirror from '_com/CodeMirror/CodeMirror';
import dataServiceTable from '_com/MxGraphDialogs/DataServiceCodeTable';
import { postDataServiceCode } from '@/api/request';
export default {
  components: { codeMirror, dataServiceTable },

  computed: {},

  data() {
    return {
      // 默认的语法类型
      mode: 'Python',
      // 支持切换的语法高亮类型，对应 JS 已经提前引入
      // 使用的是 MIME-TYPE ，不过作为前缀的 text/ 在后面指定时写死了
      modes: [
        {
          value: 'clike',
          label: 'C'
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
      ],
      currentCode: '',
      form: {
        name: '',
        description: '',
        token: '',
        dependencyItemList: [],
        inputItemList: [],
        outputItemList: [],
        parameteItemrList: [],
        code: ''
      }
    };
  },

  methods: {
    changeMode() {},
    async submit() {
      console.log(this.form);
      await postDataServiceCode(this.form);
    },
    codeInput(val) {
      this.currentCode = val;
      this.form.code = val;
    },
    addItem(val) {
      let item = {
        name: '',
        description: '',
        type: ''
      };
      if (val == 'dependency') {
        this.form.dependencyItemList.push(item);
      }
      if (val == 'input') {
        this.form.inputItemList.push(item);
      }
      if (val == 'parameter') {
        this.form.parameteItemrList.push(item);
      }
      if (val == 'output') {
        this.form.outputItemList.push(item);
      }
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  .el-form-item {
    margin-bottom: 10px;
  }
}
</style>
