package com.mao.his.sudo;

import java.util.ArrayList;
import java.util.List;

/**
 * 数独解析
 * 遍历数独
 * 有空值
 * 查找该空值可能性
 * 填上空值
 * 继续填下一空值
 * 可能性为为0，结束
 * 直到填空完毕
 *
 * 经典数独：
 * ╔═════╤═════╤═════╦═════╤═════╤═════╦═════╤═════╤═════╗
 * ║     │     │  5  ║  3  │     │     ║     │     │     ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║  8  │     │     ║     │     │     ║     │  2  │     ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │  7  │     ║     │  1  │     ║  5  │     │     ║
 * ╠═════╪═════╪═════╬═════╪═════╪═════╬═════╪═════╪═════╣
 * ║  4  │     │     ║     │     │  5  ║  3  │     │     ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │  1  │     ║     │  7  │     ║     │     │  6  ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │     │  3  ║  2  │     │     ║     │  8  │     ║
 * ╠═════╪═════╪═════╬═════╪═════╪═════╬═════╪═════╪═════╣
 * ║     │  6  │     ║  5  │     │     ║     │     │  9  ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │     │  4  ║     │     │     ║     │  3  │     ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │     │     ║     │     │  9  ║  7  │     │     ║
 * ╚═════╧═════╧═════╩═════╧═════╧═════╩═════╧═════╧═════╝
 *
 * create by mao at 2020/4/18 10:57
 */
public class SudoKu {

    /**
     * 如果有输入的数独数少，计算结果太多会有可能爆内存，限制成功5种就不再尝试
     */
    private static final int MAX_SUCCESS = 5;
    private final List<int[][]> result;
    private int success;

    public SudoKu(){
        this.result = new ArrayList<>();
        this.success = 0;
    }

    public List<int[][]> getResult(){
        return this.result;
    }

    public void analyse(int[][] arr){
        jx(arr,0,0);
    }

    /**
     * 解析主方法：
     * 如果此坐标[x,y]为0，表示需要填入数据
     *      先将数独克隆一份，使用克隆的数独进行操作（由于是各种可能性同时进行）
     *      获取该坐标可能的数据集合【标】
     *          如果集合为空：数组填到这说明填错了，该填充进程就此放弃
     *          如果集合不为空：说明还可以继续，遍历集合，让所有可能性继续走
     *              如果坐标为最后一位，说明填满了：此进程正确，放入map中
     *              如果坐标还有，继续作填下一个数的操作（递归）
     * 如果此坐标[x,y]不为0，表示这是数独中原有数据
     *      如果没到最后一个坐标，继续作填下一个数的操作（递归）
     *      如果到了最后一个坐标，那此进程正确，放入map
     *
     * 方法主要靠获取该坐标的可能数据集合进行判别
     * 而不需要在每次填入一个数据后，都要判断整个数独一行、一列、一宫有没有重复数字
     * 可以细细思量。
     * @param src 数独
     * @param x x横
     * @param y y竖
     */
    private void jx(int[][] src, int x, int y){
        if (success < MAX_SUCCESS)
            if (src[x][y] == 0){
                int[][] copy = copy(src);
                int[] probability = getProbability(copy, x, y);
                if (probability != null && probability.length > 0)
                    for (int i : probability) {
                        copy[x][y] = i;
                        int[] position = nextPosition(x, y);
                        if (null == position) {
                            result.add(copy);
                            success ++;
                        } else
                            jx(copy,position[0],position[1]);
                    }
            } else {
                int[] position = nextPosition(x, y);
                if (null == position) {
                    result.add(src);
                    success ++;
                } else
                    jx(src,position[0],position[1]);
            }
    }

    /**
     * 获取下一坐标
     * 8是最大值，以8作判断
     * @param x x横
     * @param y y竖
     * @return [x,y]
     */
    private int[] nextPosition(int x, int y){
        if (x == 8)
            if (y == 8) return null;
            else return new int[]{x,y+1};
        else
            if (y == 8) return new int[]{x+1,0};
            else return new int[]{x,y+1};
    }

    /**
     * 获取该坐标可以放置的值
     * 获取一横有的数
     * 获取一竖有的数
     * 获取一宫有的数
     * 统计3个数组获取1-9没有出现的数
     * @param src 源数独
     * @param x x横
     * @param y y竖
     * @return 可能值
     */
    private int[] getProbability(int[][] src, int x, int y){
        int[][] count = {{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},{8,0},{9,0}};
        for (int i = 0; i < 9; i++)
            if (src[i][y] > 0)
                count[src[i][y] - 1][1]++;
        for (int j = 0; j < 9; j++)
            if (src[x][j] > 0)
                count[src[x][j] - 1][1]++;
        for (int i : getGongRange(x))
            for (int j : getGongRange(y))
                if (src[i][j] > 0)
                    count[src[i][j] - 1][1]++;
        int[] pre = new int[9];
        int c = 0;
        for (int[] i : count)
            if (i[1] == 0){
                pre[c] = i[0];
                c++;
            }
        if (c == 0) return null;
        int[] res = new int[c];
        System.arraycopy(pre, 0, res, 0, c);
        return res;
    }

    /**
     * 获取该数值所在宫的范围
     * 如：i=1  -> [0,1,2]
     */
    private int[] getGongRange(int i){
        return i < 3 ? new int[]{0,1,2} : (i < 6 ? new int[]{3,4,5} : new int[]{6,7,8});
    }

    /**
     * 克隆
     */
    private int[][] copy(int[][] src){
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(src[i], 0, copy[i], 0, 9);
        return copy;
    }

}
