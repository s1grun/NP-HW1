package com.company;

import com.company.common.Message;
import com.company.common.Words;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by weng on 2019/11/5.
 */
public class GameHandler {


    private int score;
    private String word;
    private String underline;

    public int getCounter() {
        return counter;
    }

    private int counter;

    public GameHandler(int score){
        this.score = score;
        this.word = "coop";

        this.underline = "";
        for (char c : word.toCharArray()){
            underline +="_";
        }
        this.counter = word.length();



    }


    private String generateWord(){
        String w = null;
        try {
            w = Words.getWord().toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return w;
    }


    public String getWord() {
        return word;
    }
    public String getUnderline() {
        return underline;
    }

    public int getScore() {
        return score;
    }

    public Message guess(String str) throws IOException, InterruptedException {
        if(counter>=1){


            if (word.indexOf(str)>=0){
                ArrayList<String> w_u = replaceChar(word,str,underline);
                underline = w_u.get(1);
                word = w_u.get(0);
                if (underline.indexOf("_")<0){
                    score++;
                    return finishOneGame(score);

                }else {

                }
            }else {
                counter--;
            }

        }
        if(counter==0 && underline.indexOf("_")>=0){

            score--;

            return finishOneGame(score);

        }
        return printToClient(underline, counter, score);

    }



    public static ArrayList<String> replaceChar(String word1, String c, String underline){

        int ind = word1.indexOf(c);
        String  word2 = word1;
//        System.out.println("word:"+word);
        while(ind>=0){
            underline = underline.substring(0, ind) + c + underline.substring(ind+c.length());
//            System.out.println("underline:"+underline);
            String u="_";
            word2 = word2.substring(0, ind) + String.format(String.format("%%%ds", c.length()), " ").replace(" ",u) + word2.substring(ind+c.length());
//            System.out.println("word2:"+ word2);
            ind = word2.indexOf(c);
        }

        ArrayList<String> res = new ArrayList<String>();
        res.add(word1);
        res.add(underline);
        return res;

    }


    public static Message finishOneGame(int score) throws IOException, InterruptedException {
        Message w = new Message("finish", Integer.toString(score));

        return w;

    }

    public static Message printToClient(String underline, int counter, int score) throws IOException {

        String str = underline+","+Integer.toString(counter)+","+Integer.toString(score);
        Message w = new Message("update", str);
        return w;
    }


}
