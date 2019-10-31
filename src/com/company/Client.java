package com.company;

/**
 * Created by weng on 2018/3/31.
 */

import com.company.common.Message;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
          public static void main(String[] args)
          {
              try{
                  Socket clientSocket =new Socket ("127.0.0.1",3000);
                  System.out.println("connect to 3000");

                  PrintWriter output =new PrintWriter(clientSocket.getOutputStream());
                  DataInputStream input=new DataInputStream(clientSocket.getInputStream());

                  String response;
                  while (( response= input.readLine())!=null){
//                      response=input.readLine();
                      System.out.println("server: "+" "+response);
                  }

                  System.out.println("guess:");
                  Scanner scan = new Scanner(System.in);
                  String ch = scan.nextLine();

                  output.println(new Message("try", ch).toString());
                  response=input.readLine();
                  System.out.println("server: "+" "+response);
//                  for(int i=2;i<=num/2;i++){
//                      output.println(i);
//                      output.flush();
//                      response=input.readLine();
//                      System.out.println("Communication: "+i+" "+response);
//                      if(response.equals("true")){
//                          output.println(num-i);
//                          output.flush();
//                          response=input.readLine();
//                          System.out.println("Communication:"+(num-i)+" "+response);
//                          if(response.equals("true")){
//                              System.out.println(num +"="+i+"+"+(num-i));
//                              break;
//                          }else{
//                              continue;
//                          }
//                      }else{
//                          continue;
//                      }
//                    }


//                  output.close();
//                  input.close();
//                  clientSocket.close();
              } catch (Exception e){
                System.err.println("Exception :"+e);
              }
        }


        private static void handler(PrintWriter out, DataInputStream in){

        }


    }


