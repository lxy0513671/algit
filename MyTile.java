package com.my.app;

import java.io.Serializable;
import java.util.Objects;

public class MyTile implements Serializable {

    private int id; // 方块在一行中的位置(0,1,2,3)
    private int value;  // 方块中的值(2,4,8,16······)
    private boolean merge = false; // 是否合成

    public MyTile(){

    }
    public MyTile(int id, int value){
        this.id = id;
        this.value = value;
    }

    public void crash(MyTile other){
        if(this.value == 0){
            this.value = other.value;
        }else if(other.value == this.value){
            this.value += other.value;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isMerge() {
        return this.merge;
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public MyTile merge(MyTile next){
        this.setValue(this.getValue() + next.getValue());
        next.setValue(0);
        return this;
    }

    @Override
    public String toString() {
        return "MyTile{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyTile tile = (MyTile) o;
        return id == tile.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
