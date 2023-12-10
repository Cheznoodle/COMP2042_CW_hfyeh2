# Brick Breaker Game

## Description:

Brick Game is a JavaFX-based application where users can play a brick-breaking game with various levels and challenges.

# Prerequisites

* Ensure Java JDK 19 is installed on your system. If not, download and install it from the [Oracle website](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html) or any other preferred source
* Internet connection for downloading software and dependencies.

## Step 1: Installing IntelliJ IDEA

1. Download IntelliJ IDEA:
* Visit the [JetBrains Website](https://www.jetbrains.com/idea/download/?section=windows) and download the latest version of IntelliJ IDEA (Community or Ultimate Edition).

2. Install IntelliJ IDEA:
* Run the downloaded installer and follow the installation prompts.

## Step 2: Configuring JavaFX SDK

1. Download JavaFX SDK:
* Visit the [OpenJFX Website](https://openjfx.io/) and download the latest JavaFX SDK.

2. Extract JavaFX SDK:
* Extract the downloaded ZIP file to a desired location on your system.

3. Configure JavaFX in IntelliJ IDEA:
* Open IntelliJ IDEA.
* Create a new project or open an existing one.
* Go to `File > Project Structure > Libraries`.
* Click the `+` sign and select `Java`.
* Navigate to the `lib` folder inside the extracted JavaFX SDK directory, select it, and click `OK`.

## Step 3: Setting up JDK in IntelliJ IDEA

1. Open IntelliJ IDEA:
* Launch IntelliJ IDEA on your computer.

2. Access Project Structure:
* If you have a project open, go to `File > Project Structure`.
* For a new setup without a project, go to `Configure > Project Defaults > Project Structure`.

3. Add JDK 19:
* In the `Project Structure` dialog, select the `SDKs` under the `Platform Settings`.
* Click the `+` sign at the top of the pane and select `JDK`.
* In the file chooser, navigate to the location where JDK 19 is installed on your system and select the JDK’s root directory. IntelliJ IDEA will automatically detect the JDK version.
* Click `OK` to add JDK 19 to IntelliJ IDEA.

## Step 4: Configuring Your Project to Use JDK 19

1. Set Project SDK:
* Still in the `Project Structure` dialog, select `Project` from the left sidebar.
* In the `Project SDK` section, select the newly added JDK 19 from the dropdown list.

2. Set Project Language Level:
* In the same `Project` settings, adjust the `Project language level` to match JDK 19. Choose the level that provides the features you need for development.

3. Apply and Close:
* Click `Apply` and then `OK` to save the changes and close the dialog.

## Step 5: Configuring Maven
1. Configure Maven in IntelliJ:
* Go to `File > Settings`.
* Search for "Maven" and ensure the "Maven home directory" points to the correct installation path.

2. Apply and Confirm:
* Click `Apply` and `OK` to save the Maven configuration.

## Step 6: Import Maven Dependencies

1. Open Maven Tool Window:
* Use the Maven tab in the right sidebar of IntelliJ IDEA.

2. Reimport All Maven Projects:
* Click the refresh button to reimport all Maven projects. This action downloads and sets up all the necessary dependencies.

## Step 7: Build and Run the Project

1. Build the Project:
* Use IntelliJ IDEA's build feature to compile the project.

2. Run the Application:
* Execute `Main.java` from the `brickGame.Model` package to start the game.

# Implemented and Working Properly:
1) Added Background Image (A football field background is displayed as the background image).
2) Added Background Sound (The wii game music track is played as the background sound. 
3) Added Game Start Menu. (Three buttons that display "Load Game", "Start New Game", and "Exit Game" after the game just started running) 
4) Added image effects when the ball hits the heart block, deducted heart, bonus ball, and golden ball block (PNG images will display according to the power-up which is activated by the player).
5) Added sound effects when the ball hits the heart block, deducted heart, bonus ball, and golden ball block (mp3 sound effects work simultaneously some when a power-up is activated by the player).
6) Added a pause button when the user presses the 'ESC' key (The player can pause the gameplay by pressing the ESCAPE key).
7) Changes font style and background color to heart, score, and level label (Added CSS styles to change the visual style of the game).
8) Shake stage when the heart is deducted (The stage will rumble when a heart is deducted from the player).
9) Mute sound button when the user presses the 'M' key (The player can mute the sound of the game when he/she presses the M key).
10) Ball spawn is fixed to the center of the scene (Ball always spawns in the middle of the scene at the start of every level).
11) Added more interactive start menu buttons (Buttons enlarge when the player hovers over them).
12) Added sound effects when the user hovers over a button (A sound effect will play when the player hovers over a button in the start menu).

