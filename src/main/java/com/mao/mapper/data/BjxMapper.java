package com.mao.mapper.data;

import com.mao.entity.data.bjx.Bjx;
import com.mao.entity.data.bjx.BjxParam;
import com.mao.entity.data.bjx.BjxSrc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 百家姓数据处理
 * @author : create by zongx at 2020/4/16 11:09
 */
@Mapper
public interface BjxMapper {

    /**
     * 根据过滤参数获取百家姓列表
     * @param bjxParam 过滤参数
     * @return 百家姓列表
     */
    List<Bjx> getBjx(BjxParam bjxParam);

    /**
     * 根据查询参数获取百家姓列表
     * @param bjxParam 查询参数
     * @return 百家姓列表
     */
    List<Bjx> searchBjx(BjxParam bjxParam);

    /**
     * 根据过滤参数获取百家姓列表
     * @param bjxParam 过滤参数
     * @return 百家姓列表
     */
    Long getBjxTotalPage(BjxParam bjxParam);

    /**
     * 根据id擦汗寻百家姓详情
     * @param id id
     * @return 百家姓详情
     */
    BjxSrc getBjxSrc(@Param("id") long id);

}
