package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.pic.Pic;
import com.mao.entity.data.pic.PicClass;
import com.mao.entity.data.pic.PicParam;
import com.mao.entity.data.pic.PicSrc;
import com.mao.mapper.data.PicMapper;
import com.mao.service.BaseService;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 图片数据处理
 * create by mao at 2020/4/17 0:02
 */
public class PicService extends BaseService {

    /**
     * 小图片地址前缀
     */
    private static final String S_IMAGE_PRE = "/file/image/pic/";

    /**
     * 大图片地址前缀
     */
    private static final String IMAGE_PRE = "/file/image/pic_big/";

    /**
     * 获取图片分类选择数据
     */
    public void getPicClass(RoutingContext ctx){
        SqlSession session = MybatisConfig.getSession();
        PicMapper mapper = session.getMapper(PicMapper.class);
        List<PicClass> picClass = mapper.getPicClass();
        session.close();
        sendData(ctx,picClass);
    }

    /**
     * 图片列表的查询
     * 分选择分页查询 和 搜索查询
     * 选择分页查询参数必须
     */
    public void getPics(RoutingContext ctx){
        PicParam picParam = bodyParam(ctx,PicParam.class);
        if (null == picParam)
            sendError(ctx,"invalid body param");
        else {
            if (SU.isEmpty(picParam.getName())){
                if (SU.isNotZs(picParam.getPid()) || SU.isNotZs(picParam.getSid()))
                    sendError(ctx,"param is necessary");
                else {
                    SqlSession session = MybatisConfig.getSession();
                    PicMapper mapper = session.getMapper(PicMapper.class);
                    Integer page = picParam.getPage();
                    transPage(picParam);
                    List<Pic> pics = mapper.getPics(picParam);
                    formatPicImage(pics);
                    Long totalPage = mapper.getPicsTotalPage(picParam);
                    PageData<Pic> data = pageData(pics,page,totalPage,picParam);
                    session.close();
                    sendData(ctx,data);
                }
            } else {
                SqlSession session = MybatisConfig.getSession();
                PicMapper mapper = session.getMapper(PicMapper.class);
                List<Pic> pics = mapper.searchPics(picParam);
                session.close();
                sendData(ctx,pics);
            }
        }
    }

    /**
     * 图片路径的补全
     */
    private void formatPicImage(List<Pic> pics){
        pics.forEach(pic -> pic.setS_image(S_IMAGE_PRE + pic.getPrl() + "/" + pic.getSrl() + "/" + pic.getS_image()));
    }

    /**
     * 图片路径的补全
     */
    private void formatPicImage(PicSrc picSrc){
        if (null != picSrc) {
            picSrc.setS_image(S_IMAGE_PRE + picSrc.getPrl() + "/" + picSrc.getSrl() + "/" + picSrc.getS_image());
            picSrc.setImage(IMAGE_PRE + picSrc.getPrl() + "/" + picSrc.getSrl() + "/" + picSrc.getImage());
        }
    }

    /**
     * 图片详情的查询
     */
    public void getPic(RoutingContext ctx){
        Long id = pathLong(ctx,"id");
        if (null == id)
            sendError(ctx,"invalid param id");
        else {
            SqlSession session = MybatisConfig.getSession();
            PicMapper mapper = session.getMapper(PicMapper.class);
            PicSrc src = mapper.getPicSrc(id);
            session.close();
            formatPicImage(src);
            sendData(ctx,src);
        }
    }

}
