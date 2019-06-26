package com.tonyzhao.mariosweeper;

import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

/**
 * Mariosweeper
 * Programming 12 Minesweeper Project 
 * Last edited: 2019-06-24
 * 
 * @author Tony Zhao
 * @version v1.0
 *
 * Issues:
 * - Double clicking uses original button, meaning 1.5 chording is impossible
 * - Closing panes moves entire JFrame
 *
 * Extra Features: 
 * - Double clicking C
 * - Custom cursor 
 * - Detailed score tracking
 * - Music Panel
 * - Faithful recreation of WINMINE mechanics
 */
public class Mariosweeper {

    Random rand = new Random();
    int NUM_MINES;
    int rows;
    int cols;
    int totalTiles;
    int tilesExposed;
    int tilesFlagged;
    boolean isPlaying;
    String mode;

    private int tileSize = 35;
    private int[][] grid;

    JFrame frame;
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JPanel gridPanel;
    private JPanel settingPanel;
    JPanel scorePanel;
    boolean isScorePanelVisible;
    JPanel achievementPanel;
    boolean isAchievementPanelVisible;

    private MenuButton[] menuButtons;
    private MenuButton restartButton;
    private MenuButton toggleAchievementPanelButton;
    private MenuButton toggleScorePanelButton;
    private MinesweeperButton[][] buttons;
    private JLabel timerLabel;
    private JLabel[] scoreLabels;
    private Timer timer;
    private int time;
    JLabel minesLabel;
    
    ImageIcon FLAGIMG;
    ImageIcon BOMBIMG;
    ImageIcon TILEIMG;
    ImageIcon ACTIVEIMG;
    ImageIcon RESTARTIMG;
    ImageIcon FLOWERIMG;
    ImageIcon activeMineImage;

    private Sound[] sounds;
    Sound currentSound;
    private Cursor cursor;
    private ImageIcon CURSORIMG;
    private HighscoreHandler scoreHandler;

