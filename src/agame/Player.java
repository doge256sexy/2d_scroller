package agame;

import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public final class Player {

    public float xpos;
    public float ypos;
    public float xvel;
    public float yvel;
    public float airfriction;
    public float staticFriction;
    public float gravity;
    public float xAcceleration;
    public float targetXSpeed;
    public float targetXSpeed_running;
    public float jumpForce;
    boolean grounded = false;
    Image bitMask;

    int health;
    Sprite playerSprite;
    Sprite statXpos;
    Sprite statYpos;

    public Player(HashMap physics, HashMap stats){
        float ms = (Main.ticksPerSecond);
        float mss = ms * ms;
        
        airfriction = getFloat(physics.get("airfriction"));
        staticFriction = getFloat(physics.get("staticFriction"));
        gravity = getFloat(physics.get("gravity"))/ mss;
        targetXSpeed = getFloat(physics.get("targetXSpeed")) / ms;
        targetXSpeed_running = getFloat(physics.get("targetXSpeed_running")) / ms;
        xAcceleration = getFloat(physics.get("xAcceleration")) / mss;
        jumpForce = getFloat(physics.get("jumpForce")) / mss;
        
        xpos = getFloat(stats.get("xpos"));
        ypos = getFloat(stats.get("ypos"));
        xvel = getFloat(stats.get("xvel"));
        yvel = getFloat(stats.get("yvel"));       
        health = Integer.parseInt(String.valueOf(stats.get("health")));
        
        playerSprite = fileLoader.spriteLib.get("player");
        
        
    }
    
     public float getFloat(Object o){
        return Float.parseFloat(String.valueOf(o));
    }
    
    public void update() {
        processKeyboard();
        move();
        updateSprites();
    }
    
    private void updateSprites(){
    
    }

    void move() {
        xpos += xvel;
        yvel -= gravity;

        collision();

        if (grounded == true) {
            xvel *= staticFriction;
        }
        xvel *= airfriction;
        yvel *= airfriction;

    }

    private void collision() {

        bitMask = Renderer.getImage("bitmapcollision");
        if (getWhites(-1, 0)) {
            grounded = true;
            yvel = 0;
        } else {
            ypos += yvel;
            if (ypos + yvel < 0) {
                ypos = 0;
                yvel = 0;
                grounded = true;
            } else {
                grounded = false;
            }
        }
        if (getWhites(0,0)){
            ypos++;
        }
    }

    private boolean getWhites(int x, int y) {
        try {
            Color c = bitMask.getColor((int) xpos + x, (int) ypos + y);
            boolean b = (c.equals(Color.white));
            return b;

        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

    }

    public void processKeyboard() {
       boolean xSpeedCap = (+xvel < (Keyboard.isKeyDown(Keyboard.KEY_Z)? targetXSpeed_running : targetXSpeed));

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            if (grounded) {
                if (xSpeedCap) {
                    xvel += xAcceleration;
                }
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            if (grounded) {
                if (xSpeedCap) {
                    xvel -= xAcceleration;
                }
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
            if (grounded == true) {
                yvel += jumpForce;
            }
        }
    }

    public Sprite getSprite() {
        return playerSprite;
    }

}
