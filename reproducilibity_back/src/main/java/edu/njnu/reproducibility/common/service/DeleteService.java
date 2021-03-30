package edu.njnu.reproducibility.common.service;

/**
 * @InterfaceName DeleteService
 * @Description 删除实体Service
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface DeleteService {
    /**
     * 根据实体id删除实体
     *
     * @param id 实体id
     */
    void delete(String id);
}
