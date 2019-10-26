package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;
    private static final Random RANDOM = new Random();

    static class Position {
        int x;
        int y;
        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void addHexagon(TETile[][] world, Position p, int size, TETile t) {
        int initX = p.x;
        int initY = p.y;
        int maxWidth = getWidth(size);
        int height = getHeight(size);

        int curWidth = size;
        for (int y = initY; y!=initY + height; y++) {
            int startXIndex = getX(maxWidth, curWidth, initX);
            for (int x = startXIndex; x!=startXIndex+curWidth; x++) {
                world[x][y] = t;
            }
            if (y < initY + height/2 - 1) {
                curWidth+=2;
            } else if (y > initY + height/2 - 1) {
                curWidth-=2;
            }
        }
    }

    private static int getX(int maxWidth, int curWidth, int x) {
        return x + (maxWidth - curWidth)/2;
    }

    private static int getWidth(int size) {
        return size * 2 + (size - 2);
    }

    private static int getHeight(int size) {
        return size * 2;
    }

    private static List<Position> getPatPosition(Position lowest) {
        List<Position> lp = new ArrayList<>();
        lp.add(lowest);
        int initX = lowest.x;
        int initY = lowest.y;
        int deltaX = getWidth(3) + 3;
        int deltaY = getHeight(3);

        Position highest = new Position(initX, initY+4*deltaY);
        lp.add(highest);

        for (int y = initY+deltaY; y!=initY+4*deltaY; y+=deltaY) {
            for (int x = initX-deltaX; x!=initX+2*deltaX; x+=deltaX) {
                lp.add(new Position(x, y));
            }
        }

        for (int y = initY+3; y!=initY+3+4*deltaY; y+=deltaY) {
            for (int x = initX-5; x!=initX-5+2*deltaX; x+=deltaX) {
                lp.add(new Position(x, y));
            }
        }
        return lp;
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.MOUNTAIN;
            case 3: return Tileset.TREE;
            case 4: return Tileset.SAND;
            default: return Tileset.MOUNTAIN;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        List<Position> lp = getPatPosition(new Position(WIDTH/2, 0));
        for (Position p: lp) {
            addHexagon(world, p, 3, randomTile());
        }

        ter.renderFrame(world);
    }

}
