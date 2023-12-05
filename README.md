# COMP2042_CW_hfyeh2

***Compilation Instructions:***
Step 1) Press `Code` dropdown menu and download zip file.

Using Intellij:
Step 2) Import project into workspace.

Step 3) Go to File > Project Structure > Global Libraries > Click on '+' icon > Select javafx-sdk lib > Press 'Apply' then 'OK'

Step 4) Configure Java SDK, select the preferred version

Step 5) Press 'Configure Maven Tools'

Step 6) Go to `Main.java`, press Green Triangle to build, then run the game.

***Implemented and Working Properly:***
1) Added Background Image
2) Added Background Sound
3) Added Game Start Menu
4) Added image effects when the ball hits the heart block, deducted heart, bonus ball, golden ball block
5) Added sound effects when the ball hits the heart block, deducted heart, bonus ball, golden ball block
6) Added pause button
7) Changes font style and background color to heart, score, level label
8) Shake stage when heart is deducted

***Implemented but Not Working Properly:***
1) Pause button menu

***Features Not Implemented:***
1) Special block multiplier
2) Load Game function
3) Save Game function

***New Java Classes:***

***Modified Java Classes:***

***Unexpected Problems:***
1) JavaFX runtime error (NullExceptionPointer)
2) Ball would freeze but the game is playing in the background
3) Sound effects would occasionally either be played halfway or not at all
4) The game would end randomly crash when the ball hit the bottom, causing the heart to be deducted even though the heart counter was many
