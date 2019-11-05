package com.company;

import com.company.common.Message;
import com.company.common.Serialize;
import com.company.common.Words;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by weng on 2019/10/31.
 */
public class ServerHandler implements Runnable{
    private Socket clientSocket;
    private int score;
    private String word;
    private String underline;

    ServerHandler(Socket socket){
        this.clientSocket = socket;
        this.score = 0;
        this.word = generateWord();

        this.underline = "";
        for (char c : word.toCharArray()){
            underline +="_";
        }
    }




    @Override
    public void run(){
        try {
            playGame();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



//    private void playGame() throws IOException {
//
//
//
//
//
//    }

    private String generateWord(){
        String w = null;
        try {
            w = Words.getWord().toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return w;
    }

    private void startNewGame() throws IOException, InterruptedException {
        this.word = generateWord();

        this.underline = "";
        for (char c : word.toCharArray()){
            underline +="_";
        }

        playGame();
    }


    private void playGame() throws IOException, InterruptedException {
        DataInputStream input= null;
        try {
            input = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        PrintWriter output=new PrintWriter(clientSocket.getOutputStream());
        DataOutputStream output=new DataOutputStream(clientSocket.getOutputStream());

        String inputLine;
        int counter = word.length();

//        output.println(new Message("word", underline).toString());
//        output.flush();
//        output.println(new Message("attempts_left", Integer.toString(word.length())).toString());
//        output.flush();
        printToClient(output, underline, counter, score);


        while (true){
            int datalen = input.readInt();
            System.out.println(datalen);
            byte[] data = new byte[datalen];
            input.readFully(data);
            Message msg =(Message) Serialize.toObject(data);


            String type = msg.getType();
            String body = msg.getBody();

//            System.out.println(type);
            System.out.println(body);
            switch(type) {
                case "try":
                    // code block
//                    Thread.currentThread().sleep(10000);
                    if(counter>=1){

//                        System.out.println("word0:"+word);
                        if (word.indexOf(body)>=0){
//                            System.out.println(word.indexOf(body));
                            ArrayList<String> w_u = replaceChar(word,body,underline);
                            underline = w_u.get(1);
                            word = w_u.get(0);
                            if (underline.indexOf("_")<0){
                                score++;
                                finishOneGame(output, score);
                                break;
                            }else {
//                                break;
                            }
                        }else {
                            counter--;
//                            break;
                        }

                    }
                    if(counter==0 && underline.indexOf("_")>=0){

                        score--;
//                        String w = new Message("finish", "score: "+Integer.toString(score)+" new game!").toString();
//                        Serialize serialized = new Serialize(w);
//                        output.write(serialized.getLength());
//                        output.println(w);
//                        output.flush();
                        finishOneGame(output, score);

                        break;
                    }
                    printToClient(output, underline, counter, score);
                    break;

                case "disconnect":
                    close(input, output);
                    break;
            }



        }

    }

    public static void printToClient(DataOutputStream output,String underline, int counter, int score) throws IOException {
//        output.println();
        String str = underline+","+Integer.toString(counter)+","+Integer.toString(score);
        Message w = new Message("update", str);
        Message.sendMsg(output,w);
//        Serialize serialized = new Serialize(w);
//        output.writeInt(serialized.getLength());
//
////        String l = new Message("attempts_left", ).toString();
////        String s = new Message("score", ).toString();
////        System.out.println(Integer.toBinaryString(serialized.getLength()));
//
//        output.write(serialized.getOut());
////        System.out.println(serialized.getOutString());
//        System.out.println(w);
//        output.flush();
    }


    public void finishOneGame(DataOutputStream output, int score) throws IOException, InterruptedException {
        Message w = new Message("finish", Integer.toString(score));
//        Serialize serialized = new Serialize(w);
////        output.print(serialized.getLength());
////        output.println(w);
//        output.writeInt(serialized.getLength());
//        output.write(serialized.getOut());
//        output.flush();
        Message.sendMsg(output,w);
        startNewGame();
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
        String  word2 = word;
//        System.out.println("word:"+word);
        while(ind>=0){
            underline = underline.substring(0, ind) + c + underline.substring(ind+c.length());
//            System.out.println("underline:"+underline);
            String u="_";
            word2 = word.substring(0, ind) + String.format(String.format("%%%ds", c.length()), " ").replace(" ",u) + word.substring(ind+c.length());
            ind = word2.indexOf(c);
        }

        ArrayList<String> res = new ArrayList<String>();
        res.add(word);
        res.add(underline);
        return res;

    }

    public void close(DataInputStream input, DataOutputStream output) throws IOException {
        input.close();
        output.close();
        System.out.println(Thread.currentThread()+" client quit");
        clientSocket.close();
        Thread.currentThread().stop();


    }


}
