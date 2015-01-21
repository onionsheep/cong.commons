package cong.commons.test.io;

import cong.common.io.FileUtils;

import java.util.ArrayList;

public class TestFileUtils {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //t0("test/UTF-8.txt", "UTF-8");
        //t0("test/ANSI.txt", "GBK");
        //t1("test/UTF-8.txt", "UTF-8");
        //t1("test/ANSI.txt", "GBK");
        //tWriteString("123你好，and&*（）——%……&", "test/wUTF8.txt", "UTF-8");
        //tWriteString("\n四大行如\t果sdkjghkjas\ndh!@#$%^\n&*(sdjfgvb008990", "test/wGBK.txt", "GBK");
        //tReadStringNIO("test/wGBK.txt");
        //tReadStringNIO("test/wUTF8.txt");
        //tReadStringNIO("test/ANSI.txt");
        //tReadStringNIO("test/UTF-8.txt");
        tReadStringNIO("test/num.txt");
    }

    public static void tReadLines(String f, String chset) {
        ArrayList<String> lines = FileUtils.readLines(f, chset);
        for (String l : lines) {
            System.out.println(l);
        }
    }

    public static void tReadString(String f, String chset) {
        System.out.println(FileUtils.readFileToString(f, chset));
    }

    public static void tWriteString(String str, String filePath, String chset) {
        FileUtils.writeStringToFile(filePath, str, chset, false);
    }

    public static void tReadStringNIO(String filePath) {
        @SuppressWarnings("deprecation")
        String str = FileUtils.readFileToStringNIO(filePath);
        System.out.println(str);
    }

}