    static String BEGINNER = "Beginner";
    static String INTERMEDIATE = "Intermediate";
    static String EXPERT = "Expert";
    static int SCORE_PANEL_WIDTH = 170;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                Mariosweeper minesweeper = new Mariosweeper();
            }
        });
    }
    JScrollPane scorePanelWrapper;
    /** Sets difficulty.
     * 
     * @param mode 
     */
    void setMode(String mode) {
        this.mode = mode;
    }
    /** Gets current difficulty
     * 
     * @return 
     */
    String getMode() {
        return mode;
    }
    
    void setCurrentSound(Sound newSound) {
        currentSound = newSound;
    }

    /**
     * Helper method to generate list of mines
     *
     * @return an ArrayList of positions of the mines.
     */
    private ArrayList<Integer> generateMines() {
        ArrayList<Integer> mineList = new ArrayList<>();
        for (int i = 0; i < NUM_MINES; i++) {
            int newMine = rand.nextInt(rows * cols);
            if (mineList.contains(newMine)) { // Only add mines to list if it isn't at that position already
                i--;
            } else {
                mineList.add(newMine);
            }
        }
        return mineList;
    }

    /**
     * Helper method to generate list of mines, excluding the first click
     *
     * @param notPermissible not permissible positions of mines
     * @return an ArrayList of the positions of the mines.
     */
    private ArrayList<Integer> generateMines(ArrayList<Integer> notPermissible) {
        ArrayList<Integer> mineList = new ArrayList<>();
        for (int i = 0; i < NUM_MINES; i++) {
            int newMine = rand.nextInt(rows * cols);
            if (mineList.contains(newMine) || notPermissible.contains(newMine)) { // Only add mines to list if it isn't at that position already
                i--;
            } else {
                mineList.add(newMine);
            }
        }
        return mineList;
    }

    /**
     * Builds the grid with the values of the buttons randomly
     */
    private void buildGrid() {
        ArrayList<Integer> mineNums = new ArrayList<>(generateMines());
        grid = new int[rows][cols];
        for (int mineNum : mineNums) { // Adds mines
            grid[mineNum / cols][mineNum % cols] = -1;
        }
        // Iterate through integer array to generate the numbers around the mines
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == -1) { // If loops through a mine, increment the surrounding tiles
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            if (i + a >= 0 && i + a < grid.length && j + b >= 0 && j + b < grid[i].length) { // Checks if tiles is not out of bounds
                                if (grid[i + a][j + b] != -1) {
                                    grid[i + a][j + b]++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Builds the grid with the value of the button, ensuring that there is no
     * mines generated adjacent to the give coordinate
     *
     * @param coord of the first click
     */
    void buildGrid(int[] coord) {
        ArrayList<Integer> notPermissible = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (coord[0] + i >= 0 && coord[0] + i < rows && coord[1] + j >= 0 && coord[1] + j < cols) {
                    notPermissible.add((coord[0] + i) * cols + coord[1] + j);
                }
            }
        }

        ArrayList<Integer> mineNums = new ArrayList<>(generateMines(notPermissible));

        grid = new int[rows][cols];
        for (int mineNum : mineNums) { // Adds mines
            grid[mineNum / cols][mineNum % cols] = -1;
        }
        // Iterate through integer array to generate the numbers around the mines
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == -1) { // If loops through a mine, increment the surrounding tiles
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            if (i + a >= 0 && i + a < grid.length && j + b >= 0 && j + b < grid[i].length) { // Checks if tiles is not out of bounds
                                if (grid[i + a][j + b] != -1) {
                                    grid[i + a][j + b]++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Debug function to print current grid, displaying 0s as commas
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

    /**
     * Sets the buttons in the game
     */
    private void buildButtons() {
        buttons = new MinesweeperButton[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new MinesweeperButton(tileSize, new MinesweeperButtonHandler(this));
                buttons[i][j].setCoordinates(i, j);
                buttons[i][j].setIcon(TILEIMG);
                buttons[i][j].setRolloverIcon(ACTIVEIMG);
                buttons[i][j].setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                gridPanel.add(buttons[i][j]);
            }
        }
    }

    /**
     * Implements mines to buttons
     *
     */
    void setButtonValues() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j].setValue(grid[i][j]);
            }
        }
    }
    /** Toggle visibility of achievement panel
     * 
     */
    void toggleAchievementPanel() {
        isAchievementPanelVisible = !isAchievementPanelVisible;
        achievementPanel.setVisible(isAchievementPanelVisible);
        frame.pack();
    }
    /** Toggle visibility of score panel
     * 
     */
    void toggleScorePanel() {
        isScorePanelVisible = !isScorePanelVisible;
        scorePanelWrapper.setVisible(isScorePanelVisible);
        frame.pack();
    }
    
    /**
     * Function to end game after hitting a mine.
     */
    void endGame() {
        timer.stop();
        isPlaying = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (buttons[i][j].getValue() == -1) {
                    buttons[i][j].setIcon(activeMineImage);
                }
                buttons[i][j].expose();
            }
        }
    }

    /**
     * Checks if only mines are unexposed and flags remaining mines
     */
    void winGame() {
        if (totalTiles - tilesExposed == NUM_MINES) {
            timer.stop();
            scoreHandler.addHighscore(time, mode);
            minesLabel.setText(Integer.toString(NUM_MINES - tilesFlagged));
            updateHighscores();
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

    /**
     * Clears tiles around a 0 and checks to see if game is won, recursively
     *
     * @param coord of button to clear near
     */
    void exposeEmpty(int[] coord) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (coord[0] + i >= 0 && coord[0] + i < rows && coord[1] + j >= 0 && coord[1] + j < cols) {
                    if (!buttons[coord[0] + i][coord[1] + j].isExposed() && !buttons[coord[0] + i][coord[1] + j].isFlagged()) {
                        buttons[coord[0] + i][coord[1] + j].expose();
                        tilesExposed++;
                        winGame();
                        if (buttons[coord[0] + i][coord[1] + j].getValue() == 0) {
                            exposeEmpty(new int[]{coord[0] + i, coord[1] + j});
                        }
                    }
                }
            }
        }
    }

    /**
     * Double click function to clear surroundings of a tile
     *
     * @param coord of button to clear near
     */
    void clearNear(int[] coord) {
        int x = coord[0];
        int y = coord[1];
        int flagCount = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i >= 0 && x + i < rows && y + j >= 0 && y + j < cols) {
                    if (buttons[x + i][y + j].isFlagged()) {
                        flagCount++;
                    }
                }
            }
        }
        if (flagCount == buttons[x][y].getValue()) { // Checks if the correct amount of flags are around the tile before clearing
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (x + i >= 0 && x + i < rows && y + j >= 0 && y + j < cols) {
                        if (!buttons[x + i][y + j].isExposed()) {
                            if (!buttons[x + i][y + j].isFlagged() && buttons[x + i][y + j].getValue() == -1) { // Ends game if incorrectly flagged
                                endGame();
                                buttons[x + i][y + j].setBackground(Color.RED);
                            }
                            if (buttons[x + i][y + j].getValue() == 0) {
                                exposeEmpty(buttons[x + i][y + j].getCoordinates());
                            }
                            if (buttons[x + i][y + j].getValue() > 0) {
                                buttons[x + i][y + j].expose();
                                tilesExposed++;
                                winGame();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Highlight tiles near cursor
     *
     * @param coord of the button to highlight near
     */
    void highlightNear(int[] coord) {
        int x = coord[0];
        int y = coord[1];
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i >= 0 && x + i < rows && y + j >= 0 && y + j < cols) {
                    if (!buttons[x + i][y + j].isExposed() && !buttons[x + i][y + j].isFlagged()) {
                        buttons[x + i][y + j].setIcon(ACTIVEIMG);
                    }
                }
            }
        }
    }

    /**
     * Dehighlight all tiles
     */
    void dehighlightAll() {
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                if (!buttons[x][y].isExposed() && !buttons[x][y].isFlagged()) {
                    buttons[x][y].setIcon(TILEIMG);
                }
            }
        }
    }

    /**
     * Updates the score panel, refreshing highscores.
     */
    void updateHighscores() { 
        mainPanel.remove(scorePanelWrapper); 
        
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
        scorePanel.add(new JLabel("Hall of Fame"));
        ArrayList<String> topThree = scoreHandler.getTopScores();
        for (String score : topThree) {
            scorePanel.add(new JLabel(score));
        }
        scorePanel.add(new JLabel(" "));
        scorePanel.add(new JLabel("Recent Games"));
        ArrayList<String> highscores = scoreHandler.getHighscores(getMode());
        scoreLabels = new JLabel[highscores.size()];
        for (int i = 0; i < highscores.size(); i++) {
            scoreLabels[i] = new JLabel(highscores.get(i));
            scorePanel.add(Box.createHorizontalGlue());
            scorePanel.add(scoreLabels[i]);
            scorePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        scorePanel.add(new JLabel(" "));
        scorePanel.add(new JLabel("Average Time"));
        scorePanel.add(new JLabel(mode + " | " + scoreHandler.getAverageTime(mode)));
        GridBagConstraints scorePanelConstraints = new GridBagConstraints();
        scorePanelConstraints.gridx = 2;
        scorePanelConstraints.gridy = 1;
        scorePanelConstraints.insets = new Insets(0, 5, 0, 5);
        scorePanelConstraints.anchor = GridBagConstraints.LINE_END;
        scorePanelWrapper = new JScrollPane(scorePanel); // Rebinds the scroll pane to the score panel
        mainPanel.add(scorePanelWrapper, scorePanelConstraints);
        scorePanelWrapper.getViewport().setPreferredSize(new Dimension(170, gridPanel.getHeight()));
        scorePanelWrapper.setVisible(isScorePanelVisible);
        frame.pack();
        
    }

    /**
     * Helper method to create a scaled square image icon
     *
     * @return ImageIcon
     * @param file an image file to convert
     */
    private ImageIcon generateIcon(String fileName) {
        return new ImageIcon(new ImageIcon(getClass().getResource(fileName)).getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
    }

    private ImageIcon generateIcon(String file, int size) {
        return new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }

    /**
     * Restarts the game with new sizes and mines
     *
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
        gridPanel.setLayout(new GridLayout(rows, cols, 0, 0));
        buildButtons();
        GridBagConstraints gridPanelConstraints = new GridBagConstraints();
        gridPanelConstraints.gridx = 1;
        gridPanelConstraints.gridy = 1;
        gridPanelConstraints.anchor = GridBagConstraints.CENTER;
        mainPanel.add(gridPanel, gridPanelConstraints);
        
        timer.start();
        frame.pack();
        updateHighscores(); //after packing as high score panel needs height of gridPanel
        frame.setLocationRelativeTo(null); // Starts Frame in Center
    }

    /**
     * Initializes images
     */
    void initializeImages() {
        TILEIMG = generateIcon("/images/tile.png");
        FLAGIMG = generateIcon("/images/flag.png");
        BOMBIMG = generateIcon("/images/mine.png");
        ACTIVEIMG = generateIcon("/images/active.png");
        RESTARTIMG = generateIcon("/images/restart.png");
        CURSORIMG = generateIcon("/images/cursor.png");
        FLOWERIMG = generateIcon("/images/flower.png");
    }

    /**
     * Builds JPanel and JFrame objects
     */
    void initializeFrame() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(rows, cols, 0, 0));
        settingPanel = new JPanel();
        settingPanel.setLayout(new FlowLayout());
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
        scorePanelWrapper = new JScrollPane(scorePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        achievementPanel = new JPanel();
        achievementPanel.setLayout(new BoxLayout(achievementPanel, BoxLayout.PAGE_AXIS));

        //Setting main panel
        GridBagConstraints infoPanelConstraints = new GridBagConstraints();
        infoPanelConstraints.gridx = 1;
        infoPanelConstraints.gridy = 0;
        infoPanelConstraints.anchor = GridBagConstraints.PAGE_START;
        GridBagConstraints gridPanelConstraints = new GridBagConstraints();
        gridPanelConstraints.gridx = 1;
        gridPanelConstraints.gridy = 1;
        gridPanelConstraints.anchor = GridBagConstraints.CENTER;
        GridBagConstraints settingPanelConstraints = new GridBagConstraints();
        settingPanelConstraints.gridx = 1;
        settingPanelConstraints.gridy = 2;
        settingPanelConstraints.anchor = GridBagConstraints.PAGE_END;
        GridBagConstraints scorePanelConstraints = new GridBagConstraints();
        scorePanelConstraints.gridx = 2;
        scorePanelConstraints.gridy = 1;
        scorePanelConstraints.insets = new Insets(0, 5, 0, 5);
        scorePanelConstraints.anchor = GridBagConstraints.LINE_END;
        GridBagConstraints achievementPanelConstraints = new GridBagConstraints();
        achievementPanelConstraints.gridx = 0;
        achievementPanelConstraints.gridy = 1;
        achievementPanelConstraints.insets = new Insets(0, 5, 0, 5);
        achievementPanelConstraints.anchor = GridBagConstraints.LINE_START;

        mainPanel.add(infoPanel, infoPanelConstraints);
        mainPanel.add(gridPanel, gridPanelConstraints);
        mainPanel.add(settingPanel, settingPanelConstraints);
        mainPanel.add(scorePanelWrapper, scorePanelConstraints);
        mainPanel.add(achievementPanel, achievementPanelConstraints);

        //Setting grid panel
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
        restartButton = new MenuButton("Restart", tileSize, tileSize);
        restartButton.setText(null);
        restartButton.setIcon(RESTARTIMG);
        restartButton.addMouseListener(new MenuButtonHandler(this));
        restartButton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        restartButton.setBackground(new Color(0, 0, 0, 0));
        restartButton.setOpaque(false);
        restartButton.setRolloverEnabled(false);

        toggleAchievementPanelButton = new MenuButton("<");
        isAchievementPanelVisible = true;
        toggleScorePanelButton = new MenuButton(">");
        isScorePanelVisible = true;
        
        toggleAchievementPanelButton.addMouseListener(new MenuButtonHandler(this));
        toggleScorePanelButton.addMouseListener(new MenuButtonHandler(this));
        infoPanel.add(toggleAchievementPanelButton);
        infoPanel.add(timerLabel);
        infoPanel.add(restartButton);
        infoPanel.add(minesLabel);
        infoPanel.add(toggleScorePanelButton);

        //Setting settings panel
        menuButtons = new MenuButton[3];
        menuButtons[0] = new MenuButton("Beginner", 77, 24);
        menuButtons[1] = new MenuButton("Intermediate", 91, 24);
        menuButtons[2] = new MenuButton("Expert", 68, 24);
        for (MenuButton menuButton : menuButtons) {
            settingPanel.add(menuButton);
            menuButton.addMouseListener(new MenuButtonHandler(this));
        }

        //Setting score panel
        scorePanel.add(new JLabel("Hall of Fame"));
        ArrayList<String> topThree = scoreHandler.getTopScores();
        for (String score : topThree) {
            scorePanel.add(new JLabel(score));
        }
        scorePanel.add(new JLabel(" "));
        scorePanel.add(new JLabel("Recent Games"));
        ArrayList<String> highscores = scoreHandler.getHighscores(getMode());
        scoreLabels = new JLabel[highscores.size()];
        for (int i = 0; i < highscores.size(); i++) {
            scoreLabels[i] = new JLabel(highscores.get(i));
            scorePanel.add(Box.createHorizontalGlue());
            scorePanel.add(scoreLabels[i]);
            scorePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        scorePanel.add(new JLabel(" "));
        scorePanel.add(new JLabel("Average Time"));
        scorePanel.add(new JLabel(mode + " | " + scoreHandler.getAverageTime(mode)));

        //Setting achievement panel
        achievementPanel.add(new JLabel("Music"));
        achievementPanel.add(new SoundButton("Athletic", sounds[0], new SoundButtonHandler(this)));
        achievementPanel.add(new SoundButton("Flower Garden", sounds[1], new SoundButtonHandler(this)));
        achievementPanel.add(new SoundButton("Fever", sounds[2], new SoundButtonHandler(this)));
        achievementPanel.add(new SoundButton("Lullaby", sounds[3], new SoundButtonHandler(this)));
        achievementPanel.add(new SoundButton("Underwater", sounds[4], new SoundButtonHandler(this)));
        achievementPanel.add(new SoundButton("Overworld", sounds[5], new SoundButtonHandler(this)));
        achievementPanel.add(new JLabel(" ")); //Spacing
        achievementPanel.add(new JLabel("Change Flavor"));
        achievementPanel.add(new ThemeButton(BOMBIMG, FLOWERIMG, new ThemeButtonHandler(this)));

        //Setting frame
        frame = new JFrame("Mariosweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        scorePanelWrapper.getViewport().setPreferredSize(new Dimension(SCORE_PANEL_WIDTH, gridPanel.getHeight()));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Starts Frame in Center
        frame.setIconImage(ACTIVEIMG.getImage());
        frame.setCursor(cursor);
        frame.pack();
        frame.setLocationRelativeTo(null); // Starts Frame in Center

    }
    /** Loads sounds
     * 
     */
    void initializeSounds() {
        sounds = new Sound[]{
            new Sound("/sounds/athletic.wav"),
            new Sound("/sounds/flowergarden.wav"),
            new Sound("/sounds/fever.wav"),
            new Sound("/sounds/lullaby.wav"),
            new Sound("/sounds/underwater.wav"),
            new Sound("/sounds/overworld.wav")
        };
    }

    public Mariosweeper() {
        // Setting constants
        try { //Allows files to be run from both .jar and inside IDE.
            scoreHandler = new HighscoreHandler("highscores.sav");
        } catch (FileNotFoundException ex) {
            try {
                scoreHandler = new HighscoreHandler(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replace("/target/mariosweeper-1.0.jar", "/highscores.sav") );
                ex.printStackTrace();
            } catch (URISyntaxException ex1) {
                ex1.printStackTrace();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        rows = 8;
        cols = 8;
        NUM_MINES = 10;
        totalTiles = rows * cols;
        tilesExposed = 0;
        tilesFlagged = 0;
        isPlaying = false;
        setMode(BEGINNER);

        initializeImages();
        activeMineImage = BOMBIMG;

        initializeSounds();
        initializeFrame();
        cursor = Toolkit.getDefaultToolkit().createCustomCursor(CURSORIMG.getImage(), new Point(0, 0), "Cursor");
        frame.setCursor(cursor);
        currentSound = sounds[0];
        sounds[0].playMusic();
    }
}
