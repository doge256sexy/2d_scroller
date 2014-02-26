package agame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

//This class loads the image library. It loads the sprites. It reads files.
public class fileLoader {

    final static String source = "/res/";
    private final String levelSource = source + "levels/";
    public int levelTobeLoaded = 1;

    public static HashMap imageLib = new HashMap();
    public static HashMap<String, Sprite> spriteLib = new HashMap();

    public fileLoader() {
    }

    public void loadLevel() {
        loadImageLibrary();
        loadDynamicSprites();
        loadStaticSprites();
    }

    private void loadDynamicSprites() {
        HashMap h = readValueList(getLevelDirectory() + "sprites.dat");
        for (Object key : h.keySet()) {
            h.put(key, new Sprite((HashMap) h.get(key)));
        }
        spriteLib.putAll(h);
    }

    private void loadStaticSprites() {
        HashMap h = readValueList(source + "static/sprites.dat");
        for (Object key : h.keySet()) {
            h.put(key, new Sprite((HashMap) h.get(key)));
        }
        spriteLib.putAll(h);
    }

    //reads from a file. Each hashmap has its own line, so the whole file in turn is a hashmap
    public HashMap readValueList(String path) {
        ArrayList<String[]> spritesData = readFile(path, " ");
        HashMap j = new HashMap();
        for (String[] i : spritesData) {
            HashMap h = readVariableData(i);
            j.put(h.get("name"), h);
        }
        return j;
    }

    private static HashMap readVariableData(String[] qualities) {
        HashMap attributes = new HashMap();
        for (String s : qualities) {
            String[] oneQuality = s.split("=");
            attributes.put(oneQuality[0], oneQuality[1]);
        }
        return attributes;
    }

    //returns the directory of the current level
    private String getLevelDirectory() {
        return levelSource + "level_" + levelTobeLoaded + "/";
    }

    public Player getPlayer() {
        HashMap h = readValueList(source + "static/phys/playerPhys.dat");
        h = (HashMap) h.get("playerPhys");
        HashMap m = readValueList(getLevelDirectory() + "playerInfo.dat");
        m = (HashMap) m.get("playerInfo");
        return new Player(h, m);
    }

    private void loadImageLibrary() {
        imageLib.clear();
        addImageListFromFile(getLevelDirectory() + "image.dat");
        addImageListFromFile(source + "static/" + "image.dat");
    }

    private void addImageListFromFile(String path) {
        ArrayList<String[]> imageList = readFile(path, " ");
        for (String[] i : imageList) {
            preparePicture(i[0], i[1]);
        }
    }

    //flips the picture and adds it to the images library
    private void preparePicture(String picturePath, String name) {
        try {
            imageLib.put(name, new Image(picturePath).getFlippedCopy(false, true));
        } catch (SlickException ex) {
            System.out.println("no picture at " + picturePath + " which would be named " + name);
        }
    }

//returns an ArrayList of String[]'s. Each String[] is determined by each own line in the file.
    public ArrayList<String[]> readFile(String fileName, String split) {
        ArrayList<String[]> dataList = new ArrayList<>();
        InputStream is = this.getClass().getResourceAsStream(fileName);
        System.out.println(fileName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"))) {
            // Read br and store a line in 'data', print data
            String data;
            
            while ((data = br.readLine()) != null) {
                //data = br.readLine( );
                String[] line = data.split(split);
                if (line != null) {
                    dataList.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("file not work");
        }
        return dataList;
    }
}
