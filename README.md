# Mariosweeper
Mario-themed minesweeper clone, written in Java using Swing and AWT.  
![](https://img.shields.io/badge/build-passing-green.svg)
![](https://img.shields.io/github/last-commit/tawnyzhao/mariosweeper.svg)


## About
This was written for a high school programming course, Programming 12. This was created to mimick the original WINMINE that was released with the Windows Entertainment Pack. 
![preview](screenshot.png)

## Features
There are a few extra features included.
* Music selection: choose from 6 soundtracks
* Change tile set: change the the mine image
* Detailed high score panel: view your recent games and average times  


There are some key gameplay differences between this version and original. 
* The timer starts at 0, not 1.
* Chording (double clicking/middle clicking) does NOT follow your mouse drag.  

## Requirements 
* Java Runtime Environment
* Maven

## Installation and Usage
Clone this repository:
```
git clone https://github.com/tawnyzhao/mariosweeper/
```

In the project directory, run the following in command line or terminal:
```
mvn clean package
```

Then run `target/minesweeper-1.0.jar`:
```
java -jar "${project_directory}/target/minesweeper-1.0.jar"
```

Alternatively, the project can be run in your IDE by opening Mariosweeper.java. 
NOTE: highscores.sav **must** be in the base directory. 

## Legal
This project was for educational purposes only. Images and music are property of Nintendo Co., Ltd. Use at your own risk. 

