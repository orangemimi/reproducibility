const path = require('path');

const resolve = dir => {
  return path.join(__dirname, dir);
};

const BASE_URL = '/';

module.exports = {
  publicPath: BASE_URL,
  css: {
    loaderOptions: {
      // 向 CSS 相关的 loader 传递选项
      less: {
        // javascriptEnabled: true
      },
      sass: {
        additionalData: `@import 'src/assets/variable.scss';`
      }
    }
  },
  chainWebpack: config => {
    config.resolve.alias.set('@', resolve('src')).set('_com', resolve('src/components'));

    config.plugin('webpack-bundle-analyzer').use(require('webpack-bundle-analyzer').BundleAnalyzerPlugin);
  },

  devServer: {
    host: '0.0.0.0',
    port: 8080,
    proxy: {
      '/r': {
        target: 'http://127.0.0.1:8081/',
        ws: true,
        changeOrigin: true
        // pathRewrite: {
        //   "^/api": ""
        // }
      }
      // '/api': {
      //   target: 'http://api.map.baidu.com',
      //   ws: true,
      //   changeOrigin: true,
      //   pathRewrite: {
      //     "^/api": ""
      //   }
      // }
    }
  }
};
