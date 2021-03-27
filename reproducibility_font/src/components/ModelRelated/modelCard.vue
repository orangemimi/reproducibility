<!-- card -->
<template>
  <div class>
    <el-card class="cardBody">
      <div>
        <div class="card_avatar">
          <el-tooltip placement="bottom" max-width="600">
            <avatar
              :username="item.toolName"
              :size="30"
              style="margin-bottom: 6px"
              :rounded="false"
              v-if="item.toolImg == '' || item.toolImg == undefined"
            ></avatar>
            <div class="toolImgOuter" v-else>
              <img :src="item.toolImg" class="toolImg" />
            </div>
            <div slot="content">
              <div style="text-align: center">{{ item.description }}</div>
              <br v-if="item.categoryTag.length > 0" />
              <span>
                <i>{{ item.categoryTag.join('|') }}</i>
              </span>
            </div>
          </el-tooltip>
        </div>
        <div class="card_info">
          <h4 :title="item.toolName" class="card_info_name">
            {{ item.toolName }}
          </h4>
        </div>
      </div>
    </el-card>
    <!-- 
    <div v-show="openTool" title="Tool preview" width="800" class="mask">
      <i class="el-icon-close maskBtn" title="Close the tool information"></i>

      <tool-preview :selectTool="item"></tool-preview>
    </div> -->
  </div>
</template>

<script>
import Avatar from 'vue-avatar';
// import toolPreview from '@/components/common/tools/toolPreview';
export default {
  props: {
    toolFrom: {
      type: Object
    },
    isOpenTool: {
      type: Boolean,
      default: false
    }
  },

  watch: {
    toolFrom: {
      handler(val) {
        this.item = val;
      },
      deep: true
    }
  },

  components: {
    Avatar
    //   toolPreview
  },
  data() {
    return {
      item: this.toolFrom,
      isOpen: this.isOpenTool,
      openTool: false,
      toolPreview
    };
  },

  methods: {}
};
</script>
<style lang="scss" scoped>
.cardBody {
  background-color: ghostwhite;
  cursor: pointer;
  height: 50px;
  // width: 250px;

  .toolImgOuter {
    height: 30px;
    width: 30px;
    .toolImg {
      width: auto;
      height: auto;
      max-width: 100%;
      max-height: 100%;
    }
  }
  .card_avatar {
    width: 30px;
    margin-right: 5px;
    float: left;
  }
  .card_info {
    width: 130px;
    .card_info_name {
      display: block;
      width: 90px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
  >>> .el-card__body {
    padding: 8px 5px;
  }
}

/* 遮盖罩 */
.mask {
  position: fixed;
  /* 最顶层 */
  z-index: 1;
  left: 0px;
  top: 0px;
  right: 0px;
  bottom: 0px;
  background-color: rgba(247, 241, 241, 0.938);
  padding: 3% 10% 0 10%;
  overflow-y: scroll;
  /* overflow-x:hidden; */

  .maskBtn {
    position: absolute;
    z-index: 2;
    right: 10%;
    font-size: 30px;
  }

  .maskBtn:hover {
    cursor: pointer;
  }
}
</style>
