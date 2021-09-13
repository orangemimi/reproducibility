
<template>
  <div>
    <el-button class="button" type="text" @click="click">Edit</el-button>
    <el-dialog :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false">
      <el-form>
        <el-form-item v-for="(value, key) in input" :key="key" :label="key" label-width="120">
          <div v-if="key != 'email'">
            <el-input v-model="input[key]" :id="key"></el-input>
          </div>
        </el-form-item>
        <el-form-item class="btn">
          <el-button type="primary" @click="submit">Submit</el-button>
          <el-button @click="reset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { updateUserByJwtUserId } from '@/api/request';
export default {
  data() {
    return {
      dialogVisible: false,
      input: {},
    };
  },
  props: ['user', 'form'],
  
  methods: {
    reset() {
      let flag = 0;
      let id = '';
      for (let temp in this.input) {
        if (flag == 0) {
          id = temp;
          flag = 1;
        }
        this.input[temp] = '';
      }
      document.getElementById(id).focus();
    },

    async submit() {
      try {
        await updateUserByJwtUserId(this.input);
        this.$message({
          type: 'success',
          message: 'User information modification successfully!',
        });
        for (let temp in this.input) {
          localStorage[temp] = this.input[temp]
        }
        this.dialogVisible = false
        location.reload()
      } catch {
        this.$message({
          type: 'error',
          message: 'User information modification error!',
        });
      }
    },

    click() {
      this.dialogVisible = true;
      this.input = this.form
      for (let key in this.input) {
        if(this.user[key] != undefined) {
          this.input[key] = this.user[key]
        }
      }
      delete this.input.email
      console.log(this.input)
      console.log(this.form)
    },
  },
};
</script>

<style scoped lang="scss">
.btn {
  text-align: center;
}
</style>
