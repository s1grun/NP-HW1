package com.company.common;

/**
 * Created by weng on 2019/10/30.
 */
public class Message {
    private String type;
    private String body;
    public Message(String type, String body){
        this.type = type;
        this.body = body;
    }

    public String getBody(){
        return body;
    }


    public String getType() {
        return type;
    }

    public String toString(){
        return "{type:"+ type+", body:"+body+"}";
    }

    public static Message toMessage(String str){
        String[] strArr = str.split(",");
//        System.out.println(strArr[0].substring(6));
        return new Message(strArr[0].substring(6),strArr[1].substring(6,strArr[1].length()-1));
    }
}
