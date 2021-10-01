<!--  -->
<template>
  <div class="mainContainer">
    <!-- <el-table :data="tableData" row-key="name" border height="380">
      <el-table-column type="expand">
        <template #default="scope">
          <el-form label-position="right" label-width="100px" v-show="scope.row.name == 'Result Item'">
            <div v-for="(item, index) in formResult" :key="index">
              <el-form-item :label="item.name">
                <el-input v-model="formResultValue[item.name]"></el-input>
              </el-form-item>
            </div>
          </el-form>

          <el-form label-position="right" label-width="100px" v-show="scope.row.name == 'Comparison'">
            <div v-for="(item, index) in formComparison" :key="index">
              <el-form-item :label="item.name">
                <el-input v-model="formComparisonValue[item.name]"></el-input>
              </el-form-item>
            </div>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="Key" sortable></el-table-column> -->
    <!-- <el-table-column prop="value" label="Value" sortable></el-table-column> -->

    <!-- <el-table-column label="Action">
        <el-button size="mini" @click="editResultDialogShow = true">Edit</el-button>
        <el-button size="mini" @click="addItem">Add Item</el-button>
      </el-table-column>
    </el-table> -->

    <vue-scroll style="height: 400px;">
      <el-form label-position="right" label-width="100px">
        <div v-for="(item, index) in formResult" :key="index">
          <el-form-item :label="item.name">
            <el-input v-model="formResultValue[item.name]"></el-input>
          </el-form-item>
        </div>
      </el-form>
    </vue-scroll>

    <!-- upload data -->
    <div class="dialogs">
      <el-dialog title="Edit result" :visible.sync="editResultDialogShow" width="40%" :close-on-click-modal="false" :append-to-body="true">
        <!-- <pre>{{ JSON.stringify(tableData, null, 4) }}</pre> -->
        <!-- <b-code-editor v-model="(JSON.stringify(JSON.parse(tableData),null,2))" :indent-unit="4" height="auto" /> -->

        <vue-json-editor
          v-model="tableData"
          :expand_height="'true'"
          :mode="'code'"
          class="vue-json-editor"
          @json-change="onJsonChange"
          @json-save="onJsonSave"
          style="height: 600px"
        />
        <div style="margin-top: 10px">
          <el-button size="mini" @click="saveResult">Save</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return {
      tableData: [
        {
          name: 'Result Item',
          value: '',
          id: 1,

          // children: [
          //   { name: 'Name', value: '', id: 2 },
          //   { name: 'Description', value: '', id: 3 },
          //   { name: 'Keywords', value: '', id: 4 },
          //   { name: 'Version', value: '', id: 5 },
          //   { name: 'Distributors', value: '', id: 6 },
          //   { name: 'Existing File', value: '', id: 7 },
          //   { name: 'Restriction', value: '', id: 8 },
          //   { name: 'Conclusion', value: '', id: 9 }
          // ]
        },
        {
          name: 'Comparison',
          value: '',
          id: 10,
          // children: [{ name: 'Method', value: '', id: 11 }]
        },
      ],
      formResult: [
        { name: 'Name', value: '', id: 2 },
        { name: 'Description', value: '', id: 3 },
        { name: 'Keywords', value: '', id: 4 },
        { name: 'Version', value: '', id: 5 },
        { name: 'Distributors', value: '', id: 6 },
        { name: 'Existing File', value: '', id: 7 },
        { name: 'Restriction', value: '', id: 8 },
        { name: 'Conclusion', value: '', id: 9 },
      ],
      formComparison: [{ name: 'Method', value: '', id: 11 }],
      formResultValue: {},
      formComparisonValue: {},
      editResultDialogShow: false,
    };
  },

  methods: {
    edit(index, row) {
      console.log(index, row);
    },
    addItem(index, row) {
      console.log(index, row);
    },
    // eslint-disable-next-line no-unused-vars
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 2) {
        return [11, 1];
      }
    },

    onJsonChange() {
      // 数据改变时触发
    },
    onJsonSave() {
      // 点击保存触发
    },
    saveResult() {},
  },

  mounted() {},
};
</script>
//
<style lang="scss" scoped>
// .mainContainer {
//   width: 98%;
//   margin: 16px auto;
//   height: 100%;

//   .dialogs {

//   }
// }
//
</style>

<style lang="less">
.jsoneditor-vue {
  height: 100%;
}
.el-dialog__body {
  padding: 20px;
}
</style>
