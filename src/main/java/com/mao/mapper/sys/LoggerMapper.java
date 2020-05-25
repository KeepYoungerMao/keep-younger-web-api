package com.mao.mapper.sys;

import com.mao.entity.log.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * xml中使用foreach标签保存列表，如果参数没有使用@Param的话，
 * 则foreach标签中的collection值为“list”（对于传入的集合是List的情况下），
 * 如果使用@Param设置了名称的话则使用设置的名称。
 * 其它参数如：item表示集合迭代时每一个元素迭代时的名称，
 * index表示迭代时此时迭代的位置的名称
 * open表示该语句以什么开始，通常使用该参数写 “(”
 * close表示该语句以什么结束，通常使用该参数写 “)”
 * separator表示每一条语句使用什么来隔开，通常使用该参数写 “,”
 * @see #saveLog(List) 是一个保存列表数据的例子
 * @author : create by zongx at 2020/5/25 13:57
 */
public interface LoggerMapper {

    /**
     * 保存日志列表数据
     * @param logs 日志列表
     */
    void saveLog(@Param("logs") List<Log> logs);

}
