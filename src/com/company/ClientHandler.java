package com.company;

import java.net.Socket;
import java.util.Scanner;

/**
 * Created by weng on 2019/10/31.
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private Scanner console = new Scanner(System.in);



    ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){

    }
}
