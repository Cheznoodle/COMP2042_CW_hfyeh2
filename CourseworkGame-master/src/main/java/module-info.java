module brickGame {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.logging;
    requires javafx.media;
    opens brickGame to javafx.fxml;
    exports brickGame;
    exports brickGame.Model;
    opens brickGame.Model to javafx.fxml;
}