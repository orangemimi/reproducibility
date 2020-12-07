<template>
  <div class="workflow">
    <VueBlocksContainer
      ref="container"
      :blocksContent="blocks"
      :scene.sync="scene"
      @blockSelect="selectBlock"
      @blockDeselect="deselectBlock"
      @dblclick.native="seeDetail"
      class="container"
    />
  </div>
</template>

<script>
import VueBlocksContainer from "_com/vue-blocks/components/observe/VueBlocksContainer";
export default {
  props: ["expectedInstances"],
  components: {
    VueBlocksContainer
  },
  data: function() {
    return {
      blocks: [],
      scene: {
        blocks: []
      },
      selectedBlock: null
    };
  },
  watch: {
    expectedInstances() {
      this.init();
    }
  },
  methods: {
    seeDetail() {
      let { id, instanceEnum, serviceId } = this.selectedBlock;
      if (instanceEnum === "PROCESS") {
        this.$router.push({
          path: `/resource/${serviceId}/process/invoke?instanceId=${id}`
        });
      } else {
        this.$router.push({
          path: `/resource/${serviceId}/model/invoke?instanceId=${id}`
        });
      }
    },
    async init() {
      let getExpectedInstances = this.expectedInstances;
      this.blocks = [];
      let realBlocks = [];
      let realLinks = [];
      getExpectedInstances.forEach(({ instanceEnum, service, id, name }) => {
        let block = {
          instanceEnum,
          name: name
        };
        let fields = [];
        service.behavior.inputs.forEach(input => {
          fields.push({
            name: input.name,
            attr: "input"
          });
        });
        service.behavior.outputs.forEach(output => {
          fields.push({
            name: output.name,
            attr: "output"
          });
        });
        block.fields = fields;
        this.blocks.push(block);

        realBlocks.push({
          id: id,
          x: -594,
          y: 43,
          name: name,
          instanceEnum,
          serviceId: service.id
        });
      });

      //遍历每个实例的输出与非自身实例的输入是否存在数据引用关系，存在则生成Link
      getExpectedInstances.forEach(instanceSource => {
        let originID = instanceSource.id;
        instanceSource.service.behavior.outputs.forEach(
          (output, indexOrigin) => {
            let originSlot = indexOrigin;
            let withOutSelf = getExpectedInstances.filter(instanceTarget => {
              return originID != instanceTarget.id;
            });

            withOutSelf.forEach(instanceTarget => {
              let targetID = instanceTarget.id;
              instanceTarget.service.behavior.inputs.forEach(
                (input, indexTarget) => {
                  if (input.dataServiceId == output.dataServiceId) {
                    let targetSlot = indexTarget;
                    realLinks.push({
                      originID,
                      originSlot,
                      targetID,
                      targetSlot
                    });
                  }
                }
              );
            });
          }
        );
      });

      this.scene = {
        blocks: realBlocks,
        links: realLinks,
        container: { centerX: 1042, centerY: 140, scale: 1 }
      };
    },
    selectBlock(block) {
      this.selectedBlock = block;
    },
    deselectBlock() {
      this.selectedBlock = null;
    }
  }
};
</script>

<style lang="scss">
.workflow {
  width: calc(100% - 40px);
  height: 500px;
  padding: 20px 0 0 20px;
}

.container {
  width: 100%;
  height: calc(100% - 50px);
  margin-top: 20px;
  background-color: #f8f8f9;
}
</style>
