<!-- user card creator member different -->
<template>
  <div class="user">
    <div v-if="user.role == 'builder'" class="builder ">
      {{ user.name }}
    </div>
    <div v-else class="member ">
      {{ user.name }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    user: {
      type: Object
    }
  },

  computed: {},

  data() {
    return {};
  },

  methods: {},

  mounted() {}
};
</script>
<style lang="scss" scoped>
.user {
  height: 50px;
  line-height: 50px;
  width: 280px;
  padding: 0 15px;
  margin-bottom: 5px;
  border: 1px solid $btnBorder;
  vertical-align: middle;
  font-family: inherit;

  .builder {
  }
  .member {
  }
}
.user:hover {
  cursor: pointer;
}
</style>
