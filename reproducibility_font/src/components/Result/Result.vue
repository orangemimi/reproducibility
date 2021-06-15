<!--  -->
<template>
  <div>
    <el-table :data="tableData" style="width: 100%;margin-bottom: 20px;" row-key="id" border default-expand-all :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
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
          name: 'Entity',
          value: '',
          id: 0,
          pid: 0,
          children: [
            {
              name: 'Result Item',
              value: '',
              id: 4,
              pid: 10,
              children: [
                { name: 'Dimension', value: '', id: 4, pid: 10 },
                { name: 'Form', value: '', id: 4, pid: 10 }
              ]
            }
          ]
        },
        {
          name: 'Agent',
          value: '',
          id: 1,
          pid: 0
        },
        {
          name: 'Activity',
          value: '',
          id: 3,
          pid: 0
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
