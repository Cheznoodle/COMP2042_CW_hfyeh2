package brickGame;

/**
 * Represents the game engine for a brick game.
 * Manages game logic, rendering, physics, and timing through separate threads.
 */
public class GameEngine {

    private OnAction onAction;
    private int fps = 60;

    private Thread updateThread;
    private Thread renderThread;
    private Thread physicsThread;
    private Thread timeThread;

    private volatile boolean running = false; // Flag to control the threads
    private long time = 0;

    /**
     * Sets the action listener for game updates.
     * @param onAction Interface instance containing methods for game update actions.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the frames per second for the game.
     * @param fps Frames per second.
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    /**
     * Starts the update thread for handling game logic.
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
     * Initializes the game state using the action listener.
     */
    private void Initialize() {
        onAction.onInit();
    }

    /**
     * Starts the thread for physics calculations in the game.
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
     * Starts the game engine, initiating threads for updating game state, rendering, and physics calculations.
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
     * Stops the game engine, terminating all threads in a graceful manner.
     */
    public void stop() {
        running = false;
        joinThread(updateThread);
        joinThread(renderThread);
        joinThread(physicsThread);
        joinThread(timeThread);
    }

    /**
     * Safely joins a given thread, ensuring it stops gracefully.
     * @param thread The thread to join.
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
     * Starts the rendering thread for the game's graphical output.
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
     * Manages and updates the in-game time.
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
     * Interface defining methods for handling various actions during the game's execution.
     * Methods include updates for game logic, rendering, initialization, physics, and time tracking.
     */
    public interface OnAction {
        void onUpdate();
        void onRender();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }
}
