import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are <= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {
    private PriorityQueue<Flight> startMinPQ;
    private PriorityQueue<Flight> endMinPQ;
    private int maxPassengers = 0;

    public FlightSolver(ArrayList<Flight> flights) {
        /* FIX ME */
        Comparator<Flight> startCmp = (Flight o1, Flight o2) -> Integer.compare(o1.startTime(), o2.startTime());
        Comparator<Flight> endCmp = (Flight o1, Flight o2) -> Integer.compare(o1.endTime(), o2.endTime());
        startMinPQ = new PriorityQueue<>(startCmp);
        endMinPQ = new PriorityQueue<>(endCmp);
        startMinPQ.addAll(flights);
        endMinPQ.addAll(flights);

        int tally = 0;
        while (startMinPQ.size() != 0) {
            if (startMinPQ.peek().startTime() <= endMinPQ.peek().endTime()) {
                tally += startMinPQ.poll().passengers();
                maxPassengers = tally > maxPassengers ? tally : maxPassengers;
            } else {
                tally -= endMinPQ.poll().passengers();
            }
        }
    }

    public int solve() {
        return maxPassengers;
    }

}
