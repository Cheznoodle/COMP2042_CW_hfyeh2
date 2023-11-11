package brickGame;

public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    private Thread timeThread;
    private volatile boolean running = true; // Flag to control the threads
    private long time = 0;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    private synchronized void Update() {
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) { // Check the flag
                    try {
                        onAction.onUpdate();
                        Thread.sleep(fps);
                    } catch (InterruptedException e) {
                        // Handle exception if needed
                    }
                }
            }
        });
        updateThread.start();
    }

    private void Initialize() {
        onAction.onInit();
    }

    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) { // Check the flag
                    try {
                        onAction.onPhysicsUpdate();
                        Thread.sleep(fps);
                    } catch (InterruptedException e) {
                        // Handle exception if needed
                    }
                }
            }
        });
        physicsThread.start();
    }

    public void start() {
        running = true; // Start the threads
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
    }

    public void stop() {
        running = false; // Signal all threads to stop
        try {
            if (updateThread != null) {
                updateThread.join(); // Wait for updateThread to finish
            }
            if (physicsThread != null) {
                physicsThread.join(); // Wait for physicsThread to finish
            }
            if (timeThread != null) {
                timeThread.join(); // Wait for timeThread to finish
            }
        } catch (InterruptedException e) {
            // Optionally, re-interrupt the thread if it's important to propagate the interrupt status
            Thread.currentThread().interrupt();
        }
    }

    private void TimeStart() {
        timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) { // Check the flag
                    try {
                        time++;
                        onAction.onTime(time);
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // Handle exception if needed
                    }
                }
            }
        });
        timeThread.start();
    }

    public interface OnAction {
        void onUpdate();
        void onInit();
        void onPhysicsUpdate();
        void onTime(long time);
    }
}
