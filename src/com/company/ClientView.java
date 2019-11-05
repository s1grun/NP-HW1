package com.company;

import com.company.common.Message;

/**
 * Created by weng on 2019/11/4.
 */
public class ClientView {

    public ClientView(Message msg){
        String type = msg.getType();
        String body = msg.getBody();

        switch (type){
            case "update":
                String[] body_arr = body.split(",");
                String word = body_arr[0];
                String attempts_left = body_arr[1];
                String score = body_arr[2];
                System.out.println("word:"+ word +" attempts_left:"+attempts_left+" score:"+score);
                break;
            case "finish":
                System.out.println("game finish, score: "+ body + " new game start!");
        }

    }


}
