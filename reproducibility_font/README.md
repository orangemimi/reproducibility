# r2

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Run your tests
```
npm run test
```

### Lints and fixes files
```
npm run lint
```

### PORT设置

- socketServer 7999
- geoserver 8080
- container 8081
- r2 back 8082
- r2 front 8083


### 说明

- 1.目前的数据服务下载是直接挂靠的容器的下载接口，并没有从复现研究平台进行转发
- 2.服务的枚举 注意数据处理服务的 简称是PROCESS

## 模拟验证服务

获取数据的参考模板

```javascript
var dataUrl="http://localhost:8081/data_service/fetch/5eb12f37a6e62808b4922837";
(async () => {
        try {
            let res = await fetch(dataUrl);
            let text = await res.text();
        } catch (e) {
            console.log(e);
        }
})();
```