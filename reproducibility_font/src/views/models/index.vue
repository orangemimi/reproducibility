<template>
  <div class="container">
    <div class="top">
      <div class="top-container">
        <div class="backgroud">
          <picture class="bg-img">
            <img src="@/assets/images/orange1.jpg" />
          </picture>
        </div>
        <el-row class="top-info">
          <el-row class="top-title">
            Models
          </el-row>
          <el-row class="top-desc">
            Various, useful Models.
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
            Trending searches: Geodynamics,Geostatics,Hydrology,Coastal Vulnerability,Urban Noise
          </el-row>
          <el-row>
            <el-button plain class="add-btn" @click="addModelDialogShow = true">
              Add your model service ➔
            </el-button>
          </el-row>
        </el-row>
      </div>
    </div>
    <div class="main">
      <div class="main-container">
        <transition-group name="list" tag="p">
          <ServiceCard :item="item" type="model" v-for="item in data" :key="item.id"></ServiceCard>
        </transition-group>
      </div>
    </div>

    <!-- add model -->
    <el-dialog
      title="Add model in Reproducibilty"
      :visible.sync="addModelDialogShow"
      width="40%"
      :close-on-click-modal="false"
    >
      <create-model></create-model>
    </el-dialog>
  </div>
</template>

<script>
import { get } from '@/axios';
import ServiceCard from '_com/common/ServiceCard';
import createModel from './create';
export default {
  data() {
    return {
      data: [],
      is_extending: true,
      value: '',
      dataFilter: {
        pageSize: 8,
        page: 0
      },
      addModelDialogShow: false
    };
  },
  methods: {
    searchData() {
      this.dataFilter.page = 0;
      this.getData(false);
    },
    async getData(extend = true) {
      //   try {
      // let { content } = await get(
      //   '/models',
      //   {
      //     page: this.dataFilter.page,
      //     pageSize: this.dataFilter.pageSize,
      //     asc: true,
      //     value: this.value
      //   },
      //   null,
      //   false
      // );

      let content = await get('/modelItems');
      console.log(content);
      if (extend) {
        if (content.length == 0) {
          this.is_extending = false;
          return;
        }
        this.data = this.data.concat(content);
      } else {
        this.data = content;
      }
      //     this.is_extending = true;
      //   } catch (error) {
      //     this.$throw(error);
      //   }
    },

    extendData() {
      this.dataFilter.page++;
      this.getData();
    },

    scrollDown() {
      let scroll = document.documentElement.scrollTop || document.body.scrollTop;
      if (scroll + window.innerHeight + 20 >= document.documentElement.offsetHeight) {
        if (this.is_extending) {
          this.is_extending = false;
          this.extendData();
        }
      }
    },
    addModelItem() {}
  },
  mounted() {
    this.getData();
    window.addEventListener('scroll', this.scrollDown);
  },
  components: { ServiceCard, createModel }
};
</script>

<style lang="scss">
.container {
  .top {
    // background-color: #000;
    color: #fff;
    //   margin-bottom: 48px;
    .top-container {
      position: relative;
      .bg-img {
        position: relative;
        img {
          position: relative;
          object-fit: cover;
          //obackground-size: cover;
          width: 100%;
          height: calc(100vh - 60px);
          filter: brightness(0.8);
        }
      }
    }

    .top-info {
      position: absolute;
      left: 50%;
      top: 45%;
      width: 45%;
      transform: translate3d(-50%, -50%, 0);
      font-size: 1.4rem;
      font-family: Arial, Georgia, Times, 'Times New Roman', serif;
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
      .el-button {
        background: rgba($color: #fff, $alpha: 0.5);
        color: #fff;
        font-weight: 700;
        font-size: 16px;
        height: 50px;
        margin-top: 10px;
        //   border: 1px solid rgba($color: #fff, $alpha: 0.5);
      }
      .el-button:hover {
        border: 1.5px solid $selectOrangeBorderColor;
        color: $selectOrangeBorderColor;
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
}
</style>
