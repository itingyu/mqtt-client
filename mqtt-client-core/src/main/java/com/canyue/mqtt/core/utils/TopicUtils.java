package com.canyue.mqtt.core.utils;

import com.canyue.mqtt.core.exception.MqttIllegalArgumentException;

import java.io.UnsupportedEncodingException;

/**
 * @author canyue
 */
public class TopicUtils {
    private static final int MIN_LEN = 1;
    private static final int MAX_LEN=65535;
    private static final char MULTI_LEVEL_WILDCARD = '#';
    private static final char SINGLE_LEVEL_WILDCARD = '+';
    private static final char TOPIC_SEPARATOR = '/';

    private TopicUtils(){};
    private static void validate(String str,boolean wildcardAllowed) throws MqttIllegalArgumentException {
        int len = 0;
        try {
            len = str.getBytes("utf8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(len<MIN_LEN||len>MAX_LEN){
            throw new MqttIllegalArgumentException("长度不合法");
        }

        if(wildcardAllowed){
            //是#或者+
           if("#".equals(str)||"+".equals(str)){
               return;
           }
           //只包含一个#且是/#结尾
            if(str.contains("#")){
                if(str.substring(0,str.length()-1).contains("#")||(!str.endsWith("/#"))){
                    throw new MqttIllegalArgumentException("多级通配符使用不正确!");
                }
            }
           //+号前后:没有字符或者是/
            char[] chars = str.toCharArray();
            char prev='\u0000',next='\u0000';
            for(int i=0;i<chars.length;i++){
                if(chars[i]=='+'){
                   prev=(i>1)?chars[i-1]:'\u0000';
                   next=(i<chars.length-1)?chars[i+1]:'\u0000';
                   if((prev!='/'&&prev!='\u0000')||(next!='/'&&next!='\u0000')){
                       throw new MqttIllegalArgumentException("单级通配符使用不正确!");
                   }
                }
            }
        }else {
            if(str.contains("#")||str.contains("+")){
                throw new MqttIllegalArgumentException("主题名不能包含通配符");
            }
        }
    }
    public static void validateTopicName(String topicName) throws MqttIllegalArgumentException {
        validate(topicName,false);
    }
    public static void validateTopicFilter(String topicFilter) throws MqttIllegalArgumentException {
        validate(topicFilter,true);
    }
}
