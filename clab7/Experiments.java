import edu.princeton.cs.algs4.StdOut;
import huglife.In;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hug.
 */
public class Experiments {
    public static void experiment1() {
        Random r = new Random();
        BST<Integer> bst = new BST<>();
        List<Integer> xValues = new ArrayList<>();
        List<Double> avgDepthMy = new ArrayList<>();
        List<Double> avgDepthOpt = new ArrayList<>();
        int x = 1;
        while ( x!= 5001){
            int randomInt = r.nextInt(5000);
            if (bst.contains(randomInt)) { continue; }

            bst.add(randomInt);
            xValues.add(x);
            avgDepthMy.add(bst.averageDepth());
            avgDepthOpt.add(ExperimentHelper.optimalAverageDepth(x));
            x++;
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("number of items").yAxisTitle(" averageDepth").build();
        chart.addSeries("myBST", xValues, avgDepthMy);
        chart.addSeries("optBST", xValues, avgDepthOpt);

        new SwingWrapper(chart).displayChart();
    }

    public static void experiment2() {
        List<Integer> operations = new ArrayList<>();
        List<Double> avgDepths = new ArrayList<>();
        Random r = new Random();
        BST<Integer> bst = new BST<>();
        int randomInt;
        while ( bst.size() != 2000 ) {
            randomInt = r.nextInt(4000);
            bst.add(randomInt);
        }
        for ( int i = 0; i!=1000000; i++) {
            ExperimentHelper.insertAndDeleteSuccessor(bst);
            StdOut.println(i);
            double avgDepth = bst.averageDepth();
            operations.add(i);
            avgDepths.add(avgDepth);
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("number of operation").yAxisTitle(" averageDepth").build();
        chart.addSeries("Knott_experiment", operations, avgDepths);
        new SwingWrapper(chart).displayChart();
}

    public static void experiment3() {
        List<Integer> operations = new ArrayList<>();
        List<Double> avgDepths = new ArrayList<>();
        Random r = new Random();
        BST<Integer> bst = new BST<>();
        int randomInt;
        while ( bst.size() != 2000 ) {
            randomInt = r.nextInt(4000);
            bst.add(randomInt);
        }
        for ( int i = 0; i!=1000000; i++) {
            ExperimentHelper.insertAndDeleteRandom(bst);
            StdOut.println(i);
            double avgDepth = bst.averageDepth();
            operations.add(i);
            avgDepths.add(avgDepth);
        }
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("number of operation").yAxisTitle(" averageDepth").build();
        chart.addSeries("Epplinger_experiment", operations, avgDepths);
        new SwingWrapper(chart).displayChart();
    }

    public static void main(String[] args) {
//        experiment1();
//        experiment2();
        experiment3();
    }
}


