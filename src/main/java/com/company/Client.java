package com.company;

/**
 * Created by weng on 2018/3/31.
 */

import com.company.common.Message;
import com.company.common.Serialize;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public String token = null;
    public static void main(String[] args)
    {

      Client client = new Client();
      client.startClient(client);

    }

    public String getToken(){
          return token;
    }


    public void startClient(Client client){
        try(Socket clientSocket =new Socket("127.0.0.1",3000)){

            System.out.println("connect to 3000");

            Thread cmdhandler = new Thread(new CmdHandler(clientSocket,client));
            cmdhandler.setPriority(Thread.MAX_PRIORITY);
            cmdhandler.start();



            DataInputStream inStream = new DataInputStream(clientSocket.getInputStream());
            while(true){
                int datalen = inStream.readInt();
                System.out.println(datalen);
                byte[] data = new byte[datalen];
                inStream.readFully(data);
                Message msg =(Message) Serialize.toObject(data);


                String type = msg.getType();
                String body = msg.getBody();

                switch (type){
                    case "update":
                        new ClientView(msg);
                        break;
                    case "finish":
                        new ClientView(msg);
                        break;
                    case "token":
                        token = msg.getJwt();

                        break;
                    case "login":
                        System.out.println("server: "+" "+body);
                        System.out.println("please login");
                        break;

                    default:
                        System.out.println("server: "+" "+body);

                }
            }


        } catch (Exception e){
            System.err.println("Exception :"+e);
        }
    }

}



