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
public class HMM1 {
    public static double trow;
    public static double tcol;
    public static double erow;
    public static double ecol;
    public static double irow;
    public static double icol;
    public static double numObs;

    public static double alphaPass(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        int numStates = initial[0].length;
        double[][] obsProbStorage = new double[ numStates][obsSequence.length];
        double result = 0;
        for (int state = 0; state < numStates; state++) {
            int observation = (int) obsSequence[0];
            obsProbStorage[state][0] = obs[state][observation] * initial[0][state];
        }
        for (int obsIndex = 1; obsIndex < obsSequence.length; obsIndex++) {
            int currentObs = (int) obsSequence[(int) obsIndex];               
            for (int stateTo = 0; stateTo < numStates; stateTo++) {
                obsProbStorage[stateTo][obsIndex] =0;
                for (int stateFrom = 0; stateFrom < numStates; stateFrom++) {
                   obsProbStorage[stateTo][obsIndex] += (obsProbStorage[stateFrom][obsIndex-1]*transitions[stateFrom][stateTo]);
                }
                obsProbStorage[stateTo][obsIndex] = obsProbStorage[stateTo][obsIndex] * obs[stateTo][currentObs] ;
            }
        }
        for (int index = 0; index < numStates; index++) {
            result += obsProbStorage[index][obsSequence.length - 1];
        }
        return result;
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
        
        System.out.print(alphaPass(transitions, emmissions, initial, obsSequence));
    }
}
