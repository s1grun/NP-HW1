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
          public static void main(String[] args)
          {
              try(Socket clientSocket =new Socket("127.0.0.1",3000)){

                  System.out.println("connect to 3000");

                  Thread cmdhandler = new Thread(new CmdHandler(clientSocket));
                  cmdhandler.setPriority(Thread.MAX_PRIORITY);
                  cmdhandler.start();

                  PrintWriter output =new PrintWriter(clientSocket.getOutputStream());
//                  BufferedReader input = new BufferedReader(
//                          new InputStreamReader(clientSocket.getInputStream()));
//
//                  String response;
//                  while (( response= input.readLine())!=null){
////                      response=input.readLine();
//                      System.out.println("aaaa "+ response);
//                      Message resp = Message.toMessage(response);
//                      String type = resp.getType();
//                      String body = resp.getBody();
//
//                      System.out.println(type);
//                      switch (type){
//                          case "update":
//                              new ClientView(resp);
//                              break;
//                          default:
//                              System.out.println("server: "+" "+body);
//
//                      }
//
//
////                      if (type.equals("content")){
////                          System.out.println("server: "+" "+response);
////                          System.out.println("guess:");
//////                          Scanner scan = new Scanner(System.in);
//////                          String ch = scan.nextLine();
//////                          if(ch.length()==0 || ch.equals("")){
//////                              ch = scan.nextLine();
//////                          }else{
//////                              output.println(new Message("try", ch.toLowerCase()).toString());
//////                              output.flush();
//////                          }
////
////                      }else {
////                          System.out.println("server: "+" "+body);
////                      }
//
//                  }
                  DataInputStream inStream = new DataInputStream(clientSocket.getInputStream());
                  while(true){
                      int datalen = inStream.readInt();
                      System.out.println(datalen);
                      byte[] data = new byte[datalen];
                      inStream.readFully(data);
                      Message msg =(Message) Serialize.toObject(data);
//                      System.out.println(msg.toString());

                      String type = msg.getType();
                      String body = msg.getBody();

                      switch (type){
                          case "update":
                              new ClientView(msg);
                              break;
                          case "finish":
                              new ClientView(msg);
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


