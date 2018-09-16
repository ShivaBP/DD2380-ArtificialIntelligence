/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hiddenmarkovmodels;

/**
 *
 * @author shivabp
 */
import java.util.Scanner;

/**
 *
 * @author shivabp
 */
public class HMM0 {

    public static double trow;
    public static double tcol;
    public static double erow;
    public static double ecol;
    public static double irow;
    public static double icol;

    public static double[][] matrixMultiplication(double[][] a, double[][] b) {
        double rowsA = a.length;
        double colsA = a[0].length;
        double rowsB = b.length;
        double colsB = b[0].length;
        double[][] result = new double[(int) rowsA][(int) colsB];
        if (colsA != rowsB) {
            System.out.println("The matrix dimensions are not valid!" + colsA + rowsB);
        } else {
            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsB; j++) {
                    for (int k = 0; k < colsA; k++) {
                        result[i][j] = result[i][j] + a[i][k] * b[k][j];
                    }
                }
            }
        }
        return result;
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

        double[][] transDistribution = matrixMultiplication(initial, transitions);
        double[][] emissionDistribution = matrixMultiplication(transDistribution , emmissions);
        matrixPrint(emissionDistribution);
    }
}
