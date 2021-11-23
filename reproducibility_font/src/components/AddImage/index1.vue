<template>
  <div>
    <el-upload action="#" list-type="picture-card" :auto-upload="false" ref="upload" :multiple="true" :on-change="change">
      <template #default>
        <i class="el-icon-plus"></i>
      </template>
      <template #file="{ file }">
        <div>
          <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
          <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
              <i class="el-icon-zoom-in"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
              <i class="el-icon-delete"></i>
            </span>
          </span>
        </div>
      </template>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" :modal="false" style="width: 100%">
      <img width="100%" :src="dialogImageUrl" alt="" />
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      fileList: [],
    };
  },
  methods: {
    change(file, fileList) {
      this.$emit("scenarioUser", fileList)
    },

    handleRemove(file) {
      this.$refs.upload.$children[0].files.forEach((item, index) => {
        if (file.uid == item.uid) {
          this.$refs.upload.$children[0].files.splice(index, 1);
        }
      });
      this.$emit("scenarioUser", this.$refs.upload.$children[0].files)
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
  },
};
</script>

<style lang="scss" scoped>
</style>