<!-- BInd reosurce -->
<template>
  <div class="main">
    <div v-if="resourceType == 'file'">
      <file-upload @returnFileUrl="returnFileUrl"></file-upload>
    </div>

    <div v-if="resourceType == 'shared_file'">
      <el-input v-model="value"></el-input>
      Please enter the url in data container!
    </div>

    <div v-if="resourceType == 'parameter'">
      <el-input v-model="value"></el-input>
      Please enter the url in data container!
    </div>

    <div class="submitBtn">
      <el-button @click="submitBtn">Submit</el-button>
    </div>
  </div>
</template>

<script>
import fileUpload from './FileUpload';
import { mapState } from 'vuex';

export default {
  components: { fileUpload },

  watch: {
    resourceType: {
      handler(val) {
        this.type == val;
      },
      // 深度观察监听
      deep: true,
      immediate: true
    }
  },

  props: {
    resourceType: {
      type: String
    }
  },

  computed: {
    ...mapState({
      role: state => state.permission.role
    })
  },

  data() {
    return {
      activeName: 'file',
      value: ''
    };
  },

  methods: {
    returnFileUrl(value) {
      this.value = value.address;
    },

    handleClick() {
      this.value = '';
    },
    submitBtn() {
      this.$emit('returnResourceUrl', this.value);
    }
    //get all the file
  },

  mounted() {}
};
</script>
<style lang="scss" scoped>
.main {
  min-height: 200px;
}
</style>
