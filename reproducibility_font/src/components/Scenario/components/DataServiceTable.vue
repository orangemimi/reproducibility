<!--  -->
<template>
  <div class="main">
    <el-table :data="currentTable" border style="width: 100%">
      <template slot="empty">
        <el-button size="mini" @click="addItemInList">Add a description</el-button>
      </template>
      <el-table-column prop="name" label="Name" width="180">
        <template #default="scope"><el-input v-model="scope.row.name" size="mini" /></template>
      </el-table-column>

      <el-table-column prop="description" label="Url" width="180" v-if="currentType == 'dependency'">
        <template #default="scope"><el-input v-model="scope.row.url" size="mini" /></template>
      </el-table-column>

      <el-table-column prop="description" label="Description" width="180" v-if="currentType != 'dependency'">
        <template #default="scope"><el-input v-model="scope.row.description" size="mini" /></template>
      </el-table-column>
      <el-table-column prop="type" label="Type" v-if="currentType != 'dependency'">
        <template #default="scope"><el-input v-model="scope.row.type" size="mini" /></template>
      </el-table-column>
      <el-table-column label="Action">
        <el-button size="mini">Add</el-button>
        <el-button size="mini">Deleate</el-button>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    currentTable: {
      type: Array
    },
    currentType: {
      type: String
    }
  },
  components: {},

  watch: {},

  computed: {},

  data() {
    return {};
  },

  methods: {
    addItemInList() {
      this.$emit('addItem', this.currentType);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  .el-table {
    /deep/ th {
      padding: 0;
    }
  }
}
</style>
