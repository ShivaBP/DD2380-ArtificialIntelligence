/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hiddenmarkovmodels;
import java.util.Random;
import java.util.Scanner;
import java.lang.Math;
import static java.lang.Math.log;
/**
 *
 * @author shivabp
 */
public class HMM3Complete {
    public static double trow;
    public static double tcol;
    public static double erow;
    public static double ecol;
    public static double irow;
    public static double icol;
    public static double sequenceLength;
    public static double logProb;


    /* Probability of the observation sequence happening from all possible combinations of states. 
    - given sequence index
    - given currentState*/
    public static double[][] alphaPass(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        double[] scale = new double[obsSequence.length];
        int numStates = initial[0].length;
        int sequenceSize = obsSequence.length;
        double[][] obsProbStorage = new double[numStates][sequenceSize];

        scale[0] = 0;
        for (int state = 0; state < numStates; state++) {
            int observation = (int) obsSequence[0];
            obsProbStorage[state][0] = obs[state][observation] * initial[0][state];
            scale[0] += obsProbStorage[state][0];
        }
        scale[0] = 1 / scale[0];
        for (int state = 0; state < numStates; state++) {
            obsProbStorage[state][0] = obsProbStorage[state][0] * scale[0];
        }
        for (int obsIndex = 1; obsIndex < sequenceSize; obsIndex++) {
            int currentObs = (int) obsSequence[(int) obsIndex];
            scale[obsIndex] = 0;
            for (int stateTo = 0; stateTo < numStates; stateTo++) {
                obsProbStorage[stateTo][obsIndex] = 0;
                for (int stateFrom = 0; stateFrom < numStates; stateFrom++) {
                    obsProbStorage[stateTo][obsIndex] += (obsProbStorage[stateFrom][obsIndex - 1] * transitions[stateFrom][stateTo]);
                }
                obsProbStorage[stateTo][obsIndex] = obsProbStorage[stateTo][obsIndex] * obs[stateTo][currentObs];
                scale[obsIndex] += obsProbStorage[stateTo][obsIndex];
            }
            scale[obsIndex] = 1 / scale[obsIndex];
            for (int stateTo = 0; stateTo < numStates; stateTo++) {
                obsProbStorage[stateTo][obsIndex] = obsProbStorage[stateTo][obsIndex] * scale[obsIndex];
            }
        }
        return obsProbStorage;
    }

    /* Probability of the rest of observation at a given state. 
    - given state
    - given sequenceIndex*/
    public static double[][] betaPass(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        double[] scale = scale(transitions, obs, initial, obsSequence);
        int numStates = initial[0].length;
        int sequenceSize = obsSequence.length;
        double[][] restObservations = new double[numStates][sequenceSize];

        for (int currentState = 0; currentState < numStates; currentState++) {
            restObservations[currentState][sequenceSize - 1] = scale[sequenceSize - 1];
        }
        for (int sequenceIndex = sequenceSize - 2; sequenceIndex >= 0; sequenceIndex--) {
            for (int currentState = 0; currentState < numStates; currentState++) {
                restObservations[currentState][sequenceIndex] = 0;
                for (int nextState = 0; nextState < numStates; nextState++) {
                    restObservations[currentState][sequenceIndex] += (restObservations[nextState][sequenceIndex + 1] * transitions[currentState][nextState] * obs[nextState][(int) obsSequence[sequenceIndex + 1]]);
                }
                restObservations[currentState][sequenceIndex] = scale[sequenceIndex] * restObservations[currentState][sequenceIndex];
            }
        }
        return restObservations;
    }

    public static double[] scale(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        double[] scale = new double[obsSequence.length];
        int numStates = initial[0].length;
        int sequenceSize = obsSequence.length;
        double[][] obsProbStorage = new double[numStates][sequenceSize];

        scale[0] = 0;
        for (int state = 0; state < numStates; state++) {
            int observation = (int) obsSequence[0];
            obsProbStorage[state][0] = obs[state][observation] * initial[0][state];
            scale[0] += obsProbStorage[state][0];
        }
        scale[0] = 1 / scale[0];
        for (int state = 0; state < numStates; state++) {
            obsProbStorage[state][0] = obsProbStorage[state][0] * scale[0];
        }
        for (int obsIndex = 1; obsIndex < sequenceSize; obsIndex++) {
            int currentObs = (int) obsSequence[(int) obsIndex];
            scale[obsIndex] = 0;
            for (int stateTo = 0; stateTo < numStates; stateTo++) {
                obsProbStorage[stateTo][obsIndex] = 0;
                for (int stateFrom = 0; stateFrom < numStates; stateFrom++) {
                    obsProbStorage[stateTo][obsIndex] += (obsProbStorage[stateFrom][obsIndex - 1] * transitions[stateFrom][stateTo]);
                }
                obsProbStorage[stateTo][obsIndex] = obsProbStorage[stateTo][obsIndex] * obs[stateTo][currentObs];
                scale[obsIndex] += obsProbStorage[stateTo][obsIndex];
            }
            scale[obsIndex] = 1 / scale[obsIndex];
            for (int stateTo = 0; stateTo < numStates; stateTo++) {
                obsProbStorage[stateTo][obsIndex] = obsProbStorage[stateTo][obsIndex] * scale[obsIndex];
            }
        }
        return scale;
    }

