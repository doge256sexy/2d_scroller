package agame;

import java.util.ArrayList;
import static java.util.Collections.sort;
import java.util.HashMap;

public class Sprite implements Comparable<Sprite> {

    public static ArrayList<Sprite> stack = new ArrayList<>(); //List of Sprites needing to be rendered 
    public static ArrayList<Sprite> textStack = new ArrayList<>();  //List of text needing to be rendered

    //each sprite is just a hashmap.
    public HashMap criteria = new HashMap();

    public Sprite(HashMap attributes) {
        criteria = attributes;
        if (criteria.get("type") == "text") {
            textStack.add(this);
        } else {
            stack.add(this);
        }

    }

    public static void sortSpriteStack() {
        sort(stack);
    }

    public void setpos(double x, double y) {
        criteria.put("xpos", x);
        criteria.put("ypos", y);
    }

    public void delete() {
        stack.remove(this);
    }

    @Override
    public int compareTo(Sprite s) {
        return (int) (Float.parseFloat(s.criteria.get("depth").toString()) - Float.parseFloat(criteria.get("depth").toString()));
    }
}
