package edu.njnu.reproducibility.common.service;

/**
 * @InterfaceName UpdateService
 * @Description 更新实体Service
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface UpdateService<E, UD> {
    /**
     * 更新实体信息
     *
     * @param id        实体id
     * @param updateDTO 实体信息
     */
    E update(String id, UD updateDTO);
}
