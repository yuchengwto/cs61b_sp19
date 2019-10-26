package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;


public class Engine {

    TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    private static final int TILE_SIZE = 16;

    private Avatar avatar;
    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    private StringBuilder record = new StringBuilder();
    private static final Font slenderFont = new Font("Arial", Font.ITALIC, 20);
    private static final Font boorishFont = new Font("Monaco", Font.BOLD, 30);
    private static final Font defaultFont = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);

    public Engine() {
        ter.initialize(WIDTH, HEIGHT);
    }

    private static void save(String record) {
        File f = new File("./save_data.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(record);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private static String load() {
        File f = new File("./save_data.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (String) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return "";
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        InputSource inputSource = new KeyboardInputSource();
        drawMenu();
        interactWithMenu(inputSource);
        interactWithWorld(inputSource);
    }

    private void interactWithWorld(InputSource inputSource) {
        Position mousePos;
        while (true) {
            mousePos = new Position((int)StdDraw.mouseX(), (int)StdDraw.mouseY());
            showTileInfo(mousePos);
            if (StdDraw.hasNextKeyTyped()) {
                char c = inputSource.getNextKey();
                if (c == ':' && inputSource.getNextKey() == 'Q') {
                    save(record.toString());
                    System.exit(0);
                }
                if (c != ':') {
                    record.append(c);
                }
                avatar.move(c);
                ter.renderFrame(world);
                if (avatar.isReachDoor()) {
                    drawOver();
                    quitGame(inputSource);
                }
            }
        }
    }

    private void quitGame(InputSource inputSource) {
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            if (c == 'Q') {
                System.exit(0);
            }
        }
    }

    private void recurrentWorld(InputSource inputSource) {
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            if (c == ':' && inputSource.getNextKey() == 'Q')  {
                save(record.toString());
                break;
            }
            if (c != ':') {
                record.append(c);
            }
            avatar.move(c);
        }
    }

    private long getSeed(InputSource inputSource) {
        StringBuilder seedString = new StringBuilder();
        if (inputSource.getClass().equals(KeyboardInputSource.class)) {
            draw("Input Seed: ");
        }
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            record.append(c);
            if (c == 'S') { break; }
            seedString.append(c);
            if (inputSource.getClass().equals(KeyboardInputSource.class)) {
                draw("Input Seed: " + seedString.toString());
            }
        }
        return Long.parseLong(seedString.toString());
    }

    private void generateRandomWorld(InputSource inputSource) {
        long seed = getSeed(inputSource);
        WorldGenerator wg = new WorldGenerator(seed);
        world = wg.getMyWorld();

        // Create my avatar
        Position avatarPos = Position.randomPositon();
        while (world[avatarPos.x][avatarPos.y] != Tileset.FLOOR) {
            avatarPos = Position.randomPositon();
        }
        avatar = new Avatar(Tileset.AVATAR, avatarPos, world, ter);
        avatar.appear();
    }

    private void loadOldWorld() {
        String oldWorld = load();
        world = interactWithInputString(oldWorld);
        ter.renderFrame(world);
    }

    private void interactWithMenu(InputSource inputSource) {
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            if (c == 'N') {
                record.append(c);
                generateRandomWorld(inputSource);
                break;
            } else if (c == 'L') {
                loadOldWorld();
                break;
            } else if (c == 'Q') {
                System.exit(0);
            } else {
                throw new IllegalArgumentException("Input must be one of N, L and Q.");
            }
        }
    }

    private void drawMenu() {
        // Title
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(boorishFont);
        StdDraw.text(WIDTH/2, HEIGHT - 15, "SHABBY GAME");

        // Set options
        StdDraw.setFont(slenderFont);
        StdDraw.text(WIDTH/2, HEIGHT-25, "New Game(N)");
        StdDraw.text(WIDTH/2, HEIGHT-27, "Load Game(L)");
        StdDraw.text(WIDTH/2, HEIGHT-29, "Quit(Q)");
        StdDraw.show();

        StdDraw.setFont(defaultFont);
    }

    private void drawOver() {
        // Title
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(boorishFont);
        StdDraw.text(WIDTH/2, HEIGHT - 15, "GAME OVER!!");

        StdDraw.setFont(slenderFont);
        StdDraw.text(WIDTH/2, HEIGHT-25, "Congratulations!!");
        StdDraw.text(WIDTH/2, HEIGHT-27, "U got me!! See ya!!");
        StdDraw.text(WIDTH/2, HEIGHT-35, "noob author: Cheng Yu");
        StdDraw.text(WIDTH/2, 1, "press Q to exit the game (Q)");
        StdDraw.show();

        StdDraw.setFont(defaultFont);
    }

    private void draw(String str) {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(boorishFont);
        StdDraw.text(WIDTH/2, HEIGHT/2, str);
        StdDraw.show();

        StdDraw.setFont(defaultFont);
    }

    private void showTileInfo(Position mousePos) {
        StdDraw.clear(StdDraw.BLACK);
        ter.renderFrame(world);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, HEIGHT-1, world[mousePos.x][mousePos.y].description());
        StdDraw.show();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        InputSource inputSource = new StringInputDevice(input);
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            if (c == 'N') {
                record.append(c);
                generateRandomWorld(inputSource);
                break;
            }
            if (c == 'L') {
                String oldWorld = load();
                interactWithInputString(oldWorld);
                break;
            }
        }
        recurrentWorld(inputSource);
        return this.world;
    }

    public void showMeTheWorld() {
        ter.renderFrame(world);
    }
}
