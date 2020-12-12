<!--  -->
<template>
  <div>
    <div>Create project</div>
    <div>
      <el-form ref="form" :model="form" label-width="80px" size="small">
        <el-form-item label="Name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Privacy">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Tag">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Image">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Introduction">
          <el-input v-model="form.name" />
        </el-form-item>
      </el-form>
    </div>
    <div><el-button>Create</el-button></div>
  </div>
</template>

<script>
export default {
  components: {},

  watch: {},

  computed: {},

  data() {
    return {};
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped></style>
