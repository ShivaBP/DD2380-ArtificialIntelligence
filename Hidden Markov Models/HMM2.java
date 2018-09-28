/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hiddenmarkovmodels;
import java.util.Scanner;

/**
 *
 * @author shivabp
 */
public class HMM2 {

    public static double trow;
    public static double tcol;
    public static double erow;
    public static double ecol;
    public static double irow;
    public static double icol;
    public static double numObs;

    public static int[] stateSequence(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        int numStates = transitions.length;
        int numObs = obsSequence.length;
        double endStateProbability = 0.0;
        double currentState;
        int endStateIndex = -1;
        double[][] stateProbabilities = new double[numStates][numObs];
        int[][] stateIndices = new int[numStates][numObs];
        int[] stateSequence = new int[numObs];

        // maximum obs vector per observation
        for (int currentObs = 0; currentObs < numObs; currentObs++) {
            for (int stateTo = 0; stateTo < numStates; stateTo++) {
                if (currentObs == 0) {
                    stateProbabilities[stateTo][currentObs] = obs[stateTo][(int) obsSequence[currentObs]] * initial[0][stateTo];
                } else {
                    double maxPerState = 0.0;
                    int maxStateFrom = 0;
                    for (int stateFrom = 0; stateFrom < numStates; stateFrom++) {
                        Double currentProbability = stateProbabilities[stateFrom][currentObs - 1] * transitions[stateFrom][stateTo] * obs[stateTo][(int) obsSequence[currentObs]];
                        if (currentProbability >= maxPerState) {
                            maxPerState = currentProbability;
                            maxStateFrom = stateFrom;
                        }
                    }
                    stateProbabilities[stateTo][currentObs] = maxPerState;
                    stateIndices[stateTo][currentObs - 1] = maxStateFrom;
                }
            }
        }
        // Most likely terminating state
        for (int state = 0; state < numStates; state++) {
            currentState = stateProbabilities[state][numObs - 1];
            if (currentState > endStateProbability) {
                endStateProbability = currentState;
                endStateIndex = state;
            }
        }
        for (int state = 0; state < numStates; state++) {
            stateIndices[state][numObs - 1] = endStateIndex;
        }
        //filling in the most likely state sequence vector
        for (int obsIndex = numObs - 1; obsIndex >= 0; obsIndex--) {
            if (obsIndex == numObs - 1) {
                stateSequence[obsIndex] = stateIndices[0][obsIndex];
            } else {
                stateSequence[obsIndex] = stateIndices[stateSequence[obsIndex + 1]][obsIndex];
            }
        }
        return stateSequence;
    }

    public static void vectorPrint(int[] vector) {
        for (int i = 0; i < vector.length; i++) {
            System.out.print(vector[i] + " ");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        trow = input.nextDouble();
        tcol = input.nextDouble();
        double[][] transitions = new double[(int) trow][(int) tcol];
        for (int i = 0; i < trow; i++) {
            for (int j = 0; j < tcol; j++) {
                transitions[i][j] = input.nextDouble();
            }
        }

        input.nextLine();
        erow = input.nextDouble();
        ecol = input.nextDouble();
        double[][] emmissions = new double[(int) erow][(int) ecol];
        for (int i = 0; i < erow; i++) {
            for (int j = 0; j < ecol; j++) {
                emmissions[i][j] = input.nextDouble();
            }
        }

        input.nextLine();
        irow = input.nextDouble();
        icol = input.nextDouble();
        double[][] initial = new double[(int) irow][(int) icol];
        for (int i = 0; i < irow; i++) {
            for (int j = 0; j < icol; j++) {
                initial[i][j] = input.nextDouble();
            }
        }

        input.nextLine();
        numObs = input.nextDouble();
        double[] obsSequence = new double[(int) numObs];
        for (int i = 0; i < obsSequence.length; i++) {
            obsSequence[i] = input.nextDouble();
        }

        int[] result = new int[obsSequence.length];
        result = stateSequence(transitions, emmissions, initial, obsSequence);
        vectorPrint(result);

    }
}
