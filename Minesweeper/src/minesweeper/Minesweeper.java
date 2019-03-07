/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Programming 12 Minesweeper Project
 * Lasted edited: 2019-03-05
 * @author Tony Zhao
 * 
 * To-do:
 * Add sound effects for clear
 * Implement timer
 * Implement highscores
 * Implement mines left counter
 * Fix screen resizing on reset? 
 * 
 * Extra Features:
 * Double clicking 
 */

public class Minesweeper {
    Random rand = new Random();

    private ArrayList<Integer> mineNums;
    int NUM_MINES;
    int rows;
    int cols;
    int totalTiles;
    int tilesExposed;
    private int tileSize = 34;

    private int[][] grid;
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel gridPanel;
    private JPanel settingPanel;
    
    private MenuButton[] menuButtons;
    private MenuButton restartButton;
    private MinesweeperButton[][] buttons;
    private JLabel timerLabel;
    private Timer timer;
    private int time;
    private JLabel mineCounter;
    private boolean isPlaying;

    ImageIcon FLAGIMG;
    private ImageIcon MINEIMG;
    ImageIcon TILEIMG;
    private ImageIcon ACTIVEIMG;
    private ImageIcon PLAYERIMG;
    private ImageIcon RESTARTIMG;
    
    private Sound[] BGMusic;

    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper(8,8,10);
    }
    
    ArrayList<Integer> generateMines() {
        ArrayList<Integer> mineList = new ArrayList<>();
        for (int i = 0; i < NUM_MINES; i++) {
            int newMine = rand.nextInt(rows*cols);
            if (mineList.contains(newMine)) {
                i--;
            } else {
                mineList.add(newMine);
            }
        }
        return mineList;
    }

    private void buildGrid() {
        mineNums = new ArrayList<>(generateMines());
        grid = new int[rows][cols];
        for (int mineNum : mineNums) {
            grid[mineNum/cols][mineNum%cols] = -1;
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == -1) { 
                        for (int a = -1; a <= 1; a++) {
                            for (int b = -1; b <= 1; b++) {
                                if (!(i + a < 0 || i + a > grid.length - 1 || j + b < 0 || j + b > grid[i].length - 1)) {
                                    if (grid[i+a][j+b] != -1) {
                                        grid[i+a][j+b]++;    
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == -1) {
                    System.out.print("* ");
                } else if (grid[i][j] == 0) {
                    System.out.print(", ");
                } else {
                    System.out.print(grid[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
    
    void endGame(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (buttons[i][j].getValue() == -1) {
                    buttons[i][j].setIcon(MINEIMG);
                }
                buttons[i][j].expose();
            }
        }
    }

    void winGame(){
        if (totalTiles - tilesExposed == NUM_MINES) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (!buttons[i][j].isExposed()) {
                        buttons[i][j].flag();
                        buttons[i][j].setIcon(FLAGIMG);
                    }
                    buttons[i][j].expose();
                }
            }
        }
    }

    void exposeEmpty(int[] coord) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int[] newCoord = new int[]{coord[0] + i,coord[1] + j};
                if (newCoord[0] >= 0 && newCoord[0] < rows && newCoord[1] >= 0 && newCoord[1] < cols) {
                    if (!buttons[newCoord[0]][newCoord[1]].isExposed() && !buttons[newCoord[0]][newCoord[1]].isFlagged()) {
                        buttons[newCoord[0]][newCoord[1]].expose();
                        tilesExposed++;
                        winGame();
                        if (buttons[newCoord[0]][newCoord[1]].getValue() == 0) {
                            exposeEmpty(newCoord);
                        } 
                    }
                }
            }
        }
    }
     
    void clearNear(int[] coord) {
        int x = coord[0];
        int y = coord[1];
        int flagCount = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x+i >= 0 && x+i < rows && y+j >= 0 && y+j < cols) {
                    if (buttons[x+i][y+j].isFlagged()) {
                        flagCount++;
                    }
                }
            }
        }
        if (flagCount == buttons[x][y].getValue()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (x+i >= 0 && x+i < rows && y+j >= 0 && y+j < cols) {
                        if (!buttons[x+i][y+j].isExposed()) {
                            if (!buttons[x+i][y+j].isFlagged() && buttons[x+i][y+j].getValue() == -1) {
                                endGame();
                                buttons[x+i][y+j].setBackground(Color.RED);
                            } 
                            if (buttons[x+i][y+j].getValue() == 0) {
                                exposeEmpty(buttons[x+i][y+j].getCoordinates());
                            } if (buttons[x+i][y+j].getValue() > 0) {
                                buttons[x+i][y+j].expose();
                                tilesExposed++;
                                winGame();
                            }
                        }
                    }
                }
            }
        }
    }

    void highlightNear(int[] coord) {
        int x = coord[0];
        int y = coord[1];
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x+i >= 0 && x+i < rows && y+j >= 0 && y+j < cols) {
                    if (!buttons[x+i][y+j].isExposed() && !buttons[x+i][y+j].isFlagged()){
                        buttons[x+i][y+j].setIcon(ACTIVEIMG);
                    }
                }
            }
        }
    }

    void dehighlightAll() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if (!buttons[x][y].isExposed() && !buttons[x][y].isFlagged()){
                    buttons[x][y].setIcon(TILEIMG);
                }
            }
        }
    }

    private ImageIcon generateIcon(String file) {
        return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
    }
    

    void reset(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.NUM_MINES = numMines;
        frame.remove(mainPanel);
        mainPanel.remove(settingPanel);
        mainPanel.remove(gridPanel);
        totalTiles = rows * cols;
        tilesExposed = 0;
        buttons = new MinesweeperButton[rows][cols];
        buildGrid();
        printGrid();
        
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows,cols,0,0));     
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new MinesweeperButton(tileSize);
                buttons[i][j].setValue(grid[i][j]);
                buttons[i][j].setCoordinates(i,j);
                buttons[i][j].addMouseListener(new MouseHandler(this));
                buttons[i][j].setIcon(TILEIMG);
                buttons[i][j].setRolloverIcon(ACTIVEIMG);
                buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
                gridPanel.add(buttons[i][j]);
            }

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        frame.add(mainPanel);

        frame.pack();
        mainPanel.add(settingPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null); // Starts Frame in Center

        }
    }
    
    public Minesweeper(int rows, int cols, int NUM_MINES){
        this.rows = rows;
        this.cols = cols;
        this.NUM_MINES = NUM_MINES;

        TILEIMG = generateIcon("images/tile.png");
        FLAGIMG = generateIcon("images/flag.png");
        MINEIMG = generateIcon("images/mine.png");
        ACTIVEIMG = generateIcon("images/active.png");
        PLAYERIMG = generateIcon("images/player.png");
        RESTARTIMG = generateIcon("images/restart.png");
        
        buildGrid();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows,cols,0,0));
        
        settingPanel = new JPanel();

        settingPanel.setLayout(new FlowLayout());
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(settingPanel, BorderLayout.SOUTH);

        totalTiles = rows * cols;
        tilesExposed = 0;

        buttons = new MinesweeperButton[rows][cols];
        // Generates buttons
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new MinesweeperButton(tileSize);
                buttons[i][j].setValue(grid[i][j]);
                buttons[i][j].setCoordinates(i,j);
                buttons[i][j].addMouseListener(new MouseHandler(this));
                buttons[i][j].setIcon(TILEIMG);
                buttons[i][j].setRolloverIcon(ACTIVEIMG);   
                buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
                gridPanel.add(buttons[i][j]);

            }
        }
        timerLabel = new JLabel();
        
        time = 0;
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (isPlaying) {
                    time++;
                    timerLabel.setText(Integer.toString(time));
                }
            }
        });
        
        menuButtons = new MenuButton[3];
        menuButtons[0] = new MenuButton("Beginner",74 ,25);
        menuButtons[1] = new MenuButton("Intermediate", 91, 25);
        menuButtons[2] = new MenuButton("Expert",68, 25);
        
        restartButton = new MenuButton("Restart",tileSize+2,tileSize+2);
        restartButton.setText(null);
        restartButton.setIcon(RESTARTIMG);
        restartButton.addMouseListener(new MouseHandler(this));
        
        infoPanel.add(timerLabel);
        infoPanel.add(restartButton);
        


        for (MenuButton menuButton : menuButtons) {
            settingPanel.add(menuButton);
            menuButton.addMouseListener(new MouseHandler(this));
        }
        
        BGMusic = new Sound[] {
             new Sound("sounds/athletic.wav"),
            };

        BGMusic[0].playMusic();
        
        frame = new JFrame("Mariosweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Starts Frame in Center
        frame.setIconImage(ACTIVEIMG.getImage());
        
    } 
}
