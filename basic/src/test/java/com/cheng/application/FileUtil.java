package com.cheng.application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by homelink on 2016/6/3.
 */
public class FileUtil {
    public static void main(String[] args) {
        List<String> strings =  FileUtil.readFileByLines("C:\\Users\\cheng\\Desktop\\测试账号汇总-截止8.31日.txt");
        for (String s:strings){
            System.out.println("select "+s+" union all");
        }
    }

    public static List<String> readFileByLines(String fileName) {
        List<String> data = new ArrayList<>();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                data.add(tempString);
            }
            reader.close();
        } catch (IOException e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return data;
    }

    public static void writeFileByLines(String[] content, String fileName) throws IOException {
        File file = new File(fileName);// 指定要写入的文件
        if (!file.exists()) {// 如果文件不存在则创建
            file.createNewFile();
        }
        // 获取该文件的缓冲输出流
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        // 写入信息
        for (String c : content) {
            bufferedWriter.append(c);
            bufferedWriter.newLine();// 表示换行
        }
        bufferedWriter.flush();// 清空缓冲区
        bufferedWriter.close();// 关闭输出流
    }

}
