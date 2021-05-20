<!--  -->
<template>
  <div>
    <el-form ref="form" :model="childForm" label-width="80px">
      <el-form-item label="Key">
        <el-input v-model="childForm.name"></el-input>
      </el-form-item>
      <el-form-item label="Value">
        <el-input v-model="childForm.value"></el-input>
      </el-form-item>
    </el-form>
    <el-button @click="submit">Submit</el-button>
  </div>
</template>

<script>
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return {
      childForm: {
        name: '',
        value: ''
      }
    };
  },

  methods: {
    submit() {
      this.$emit('getChildForm', this.childForm);
    }
  },

  mounted() {}
};
</script>
<style lang="scss" scoped></style>
