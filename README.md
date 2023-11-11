# COMP2042_CW_hfyeh2
### Push: 1
No changes made

### Push: 2
**Block.java**
1) Added Encapsulation: Fields are now private with appropriate getters and setters.
2) Added Resource Management: The `loadImagePatternBasedOnType` method improves resource handling.
3) Added Use of Constants: Replaced magic numbers with constants for block types and hit types.
4) Added Static Fields: width, height, paddingTop, and paddingH are now static.
5) Added Boolean Getter: The getter for the `isDestroyed` field is named `isDestroyed()` to follow the convention for boolean getters.
6) Added Handling Rectangles: The `Rectangle` type has a getter and setter. Ensure that any changes to the `Rectangle` object are consistent with the block`s state.

**Main.java**
1) Adding Blocks to the Scene: Replace direct access to the `rect` property of `Block` with the getter method.
2) Saving Game State: When saving the game state, use getters to access the `Block` properties.
3) Updating Blocks: In the `onUpdate()` method, when you modify `Block` instances, use the setter methods.
4) Checking for Block Hits: When checking for hits against blocks, use getters to access the properties of `Block`.
5) Added getter methods

**GameEngine.java**
1) Added a Volatile Boolean Flag: Volatile boolean flag is added that indicates whether the thread should keep running.
2) Implemented a Safe Stop Method: Implemented a method in `GameEngine` to safely stop the thread by setting this flag to false.

**Score.java**
1) Replaced manual thread creation with JavaFX Timeline for animations.
2) Extracted redundant code into createLabel() and animateLabel() methods.
3) Improved code formatting for better readability.
4) Simplified the string concatenation in the show() method.

**LoadSave.java**
1) Added Logger to the class
2) Replaced `printStackTrace()` with Logging

Explanation:
- A `Logger` instance is created for the purpose of logging errors, information, and warnings.
- Instead of using `printStackTrace()` when an exception is caught, the exception is logged using the `Logger` along with a helpful message and the exception object itself. This way, you get the stack trace as well as any additional context you choose to include in the log message.

This approach is more flexible and robust compared to using `printStackTrace()`. You can further configure the `Logger` to write to files, use different formats, or adjust logging levels depending on your application's needs.