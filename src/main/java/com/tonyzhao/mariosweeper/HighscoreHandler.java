package com.tonyzhao.mariosweeper;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.*;

/** Handles reading and writing of high scores to a file
 * 
 * @author Tony Zhao
 */
public class HighscoreHandler {
    private BufferedReader reader;
    private BufferedWriter writer;
    private Date date;
    private DateFormat dateFormat;
    private String fileLocation;
    /** Creates a HighscoreHandler
     * 
     * @param fileLocation location of highscores.txt 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public HighscoreHandler(String fileLocation) throws FileNotFoundException, IOException {
        this.fileLocation = fileLocation;
        reader = new BufferedReader(new FileReader(fileLocation));
        writer = new BufferedWriter(new FileWriter(fileLocation, true)); //second argument appends lines
        
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    }
    
    /** Adds a new entry to the high score file
     * 
     * @param time clear time
     * @param mode difficulty
     */
    void addHighscore(int time, String mode) {
        date = new Date();
        
        try {
            writer.write(mode + " | " + Integer.toString(time) + " | " + dateFormat.format(date));
            writer.newLine();
            writer.close();
            writer = new BufferedWriter(new FileWriter(fileLocation, true)); //Reopens file
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Highscore added!");
    }
    /** Gets the 7 most recent games played in a category
     * 
     * @param category 
     * @return ArrayList<String> the 7 most recent games
     */
    ArrayList<String> getHighscores(String category) {
        ArrayList<String> result = new ArrayList<>();
        String currentLine;
        try {
            while ((currentLine = reader.readLine()) != null) { // Reads until end of file
                if (currentLine.matches("^" + category + ".*")) { //Checks category matches entry
                    result.add(currentLine);
                }
            }
            reader.close();
            reader = new BufferedReader(new FileReader(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    /** Gets the average time of the last five clears
     * 
     * @param category
     * @return String the average time of the last five clears
     */
    String getAverageTime(String category) {
        double result = 0;
        Pattern scorePattern = Pattern.compile("(?<=| )\\d+"); //Matches first number after |
        Matcher scoreMatcher;
        
        ArrayList<String> scores = getHighscores(category);
        if (scores.size() > 5) {
            for (int i = 1; i <= 5; i++) {
                String line = scores.get(scores.size() - i);
                scoreMatcher = scorePattern.matcher(line);
                if (scoreMatcher.find()) {
                    result += Double.parseDouble(scoreMatcher.group());
                }
            }
            result /= 5;
            return Double.toString(result) + " ao5";
        } else {
            for (int i = 1; i <= scores.size(); i++) {
                String line = scores.get(scores.size() - i);
                scoreMatcher = scorePattern.matcher(line);
                if (scoreMatcher.find()) {
                    result += Double.parseDouble(scoreMatcher.group());
                }
            }
            result /= scores.size();
            return Double.toString(result) + " ao" + Integer.toString(scores.size());
        }
    }
    /** Gets the lowest times of each category
     * 
     * @return ArrayList of the lowest times
     */
    ArrayList<String> getTopScores() {
        ArrayList<String> result = new ArrayList<>();

        String currentLine;
        Pattern scorePattern = Pattern.compile("(?<=| )\\d+");
        Matcher scoreMatcher;
        ArrayList<Integer> beginnerArrayLow = new ArrayList<>();
        ArrayList<Integer> intermediateArrayLow = new ArrayList<>();
        ArrayList<Integer> expertArrayLow = new ArrayList<>();

        try {
            while ((currentLine = reader.readLine()) != null) { // Reads until end of file
                scoreMatcher = scorePattern.matcher(currentLine);
                if (scoreMatcher.find()) {
                    if (currentLine.matches("^Beginner.*")){
                        beginnerArrayLow.add(Integer.parseInt(scoreMatcher.group()));
                    } else if (currentLine.matches("^Intermediate.*")) {
                        intermediateArrayLow.add(Integer.parseInt(scoreMatcher.group()));
                    } else if (currentLine.matches("^Expert.*")) {
                        expertArrayLow.add(Integer.parseInt(scoreMatcher.group()));
                    }
                }
            }
            if (beginnerArrayLow.size() > 0) {
                result.add("Beginner | " + Integer.toString(Collections.min(beginnerArrayLow)));
            }
            if (intermediateArrayLow.size() > 0) {
                result.add("Intermediate | " +Integer.toString(Collections.min(intermediateArrayLow)));
            }
            if (expertArrayLow.size() > 0) {
                result.add("Expert | " +Integer.toString(Collections.min(expertArrayLow)));
            }
            reader.close();
            reader = new BufferedReader(new FileReader(fileLocation));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /** Allows to get specific number of highscores 
     * 
     * @param category
     * @param numberOfEntries
     * @return 
     */
    ArrayList<String> getHighscores(String category, int numberOfEntries){
        ArrayList<String> result = getHighscores(category);
        return new ArrayList<String>(result.subList(0, numberOfEntries - 1));
    }
    
}