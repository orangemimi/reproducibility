package edu.njnu.reproducibility.common.untils;

import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName JavaBeanCollectionUtils
 * @Description 对具有setter、getter的Java Bean集合进行操作
 * @Author sun_liber
 * @Date 2019/7/9
 * @Version 1.0.0
 */
public class JavaBeanCollectionUtils {
    /**
     * demo: Set<String> strings = JavaBeanCollectionUtils.fetchProperty(books, Book::getName);
     * 作用：获取所有的Name字段
     * @param collection
     * @param mappingFunction 即R的某个字段的getter函数
     * @param <R> 返回类型
     * @param <T> 指定的javaBean
     * @return 以R为返回类型的Set
     */
    public static <R,T> Set<R> fetchProperty(final Collection<T> collection, Function<T, R> mappingFunction) {
        return org.springframework.util.CollectionUtils.isEmpty(collection) ?
                Collections.emptySet() :
                collection.stream().map(mappingFunction).collect(Collectors.toSet());
    }


    /**
     * deomo: Map<String, Book> stringBookMap = JavaBeanCollectionUtils.convertToMap(books, Book::getName);
     * 作用： 根据Name 获取 Book
     * @param collection
     * @param mappingFunction T获取R的setter函数
     * @param <R>
     * @param <T> 指定的javaBean
     * @return 以R为key，以T为value的Map对象
     */
    public static <R, T> Map<R, T> convertToMap(Collection<T> collection, Function<T, R> mappingFunction) {
        Assert.notNull(mappingFunction, "mapping function must not be null");

        if (org.springframework.util.CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }

        Map<R, T> resultMap = new HashMap<>();

        collection.forEach(data -> resultMap.putIfAbsent(mappingFunction.apply(data), data));

        return resultMap;
    }

    /**
     * Example： Map<String, String> stringStringMap = JavaBeanCollectionUtils.convertToMap(books, Book::getId, Book::getName);
     * 作用： 构建 id：name的键值对
     * @param collection
     * @param keyFunction T获取R的getter函数
     * @param valueFunction T获取V的getter函数
     * @param <T> 指定的javaBean
     * @param <R>
     * @param <V>
     * @return 以R为key，以V为value的Map集合
     */
    public static <T, R, V> Map<R, V> convertToMap(Collection<T> collection, Function<T, R> keyFunction, Function<T, V> valueFunction) {
        Assert.notNull(keyFunction, "mapping function must not be null");

        if (org.springframework.util.CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }

        Map<R, V> resultMap = new HashMap<>();

        collection.forEach(data -> resultMap.putIfAbsent(keyFunction.apply(data), valueFunction.apply(data)));

        return resultMap;
    }
}