    public static double[][] tansitionMatrix(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        int numStates = initial[0].length;
        int sequenceSize = obsSequence.length;
        double[][] alphaMatrix = HMM3Complete.alphaPass(transitions, obs, initial, obsSequence);
        double[][] betaMatrix = HMM3Complete.betaPass(transitions, obs, initial, obsSequence);
        double[][][] digamma = new double[sequenceSize][numStates][numStates];
        double[][] gamma = new double[sequenceSize][numStates];
        double[][] newTransitions = new double[numStates][numStates];

        for (int obsIndex = 0; obsIndex < sequenceSize - 1; obsIndex++) {
            double denominator = 0;
            for (int currentState = 0; currentState < numStates; currentState++) {
                for (int nextState = 0; nextState < numStates; nextState++) {
                    denominator += (alphaMatrix[currentState][obsIndex] * transitions[currentState][nextState] * obs[nextState][(int) obsSequence[obsIndex + 1]] * betaMatrix[nextState][obsIndex + 1]);
                }
            }
            for (int currentState = 0; currentState < numStates; currentState++) {
                gamma[obsIndex][currentState] = 0;
                for (int nextState = 0; nextState < numStates; nextState++) {
                    digamma[obsIndex][currentState][nextState] = (alphaMatrix[currentState][obsIndex] * transitions[currentState][nextState] * obs[nextState][(int) obsSequence[obsIndex + 1]] * betaMatrix[nextState][obsIndex + 1]) / denominator;
                    gamma[obsIndex][currentState] += digamma[obsIndex][currentState][nextState];
                }
            }
        }
        double denom = 0;
        for (int currentState = 0; currentState < numStates; currentState++) {
            denom += alphaMatrix[currentState][sequenceSize - 1];
        }
        for (int currentState = 0; currentState < numStates; currentState++) {
            gamma[sequenceSize - 1][currentState] = alphaMatrix[currentState][sequenceSize - 1] / denom;
        }
        for (int currentState = 0; currentState < numStates; currentState++) {
            for (int nextState = 0; nextState < numStates; nextState++) {
                double numerator = 0;
                double denominator = 0;
                for (int obsIndex = 0; obsIndex < sequenceSize - 1; obsIndex++) {
                    numerator += digamma[obsIndex][currentState][nextState];
                    denominator += gamma[obsIndex][currentState];
                }
                newTransitions[currentState][nextState] = numerator / denominator;
            }
        }
        return newTransitions;
    }

    public static double[][] emissionMatrix(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        int numStates = initial[0].length;
        int numObs = obs[0].length;
        int sequenceSize = obsSequence.length;
        double[][] alphaMatrix = HMM3.alphaPass(transitions, obs, initial, obsSequence);
        double[][] betaMatrix = HMM3.betaPass(transitions, obs, initial, obsSequence);
        double[][][] digamma = new double[sequenceSize][numStates][numStates];
        double[][] gamma = new double[sequenceSize][numStates];
        double[][] newEmissions = new double[numStates][numObs];

        for (int obsIndex = 0; obsIndex < sequenceSize - 1; obsIndex++) {
            double denominator = 0;
            for (int currentState = 0; currentState < numStates; currentState++) {
                for (int nextState = 0; nextState < numStates; nextState++) {
                    denominator += (alphaMatrix[currentState][obsIndex] * transitions[currentState][nextState] * obs[nextState][(int) obsSequence[obsIndex + 1]] * betaMatrix[nextState][obsIndex + 1]);
                }
            }
            for (int currentState = 0; currentState < numStates; currentState++) {
                gamma[obsIndex][currentState] = 0;
                for (int nextState = 0; nextState < numStates; nextState++) {
                    digamma[obsIndex][currentState][nextState] = (alphaMatrix[currentState][obsIndex] * transitions[currentState][nextState] * obs[nextState][(int) obsSequence[obsIndex + 1]] * betaMatrix[nextState][obsIndex + 1]) / denominator;
                    gamma[obsIndex][currentState] += digamma[obsIndex][currentState][nextState];
                }
            }
        }
        double denom = 0;
        for (int currentState = 0; currentState < numStates; currentState++) {
            denom += alphaMatrix[currentState][sequenceSize - 1];
        }
        for (int currentState = 0; currentState < numStates; currentState++) {
            gamma[sequenceSize - 1][currentState] = alphaMatrix[currentState][sequenceSize - 1] / denom;
        }
        for (int currentState = 0; currentState < numStates; currentState++) {
            for (int observation = 0; observation < numObs; observation++) {
                double numerator = 0;
                double denominator = 0;
                for (int obsIndex = 0; obsIndex < sequenceSize; obsIndex++) {
                    if (obsSequence[obsIndex] == observation) {
                        numerator += gamma[obsIndex][currentState];
                    }
                    denominator += gamma[obsIndex][currentState];
                }
                newEmissions[currentState][observation] = numerator / denominator;
            }
        }
        return newEmissions;
    }
    
