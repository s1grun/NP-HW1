package com.company;

import com.company.common.Message;
import com.company.common.Serialize;
import com.company.common.Words;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by weng on 2019/10/31.
 */
public class ServerHandler implements Runnable{
    private Socket clientSocket;
    private int score;
    GameHandler game = null;
    Key key;
    boolean status = true;

    ServerHandler(Socket socket){
        this.clientSocket = socket;
        this.score = 0;

    }




    @Override
    public void run(){
        DataInputStream input= null;
        DataOutputStream output=null;
        try {
            input = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            output=new DataOutputStream(clientSocket.getOutputStream());




            while (status){
                int datalen = input.readInt();
//                System.out.println(datalen);
                byte[] data = new byte[datalen];
                input.readFully(data);
                Message msg =(Message) Serialize.toObject(data);


                String type = msg.getType();
                String body = msg.getBody();


                switch (type){
                    case "login":
                        String username = body.split(",")[0];
                        String pw = body.split(",")[1];
                        if (username.equals("a")&& pw.equals("123")){

                            key= Keys.secretKeyFor(SignatureAlgorithm.HS256);
                            Date date= new Date();
                            long d = date.getTime();
                            date = new Date(d+30*60*1000);
                            String jws = Jwts.builder().setSubject("a").setExpiration(date).signWith(key).compact();
                            Message login_msg = new Message("token","login succeed!", jws);
                            Message.sendMsg(output,login_msg);

                            game = new GameHandler(score);
                            Message start_msg = new Message("update",game.getUnderline()+","+Integer.toString(game.getCounter())+","+Integer.toString(game.getScore()));
                            Message.sendMsg(output,start_msg);

                        }else{
                            Message start_msg = new Message("login","user is not authenticated" );
                            Message.sendMsg(output,start_msg);
                        }

                        break;
                    case "try":
                        String token = msg.getJwt();

                        if(!token.equals(null) && Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject().equals("a")){
                            String str = body;
                            Message res =  game.guess(str);
                            Message.sendMsg(output,res);
                            if(res.getType().equals("finish")){
                                this.score = Integer.valueOf(res.getBody());
                                game = new GameHandler(score);
                                Message start_msg = new Message("update",game.getUnderline()+","+Integer.toString(game.getCounter())+","+Integer.toString(game.getScore()) );
                                Message.sendMsg(output,start_msg);
                            }
                        }else{
                            Message start_msg = new Message("login","user is not authenticated" );
                            Message.sendMsg(output,start_msg);
                        }

                        break;
                    case "disconnect":
                        Message disconnect = new Message("disconnect","disconnect successfully" );
                        Message.sendMsg(output,disconnect);
                        close(input,output);
                        break;

                }



            }









        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




    public void close(DataInputStream input, DataOutputStream output) throws IOException {
        status = false;
        input.close();
        output.close();
        System.out.println(Thread.currentThread()+" client quit");
        clientSocket.close();
        Thread.currentThread().stop();


    }


}
