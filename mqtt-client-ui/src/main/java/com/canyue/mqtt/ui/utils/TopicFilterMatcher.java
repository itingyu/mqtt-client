package com.canyue.mqtt.ui.utils;

import com.canyue.mqtt.core.exception.MqttIllegalArgumentException;
import com.canyue.mqtt.core.utils.TopicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: canyue
 * @Date: 2020/5/5 16:31
 */
public class TopicFilterMatcher {
    private TopicFilterMatcher() {
    }

    ;

    /**
     * 用于判断主题过滤器是否匹配主题
     *
     * @param topicFilter 主题过滤器
     * @param topicName   主题名
     * @return 匹配结果
     * @throws MqttIllegalArgumentException
     */
    public static boolean match(String topicFilter, String topicName) throws MqttIllegalArgumentException {
        try {
            TopicUtils.validateTopicFilter(topicFilter);
            TopicUtils.validateTopicName(topicName);
        } catch (MqttIllegalArgumentException e) {
            throw new MqttIllegalArgumentException("不合法的主题过滤器或主题！");
        }
        String[] tf = spilt(topicFilter);
        String[] tn = spilt(topicName);

        //对于$开头的主题，先判断带$的开头是否一样,因为不能将 $ 字符开头的主题名匹配通配符 (#或+) 开头的主题过滤器
        if ((tn[0].startsWith("$") || tf[0].startsWith("$")) && (!tf[0].equals(tn[0]))) {
            return false;
        }
        int len = tf.length;

        if (tf[len - 1].equals("#")) {
            len = tf.length - 1;
        } else if (tf.length != tn.length) {
            return false;
        }
        for (int index = 0; index < len; index++) {
            if ("#".equals(tf[index])) {
                return true;
            }
            if ("+".equals(tf[index])) {
                continue;
            }
            if (!tf[index].equals(tn[index])) {
                return false;
            }
        }
        return true;
    }


    /**
     * 分割效果如下：
     * "/h"     -->     "","h"
     * "h/"     -->     "h",""
     * "h//"    -->     "h","",""
     * "//h"    -->     "","","h"
     * "a/b"    -->     "a","b"
     * "a//b"   -->     "a","","b"
     */
    private static String[] spilt(String str) {
        List<String> list = new ArrayList<>();
        char[] chars = str.toCharArray();
        char splitChar = '/';
        int beginIndex = 0;
        int lastIndex = 0;
        for (; lastIndex < chars.length; lastIndex++) {
            if (chars[lastIndex] == splitChar) {
                list.add(new String(chars, beginIndex, lastIndex - beginIndex));
                beginIndex = lastIndex + 1;
            }
        }
        list.add(new String(chars, beginIndex, lastIndex - beginIndex));
        return list.toArray(new String[0]);
    }
//    public static void main(String[] args) throws MqttIllegalArgumentException {
//        System.out.println(TopicFilterMatcher.match("sport/tennis/player1/#", "sport/tennis/player1")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("sport/tennis/player1/#", "sport/tennis/player1/ranking")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("sport/tennis/player1/#", "sport/tennis/player1/score/wimbledon")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("sport/#", "sport")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("/#", "sport")+"===false");
//        System.out.println("========================================");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("sport/tennis/+", "sport/tennis/player1")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("sport/tennis/+", "sport/tennis/player2")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("sport/tennis/+", "sport/tennis/player1/ranking")+"===false");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("sport/+", "sport")+"===false");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("sport/+", "sport/")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("+/+", "/finance")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("/+", "/finance")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("+", "/finance")+"===false");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("+", "test")+"===true");
//        System.out.println("========================================");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("#","$SYS")+"===false");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("+/monitor/Clients","$SYS/monitor/Clients")+"===false");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("$SYS/#","$SYS/")+"===true");
//        System.out.println("========================================");
//        System.out.println(TopicFilterMatcher.match("$SYS/monitor/+","$SYS/monitor/Clients")+"===true");
//
//    }
}

