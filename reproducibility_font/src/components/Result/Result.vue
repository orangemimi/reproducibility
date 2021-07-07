<!--  -->
<template>
  <div>
    <el-table :data="tableData" :row-style="{ height: '0' }" :cell-style="{ padding: '4px' }" row-key="id" border :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" height="380">
      <el-table-column prop="name" label="Key" sortable width="180"></el-table-column>
      <el-table-column prop="value" label="Value" sortable width="180"></el-table-column>

      <el-table-column label="Action">
        <el-button size="mini" @click="edit(scope.$index, scope.row)">Edit</el-button>
        <el-button size="mini" @click="addItem(scope.$index, scope.row)">Add Item</el-button>
      </el-table-column>
    </el-table>
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
          id: 0,
          pid: 120,
          children: [
            { name: 'Name', value: '', id: 21, pid: 222 }
            // { name: 'Description', value: '', id: 32, pid: 3 },
            // { name: 'Keywords', value: '', id: 42, pid: 4 },
            // { name: 'Version', value: '', id: 52, pid: 5 },
            // { name: 'Distributors', value: '', id: 62, pid: 6 },
            // { name: 'Existing File', value: '', id: 72, pid: 7 },
            // { name: 'Restriction', value: '', id: 28, pid: 8 },
            // { name: 'Conclusion', value: '', id: 92, pid: 9 }
          ]
        },
        {
          name: 'Comparison',
          value: '',
          id: 1,
          pid: 1,
          children: [{ name: 'Method', value: '', id: 10, pid: 10 }]
        }
      ]
    };
  },

  methods: {
    edit(index, row) {
      console.log(index, row);
    },
    addItem(index, row) {
      console.log(index, row);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped></style>
