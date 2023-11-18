package brickGame;

public class GameEngine {

    private OnAction onAction;
    private int fps = 60;  // Set a default fps value, can be adjusted

    private Thread updateThread;
    private Thread renderThread;

    private Thread physicsThread;

    private Thread timeThread;

    private volatile boolean running = false; // Flag to control the threads
    private long time = 0;

    /**
     * Sets the action listener for game updates.
     * @param onAction Interface instance containing game update methods
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the frames per second for the game.
     * @param fps Frames per second
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    /**
     * Starts the update thread for game logic.
     */
    private synchronized void Update() {
        updateThread = new Thread(() -> {
            while (running) {
                try {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    // Optional: handle the InterruptedException
                }
            }
        });
        updateThread.start();
    }

    /**
     * Initializes the game state.
     */
    private void Initialize() {
        onAction.onInit();
    }

    /**
     * Starts the thread for physics calculations.
     */
    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(() -> {
            while (running) {
                try {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    // Optional: handle the InterruptedException
                }
            }
        });
        physicsThread.start();
    }

    /**
     * Starts the game engine, including all threads for updating game state and rendering.
     */
    public void start() {
        running = true;
        time = 0;
        Initialize();
        Update();
        Render();
        PhysicsCalculation();
        TimeStart();
    }

    /**
     * Stops the game engine, terminating all threads gracefully.
     */
    public void stop() {
        running = false;
        joinThread(updateThread);
        joinThread(renderThread);
        joinThread(physicsThread);
        joinThread(timeThread);
    }

    /**
     * Helper method to safely join a thread.
     * @param thread The thread to join
     */
    private void joinThread(Thread thread) {
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Starts the rendering thread for the game.
     */
    private synchronized void Render() {
        renderThread = new Thread(() -> {
            while (running) {
                try {
                    onAction.onRender();
                    Thread.sleep(fps); // Adjust as needed for rendering
                } catch (InterruptedException e) {
                    // Optional: handle the InterruptedException
                }
            }
        });
        renderThread.start();
    }

    /**
     * Manages the in-game time.
     */
    private void TimeStart() {
        timeThread = new Thread(() -> {
            while (running) {
                try {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // Optional: handle the InterruptedException
                }
            }
        });
        timeThread.start();
    }

    /**
     * Interface for handling various actions in the game.
     */
    public interface OnAction {
        void onUpdate();
        void onRender();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }
}
