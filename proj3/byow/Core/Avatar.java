package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar {

    private TETile figure;
    private TETile[][] world;
    private Position pos;
    public Avatar(TETile figure, Position pos, TETile[][] world, TERenderer ter) {
        this.figure = figure;
        this.world = world;
        this.pos = pos;
    }

    public void appear() {
        world[pos.x][pos.y] = figure;
    }


    public void move(char c) {
        world[pos.x][pos.y] = Tileset.FLOOR;
        switch (c) {
            case 'W':
                moveUp();
                break;
            case 'S':
                moveDown();
                break;
            case 'A':
                moveLeft();
                break;
            case 'D':
                moveRight();
                break;
        }
        if (world[pos.x][pos.y] == Tileset.LOCKED_DOOR) {
            world[pos.x][pos.y] = Tileset.UNLOCKED_DOOR;
        } else {
            world[pos.x][pos.y] = figure;
        }
    }

    public boolean isReachDoor() {
        return world[pos.x][pos.y] == Tileset.UNLOCKED_DOOR;
    }

    private void moveLeft() {
        if (world[pos.x-1][pos.y] == Tileset.FLOOR || world[pos.x-1][pos.y] == Tileset.LOCKED_DOOR) { pos.x--; }
    }

    private void moveRight() {
        if (world[pos.x+1][pos.y] == Tileset.FLOOR || world[pos.x+1][pos.y] == Tileset.LOCKED_DOOR) { pos.x++; }
    }

    private void moveUp() {
        if (world[pos.x][pos.y+1] == Tileset.FLOOR || world[pos.x][pos.y+1] == Tileset.LOCKED_DOOR) { pos.y++; }
    }

    private void moveDown() {
        if (world[pos.x][pos.y-1] == Tileset.FLOOR || world[pos.x][pos.y-1] == Tileset.LOCKED_DOOR) { pos.y--; }
    }
}
