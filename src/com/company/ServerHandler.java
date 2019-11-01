package com.company;

import com.company.common.Message;
import com.company.common.Words;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

    private void startNewGame() throws IOException {
        this.word = generateWord();

        this.underline = "";
        for (char c : word.toCharArray()){
            underline +="_";
        }

        playGame();
    }


    private void playGame() throws IOException {
        DataInputStream input=new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        PrintWriter output=new PrintWriter(clientSocket.getOutputStream());

        String inputLine;
        int counter = word.length();

//        output.println(new Message("word", underline).toString());
//        output.flush();
//        output.println(new Message("attempts_left", Integer.toString(word.length())).toString());
//        output.flush();
        printToClient(output, underline, counter, score);


        while (( inputLine= input.readLine())!=null){
            Message resp = Message.toMessage(inputLine);
            String type = resp.getType();
            String body = resp.getBody();

//            System.out.println(type);
            System.out.println(body);
            switch(type) {
                case "try":
                    // code block
                    if(counter>=1){

//                        System.out.println("word0:"+word);
                        if (word.indexOf(body)>=0){
//                            System.out.println(word.indexOf(body));
                            ArrayList<String> w_u = replaceChar(word,body,underline);
                            underline = w_u.get(1);
                            word = w_u.get(0);
                            if (underline.indexOf("_")<0){
                                score++;
                                String w = new Message("finish", "score: "+Integer.toString(score)+" new game!").toString();
                                output.println(w);
                                output.flush();
                                startNewGame();
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
                        String w = new Message("finish", "score: "+Integer.toString(score)+" new game!").toString();
                        output.println(w);
                        output.flush();
                        startNewGame();
                        break;
                    }
                    printToClient(output, underline, counter, score);
                    break;


            }



        }

    }

    public static void printToClient(PrintWriter output,String underline, int counter, int score){
//        output.println();
        String str = "word: "+underline+", attempts_left: "+Integer.toString(counter)+", score: "+Integer.toString(score);
        String w = new Message("content", str).toString();
//        String l = new Message("attempts_left", ).toString();
//        String s = new Message("score", ).toString();
        System.out.println(w);
        output.println(w);
        output.flush();
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


}
