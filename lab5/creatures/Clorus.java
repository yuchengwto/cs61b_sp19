package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import static huglife.HugLifeUtils.random;
import static huglife.HugLifeUtils.randomEntry;

public class Clorus extends Creature {
    private int r;
    private int g;
    private int b;
    public Clorus (double energy) {
        super("clorus");
        this.energy = energy;
        r = 34;
        b = 231;
        g = 0;
    }

    public Clorus () { this(1.0); }

    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        this.energy += c.energy();
    }

    @Override
    public Creature replicate() {
        double childEnergy = this.energy * 0.5;
        this.energy -= childEnergy;
        return new Clorus(childEnergy);
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plipNeighbors = new ArrayDeque<>();
        boolean anyPlip = false;
        for (Direction d: neighbors.keySet()) {
            if (neighbors.get(d).name().equals("empty")) {
                emptyNeighbors.addLast(d);
            } else if (neighbors.get(d).name().equals("plip")) {
                anyPlip = true;
                plipNeighbors.addLast(d);
            }
        }

        if ( emptyNeighbors.isEmpty() ) {
            return new Action(Action.ActionType.STAY);
        } else if ( anyPlip ) {
            return new Action(Action.ActionType.ATTACK, randomEntry(plipNeighbors));
        } else if ( this.energy >= 1 ) {
            return new Action(Action.ActionType.REPLICATE, randomEntry(emptyNeighbors));
        } else {
            return new Action(Action.ActionType.MOVE, randomEntry(emptyNeighbors));
        }
    }

    @Override
    public Color color() {
        return color(r, g, b);
    }
}
