import axios from 'axios';
import sf from 'string-format';
import { Loading } from 'element-ui';
import { ResError } from '@/lib/error/ResError';
import store from '@/store';
import router from '@/router';
import config from '@/config';

const baseRequestUrl = config.apiURL;

const axiosInstance = axios.create({
  baseURL: baseRequestUrl,
  timeout: 200000
  // withCredentials: true
});

axiosInstance.interceptors.request.use(
  config => {
    if (config.loading == true) {
      Loading.service();
    }
    config.headers.Authorization = localStorage.getItem('token') || null;
    return config;
  },
  error => {
    //前台捕获的错误
    if (error.config.loading == true) {
      Loading.service().close();
    }
    return Promise.reject(error);
  }
);
axiosInstance.interceptors.response.use(
  res => {
    if (res.config.loading == true) {
      Loading.service().close();
    }

    //身份异常
    if (
      res.data.code == -4 || //NO_TNO_TOKENOKEN
      res.data.code == -5 || //TOKEN_WRONG
      res.data.code == -6 // USER_PASSWORD_NOT_MATCH
    ) {
      store.dispatch('user/handleLogOut');
      router.push({
        name: 'Login'
      });
    }
    //EXIST_OBJECT
    if (res.data.code == -3) {
      throw new ResError(res.data.msg);
    }
    // 状态码小于0属于异常情况
    if (res.data.code < 0) {
      //由弹窗出来，是为了给用户看
      throw new ResError(res.data.msg);
    }
    return res.data.data;
  },
  error => {
    //后台未捕获的错误
    if (error.config.loading == true) {
      Loading.service().close();
    }
    throw new ResError('请求服务器失败，请检查服务是否正常！');
  }
);

export const get = (url, params, pathVariable = null, loading = false) => {
  return axiosInstance.get(sf(url, pathVariable), {
    params: params,
    loading
  });
};

export const postFile = (url, data, pathVariable = null, loading = false) =>
  axiosInstance.post(sf(url, pathVariable), data, {
    loading,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });

export const post = (url, data, pathVariable = null, loading = false) =>
  axiosInstance.post(sf(url, pathVariable), data, {
    loading
  });

export const put = (url, data, pathVariable = null, loading = false) =>
  axiosInstance.put(sf(url, pathVariable), data, {
    loading
  });

export const patch = (url, data, pathVariable = null, loading = false) =>
  axiosInstance.patch(sf(url, pathVariable), data, {
    loading
  });

export const del = (url, params, data, pathVariable = null, loading = false) =>
  axiosInstance.delete(sf(url, pathVariable), {
    params: params,
    data: data,
    loading
  });
