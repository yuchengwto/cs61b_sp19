package bearmaps.hw4.integerhoppuzzle;

import bearmaps.hw4.AStarGraph;
import bearmaps.hw4.WeightedEdge;
import edu.princeton.cs.introcs.In;

import java.util.ArrayList;
import java.util.List;

/**
 * The Integer Hop puzzle implemented as a graph.
 * Created by hug.
 */
public class IntegerHopGraph implements AStarGraph<Integer> {

    @Override
    public List<WeightedEdge<Integer>> neighbors(Integer v) {
        ArrayList<WeightedEdge<Integer>> neighbors = new ArrayList<>();
        neighbors.add(new WeightedEdge<>(v, v * v, 10));
        neighbors.add(new WeightedEdge<>(v, v * 2, 5));
        neighbors.add(new WeightedEdge<>(v, v / 2, 5));
        neighbors.add(new WeightedEdge<>(v, v - 1, 1));
        neighbors.add(new WeightedEdge<>(v, v + 1, 1));
        return neighbors;
    }

    @Override
    public double estimatedDistanceToGoal(Integer s, Integer goal) {
        // possibly fun challenge: Try to find an admissible heuristic that
        // speeds up your search. This is tough!

//        int cost;
//        if (s.compareTo(goal) == 0) {
//            return 0;
//        } else if (s.compareTo(goal) > 0) {
//            cost = helperSBigger(s, goal);
//        } else {
//            cost = helperGBigger(s, goal);
//        }
//        return cost;
        return 0;
    }

//    private int helperSBigger(Integer s, Integer goal) {
//        int cost = 0;
//        while (s-goal > 5 && s/2 > goal) {
//            s *= 2;
//            cost += 5;
//        }
//        return cost;
//    }
//
//    private int helperGBigger(Integer s, Integer goal) {
//        int cost = 0;
//        while (Math.log(goal/s)/Math.log(2) * 5 > 10 && goal-s > 10 && s*s < goal) {
//            s = s*s;
//            cost += 10;
//        }
//        return cost;
//    }
}
