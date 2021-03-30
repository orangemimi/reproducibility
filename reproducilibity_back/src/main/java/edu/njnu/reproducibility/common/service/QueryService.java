package edu.njnu.reproducibility.common.service;

import org.springframework.data.domain.Page;

/**
 * @InterfaceName QueryService
 * @Description 查询实体Service
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
public interface QueryService<E, VO, FD> {
    /**
     * 获取所有实体（分页）
     *
     * @param findDTO 过滤条件
     *
     * @return
     */
    Page<VO> list(FD findDTO);

    /**
     * 根据id 查询实体
     *
     * @param  id 实体id
     *
     * @return
     */
    E get(String id);

    /**
     * 返回实体的总个数
     *
     * @return
     */
    long count();
}
