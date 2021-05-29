<!-- result content -->
<template>
  <div>
    <el-button type="success" size="mini" @click="addNode">Add</el-button>
    <vue-tree-list @delete-node="onDel" :model="data" default-tree-node-name="new node" default-leaf-node-name="new leaf" v-bind:default-expanded="false">
      <template v-slot:leafNameDisplay="slotProps">
        <div class="name">{{ slotProps.model.name }}</div>
        <div class="value">{{ slotProps.model.value }}</div>
      </template>
    </vue-tree-list>
  </div>
</template>

<script>
import { VueTreeList, Tree, TreeNode } from '_com/TreeDescription';
export default {
  components: {
    VueTreeList
  },

  watch: {},

  computed: {},

  data() {
    return {
      data: new Tree([
        {
          name: 'Entity',
          value: '',
          id: 0,
          pid: 0
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
      ])
    };
  },

  methods: {
    //tree

    addNode() {
      var node = new TreeNode({ name: 'new node', isLeaf: false });
      if (!this.data.children) this.data.children = [];
      this.data.addChildren(node);
    },
    onDel(node) {
      node.remove();
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped></style>
