package minesweeper;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Programming 12 Minesweeper Project
 * Last edited: 2019-05-21
 * @author Tony Zhao
 * @version v0.1.
 * 
 * TODO:
 * Add sound effects for clear 
 * Implement highscores
 * Implement mines left counter
 * Fix screen resizing on reset? 
 * 
 * Extra Features:
 * Double clicking 
 * Custom cursor
 */

public class Minesweeper {
    Random rand = new Random();
    private ArrayList<Integer> mineNums;
    int NUM_MINES;
    int rows;
    int cols;
    int totalTiles;
    int tilesExposed;
    int tilesFlagged;
    String mode;
    private int tileSize = 35;
    private int[][] grid;
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel gridPanel;
    private JPanel settingPanel;
    private JPanel scorePanel;
    private MenuButton[] menuButtons;
    private MenuButton restartButton;
    private MinesweeperButton[][] buttons;
    private JLabel timerLabel;
    private JLabel[] scoreLabels;
    private Timer timer;
    private int time;
    JLabel minesLabel;
    int mines;
    boolean isPlaying;
    ImageIcon FLAGIMG;
    private ImageIcon MINEIMG;
    ImageIcon TILEIMG;
    private ImageIcon ACTIVEIMG;
    private ImageIcon PLAYERIMG;
    private ImageIcon RESTARTIMG;
    private Sound[] sounds;
    private Cursor cursor;
    private ImageIcon CURSORIMG;
    private HighscoreHandler scoreHandler;
    static String beginner = "Beginner";
    static String intermediate = "Intermediate";
    static String expert = "Expert";

    public static void main(String[] args) {
        Minesweeper minesweeper = new Minesweeper();
    }
    
    void setMode(String mode) {
        this.mode = mode;
    }

    String getMode() {
        return mode;
    }

    /** Helper method to generate list of mines
     *  @return an ArrayList of positions of the mines 
     */
    private ArrayList<Integer> generateMines() {
        ArrayList<Integer> mineList = new ArrayList<>();
        for (int i = 0; i < NUM_MINES; i++) {
            int newMine = rand.nextInt(rows*cols);
            if (mineList.contains(newMine)) { // Only add mines to list if it isn't at that position already
                i--;
            } else {
                mineList.add(newMine);
            }
        }
        return mineList;
    }

    private ArrayList<Integer> generateMines(ArrayList notPermissible) {
        ArrayList<Integer> mineList = new ArrayList<>();
        for (int i = 0; i < NUM_MINES; i++) {
            int newMine = rand.nextInt(rows*cols);
            if (mineList.contains(newMine) || notPermissible.contains(newMine)) { // Only add mines to list if it isn't at that position already
                i--;
            } else {
                mineList.add(newMine);
            }
        }
        return mineList;
    }

    /** Builds the grid with the values of the buttons randomly
     */
    private void buildGrid() {
        mineNums = new ArrayList<>(generateMines());
        grid = new int[rows][cols];
        for (int mineNum : mineNums) { // Adds mines
            grid[mineNum/cols][mineNum%cols] = -1;
        }
        // Iterate through integer array to generate the numbers around the mines
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == -1) { // If loops through a mine, increment the surrounding tiles
                        for (int a = -1; a <= 1; a++) {
                            for (int b = -1; b <= 1; b++) {
                                if (i + a >= 0 && i + a  < grid.length  && j + b >= 0 && j + b < grid[i].length) { // Checks if tiles is not out of bounds
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

    /** Builds the grid with the value of the button, ensuring that there is no mines generated adjacent to the give coordinate
     * @param coord of the first click
     */
    void buildGrid(int[] coord) {

        ArrayList<Integer> notPermissible = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (coord[0]+i >= 0 && coord[0]+i < rows && coord[1]+j >= 0 && coord[1]+j < cols)
                    notPermissible.add((coord[0]+i)*cols + coord[1]+j);
            }
        }

        mineNums = new ArrayList<>(generateMines(notPermissible));

        grid = new int[rows][cols];
        for (int mineNum : mineNums) { // Adds mines
            grid[mineNum/cols][mineNum%cols] = -1;
        }
        // Iterate through integer array to generate the numbers around the mines
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == -1) { // If loops through a mine, increment the surrounding tiles
                        for (int a = -1; a <= 1; a++) {
                            for (int b = -1; b <= 1; b++) {
                                if (i + a >= 0 && i + a  < grid.length  && j + b >= 0 && j + b < grid[i].length) { // Checks if tiles is not out of bounds
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
    
    /** Debug function to print current grid, displaying 0s as commas
     */
    void printGrid() {
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

    /** Sets the buttons in the game
     */
    private void buildButtons() {
        buttons = new MinesweeperButton[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new MinesweeperButton(tileSize);
                buttons[i][j].setCoordinates(i,j);
                buttons[i][j].addMouseListener(new MouseHandler(this));
                buttons[i][j].setIcon(TILEIMG);
                buttons[i][j].setRolloverIcon(ACTIVEIMG);
                buttons[i][j].setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
                gridPanel.add(buttons[i][j]);
            }
        }
    }
    
    /** Implements mines to buttons
     * 
     */
    void setButtonValues() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j].setValue(grid[i][j]);
            }
        }
    }

    /** Function to end game after hitting a mine.
     */
    void endGame(){
        timer.stop();
        isPlaying = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (buttons[i][j].getValue() == -1) {
                    buttons[i][j].setIcon(MINEIMG);                }
                buttons[i][j].expose();
            }
        }
    }

