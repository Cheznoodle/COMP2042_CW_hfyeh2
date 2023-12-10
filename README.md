
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
1. Configure Maven Settings:
* In IntelliJ IDEA, go to `File > Settings` to open the settings dialog.

2. Search for "Maven":
* In the search bar, type "Maven" to quickly locate the Maven settings.

3. Check Maven Home Directory:
* Verify that the "Maven home directory" is correctly set to the path where Maven is installed on your system. IntelliJ IDEA usually detects this automatically.

4. Click "Apply" and "OK":
* Click the `Apply` button to save the Maven settings and then click `OK` to close the settings dialog.

## Step 6: Load Maven Libraries
1. Open the Maven Tool Window:
* In the right sidebar, click on the "Maven" tab to open the Maven tool window.

2. Reimport Dependencies:
* Click on the circular arrow icon ("Reimport") in the Maven tool window. This will trigger IntelliJ IDEA to fetch and load all the project's Maven dependencies.

3. Wait for Dependencies to Download:
* IntelliJ IDEA will download the necessary libraries and dependencies specified in the project's pom.xml file. Wait for this process to complete.

4. Verify Dependencies:
* You can expand the "External Libraries" section in the Project view to see the loaded Maven dependencies.

## Step 7: Build and Run the Project

# Implemented and Working Properly:
1) Added Background Image.
2) Added Background Sound.
3) Added Game Start Menu.
4) Added image effects when the ball hits the heart block, deducted heart, bonus ball, and golden ball block.
5) Added sound effects when the ball hits the heart block, deducted heart, bonus ball, and golden ball block.
6) Added a pause button when the user presses the 'ESC' key.
7) Changes font style and background color to heart, score, and level label.
8) Shake stage when the heart is deducted.
9) Mute sound button when the user presses the 'M' key.
10) Ball spawn is fixed to the center of the scene.
11) Added more interactive start menu buttons.
12) Added sound effects when the user hovers over a button.

# Implemented but Not Working Properly:
1) Pause button menu (pause menu will not display)
2) Load Game function (The game will skip levels occasionally after loading a saved game and levels may not progress to the next level even though all blocks are hit)
3) Save Game function

# Features Not Implemented:
1) Special block multiplier (e.g. 2x multiplier)
2) Timer

# New Java Classes:
1) GameController.java (handles user input/control)
2) SoundEffectUtil.java (handles power-up sound effects)
3) ImageEffectUtil.java (handles power-up effects)
4) StageEffectUtil.java (handles stage scene effects)
5) UserInterface.java (handles VBox menu's)

# Modified Java Classes:
1) Main.java
2) Block.java
3) BlockSerializable.java
4) Bonus.java
5) LoadSave.java
6) Score.java
7) GameEngine.java

***Unexpected Problems:***
1) JavaFX runtime error (NullExceptionPointer)
2) The ball would freeze but the game is playing in the background.
3) Sound effects would occasionally be played halfway or not at all.
4) The game would end randomly crashing when the ball hit the bottom, causing the heart to be deducted even though the heart counter was many.
5) The ball velocity is faster After loading a save game file. Also, the levels would occasionally skip from a certain level to a much higher level.
