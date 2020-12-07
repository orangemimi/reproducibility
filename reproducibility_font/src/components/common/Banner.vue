<template>
  <div class="banner">
    <h1 class="title">{{ text }}</h1>
  </div>
</template>

<script>
export default {
  props: {
    text: String
  }
};
</script>

<style lang="scss" scoped>
.banner {
  font-family: Arial, Georgia, sans-serif, serif;
  .title {
    font-size: 2.5rem;
    font-weight: 400;
    line-height: 2.5rem;
    padding: 1.5rem 2rem;
    background-color: #00bbd8;
    color: #fff;
  }
}
</style>
