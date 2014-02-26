/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package agame;

/**
 *
 * @author Greg
 */
//provides frame of view
public class Camera extends Renderer {

    double xpos;
    double ypos;
    double bottomY;
    double topY;
    double leftX;
    double rightX;
    int lowerBound = 0;

    //@vanishingPoint(x,y) the point of the vanishing point in relation to the screen.
    float vanishingPointX = display_width / 2;
    float vanishingPointY = display_height / 2;

    public Camera() {
        xpos = 0;
        ypos = 0;
    }

    public void setVanishingPoint(float x, float y) {
        vanishingPointX = x;
        vanishingPointY = y;
    }

    public void doStuff() {
        Player peep = Main.player;
        Sprite player = peep.getSprite();
        player.setpos(peep.xpos, peep.ypos);
        follow(player);
    }

    public void follow(Sprite t) {
        xpos = (double) (t.criteria.get("xpos")) * pixelToCoordinateRatio;
        ypos = (double) (t.criteria.get("ypos")) * pixelToCoordinateRatio;
        Collision();
    }

    private void Collision() {
        bottomY = ypos - ((display_height / 2) / zoom);
        topY = ypos + ((display_height / 2) / zoom);
        leftX = xpos - ((display_width / 2) / zoom);
        rightX = xpos + ((display_width / 2) / zoom);
        if (bottomY < lowerBound) {
            ypos = lowerBound + (vanishingPointY / zoom);
        }
    }
}
