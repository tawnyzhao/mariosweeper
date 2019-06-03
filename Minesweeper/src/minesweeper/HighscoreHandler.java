package minesweeper;

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

public class HighscoreHandler {
    private BufferedReader reader;
    private BufferedWriter writer;
    private Date date;
    private DateFormat dateFormat;
    private String fileLocation;

    public HighscoreHandler(String fileLocation) {
        this.fileLocation = fileLocation;
        try {
            reader = new BufferedReader(new FileReader(fileLocation));
            writer = new BufferedWriter(new FileWriter(fileLocation, true)); //second argument appends lines
        } catch (IOException e) {
            e.printStackTrace();
        }
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    }

    void addHighscore(int time, String mode) {
        date = new Date();
        
        try {
            writer.write(mode + " | " + Integer.toString(time) + " | " + dateFormat.format(date));
            writer.newLine();
            writer.close();
            writer = new BufferedWriter(new FileWriter(fileLocation, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Highscore added!");
    }
    
    ArrayList<String> getHighscores(String category) {
        ArrayList<String> result = new ArrayList<>();
        String currentLine;
        try {
            while ((currentLine = reader.readLine()) != null) { // Reads until end of file
                if (currentLine.matches("^" + category + ".*")) {
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

    double averageTime(String category) {
        double result = 0;
        Pattern scorePattern = Pattern.compile("(?<=| )\\d+");
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
        } else {
            for (int i = 1; i <= scores.size(); i++) {
                String line = scores.get(scores.size() - i);
                scoreMatcher = scorePattern.matcher(line);
                if (scoreMatcher.find()) {
                    result += Double.parseDouble(scoreMatcher.group());
                }
            }
            result /= scores.size();
        }
        return result;
    }

    ArrayList<String> getTopScores() {
        ArrayList<String> result = new ArrayList<>();

        String currentLine;
        Pattern scorePattern = Pattern.compile("(?<=| )\\d+");
        Matcher scoreMatcher;
        ArrayList<Integer> beginnerArray = new ArrayList<>();
        ArrayList<Integer> intermediateArray = new ArrayList<>();
        ArrayList<Integer> expertArray = new ArrayList<>();

        try {
            while ((currentLine = reader.readLine()) != null) { // Reads until end of file
                scoreMatcher = scorePattern.matcher(currentLine);
                if (scoreMatcher.find()) {
                    if (currentLine.matches("^Beginner.*")){
                        beginnerArray.add(Integer.parseInt(scoreMatcher.group()));
                    } else if (currentLine.matches("^Intermediate.*")) {
                        intermediateArray.add(Integer.parseInt(scoreMatcher.group()));
                    } else if (currentLine.matches("^Expert.*")) {
                        expertArray.add(Integer.parseInt(scoreMatcher.group()));
                    }
                }
            }
            if (beginnerArray.size() > 0) {
                result.add("Beginner | " + Integer.toString(Collections.min(beginnerArray)));
            }
            if (intermediateArray.size() > 0) {
                result.add("Intermediate | " +Integer.toString(Collections.min(intermediateArray)));
            }
            if (expertArray.size() > 0) {
                result.add("Expert | " +Integer.toString(Collections.min(expertArray)));
            }
            reader.close();
            reader = new BufferedReader(new FileReader(fileLocation));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    ArrayList<String> getHighscores(String category, int numberOfEntries){
        ArrayList<String> result = getHighscores(category);
        return new ArrayList<String>(result.subList(0, numberOfEntries - 1));
    }
    
}