    /** Checks if only mines are unexposed and flags remaining mines
     */
    void winGame(){
        if (totalTiles - tilesExposed == NUM_MINES) {
            timer.stop();
            scoreHandler.addHighscore(time, mode);
            System.out.println(scoreHandler.getHighscores(getMode()));
            isPlaying = false;
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

    /**  Clears tiles around a 0 and checks to see if game is won, recursively
     * @param coord of button to clear near
    */
    void exposeEmpty(int[] coord) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (coord[0]+i >= 0 && coord[0]+i < rows && coord[1]+j >= 0 && coord[1]+j < cols) {
                    if (!buttons[coord[0]+i][coord[1] + j].isExposed() && !buttons[coord[0]+i][coord[1]+j].isFlagged()) {
                        buttons[coord[0]+i][coord[1] + j].expose();
                        tilesExposed++;
                        winGame();
                        if (buttons[coord[0]+i][coord[1]+j].getValue() == 0) {
                            exposeEmpty(new int[] {coord[0]+i,coord[1]+j});
                        } 
                    }
                }
            }
        }
    }
    
    /** Double click function to clear surroundings of a tile
     * @param coord of button to clear near
     */
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
        if (flagCount == buttons[x][y].getValue()) { // Checks if the correct amount of flags are around the tile before clearing
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (x+i >= 0 && x+i < rows && y+j >= 0 && y+j < cols) {
                        if (!buttons[x+i][y+j].isExposed()) { 
                            if (!buttons[x+i][y+j].isFlagged() && buttons[x+i][y+j].getValue() == -1) { // Ends game if incorrectly flagged
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

    /** Highlight tiles near cursor
     * @param coord of the button to highlight near
     */
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
    
    /** Dehighlight all tiles
     */
    void dehighlightAll() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if (!buttons[x][y].isExposed() && !buttons[x][y].isFlagged()){
                    buttons[x][y].setIcon(TILEIMG);
                }
            }
        }
    }

    /** Helper method to create a scaled image icon
     * @return ImageIcon 
     * @param file an image file to convert 
     */
    private ImageIcon generateIcon(String file) {
        return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
    }
    
    /**  Restarts the game with new sizes and mines
     * @param rows 
     * @param cols
     * @param numMines
     */
    void reset(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.NUM_MINES = numMines;
        time = 0;
        timerLabel.setText(Integer.toString(time));
        isPlaying = false;
        totalTiles = rows * cols;
        tilesExposed = 0;
        tilesFlagged = 0;
        minesLabel.setText(Integer.toString(NUM_MINES - tilesFlagged));

        //Resets grid panel
        mainPanel.remove(gridPanel);
        buttons = new MinesweeperButton[rows][cols];
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows,cols,0,0)); 
        buildButtons();
        GridBagConstraints gridPanelConstraints = new GridBagConstraints();
        gridPanelConstraints.gridx = 0;
        gridPanelConstraints.gridy = 1;
        gridPanelConstraints.anchor = GridBagConstraints.CENTER;
        mainPanel.add(gridPanel, gridPanelConstraints);

        timer.start();
        frame.pack();
        frame.setLocationRelativeTo(null); // Starts Frame in Center
    }

    void initializeImages() {
        TILEIMG = generateIcon("images/tile.png");
        FLAGIMG = generateIcon("images/flag.png");
        MINEIMG = generateIcon("images/mine.png");
        ACTIVEIMG = generateIcon("images/active.png");
        PLAYERIMG = generateIcon("images/player.png");
        RESTARTIMG = generateIcon("images/restart.png");
        CURSORIMG = generateIcon("images/cursor.png");
        cursor = Toolkit.getDefaultToolkit().createCustomCursor(CURSORIMG.getImage(),new Point(0,0), "Cursor");
    }

    void initializeFrame() {
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows,cols,0,0));
        settingPanel = new JPanel();
        settingPanel.setLayout(new FlowLayout());
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel,BoxLayout.PAGE_AXIS));
        
        //Setting main panel
        GridBagConstraints infoPanelConstraints = new GridBagConstraints();
        infoPanelConstraints.gridx = 0;
        infoPanelConstraints.gridy = 0;
        infoPanelConstraints.anchor = GridBagConstraints.PAGE_START;
        GridBagConstraints gridPanelConstraints = new GridBagConstraints();
        gridPanelConstraints.gridx = 0;
        gridPanelConstraints.gridy = 1;
        gridPanelConstraints.anchor = GridBagConstraints.CENTER;
        GridBagConstraints settingPanelConstraints = new GridBagConstraints();
        settingPanelConstraints.gridx = 0;
        settingPanelConstraints.gridy = 2;
        settingPanelConstraints.anchor = GridBagConstraints.PAGE_END;
        GridBagConstraints scorePanelConstraints = new GridBagConstraints();
        scorePanelConstraints.gridx = 1;
        scorePanelConstraints.gridy = 1;
        scorePanelConstraints.anchor = GridBagConstraints.LINE_END;
        mainPanel.add(infoPanel, infoPanelConstraints);
        mainPanel.add(gridPanel, gridPanelConstraints);
        mainPanel.add(settingPanel, settingPanelConstraints);
        mainPanel.add(scorePanel, scorePanelConstraints); //TODO: renable later
        
        buildButtons();
        //Setting info panel
        timerLabel = new JLabel();
        minesLabel = new JLabel();
        time = 0;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (isPlaying) {
                    time++;
                    timerLabel.setText(Integer.toString(time));
                }
            }
        });
        timer.start();
        timerLabel.setText(Integer.toString(time));
        minesLabel.setText(Integer.toString(NUM_MINES - tilesFlagged));
        restartButton = new MenuButton("Restart",tileSize+3,tileSize+3);
        restartButton.setText(null);
        restartButton.setIcon(RESTARTIMG);
        restartButton.addMouseListener(new MouseHandler(this));
        infoPanel.add(timerLabel);
        infoPanel.add(restartButton);
        infoPanel.add(minesLabel);

        //Setting settings panel
        menuButtons = new MenuButton[3];
        menuButtons[0] = new MenuButton("Beginner",80 ,25);
        menuButtons[1] = new MenuButton("Intermediate", 91, 25);
        menuButtons[2] = new MenuButton("Expert",68, 25);
        for (MenuButton menuButton : menuButtons) {
            settingPanel.add(menuButton);
            menuButton.addMouseListener(new MouseHandler(this));
        }

        //Setting score panel
        scorePanel.add(new JLabel("Hall of Fame"));
        ArrayList<String> topThree = scoreHandler.getHighscores();
        for (String score : topThree) {
            scorePanel.add(new JLabel(score));
        }
        scorePanel.add(new JLabel("Recent Games"));
        ArrayList<String> highscores = scoreHandler.getHighscores(getMode());
        scoreLabels = new JLabel[highscores.size()];
        for (int i = 0; i < highscores.size(); i++) {
            scoreLabels[i] = new JLabel(highscores.get(i));
            scorePanel.add(Box.createHorizontalGlue());
            scorePanel.add(scoreLabels[i]);
            scorePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        
        //Setting frame
        frame = new JFrame("Mariosweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Starts Frame in Center
        frame.setIconImage(ACTIVEIMG.getImage());
        frame.setCursor(cursor);
    }
    
    public Minesweeper(){
        // Setting constants
        scoreHandler = new HighscoreHandler("highscores.txt");

        rows = 8;
        cols = 8;
        NUM_MINES = 10;
        totalTiles = rows * cols;
        tilesExposed = 0;
        tilesFlagged = 0;
        isPlaying = false;
        setMode(beginner);

        initializeImages();
        initializeFrame();
        
        sounds = new Sound[] {
            new Sound("sounds/athletic.wav"),
            };
        sounds[0].playMusic();
    } 
}
