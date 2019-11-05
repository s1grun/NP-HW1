package com.company;

import com.company.common.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by weng on 2019/11/1.
 */
public class CmdHandler implements Runnable {
    Socket socket;
    Client client;

    private Scanner console = new Scanner(System.in);
    boolean status = true;

    CmdHandler(Socket socket, Client client){
        this.socket = socket;
        this.client = client;


    }

    @Override
    public void run() {
        DataOutputStream output = null;
        try {
            output=new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (status) {
            try {
                String cmd= console.nextLine();
                String cmd_type = cmd.split(" ")[0];

                switch (cmd_type) {
                    case "QUIT":

                        Message.sendMsg(output,new Message("disconnect", "", client.getToken()));
                        status = false;
                        socket.close();
                        break;
                    case "TRY":
                        String cmd_text = cmd.split(" ")[1];
                        Message.sendMsg(output,new Message("try", cmd_text, client.getToken()));
                        break;
                    case "login":
                        String ctext = cmd.split(" ")[1];
                        Message.sendMsg(output,new Message("login", ctext));
                        break;

                }
            } catch (Exception e) {
                System.out.println("client read cmd failed");
            }
        }
    }
}
