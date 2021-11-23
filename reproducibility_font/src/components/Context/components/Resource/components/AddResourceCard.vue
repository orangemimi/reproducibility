<template>
  <div>
    <el-tabs tab-position="left" style="height: 100%">
      <el-tab-pane label="Input">
        <el-form ref="form" :model="form" label-width="100px">
          <el-form-item label="Name">
            <el-input v-model="form.name" placeholder="Please enter the name"></el-input>
          </el-form-item>
          <el-form-item label="Event Name">
            <el-input v-model="form.eventName" placeholder="Please enter the event name"></el-input>
          </el-form-item>

          <el-form-item label="Description">
            <el-input type="textarea" v-model="form.description" resize="none" :rows="3" placeholder="Please enter the description"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit('input')">Creat</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="Parameter">
        <el-form ref="form" :model="form" label-width="100px">
          <el-form-item label="Name">
            <el-input v-model="form.name" placeholder="Please enter the name"></el-input>
          </el-form-item>
          <el-form-item label="Event Name">
            <el-input v-model="form.eventName" placeholder="Please enter the event name"></el-input>
          </el-form-item>
          <el-form-item label="Data Type">
            <el-radio-group v-model="form.type">
              <el-radio label="STRING">STRING</el-radio>
              <el-radio label="INT">INT</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="value">
            <el-input v-model="form.value" placeholder="Please enter the value"></el-input>
          </el-form-item>
          <el-form-item label="Description">
            <el-input type="textarea" v-model="form.description" resize="none" :rows="3" placeholder="Please enter the description"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit('parameter')">Creat</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="Output">
        <el-form ref="form" :model="form" label-width="100px">
          <el-form-item label="Name">
            <el-input v-model="form.name" placeholder="Please enter the name"></el-input>
          </el-form-item>
          <el-form-item label="Event Name">
            <el-input v-model="form.eventName" placeholder="Please enter the event name"></el-input>
          </el-form-item>

          <el-form-item label="Data Type">
            <el-radio-group v-model="form.type">
              <el-radio label="FILE">FILE</el-radio>
              <el-radio label="STRING">STRING</el-radio>
              <el-radio label="INT">INT</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="Description">
            <el-input type="textarea" v-model="form.description" resize="none" :rows="3" placeholder="Please enter the description"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit('output')">Creat</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { addResourceCard } from '@/api/request';
export default {
  data() {
    return {
      projectId: this.$route.params.id,
      form: {
        name: '',
        eventName: '',
        type: '',
        value: '',
        description: '',
      },
    };
  },
  methods: {
    async onSubmit(type) {
      // let jsonData = this.form 在vue中，这是一个引用传递而不是值传递，dataA和dataB指向的是同一个内存地址
      let jsonData = JSON.parse(JSON.stringify(this.form)); //先转string在转对象可以解决这种值联动问题
      let form = {
        info: jsonData,
        resourceType: type,
        projectId: this.projectId,
      };

      let data = await addResourceCard(form);
      console.log(data);
    },
  },
};
</script>

<style lang="scss" scoped>
</style>