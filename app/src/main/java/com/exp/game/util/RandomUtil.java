package com.exp.game.util;

public class RandomUtil {
//platform length
    public static int getRangeX(int screenWidth) {
        double rate = Math.random();
        while (rate>=0.75){
            rate = Math.random();
        }
        return (int) (screenWidth * rate);
    }
//random platform
    public static int getRandomType(){
        double random = Math.random();
        if(random <= 0.19d){
            return 1;
        }else{
            return 0;
        }
    }
}
