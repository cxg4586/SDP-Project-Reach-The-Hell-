package com.exp.game;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity //score database by using greendao
public class DBScore {

    //id and name cannot be int
    @Id(autoincrement = true)
    private long id;
    private int score;
    private String name;
    private String time;

    @Generated(hash = 1954595045)
    public DBScore(long id, int score, String name, String time) {
        this.id = id;
        this.score = score;
        this.name = name;
        this.time = time;
    }

    @Generated(hash = 856729047)
    public DBScore() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