    public static double[][] initialMatrix(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        int numStates = initial[0].length;
        int numObs = obs[0].length;
        int sequenceSize = obsSequence.length;
        double[][] alphaMatrix = HMM3.alphaPass(transitions, obs, initial, obsSequence);
        double[][] betaMatrix = HMM3.betaPass(transitions, obs, initial, obsSequence);
        double[][][] digamma = new double[sequenceSize][numStates][numStates];
        double[][] gamma = new double[sequenceSize][numStates];
        double[][] newInitial = new double[1][numStates];

        for (int obsIndex = 0; obsIndex < sequenceSize - 1; obsIndex++) {
            double denominator = 0;
            for (int currentState = 0; currentState < numStates; currentState++) {
                for (int nextState = 0; nextState < numStates; nextState++) {
                    denominator += (alphaMatrix[currentState][obsIndex] * transitions[currentState][nextState] * obs[nextState][(int) obsSequence[obsIndex + 1]] * betaMatrix[nextState][obsIndex + 1]);
                }
            }
            for (int currentState = 0; currentState < numStates; currentState++) {
                gamma[obsIndex][currentState] = 0;
                for (int nextState = 0; nextState < numStates; nextState++) {
                    digamma[obsIndex][currentState][nextState] = (alphaMatrix[currentState][obsIndex] * transitions[currentState][nextState] * obs[nextState][(int) obsSequence[obsIndex + 1]] * betaMatrix[nextState][obsIndex + 1]) / denominator;
                    gamma[obsIndex][currentState] += digamma[obsIndex][currentState][nextState];
                }
            }
        }
        double denom = 0;
        for (int currentState = 0; currentState < numStates; currentState++) {
            denom += alphaMatrix[currentState][sequenceSize - 1];
        }
        for (int currentState = 0; currentState < numStates; currentState++) {
            gamma[sequenceSize - 1][currentState] = alphaMatrix[currentState][sequenceSize - 1] / denom;
        }
        for (int state = 0 ;  state < numStates ; state++){
            newInitial[0][state] = gamma[0][state];
        }
   return newInitial;
    }

    public static double computeLog(double[][] transitions, double[][] obs, double[][] initial, double[] obsSequence) {
        logProb = 0;
        double[] scale = scale(transitions, obs, initial, obsSequence);
        for (int obsIndex = 0; obsIndex < sequenceLength; obsIndex++) {
            logProb += Math.log(scale[obsIndex]);
        }
        logProb = -logProb;
        return logProb;
    }

    public static void matrixPrint(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        System.out.print(rows + " " + columns + " ");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        double maxIters = 100;
        double oldLogProb = Double.NEGATIVE_INFINITY;

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
        sequenceLength = input.nextDouble();
        double[] obsSequence = new double[(int) sequenceLength];
        for (int i = 0; i < obsSequence.length; i++) {
            obsSequence[i] = input.nextDouble();
        }

        double[][] transitionMatrix = new double[(int) trow][(int) tcol];
        double[][] tempTransitions = new double[(int) trow][(int) tcol];
        double[][] emissionMatrix = new double[(int) erow][(int) ecol];
        double[][] tempEmissions = new double[(int) erow][(int) ecol];
        double[][] initialMatrix = new double[(int) irow][(int) icol];
        double[][] tempInitial = new double[(int) irow][(int) icol];

        tempTransitions = transitions;
        tempEmissions = emmissions;
        tempInitial = initial;
        for (int iters = 0; iters < maxIters; iters++) {
            logProb = computeLog(tempTransitions, tempEmissions, initial, obsSequence);
            if (logProb > oldLogProb) {
                oldLogProb = logProb;
                transitionMatrix = tansitionMatrix(tempTransitions, tempEmissions, initial, obsSequence);
                emissionMatrix = emissionMatrix(tempTransitions, tempEmissions, initial, obsSequence);
                initialMatrix = initialMatrix(tempTransitions, tempEmissions, initial, obsSequence);
                tempTransitions = transitionMatrix;
                tempEmissions = emissionMatrix;
                tempInitial = initialMatrix ;
            } else {
                break;
            }
        }
        matrixPrint(tempTransitions);
        matrixPrint(tempEmissions);

    }
}
