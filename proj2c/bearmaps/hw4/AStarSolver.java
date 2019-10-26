package bearmaps.hw4;

import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.DoubleMapPQ;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.*;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private double solutionWeight;
    private double timeSpent;
    private int numStatesExplored;
    private LinkedList<Vertex> solution = new LinkedList<>();
    private Map<Vertex, Double> distToMap = new HashMap<>();
    private Map<Vertex, Vertex> edgeToMap = new HashMap<>();
    private Map<Vertex, Double> heuristicMap = new HashMap<>();
    private ArrayHeapMinPQ<Vertex> fringe = new ArrayHeapMinPQ<>();

    private void findSolution(Vertex goal) {
        solution.addFirst(goal);
        Vertex edgeTo = edgeToMap.get(goal);
        while (edgeTo!=null) {
            solution.addFirst(edgeTo);
            edgeTo = edgeToMap.get(edgeTo);
        }
    }

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {

        Stopwatch sw = new Stopwatch();
        double heuristicStart = input.estimatedDistanceToGoal(start, end);
        distToMap.put(start, 0.0);
        edgeToMap.put(start, null);
        heuristicMap.put(start, heuristicStart);
        double startP = 0.0 + heuristicStart;
        fringe.add(start, startP);

        List<WeightedEdge<Vertex>> neighborEdges;
        Vertex p;
        while (true) {
            p = fringe.getSmallest();
            if (p.equals(end)) {
                solutionWeight = distToMap.get(p);
                outcome = SolverOutcome.SOLVED;
                findSolution(end);
                timeSpent = sw.elapsedTime();
                return;
            }

            fringe.removeSmallest();
            numStatesExplored++;

            neighborEdges = input.neighbors(p);
            for (WeightedEdge<Vertex> e: neighborEdges) {
                relax(e, input, end);
            }

            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                solution.clear();
                timeSpent = sw.elapsedTime();
                return;
            } else if (fringe.size()==0) {
                outcome = SolverOutcome.UNSOLVABLE;
                solution.clear();
                timeSpent = sw.elapsedTime();
                return;
            }
        }
    }

    private void relax(WeightedEdge<Vertex> e, AStarGraph<Vertex> input, Vertex end) {
        Vertex p = e.from();
        Vertex q = e.to();
        double w = e.weight();
        double newDist = distToMap.get(p) + w;
        if (!distToMap.containsKey(q)) {
            distToMap.put(q, Double.POSITIVE_INFINITY);
        }
        if (!heuristicMap.containsKey(q)) {
            heuristicMap.put(q, input.estimatedDistanceToGoal(q, end));
        }
        if (newDist < distToMap.get(q)) {
            edgeToMap.put(q, p);
            distToMap.replace(q, newDist);
            edgeToMap.replace(q, p);
            double fringeP = distToMap.get(q) + heuristicMap.get(q);
            if (fringe.contains(q)) {
                fringe.changePriority(q, fringeP);
            } else {
                fringe.add(q, fringeP);
            }
        }
    }


    @Override
    public SolverOutcome outcome() {
        return this.outcome;
    }

    @Override
    public List<Vertex> solution() {
        return this.solution;
    }

    @Override
    public double solutionWeight() {
        if (outcome == SolverOutcome.SOLVED) {
            return this.solutionWeight;
        } else {
            return 0;
        }
    }

    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() {
        return this.timeSpent;
    }
}