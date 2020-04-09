package com.mao.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * jackson JSON处理
 * @author zongx at 2020/4/7 21:59
 */
public class JsonUtil {

    /**
     * jackson转换mapper
     * 网上说该mapper是线程安全的。可以使用单例模式
     */
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    /**
     * 将json字符串（已知是一个对象类型的json数据）转化成集合对象
     * 而且对象中没有泛型
     * @param json json字符串
     * @param t T 对象类型
     * @param <T> T 泛型
     * @return T 类型对象
     */
    public static <T> T json2obj(String json, Class<T> t)  {
        try {
            return mapper.readValue(json,t);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 将json字符串（已知是一个对象类型的json数据）转化成集合对象
     * @param json json字符串
     * @param t T 对象类型
     * @param ts T 对象中含有的其它泛型
     * @param <T> T 泛型
     * @return T 类型对象
     */
    public static <T> T json2objobj(String json, Class<T> t, Class<?>... ts) {
        try {
            return mapper.readValue(json,getJavaType(t,ts));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 将json字符串（已知是一个集合类型的json数据）转化成集合对象
     * 指定转化为Set数组
     * @param json Json字符串
     * @param t T 对象类型
     * @param ts T 对象中其他的泛型
     * @param <T> T
     * @return Set<T>
     */
    public static <T> Set<T> json2setObj(String json, Class<T> t, Class<?>... ts) {
        try {
            return mapper.readValue(json,getJavaType(Set.class,getClasses(t,ts)));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 将json字符串（已知是一个集合类型的json数据）转化成集合对象
     * 指定转化为List数组
     * @param json Json字符串
     * @param t T 对象类型
     * @param ts T 对象中其他的泛型
     * @param <T> T
     * @return List<T>
     */
    public static <T> List<T> json2listObj(String json, Class<T> t, Class<?>... ts) {
        try {
            return mapper.readValue(json,getJavaType(List.class,getClasses(t,ts)));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 提取方法：获取转化时需要用到的JavaType对象
     * 当需要转成的对象中有泛型时，需要组合JavaType以告诉jackson我需要转化为哪一种对象
     * 如json是一个集合类型数据（List<T>），那么我需要声明集合中的对象是什么。就是声明这个src参数
     * 如果这个src对象里面还有泛型，那也需要指定它是哪种类型的对象（有可能多个）
     * 因此jackson提供了一个class类型数组，我们只需要根据泛型的顺序依次指定即可。
     * 至于jackson对多个泛型是如何一对一指定的就不深究了。
     * @param src 需要当作参数返回的class类型
     * @param ts 上面class对象中存在的其他class类型
     * @return JavaType
     */
    private static JavaType getJavaType(Class<?> src, Class<?>... ts){
        return mapper.getTypeFactory().constructParametricType(src,ts);
    }

    /**
     * 提取方法：将多个泛型合并成一个数组，但主参数化的对象需要放置在第一个
     * 上面的使用泛型<T>是为了可以返回<T>，这样可以减少使用者转化后还要强转。
     * 合并数组使用到了System.arraycopy()
     * 关于此方法的用法是：
     * System.arraycopy(
     *          src,        源数组，需要从那个数组中复制数据
     *          srcPos,     源数组起始复制地点。如0 表示 从src[0]开始复制
     *          dest,       目标数组。表示需要将数据复制到这个数组中
     *          destPos,    目标数组复制起始位置，表示复制的数据从这里开始放起。
     *          length      从源数组中复制数据的长度。这个数组需要 <= src.length
     *  )
     * @param t 需要当作参数返回的class类型
     * @param ts 上面class对象中存在的其他class类型
     * @return 组合后的class数组
     */
    private static Class<?>[] getClasses(Class<?> t, Class<?>... ts){
        Class<?>[] _t = new Class[ts.length+1];
        _t[0] = t;
        System.arraycopy(ts,0,_t,1,ts.length);
        return _t;
    }

    /**
     * json转map
     * 需指定map的key、value类型
     * @param json json数据
     * @param k map的key类型
     * @param v map的value类型
     * @param <K> k 对象类型
     * @param <V> v 对象类型
     * @return Map <K,V>
     */
    public static <K, V> Map<K, V> json2map(String json, Class<K> k, Class<V> v){
        try {
            return mapper.readValue(json,getJavaType(Map.class,k,v));
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 使用jackson将对象转json
     * 对象转json相对比较简单。不区分Object是单个对象还是数组、map
     * @param object 需要转化的对象
     * @return JSON字符串
     */
    public static String obj2json(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static void main(String[] args) {

    }

}
