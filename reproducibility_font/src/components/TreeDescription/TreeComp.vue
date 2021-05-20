<template>
  <div class="main">
    <el-button @click="addChildDialogShow = true" size="mini">Add</el-button>
    <vue-tree-list
      @click="onClick"
      @change-name="onChangeName"
      @delete-node="onDel"
      @add-node="onAddNode"
      @drop="drop"
      @drop-before="dropBefore"
      @drop-after="dropAfter"
      :model="data"
      default-tree-node-name="new node"
      default-leaf-node-name="new leaf"
      v-bind:default-expanded="false"
    >
      <template v-slot:leafNameDisplay="slotProps">
        <div class="name">{{ slotProps.model.name }}</div>
        <div class="value">{{ slotProps.model.value }}</div>
      </template>
    </vue-tree-list>
    <el-dialog :visible.sync="addChildDialogShow" width="50%" title="Configuration">
      <add-child-value @getChildForm="getChildForm" />
    </el-dialog>
  </div>
</template>
<script>
import { VueTreeList, Tree, TreeNode } from '../TreeDescription';
import addChildValue from './addChildValue';
export default {
  components: {
    VueTreeList,
    addChildValue
  },
  data() {
    return {
      newTree: {},
      data: new Tree([
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
      ]),
      addChildDialogShow: false
    };
  },
  methods: {
    onDel(node) {
      // eslint-disable-next-line no-console
      console.log(node);
      node.remove();
    },

    onChangeName(params) {
      // eslint-disable-next-line no-console
      console.log(params);
    },

    onAddNode(params) {
      // eslint-disable-next-line no-console
      console.log(params);
    },

    onClick(params) {
      // eslint-disable-next-line no-console
      console.log(params);
    },

    drop: function({ node, src, target }) {
      // eslint-disable-next-line no-console
      console.log('drop', node, src, target);
    },

    dropBefore: function({ node, src, target }) {
      // eslint-disable-next-line no-console
      console.log('drop-before', node, src, target);
    },

    dropAfter: function({ node, src, target }) {
      // eslint-disable-next-line no-console
      console.log('drop-after', node, src, target);
    },

    addNode() {
      var node = new TreeNode({ name: 'new node', isLeaf: false });
      if (!this.data.children) this.data.children = [];
      this.data.addChildren(node);
    },

    getNewTree() {
      var vm = this;
      function _dfs(oldNode) {
        var newNode = {};

        for (var k in oldNode) {
          if (k !== 'children' && k !== 'parent') {
            newNode[k] = oldNode[k];
          }
        }

        if (oldNode.children && oldNode.children.length > 0) {
          newNode.children = [];
          for (var i = 0, len = oldNode.children.length; i < len; i++) {
            newNode.children.push(_dfs(oldNode.children[i]));
          }
        }
        return newNode;
      }

      vm.newTree = _dfs(vm.data);
    }
  }
};
</script>
<style lang="scss" scoped>
.main {
  .name {
    width: 100px;
    float: left;
  }
  .value {
    // width: 100px;
    float: left;
  }
}
</style>
