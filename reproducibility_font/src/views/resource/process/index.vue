<template>
  <div>
    <div class="top">
      <div class="top-container">
        <div class="backgroud">
          <picture class="bg-img">
            <img src="../../../assets/images/bg_data_process.jpg" />
          </picture>
        </div>
        <el-row class="top-info">
          <el-row class="top-title">
            Data Process Methods
          </el-row>
          <el-row class="top-desc">
            Various, useful Data Process Methods.
          </el-row>
          <el-row class="top-desc">
            Collected by generous community of OpenGMS Team. 🎁
          </el-row>
          <el-row class="input-container">
            <el-input
              @keyup.enter.native="searchData"
              v-model="value"
              placeholder=""
              prefix-icon="el-icon-search"
            ></el-input>
          </el-row>
          <el-row class="search-note">
            Trending searches: GDAL,SAGA,Geotools,Custom.
          </el-row>
        </el-row>
      </div>
    </div>
    <div class="main">
      <div class="main-container">
        <transition-group name="list" tag="p">
          <ServiceCard
            :item="item"
            type="process"
            v-for="item in data"
            :key="item.id"
          ></ServiceCard>
        </transition-group>
      </div>
    </div>
  </div>
</template>

<script>
import { get } from "@/axios";
import ServiceCard from "_com/common/ServiceCard";
export default {
  data() {
    return {
      data: [],
      is_extending: true,
      value: "",
      dataFilter: {
        pageSize: 8,
        page: 0
      }
    };
  },
  methods: {
    searchData() {
      this.dataFilter.page = 0;
      this.getData(false);
    },
    async getData(extend = true) {
      try {
        let { content } = await get(
          "/data_process_service",
          {
            page: this.dataFilter.page,
            pageSize: this.dataFilter.pageSize,
            asc: true,
            value: this.value
          },
          null,
          false
        );
        if (extend) {
          if (content.length == 0) {
            this.is_extending = false;
            return;
          }
          this.data = this.data.concat(content);
        } else {
          this.data = content;
        }
        this.is_extending = true;
      } catch (error) {
        this.$throw(error);
      }
    },
    extendData() {
      this.dataFilter.page++;
      this.getData();
    },
    scrollDown() {
      let scroll =
        document.documentElement.scrollTop || document.body.scrollTop;
      if (
        scroll + window.innerHeight + 20 >=
        document.documentElement.offsetHeight
      ) {
        if (this.is_extending) {
          this.is_extending = false;
          this.extendData();
        }
      }
    }
  },
  mounted() {
    this.getData();
    window.addEventListener("scroll", this.scrollDown);
  },
  components: { ServiceCard }
};
</script>

<style lang="scss">
.top {
  // background-color: #000;
  color: #fff;
  margin-bottom: 48px;
  .top-container {
    position: relative;
    .bg-img {
      position: relative;
      img {
        position: relative;
        object-fit: cover;
        //obackground-size: cover;
        width: 100%;
        height: 550px;
        filter: brightness(0.8);
      }
    }
  }

  .top-info {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 45%;
    transform: translate3d(-50%, -50%, 0);
    font-size: 1.4rem;
    font-family: Arial, Georgia, Times, "Times New Roman", serif;
    .el-row {
      margin: 0.5rem 0;
    }
    .top-title {
      font-size: 3.5rem;
    }
    .input-container {
      .el-input {
        .el-input__inner {
          height: 50px;
        }
      }
    }
  }
}

.main {
  position: relative;
}
.main-container {
  p {
    position: relative;
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    overflow: hidden;
  }
}

.list-enter-active,
.list-leave-active {
  transition: all 1s;
}
.list-enter,
.list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}
</style>
