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
8) Shake stage when the heart is deducted
9) Mute sound button when the user presses 'M'
10) Ball spawn is fixed to the center of the scene
11) Added more interactive start menu buttons

***Implemented but Not Working Properly:***
1) Pause button menu
2) Load Game function
3) Save Game function

***Features Not Implemented:***
1) Special block multiplier

***New Java Classes:***

***Modified Java Classes:***

***Unexpected Problems:***
1) JavaFX runtime error (NullExceptionPointer)
2) The ball would freeze but the game is playing in the background.
3) Sound effects would occasionally be played halfway or not at all.
4) The game would end randomly crashing when the ball hit the bottom, causing the heart to be deducted even though the heart counter was many.
5) The ball velocity is faster After loading a save game file. As well as the levels would occasionally skip from a certain level to a much higher level.
