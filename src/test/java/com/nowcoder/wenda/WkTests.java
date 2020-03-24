package com.nowcoder.wenda;


import java.io.IOException;

public class WkTests {
    public static void main(String[] args){
        String cmd = "D:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltoimage --quality 75 www.nowcoder.com D:\\wenda\\wk-images\\3.png";
        try {
            //异步执行
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
