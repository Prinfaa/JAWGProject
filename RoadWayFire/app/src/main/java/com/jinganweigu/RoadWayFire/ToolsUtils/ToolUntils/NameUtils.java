package com.jinganweigu.RoadWayFire.ToolsUtils.ToolUntils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/27 0027.
 */

public class NameUtils {


    public static String getName(String name){

        String nameWritten;
        int nameLen = name.length();
        //如果用户输入的姓名少于等于2个字符，不用截取

        if (0<nameLen&&nameLen < 2) {
            nameWritten = name;

        } else {
            //如果用户输入的姓名大于等于3个字符，截取后面两位
            String first = name.substring(0,1);
            if (isChinese(first)) {
                //截取倒数两位汉字
                nameWritten = name.substring(0,1);

//                Log.e("abck", "getNa====截取倒数两位汉字=====>"+nameWritten);

            } else {
                //截取前面的一个英文字母
                nameWritten = name.substring(0,1).toUpperCase();

//                Log.e("abck", "getNa====截取前面的两个英文字母=====>"+nameWritten);
            }
        }

//        String filename = outputPath + File.separator + outputName + ".jpg";
//        File file = new File(filename);
//        Font font = new Font("微软雅黑", Font.PLAIN, 30);
//        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = (Graphics2D) bi.getGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        g2.setBackground(getRandomColor());
//        g2.clearRect(0, 0, width, height);
//        g2.setPaint(Color.WHITE);
//        Font font = null;
//        //两个字及以上
//        if(nameWritten.length() >= 2) {
//            font = new Font("微软雅黑", Font.PLAIN, 30);
//            g2.setFont(font);
//            String firstWritten = nameWritten.substring(0, 1);
//            String secondWritten = nameWritten.substring(nameWritten.length()-2, nameWritten.length());
//            Log.e("abck", "===secondWritten=====>"+secondWritten );
//            //两个中文 如 言曌
//            if (isChinese(firstWritten) && isChinese(secondWritten)) {
//                g2.drawString(nameWritten, 20, 60);
//                nameWritten = nameWritten.substring(nameWritten.length()-2,nameWritten.length()-1);
//                Log.e("abck", "getNa====两个中文 如 言曌=====>"+nameWritten);
//                return  secondWritten;
//            }
//            //首中次英 如 罗Q
//            else if (isChinese(firstWritten) && !isChinese(secondWritten)) {
//                g2.drawString(nameWritten, 27, 60);
//                nameWritten = nameWritten.substring(nameWritten.length()-2,nameWritten.length()-1);
//                Log.e("abck", "getNa====首中次英 如 罗Q=====>"+nameWritten);
//
//                return  nameWritten;
//                //首英,如 AB
//            } else {
//                nameWritten = nameWritten.substring(nameWritten.length()-2,nameWritten.length()-1);
//
//                Log.e("abck", "getNa====首英,如 AB=====>"+nameWritten);
//
//                return  nameWritten;
//            }
//        }
//        //一个字
//        if(nameWritten.length() ==1) {
//            //中文
//            if(isChinese(nameWritten)) {
//                font = new Font("微软雅黑", Font.PLAIN, 50);
//                g2.setFont(font);
//                g2.drawString(nameWritten, 25, 70);
//                return nameWritten;
//            }
//            //英文
//            else {
//                font = new Font("微软雅黑", Font.PLAIN, 55);
//                g2.setFont(font);
//                g2.drawString(nameWritten.toUpperCase(), 33, 67);
//                return nameWritten;
//            }
//        }
//



        return nameWritten;
    }

    /**
     * 判断字符串是否为中文
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find())
            return true;
        else
            return false;
    }




}
