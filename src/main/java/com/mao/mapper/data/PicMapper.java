package com.mao.mapper.data;

import com.mao.entity.data.pic.Pic;
import com.mao.entity.data.pic.PicClass;
import com.mao.entity.data.pic.PicParam;
import com.mao.entity.data.pic.PicSrc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : create by zongx at 2020/4/20 14:28
 */
public interface PicMapper {

    /**
     * 获取所有图片分类列表
     * @return 图片分类列表
     */
    List<PicClass> getPicClass();

    /**
     * 根据参数获取图片列表
     * @param picParam 参数
     * @return 图片列表
     */
    List<Pic> getPics(PicParam picParam);

    /**
     * 根据参数获取图片总数
     * @param picParam 参数
     * @return 图片总数
     */
    Long getPicsTotalPage(PicParam picParam);

    /**
     * 根据参数查询图片列表
     * @param picParam 参数
     * @return 图片列表
     */
    List<Pic> searchPics(PicParam picParam);

    /**
     * 根据id查询图片详情
     * @param id 图片id
     * @return 图片详情
     */
    PicSrc getPicSrc(@Param("id") Long id);

}
