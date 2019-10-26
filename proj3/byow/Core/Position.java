package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;
import static byow.Core.WorldGenerator.*;


public class Position {
    public int x;
    public int y;
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position randomPositon() {
        int x = RandomUtils.uniform(RANDOM, WIDTH);
        int y = RandomUtils.uniform(RANDOM, HEIGHT);
        return new Position(x, y);
    }

    public static PositionWithDirection randomPerimeterPosition(Room room) {
        int type = RandomUtils.uniform(RANDOM, 4);
        switch (type) {
            case 0: return new PositionWithDirection(room.getP().move(new Position(0, RandomUtils.uniform(RANDOM, room.getHeight()))), Direction.WEST);
            case 1: return new PositionWithDirection(room.getQ().move(new Position(0, -RandomUtils.uniform(RANDOM, room.getHeight()))), Direction.EAST);
            case 2: return new PositionWithDirection(room.getP().move(new Position(RandomUtils.uniform(RANDOM, room.getWidth()), 0)), Direction.SOUTH);
            case 3: return new PositionWithDirection(room.getQ().move(new Position(-RandomUtils.uniform(RANDOM, room.getWidth()), 0)), Direction.NORTH);
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static PositionWithDirection randomLengthPosition(Room room) {
        Position p = room.getP();
        Position q = room.getQ();
        // West to east direction
        if (p.x == q.x) {
            int randomY = RandomUtils.uniform(RANDOM, p.y, q.y+1);
            if (randomY < (p.y+q.y)/2) {
                return new PositionWithDirection(new Position(p.x, randomY), Direction.SOUTH);
            } else {
                return new PositionWithDirection(new Position(p.x, randomY), Direction.NORTH);
            }
        }

        // South to north direction
        else if (p.y == q.y) {
            int randomX = RandomUtils.uniform(RANDOM, p.x, q.x+1);
            if (randomX < (p.x+q.x)/2) {
                return new PositionWithDirection(new Position(randomX, p.y), Direction.WEST);
            } else {
                return new PositionWithDirection(new Position(randomX, p.y), Direction.EAST);
            }
        }

        else {
            throw new IllegalArgumentException("Passed room must be hall, ok?");
        }
    }


    public Position move(Position o) {
        return new Position(this.x + o.x, this.y + o.y);
    }

    public boolean equals(Object o) {
        return (this.x == ((Position) o).x && this.y == ((Position) o).y);
    }

    public boolean isInside(Room room) {
        return this.x >= room.getP().x && this.x <= room.getQ().x && this.y >= room.getP().y && this.y <= room.getQ().y;
    }

    public boolean isNearFloor(TETile[][] world) {
        for (int i = x-1; i<=x+1; i++) {
            for (int j = y-1; j<=y+1; j++) {
                Position temp = new Position(i, j);
                if (temp.isInside(BACKGROUND) && world[i][j].equals(Tileset.FLOOR)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNothing(TETile[][] world) {
        return world[x][y].equals(Tileset.NOTHING);
    }

    public static void regularization(Position p, Position q) {
        if (p.x > q.x) {
             int temp = p.x;
             p.x = q.x;
             q.x = temp;
        }
        if (p.y > q.y) {
            int temp = p.y;
            p.y = q.y;
            q.y = temp;
        }
    }
}

class PositionWithDirection {
    Position p;
    Direction direction;
    public PositionWithDirection(Position p, Direction d) {
        this.p = p;
        this.direction = d;
    }
}

