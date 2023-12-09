package brickGame.Model;

/**
 * Represents the game engine for a brick game.
 * Manages game logic, rendering, physics, and timing through separate threads.
 * This class controls the core loop of the game and handles synchronization between different aspects of the game.
 */
public class GameEngine {

    private OnAction onAction;
    private int fps = 60;

    private Thread updateThread;
    private Thread renderThread;
    private Thread physicsThread;
    private Thread timeThread;

    private volatile boolean running = false; // Flag to control the game loop
    private long time = 0;

    private volatile boolean isPaused = false; // Flag to manage game pause state

    /**
     * Assigns a listener for game actions.
     * @param onAction Interface instance containing callback methods for game actions.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the frames per second for the game loop.
     * @param fps Frames per second.
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    /**
     * Starts the game logic update thread.
     * Responsible for running the main game loop and triggering update actions.
     */
    private synchronized void Update() {
        updateThread = new Thread(() -> {
            while (running) {
                synchronized (this) {
                    while (isPaused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                try {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        updateThread.start();
    }

    /**
     * Performs initial setup for the game state.
     * Called once at the start of the game.
     */
    private void Initialize() {
        onAction.onInit();
    }

    /**
     * Starts the physics calculations thread.
     * Responsible for managing physics-related updates in the game.
     */
    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(() -> {
            while (running) {
                synchronized (this) {
                    while (isPaused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                try {
                    onAction.onPhysicsUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        physicsThread.start();
    }

    /**
     * Initiates and starts the game engine.
     * Starts all necessary threads for game logic, rendering, and physics calculations.
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
     * Stops the game engine.
     * Terminates all threads gracefully and stops the game loop.
     */
    public void stop() {
        running = false;
        joinThread(updateThread);
        joinThread(renderThread);
        joinThread(physicsThread);
        joinThread(timeThread);
    }

    /**
     * Joins a thread to ensure it completes its execution.
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
     * Starts the rendering thread.
     * Handles graphical output and updates for the game.
     */
    private synchronized void Render() {
        renderThread = new Thread(() -> {
            while (running) {
                synchronized (this) {
                    while (isPaused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                try {
                    onAction.onRender();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        renderThread.start();
    }

    /**
     * Manages the in-game time and updates it continuously.
     * Responsible for tracking the passage of time within the game.
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
     * Pauses the game, stopping updates in all threads.
     */
    public synchronized void pause() {
        isPaused = true;
    }

    /**
     * Resumes the game, allowing all threads to continue updates.
     */
    public synchronized void resume() {
        isPaused = false;
        notifyAll();
    }

    /**
     * Checks if the game is currently paused.
     * @return True if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }


    /**
     * Interface defining callback methods for various game actions.
     * These methods are triggered during the game loop to handle updates, rendering, initialization, physics, and time tracking.
     */
    public interface OnAction {
        void onUpdate();
        void onRender();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }
}