# Implemented but Not Working Properly:
1) Pause button menu (pause menu will not display).
   * Did not manage to fully utilize and convert elements using 'root' to 'layeredRoot' which allows for layering of UI elements. 
2) Load Game function
   * The game will skip levels occasionally after loading a saved game and levels may not progress to the next level even though all blocks are hit
3) Save Game function
   * Does not save power-up in the current state.
   * Example: When saving the game that has the golden ball activated. The golden ball will reset to a normal ball after loading the game using the save file.

# Features Not Implemented:
1) Special block multiplier (e.g. 2x multiplier)
   * Reason for failure of implementation:
     No sufficient time to test and implement the feature properly 
3) Timer
    * Reason for failure of implementation:
     No sufficient time to test and implement the feature properly 

# New Java Classes:
1) GameController.java (handles user input/control)
   * Location: brickGame > Controller > GameController
   
2) SoundEffectUtil.java (handles power-up sound effects)
   * Location: brickGame > Model > soundEffects > SoundEffectUtil
   
3) ImageEffectUtil.java (handles power-up effects)
   * Location: brickGame > View > imageEffects > ImageEffectUtil
   
4) StageEffectUtil.java (handles stage scene effects)
   * Location: brickGame > View > stageEffects > StageEffectUtil
   
5) UserInterface.java (handles VBox menu's)
    * Location: brickGame > View > UserInterface

# Modified Java Classes:
1) Main.java
   *Changes made:
     *Applied refactoring techniques
     *Applied lambda expressions
   *Explanation:
    *Enhanced Readability: Makes code easier to understand.
    *Reduced Complexity: Simplifies complex code structures.
    *Increased Maintainability: Easier to update and maintain.
    *Improved Performance: Optimizes code for efficiency.
    *Error Reduction: Helps identify and fix potential bugs.
    *Conciseness: Reduces boilerplate code.
    *Functional Programming: Facilitates functional programming paradigms in Java.
    *Scalability: Enhances code scalability with cleaner and more modular code.
   
3) Block.java
   *Changes made:
     *Applied encapsulation
     *Added constants
     *Added resource management
   *Explanation:
    *
5) BlockSerializable.java
   *Changes made:
     *Added a string variable called 'colorString'
   *Explanation:
     *To store the current colors of the blocks of a level when the save game function is activated
7) Bonus.java
  *Changes made:
     *Added exception handling
     *Added thread management
     *Removed code duplication
     *Applied lambda expression
     *Applied JavaFX application thread
   *Explanation:
    *
9) LoadSave.java
    *Changes made:
     *Applied data validation
     *Separate File I/O from logic
   *Explanation:
    *
11) Score.java
    *Changes made:
     *Added animation management
     *Applied method refactoring
     *Added thread safety
     *Added animation libraries
   *Explanation:
    *
13) GameEngine.java
    *Changes made:
     *Added FPS calculation
     *Separate Game Logic and Rendering
   *Explanation:
    *

# Unexpected Problems:
1) JavaFX runtime error (NullExceptionPointer)
2) The ball would freeze but the game is playing in the background.
3) Sound effects would occasionally be played halfway or not at all.
4) The game would end randomly crashing when the ball hit the bottom, causing the heart to be deducted even though the heart counter was many.
5) The ball velocity is faster After loading a save game file. Also, the levels would occasionally skip from a certain level to a much higher level.
