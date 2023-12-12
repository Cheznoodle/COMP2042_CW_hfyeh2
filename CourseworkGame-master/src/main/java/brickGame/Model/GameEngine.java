package brickGame.Model;

/**
 * Represents the core game engine for a brick-breaking game.
 * Manages game logic, rendering, physics, and timing through separate threads.
 * Controls the main game loop and coordinates the synchronization between different game aspects.
 */
public class GameEngine {

    private OnAction onAction; // Callback interface for game actions
    private int fps = 60; // Frames per second for the game loop
    private Thread updateThread; // Thread for updating game logic
    private Thread renderThread; // Thread for rendering graphics
    private Thread physicsThread; // Thread for physics calculations
    private Thread timeThread; // Thread for tracking in-game time
    private volatile boolean running = false; // Flag to control the game loop
    private long time = 0; // In-game time counter
    private volatile boolean isPaused = false; // Flag to manage game pause state

    /**
     * Sets the callback interface for game actions.
     *
     * @param onAction The interface containing methods for game actions.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the frames per second for the game loop.
     *
     * @param fps Desired frames per second.
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    /**
     * Initializes and starts the game update thread.
     * This thread runs the main game loop and triggers game logic updates.
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
     * Performs the initial setup for the game state.
     * This method is called once at the start of the game.
     */
    private void Initialize() {
        onAction.onInit();
    }

    /**
     * Initializes and starts the physics calculations thread.
     * This thread is responsible for handling physics-related updates in the game.
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
     * Starts the game engine.
     * Initializes and begins all necessary threads for the game logic, rendering, physics, and timing.
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
     * Gracefully terminates all threads and stops the game loop.
     */
    public void stop() {
        running = false;
        joinThread(updateThread);
        joinThread(renderThread);
        joinThread(physicsThread);
        joinThread(timeThread);
    }

    /**
     * Waits for a thread to complete its execution.
     *
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
     * Initializes and starts the rendering thread.
     * This thread is responsible for graphical output and updates for the game.
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
     * Starts the time tracking thread.
     * This thread is responsible for managing and updating the in-game time.
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
     * Pauses the game, suspending updates in all threads.
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
     *
     * @return True if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPaused;
    }


    /**
     * Interface defining callback methods for various game actions.
     * These methods are invoked during the game loop to manage updates, rendering, initialization, physics, and time tracking.
     */
    public interface OnAction {
        void onUpdate();
        void onRender();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }
}
