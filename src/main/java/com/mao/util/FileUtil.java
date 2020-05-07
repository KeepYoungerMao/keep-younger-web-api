package com.mao.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件操作
 * @author : create by zongx at 2020/4/22 10:12
 */
public class FileUtil {

    public static byte[] readFile(InputStream inputStream){
        if (null == inputStream)
            return null;
        else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(inputStream);
                int size = 1024;
                byte[] buffer = new byte[size];
                int len;
                while(-1 != (len = in.read(buffer, 0, size))) {
                    bos.write(buffer, 0, len);
                }
                return bos.toByteArray();
            } catch (IOException e) {
                return null;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取文件
     * @param path 文件路径
     * @return 二进制数据
     */
    public static byte[] readFile(String path) {
        File file = new File(path);
        if (!file.exists())
            return null;
        else {
            try {
                return readFile(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }

    /**
     * 写入文件
     * 1.存在文件时，文件内容会被清空，写入传入的内容；
     * 2.不存在文件时，会自动新建文件。
     * @param bytes 文件数据
     * @param path 文件路径
     * @param add 是否是追加文件
     */
    public static void writeFile(byte[] bytes, String path, boolean add) {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path,add)));
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeFile(String data, String path, boolean add){
        writeFile(data.getBytes(),path,add);
    }

    public static byte[] downloadFile(String url){
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            inputStream = connection.getInputStream();
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,len);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (null != inputStream)
                    inputStream.close();
                if (null != outputStream)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFile(String url, String name, String path){
        writeFile(downloadFile(url),path+name,false);
    }

    public static void main(String[] args) {
        String url = "https://ns-strategy.cdn.bcebos.com/ns-strategy/upload/fc_big_pic/part-00577-496.jpg";
        String name = "aa.jpg";
        String path = "C:\\Users\\zongx\\Desktop\\";
        saveFile(url,name,path);
    }

}
