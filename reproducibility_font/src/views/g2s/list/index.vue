<template>
  <div class="listContent">
    <el-divider content-position="left">
      <h1>
        Reproducible Projects
      </h1>
    </el-divider>

    <div style=" display: flex; flex-direction: column;">
      <el-button
        style="width:150px;align-self:flex-end"
        @click="showCreate()"
        type="success"
        ><i class="el-icon-circle-plus-outline"></i>Create project</el-button
      >
    </div>

    <div>
      <el-card v-for="g2s in g2sList" :key="g2s.id">
        <h1>{{ g2s.name }}</h1>
        <p>{{ g2s.description }}</p>
        <p>{{ g2s.creator }}</p>
        <el-button @click="operation(g2s.id)" v-show="g2s.creator == user.name"
          >operation</el-button
        >
        <el-button @click="view(g2s.id)" v-if="g2s.isPublish">view</el-button>
      </el-card>
    </div>

    <el-dialog
      title="Create Geographic Simulation"
      :visible.sync="createVisible"
    >
      <el-form :model="g2s">
        <el-form-item label="Name">
          <el-input v-model="g2s.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Description">
          <el-input
            type="textarea"
            :rows="2"
            v-model="g2s.description"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="Background">
          <el-input
            type="textarea"
            :rows="2"
            v-model="g2s.background"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="Goals">
          <el-input
            type="textarea"
            :rows="2"
            v-model="g2s.goals"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="create()">Create</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { get, post } from "@/axios";
import { mapState } from "vuex";
export default {
  components: {},
  data() {
    return {
      createVisible: false,
      g2s: {},
      g2sList: []
    };
  },
  computed: {
    ...mapState(["user"])
  },
  methods: {
    operation(id) {
      this.$router.push({ path: `/g2s/${id}` });
    },
    view(id) {
      this.$router.push({ path: `/g2s/${id}/view` });
    },
    showCreate() {
      if (this.user.name == undefined) {
        this.$router.push({
          name: "login",
          query: {
            redirect: this.$route.fullPath
          }
        });
      }
      this.createVisible = true;
    },
    async create() {
      this.g2s.creator = this.user.name;
      let { id } = await post("/g2s", this.g2s);
      this.operation(id);
    }
  },
  async mounted() {
    //TODO 这里后面要做分页 目前就返回了10个
    let { content } = await get("/g2s");
    this.g2sList = content;
  }
};
</script>

<style scoped>
h1 {
  color: #17233d;
}

.listContent {
  margin-left: 5%;
  margin-top: 5%;
  margin-right: 5%;
}

.btnCreate {
  font-size: 15px;
  height: 40px;
  margin: 10px;
}
.btnCreate:hover {
  background-color: #19be6b;
  color: white;
}
</style>
