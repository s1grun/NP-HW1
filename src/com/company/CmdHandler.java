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
    private Scanner console = new Scanner(System.in);
    boolean status = true;

    CmdHandler(Socket socket){
        this.socket = socket;
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
//                        output.println(new Message("disconnect", "").toString());
//                        output.flush();
                        Message.sendMsg(output,new Message("disconnect", ""));
                        socket.close();
                        break;
                    case "TRY":
                        String cmd_text = cmd.split(" ")[1];
//                        output.println(new Message("try", cmd_text).toString());
//                        output.flush();
                        Message.sendMsg(output,new Message("try", cmd_text));
                        break;

                }
            } catch (Exception e) {
                System.out.println("client read cmd failed");
            }
        }
    }
}
