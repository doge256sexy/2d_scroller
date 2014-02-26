package agame;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.LWJGLException;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;
import static org.newdawn.slick.Image.FILTER_NEAREST;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.ImageIOImageData;

//This class handles all the rendering done. 
public class Renderer {

    private Camera camera;
    public static int display_width = 600;
    public static int display_height = 480;
    float pixelToCoordinateRatio = 1;
    float zoom = 1;
    TrueTypeFont trueTypeFont;

    public Renderer() {

    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        camera.doStuff();
        drawStack();
        testResize();
        Display.update();
    }

    public void init() {
        //OpenGL
        initGL();
        resizeGL();

        camera = new Camera();
    }

    public void createDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(display_width, display_height));
            Display.setResizable(true);
            Display.setFullscreen(false);
            Display.setTitle(Main.title);

            setIcon("src/res/static/icons/gameIcon.png", "src/res/static/icons/gameIcon.png");

            Display.create();
        } catch (LWJGLException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setIcon(String bigpic, String smallpic) {
        try {
            Display.setIcon(new ByteBuffer[]{
                new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File(bigpic)), false, false, null),
                new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File(smallpic)), false, false, null)
            });
        } catch (IOException ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resizeGL() {

        //2D Scene
        glViewport(0, 0, display_width, display_height);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f, display_width, 0.0f, display_height);
        glPushMatrix();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glPushMatrix();

    }

    private void initGL() {
        //2D Initialization
        glClearColor(0.5f, 0.6f, 0.8f, 0.2f);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        //transparent pngs
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
    }

    private void testResize() {
        if (Display.wasResized()) {
            display_width = Display.getWidth();
            display_height = Display.getHeight();
            camera.setVanishingPoint(display_width / 2, display_height / 2);
            resizeGL();
        }
    }

    public void drawStack() {
        Sprite.sortSpriteStack();
        drawWorld();
        drawText();
    }

    public void drawWorld() {
        for (Sprite s : Sprite.stack) {
            renderSprite(s.criteria);
        }
    }

    private void drawText() {
        glLoadIdentity();
        glScaled(1, -1, 1);
        for (Sprite s : Sprite.textStack) {
            trueTypeFont = new TrueTypeFont(new Font("comic sans", Font.PLAIN, (int) 10), true);
            dString((String) s.criteria.get("type"), (float) s.criteria.get("xpos"), (float) s.criteria.get("ypos"), trueTypeFont);
        }

    }

    //draws text somewhere on the screen
    private void dString(String s, float x, float y, TrueTypeFont font) {
        font.drawString(x, -y, s, Color.black);
    }
    
    public float getFloat(Object o){
        return Float.parseFloat(String.valueOf(o));
    }

    //renders a given sprite on the screen in relation to the camera
    private void renderSprite(HashMap o) {
        glLoadIdentity();
        Image currentImage = getImage((String) o.get("currentImage"));
        float scale = getFloat(o.get("scale"));
        float xpos =  getFloat(o.get("xpos"));
        float ypos =  getFloat(o.get("ypos"));
        double depth = Math.sqrt(getFloat (o.get("depth")));
        boolean centeredX = false;
        boolean centeredY = false;
        
        try {
            centeredX = (boolean) o.get("centeredX");
            centeredY = (boolean) o.get("centeredY");
        } catch (NullPointerException e) {

        }

        glLoadIdentity();

        float c = (float) zoom;
        float xposOn = (float) ((xpos - camera.xpos) / depth * pixelToCoordinateRatio * c);
        float yposOn = (float) ((ypos - camera.ypos) / depth * pixelToCoordinateRatio * c);
        float offsetX = 0;
        float offsetY = 0;

        if (centeredX == true) {
            offsetX = (float) ((currentImage.getWidth() / 2) * scale * c);
        }
        if (centeredY == true) {
            offsetY = (float) ((currentImage.getHeight() / 2) * scale * c);
        }
        xposOn -= offsetX;
        yposOn -= offsetY;

        currentImage.setFilter(FILTER_NEAREST);
        currentImage.draw(camera.vanishingPointX + xposOn, camera.vanishingPointY + yposOn, scale * c);
    }
    
    public static Image getImage(String name) {       
        return (Image) fileLoader.imageLib.get(name);
    }    
}
