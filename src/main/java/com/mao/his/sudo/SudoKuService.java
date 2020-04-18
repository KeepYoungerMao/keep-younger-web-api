package com.mao.his.sudo;

import com.mao.entity.response.Response;
import com.mao.util.JsonUtil;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * 数独
 * create by mao at 2020/4/18 22:03
 */
public class SudoKuService {

    /**
     * 数独解析
     * 二维数组不好转化，每一行转字符串后传递：
     * [
     *      "025000300",
     *      "000030609"
     *      ...
     * ]
     */
    public static void sudoKu(RoutingContext ctx){
        List<String> list = JsonUtil.json2listObj(ctx.getBodyAsString(), String.class);
        int[][] sudoKu = transToSudoKu(list);
        if (null == sudoKu)
            ctx.response().end(Response.error("error to format this sudoKu"));
        else {
            SudoKu method = new SudoKu();
            method.analyse(sudoKu);
            List<int[][]> result = method.getResult();
            ctx.response().end(list.isEmpty() ? Response.error("cannot get result") : Response.ok(transResult(result)));
        }
    }

    private static String[][] transResult(List<int[][]> sudoKu){
        String[][] result = new String[sudoKu.size()][9];
        for (int i = 0; i < sudoKu.size(); i++)
            for (int j = 0; j < 9; j++)
                result[i][j] = join(sudoKu.get(i)[j]);
        return result;
    }

    private static String join(int[] arr){
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 9; i++){
            res.append(arr[i]);
            if (i != 8)
                res.append(",");
        }
        return res.toString();
    }

    /**
     * 转换为数独结构：9*9的二维数组
     * 如果数据格式不正确，返回null
     */
    public static int[][] transToSudoKu(List<String> list){
        if (null == list || list.size() != 9)
            return null;
        int[][] sudoKu = new int[9][9];
        for (int i = 0; i < 9; i++) {
            char[] chars = list.get(i).toCharArray();
            if (chars.length != 9)
                return null;
            for (int j = 0; j < 9; j++)
                if (chars[j] < '0' || chars[j] > '9')
                    return null;
                else sudoKu[i][j] = chars[j] - '0';
        }
        return sudoKu;
    }

}
