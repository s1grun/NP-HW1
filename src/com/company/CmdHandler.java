package com.company;

import com.company.common.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by weng on 2019/11/1.
 */
public class CmdHandler implements Runnable {
    Socket socket;
    private Scanner console = new Scanner(System.in);
    boolean status = true;

    CmdHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        PrintWriter output = null;
        try {
            output =new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (status) {
            try {
                String cmd= console.nextLine();
                switch (cmd) {
                    case "QUIT":
                        output.println(new Message("disconnect", "").toString());
                        output.flush();
                        break;
                    case "TRY":

                        break;

                }
            } catch (Exception e) {
                System.out.println("client read cmd failed");
            }
        }
    }
}
