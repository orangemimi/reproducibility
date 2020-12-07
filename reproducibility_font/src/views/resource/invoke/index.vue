<template>
  <div
    class="main"
    v-loading="loading"
  >
    <div class="slider scrollbar">
      <div class="image">
        <img
          :src="imgPath(service.snapshot, service.name)"
          v-if="service.name != undefined"
        />
      </div>

      <h2 class="title">{{ service.name }}</h2>
      <h3 class="desc">简介</h3>
      <p>{{ service.description }}</p>
      <el-row>
        <el-button
          plain
          round
          type="primary"
          @click="invoke"
        >Invoke</el-button>
      </el-row>
    </div>

    <div class="content">
      <transition name="slide-fade">
        <config
          :service="service"
          :disabled="disabled"
          v-if="service.behavior != null"
        ></config>
      </transition>
    </div>
  </div>
</template>

<script>
import config from "./components/config";
import { get, post } from "@/axios";
import { imgBase64 } from "@/lib/utils";
export default {
  data() {
    return {
      id: this.$route.params.id,
      type: this.$route.params.type,
      service: {},
      timer: null,
      loading: false,
      disabled: false
    };
  },
  watch: {
    $route() {
      location.reload();
    }
  },
  methods: {
    async invoke() {
      this.loading = true;
      //instance里面不需要这个字段
      this.service.details = null;
      let instance = await post(
        "/instance/invoke",
        {
          name: this.service.name + "_" + new Date().toLocaleString(),
          statusEnum: "READY",
          instanceEnum: this.type === "model" ? "MODEL" : "PROCESS",
          service: this.service,
          creator: this.$store.state.user.name
        },
        null,
        false
      );
      let statusEnum = instance.statusEnum;
      let service = instance.service;
      let instanceId = instance.id;
      this.timer = setInterval(async () => {
        if (statusEnum == "ERROR") {
          this.$message.error("模型运行出错");
          this.loading = false;
          clearInterval(this.timer);
        } else if (statusEnum == "FINISH") {
          this.loading = false;
          clearInterval(this.timer);
          this.service = service;
        } else {
          instance = await get(
            "/instance/{id}",
            null,
            {
              id: instanceId
            },
            false
          );
          statusEnum = instance.statusEnum;
          service = instance.service;
        }
      }, 2000);

      console.log(instance);
    },
    imgPath(snapshot, name) {
      if (snapshot != undefined) {
        return snapshot;
      } else {
        return imgBase64(name);
      }
    }
  },
  async mounted() {
    let service;
    let instanceId = this.$route.query.instanceId;
    if (instanceId != undefined) {
      let instance = await get(
        "/instance/{id}",
        null,
        {
          id: instanceId
        },
        false
      );
      service = instance.service;
      this.disabled = true;
    } else {
      if (this.type === "model") {
        service = await get("/model_service/{id}", null, { id: this.id });
      } else {
        service = await get("/data_process_service/{id}", null, {
          id: this.id
        });
      }
      if (service.behavior.parameters != null) {
        service.behavior.parameters.forEach(parameter => {
          if (
            parameter.defaultValue != null ||
            parameter.defaultValue != undefined
          ) {
            parameter.value = parameter.defaultValue;
          }
        });
      }
    }
    this.service = service;
  },
  destroyed() {
    clearInterval(this.timer);
  },
  components: {
    config
  }
};
</script>

<style scoped>
.main {
  display: flex;
  justify-content: center;
}
.main .slider {
  border-right: solid 2px rgb(156, 154, 154);
  min-height: 700px;
  max-height: 700px;
  overflow-y: auto;
  overflow-x: hidden;
  word-wrap: break-word;
  padding: 10px 10px;
}

@media screen and (min-width: 1300px) {
  .main .slider {
    width: 300px;
  }
}

@media screen and (max-width: 1140px) {
  .main .slider {
    width: 20%;
  }
}

@media screen and (max-width: 975px) {
  .main .slider {
    width: 25%;
  }
}

.main .content {
  overflow: hidden;
  width: 83%;
  padding: 10px 10px 10px 30px;
}

.image {
  max-width: 90%;
  max-height: 45%;
  overflow: hidden;
}
.image > img {
  max-width: 100%;
  min-width: 100%;
  overflow: hidden;
}
.slider > p {
  font-size: 16px;
}

.el-button {
  margin: 5px 0px;
}
.main .content > .title {
  font-size: 18px;
  padding: 0px 0px 30px 0px;
}
.slide-fade-enter-active {
  transition: all 0.9s ease 0.7s;
}
.slide-fade-leave-active {
  transition: all 0.7s cubic-bezier(1, 0.5, 0.8, 1);
}
.slide-fade-enter {
  opacity: 0;
  transform: translate(130px, 100px);
}
.slide-fade-leave-to {
  opacity: 0;
  transform: translate(130px, -100px);
}
</style>
