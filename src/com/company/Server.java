package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


import com.company.common.*;

public class Server {
    public static void main(String[] args){
        try{
            boolean flag=true;
            Socket client=null;
            HashMap<String,Integer> score = new HashMap<String,Integer>();
//            String inputLine;
            ServerSocket serverSocket =new ServerSocket (3000);
            System.out.println("server start at 3000");


            Message.toMessage("type:134,body:234");
            sockethandler(serverSocket,score);




//        while(flag)
//            {
//             client=serverSocket.accept();
//             System.out.println(client);
//             DataInputStream input=new DataInputStream(new BufferedInputStream(client.getInputStream()));
//             PrintStream output=new PrintStream(new BufferedOutputStream(client.getOutputStream()));
//             while (( inputLine= input.readLine())!=null)
//              {
//                  System.out.println("from client :"+inputLine);
//               if(inputLine.equals("stop"))
//               {
//                flag=false;
//                break;
//               }else{
//                   prime(Integer.parseInt(inputLine));
//                   output.println(prime(Integer.parseInt(inputLine)));
//               }
//
//               output.flush();
//              }
//              output.close();
//              input.close();
//              client.close();
//             }

//             serverSocket.close();
             }catch(IOException e){

        }
    }




    private static void sockethandler(ServerSocket serverSocket,HashMap score) throws IOException {

        Socket client=serverSocket.accept();
        score.put(client,0);
        startGame(client, score);

    }

    public static void startGame(Socket client,HashMap score) throws IOException {
        String word = Words.getWord();
        String underline = "";
        int state=0;
        for (char c : word.toCharArray()){
            underline +="_";
        }

        DataInputStream input=new DataInputStream(new BufferedInputStream(client.getInputStream()));
        PrintWriter output=new PrintWriter(client.getOutputStream());

        output.println(new Message("word", underline).toString());
        output.flush();
        output.println(new Message("attempts_left", Integer.toString(word.length())).toString());
        output.flush();

        int counter = word.length();
        String inputLine;
        while (( inputLine= input.readLine())!=null){
            Message resp = Message.toMessage(inputLine);
            String type = resp.getType();
            String body = resp.getBody();

            switch(type) {
                case "try":
                    // code block
                    if(counter>0 && state==0){
                        counter--;
                        if (word.indexOf(body)>0){
                            ArrayList<String> w_u = replaceChar(word,body,underline);
                            underline = w_u.get(1);
                            word = w_u.get(0);
                            if (underline.indexOf("_")<0){
                                int sc = (int) score.get(client);
                                score.put(client,sc+1);
                                state =1;
                                startGame(client, score);
                                break;
                            }else {
                                break;
                            }
                        }else {
                            break;
                        }
                    }else if(counter<=0 && state ==0){
                        int sc = (int) score.get(client);
                        score.put(client,sc-1);
                        startGame(client, score);
                    }


            }


        }
    }


    public static ArrayList<String> replaceChar(String word, String c, String underline){
//        for(int i = 0; i < word.length(); i++)
//        {
//            String w =Character.toString(word.charAt(i));
//            if (w.equals(c)){
//                char[] chars = underline.toCharArray();
//                chars[i] = w;
//            }
//        }
        int ind = word.indexOf(c);
        while(ind>0){
            underline = underline.substring(0, ind) + c + underline.substring(ind+c.length());
            word = word.substring(0, ind) + "_" + word.substring(ind+c.length());
            ind = word.indexOf(c);
        }

        ArrayList<String> res = new ArrayList<String>();
        res.add(word);
        res.add(underline);
        return res;

    }
}



