package edu.njnu.reproducibility.common.service;

/**
 * @InterfaceName BaseService
 * @Description todo
 * @Author sun_liber
 * @Date 2019/11/22
 * @Version 1.0.0
 */
public interface BaseService<E, VO, AD, FD, UD > extends
        CreateService<E, AD>,
        QueryService<E, VO, FD>,
        DeleteService,
        UpdateService<E,UD> {
}