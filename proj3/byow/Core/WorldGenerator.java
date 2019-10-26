package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class WorldGenerator {
    static final int ROOMSIZE = 10;
    static final int HALLLENGTH = 10;
    static final int MINDIFF = 3;
    static final int ROOMNUM = 60;
    static Random RANDOM;
    static Room ROOMSPACE;
    static Room BACKGROUND;
    private TETile[][] world;

    public WorldGenerator(long seed) {
        RANDOM = new Random(seed);
        Position ORIGIN = new Position(0, 0);
        Position DIAGNAL = new Position(WIDTH - 1, HEIGHT - 1);
        ROOMSPACE = new Room(ORIGIN.move(new Position(1, 1)), DIAGNAL.move(new Position(-1, -1)));
        BACKGROUND = new Room(ORIGIN, DIAGNAL);
        world = new TETile[WIDTH][HEIGHT];
        drawBackGround();
        drawRooms();
        drawWalls();
        addLockedDoor();
    }

    public void drawRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        Room root = new Room();
        rooms.add(root);
        root.draw(world);
        int roomNum = ROOMNUM;
        while (roomNum > 0) {
            int index = RandomUtils.uniform(RANDOM, rooms.size());
            Room curRoom = rooms.get(index);
            Room nextRoom = curRoom.emerge();
            if (nextRoom.isValid() && !nextRoom.isIntersect(rooms, curRoom)) {
                rooms.add(nextRoom);
                nextRoom.draw(world);
                roomNum--;
            }
        }
    }

    public void drawWalls() {
        for (int x=0; x!=WIDTH; x++) {
            for (int y=0; y!=HEIGHT; y++) {
                Position candidate = new Position(x, y);
                if (candidate.isNothing(world) && candidate.isNearFloor(world)) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
    }

    public void addLockedDoor() {
        Position pos = Position.randomPositon();
        while (world[pos.x][pos.y] != Tileset.WALL) {
            pos = Position.randomPositon();
        }
        world[pos.x][pos.y] = Tileset.LOCKED_DOOR;
    }

    private void drawBackGround() {
        for (int x=0; x!=WIDTH; x++) {
            for (int y=0; y!=HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public TETile[][] getMyWorld() {
        return world;
    }
}
