package com.my.app;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.registry.Registry;
import java.util.*;

//
// 1、弄成安卓 或者 GUI
// 2、每次滑动有专门的纯音乐
// 3、计入分数
// 4、d撤销动作
// ......
public class MyBoard {
    private int size;

    private MyTile[][] tiles;

    private Random random = new Random();
    private List<MyTile> freeTiles = new ArrayList<>();
    private Map<Integer, MyTile> freeTiles2 = new HashMap<>();
    public MyBoard(int size){
        this.size = size;
        tiles = new MyTile[size][size];

        // 初始格子
        for(int i = 0;i < size;i++)
            for(int j = 0;j < size;j++) {
                Integer id = (4*i)+(j+1);
                tiles[i][j] = new MyTile(id, 0);
//                freeTiles2.put(id, tiles[i][j]);
                freeTiles.add(tiles[i][j]);
            }

        // 随机两个格子为2
        getTileByRandom();
        getTileByRandom();
    }

    public MyBoard getTileByRandom(){
        int index = random.nextInt(freeTiles.size());

        MyTile tile = freeTiles.get(index);
        tile.setValue(2);
        freeTiles.remove(index);
        return this;
    }

    public static void main(String[] args) {
        MyBoard board = new MyBoard(4);
        board.print();


        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        while(true){
            is = System.in;
            reader = new InputStreamReader(is);
            br = new BufferedReader(reader);
            try {
                String option = br.readLine();
                if(board.merge(option))
                    board.getTileByRandom();
                board.print();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MyBoard print(){
        for(int i = 0;i < this.size;i++){
            for(int j = 0;j < this.size;j++) {
                this.tiles[i][j].setMerge(false);
                System.out.print(this.tiles[i][j].getValue() + "\t");
            }
            System.out.println();
        }
        System.out.println();
        return this;
    }

    public boolean merge(String option){
        boolean result = false;
        switch (option){
            case "a": //←
                for(int row = 0;row < size;row++){
                    int begin = 0;
                    int rowSize = tiles[row].length;
                    while (begin < rowSize) {
                        int _begin = 0;
                        while (_begin < rowSize) {
                            // 与后面相融
                            if (tiles[row][_begin].getValue() == 0
                                && (_begin < rowSize - 1 && tiles[row][_begin + 1].getValue() != 0)){
                                tiles[row][_begin].merge(tiles[row][_begin + 1]);
                                // 与后面融合后，从盘子中取出，将后面得加入盘子
                                freeTiles.remove(tiles[row][_begin]);
//                                    freeTiles.add(tiles[row][_begin + 1]);
                                List<MyTile> temp = new ArrayList<>();
                                temp.add(tiles[row][_begin + 1]);
                                temp.addAll(freeTiles);
                                freeTiles = temp;
                                result = true;
                            }
                            // 与前面碰撞
                            if (_begin > 0
                                    && (!tiles[row][_begin - 1].isMerge() && !tiles[row][_begin].isMerge())
                                    && tiles[row][_begin - 1].getValue() > 0
                                    && tiles[row][_begin - 1].getValue() == tiles[row][_begin].getValue()){
                                //  与前面碰撞，自身归还到盘子
                                tiles[row][_begin - 1].merge(tiles[row][_begin]);
                                tiles[row][_begin - 1].setMerge(true);
//                                freeTiles.add(tiles[row][_begin]);

                                List<MyTile> temp = new ArrayList<>();
                                temp.add(tiles[row][_begin]);
                                temp.addAll(freeTiles);
                                freeTiles = temp;
                                result = true;
                            }

                            _begin++;
                        }
                        begin++;
                    }
                }
                break;
            case "d": //→
                for(int row = size - 1;row >= 0;row--){
                    int begin = tiles[row].length - 1;
                    int rowSize = tiles[row].length;
                    while (begin >= 0) {
                        int _begin = tiles[row].length - 1;;
                        while (_begin >= 0) {
                            // 与后面相融
                            if (tiles[row][_begin].getValue() == 0
                                    && (_begin > 0 && tiles[row][_begin - 1].getValue() != 0)){
                                tiles[row][_begin].merge(tiles[row][_begin - 1]);
                                // 与后面融合后，从盘子中取出，将后面得加入盘子
                                freeTiles.remove(tiles[row][_begin]);
//                                    freeTiles.add(tiles[row][_begin - 1]);

                                List<MyTile> temp = new ArrayList<>();
                                temp.add(tiles[row][_begin - 1]);
                                temp.addAll(freeTiles);
                                freeTiles = temp;
                                result = true;
                            }

                            // 与前面碰撞
                            if (_begin + 1 < rowSize //============
                                    && (!tiles[row][_begin].isMerge() && !tiles[row][_begin + 1].isMerge())
                                    && tiles[row][_begin + 1].getValue() > 0
                                    && tiles[row][_begin + 1].getValue() == tiles[row][_begin].getValue()){
                                tiles[row][_begin + 1].merge(tiles[row][_begin]);
                                tiles[row][_begin + 1].setMerge(true);
                                //  与前面碰撞，自身归还到盘子
//                                freeTiles.add(tiles[row][_begin]);

                                List<MyTile> temp = new ArrayList<>();
                                temp.add(tiles[row][_begin]);
                                temp.addAll(freeTiles);
                                freeTiles = temp;
                                result = true;
                            }

                            _begin--;
                        }
                        begin--;
                    }
                }
                break;
            case "w": //↑
                for(int col = 0; col < size; col++){
                    int begin = 0;
                    int colSize = tiles.length;
                    while(begin < colSize){
                        int _begin = 0;
                        while(_begin < colSize){
                            // 与后面相融
                            if(tiles[_begin][col].getValue() == 0
                                && (_begin < colSize - 1 && tiles[_begin + 1][col].getValue() != 0)){
                                tiles[_begin][col].merge(tiles[_begin + 1][col]);
                                // 与后面融合后，从盘子中取出，将后面得加入盘子
                                freeTiles.remove(tiles[_begin][col]);
//                                    freeTiles.add(tiles[_begin + 1][col]);

                                List<MyTile> temp = new ArrayList<>();
                                temp.add(tiles[_begin + 1][col]);
                                temp.addAll(freeTiles);
                                freeTiles = temp;
                                result = true;
                            }

                            // 与前面碰撞
                            if (_begin > 0
                                    && (!tiles[_begin - 1][col].isMerge() && !tiles[_begin][col].isMerge())
                                    && tiles[_begin - 1][col].getValue() > 0
                                    && tiles[_begin - 1][col].getValue() == tiles[_begin][col].getValue()) {
                                tiles[_begin - 1][col].merge(tiles[_begin][col]);
                                tiles[_begin - 1][col].setMerge(true);
                                //  与前面碰撞，自身归还到盘子
//                                freeTiles.add(tiles[_begin][col]);

                                List<MyTile> temp = new ArrayList<>();
                                temp.add(tiles[_begin][col]);
                                temp.addAll(freeTiles);
                                freeTiles = temp;
                                result = true;
                            }
                            _begin++;
                        }
                        begin++;
                    }
                }
                break;
            case "s": //↓
                for(int col = 0; col < size; col++){
                    int begin = tiles.length - 1;
                    int colSize = tiles.length;
                    while(begin >= 0){
                        int _begin = tiles.length - 1;
                        while(_begin >= 0){
                            // 与后面相融
                            if(tiles[_begin][col].getValue() == 0
                                && (_begin > 0 && tiles[_begin - 1][col].getValue() != 0)){
                                tiles[_begin][col].merge(tiles[_begin - 1][col]);
                                // 与后面融合后，从盘子中取出，将后面得加入盘子
                                freeTiles.remove(tiles[_begin][col]);
//                                    freeTiles.add(tiles[_begin - 1][col]);

                                List<MyTile> temp = new ArrayList<>();
                                temp.add(tiles[_begin - 1][col]);
                                temp.addAll(freeTiles);
                                freeTiles = temp;
                                result = true;
                                }

                            // 与前面碰撞
                            if (_begin + 1 < size
                                    && (!tiles[_begin + 1][col].isMerge() && !tiles[_begin][col].isMerge())
                                    && tiles[_begin + 1][col].getValue() > 0
                                    && tiles[_begin + 1][col].getValue() == tiles[_begin][col].getValue()){
                                tiles[_begin + 1][col].merge(tiles[_begin][col]);
                                tiles[_begin + 1][col].setMerge(true);
                                //  与前面碰撞，自身归还到盘子
//                                freeTiles.add(tiles[_begin][col]);

                                List<MyTile> temp = new ArrayList<>();
                                temp.add(tiles[_begin][col]);
                                temp.addAll(freeTiles);
                                freeTiles = temp;
                                result = true;
                            }
                            _begin--;
                        }
                        begin--;
                    }
                }
                break;
        }
        return result;
    }
}
