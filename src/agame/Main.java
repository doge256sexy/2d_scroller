package agame;

/**
 *
 * @author Greg
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Main {

    public static final Main main = new Main();

    public static final fileLoader level = new fileLoader();

    public static Player player;
    public static final Renderer renderer = new Renderer();

    public static String title;
    public static final int ticksPerSecond = 100;
    long lastFrame; //time at last frame   
    public int fps;  // frames per second  
    long lastFPS; //last fps time

    public static void main(String[] args) {

        try {
            while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                if (Display.isVisible()) {
                    main.update();
                } else {
                    if (Display.isDirty()) {
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                }
                Display.sync(ticksPerSecond);
            }
        } finally {
            if (main != null) {
                main.destroy();
            }
        }
    }

    //Set the title
    //create the stuff
    //initialize opengl
    //load the level
    //create the player
    static {
        
        title = new fileLoader().readFile(fileLoader.source + "static/startup/title.dat", "_").get(0)[0];

        main.create();
        renderer.init();
        level.loadLevel();
        player = level.getPlayer();
        main.startfps();
    }

    private void update() {
        renderer.render();
        player.update();
        main.updateFPS();
    }

    public Main() {
    }

    public void create() {
        try {
            renderer.createDisplay();

            //Keyboard
            Keyboard.create();

            //Mouse
            Mouse.setGrabbed(false);
            Mouse.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void destroy() {
        //Methods already check if created before destroying.
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    private void startfps() {
        getDelta();
        lastFPS = getTime();
        //initialise lastFPS by setting to current Time
    }

    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;
        return delta;
    }

    // Get the time in milliseconds     
    //@return The system time in milliseconds
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    // Calculate the FPS and set it in the title bar
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle(Main.title + " FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
}
