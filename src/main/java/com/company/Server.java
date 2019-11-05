package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args){
        try{


            Server s = new Server();
            s.server();


             }catch(IOException e){

        }
    }

    private void server() throws IOException {
        ServerSocket serverSocket =new ServerSocket (3000);
        System.out.println("server start at 3000");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("client join:"+ clientSocket);
//            clientSocket.setSoLinger(true, LINGER_TIME);




            Thread handler = new Thread(new ServerHandler(clientSocket));
            handler.setPriority(Thread.MAX_PRIORITY);
            handler.start();
        }
    }









}



