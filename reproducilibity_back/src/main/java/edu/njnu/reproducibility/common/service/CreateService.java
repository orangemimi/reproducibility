package edu.njnu.reproducibility.common.service;

/**
 * @InterfaceName CreateService
 * @Description 创建实体Service
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface CreateService<E, AD>{
    /**
     * 创建实体
     *
     * @param addDTO 实体数据
     */
    E add(AD addDTO);
}
