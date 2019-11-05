package com.company.common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by weng on 2019/10/30.
 */
public class Message implements Serializable {
    private String type;
    private String body;



    private String jwt = null;

    public Message(String type, String body){
        this.type = type;
        this.body = body;
    }
    public Message(String type, String body, String token){
        this.type = type;
        this.body = body;
        this.jwt = token;
    }

    public String getBody(){
        return body;
    }
    public String getJwt() {
        return jwt;
    }


    public String getType() {
        return type;
    }

//    public String toString(){
//        return "{type:"+ type+",body:"+body+"}";
//    }

    public static Message toMessage(String str){
        String[] strArr = str.split(",body:");
//        System.out.println(strArr[0].substring(6));
        return new Message(strArr[0].substring(6),strArr[1].substring(0,strArr[1].length()-1));
    }



    public static void sendMsg(DataOutputStream output, Message msg) throws IOException {

        Serialize serialized = new Serialize(msg);
        output.writeInt(serialized.getLength());
        output.write(serialized.getOut());
        output.flush();
    }
}
