import java.util.*;
// I am player 1 , opponent is player 2!
// https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
// http://www.ntu.edu.sg/home/ehchua/programming/java/javagame_tictactoe_ai.html
// https://www.hackerearth.com/blog/artificial-intelligence/minimax-algorithm-alpha-beta-pruning/

public class AlphaBetaPruning {

    public static int[][] winningCombinations = { { 0, 1, 2, 3 }, { 0, 16, 32, 48 }, { 0, 17, 34, 51 },
            { 0, 20, 40, 60 }, { 0, 21, 42, 63 }, { 0, 4, 8, 12 }, { 0, 5, 10, 15 }, { 1, 17, 33, 49 },
            { 1, 21, 41, 61 }, { 1, 5, 9, 13 }, { 10, 26, 42, 58 }, { 11, 26, 41, 56 }, { 11, 27, 43, 59 },
            { 12, 13, 14, 15 }, { 12, 24, 36, 48 }, { 12, 25, 38, 51 }, { 12, 28, 44, 60 }, { 12, 29, 46, 63 },
            { 13, 25, 37, 49 }, { 13, 29, 45, 61 }, { 14, 26, 38, 50 }, { 14, 30, 46, 62 }, { 15, 26, 37, 48 },
            { 15, 27, 39, 51 }, { 15, 30, 45, 60 }, { 15, 31, 47, 63 }, { 16, 17, 18, 19 }, { 16, 20, 24, 28 },
            { 16, 21, 26, 31 }, { 17, 21, 25, 29 }, { 18, 22, 26, 30 }, { 19, 22, 25, 28 }, { 19, 23, 27, 31 },
            { 2, 18, 34, 50 }, { 2, 22, 42, 62 }, { 2, 6, 10, 14 }, { 20, 21, 22, 23 }, { 24, 25, 26, 27 },
            { 28, 29, 30, 31 }, { 3, 18, 33, 48 }, { 3, 19, 35, 51 }, { 3, 22, 41, 60 }, { 3, 23, 43, 63 },
            { 3, 6, 9, 12 }, { 3, 7, 11, 15 }, { 32, 33, 34, 35 }, { 32, 36, 40, 44 }, { 32, 37, 42, 47 },
            { 33, 37, 41, 45 }, { 34, 38, 42, 46 }, { 35, 38, 41, 44 }, { 35, 39, 43, 47 }, { 36, 37, 38, 39 },
            { 4, 20, 36, 52 }, { 4, 21, 38, 55 }, { 4, 5, 6, 7 }, { 40, 41, 42, 43 }, { 44, 45, 46, 47 },
            { 48, 49, 50, 51 }, { 48, 52, 56, 60 }, { 48, 53, 58, 63 }, { 49, 53, 57, 61 }, { 5, 21, 37, 53 },
            { 50, 54, 58, 62 }, { 51, 54, 57, 60 }, { 51, 55, 59, 63 }, { 52, 53, 54, 55 }, { 56, 57, 58, 59 },
            { 6, 22, 38, 54 }, { 60, 61, 62, 63 }, { 7, 22, 37, 52 }, { 7, 23, 39, 55 }, { 8, 24, 40, 56 },
            { 8, 25, 42, 59 }, { 8, 9, 10, 11 }, { 9, 25, 41, 57 } };
    public static int me = 1;
    public static int opponent = 2;
    public static int depth = 1;

    public static int scoreRewards(int myScore, int opponentScore) {
        if (myScore == 0) {
            if (opponentScore == 1) {
                return -1;
            }
            if (opponentScore == 2) {
                return -100;
            }
            if (opponentScore == 3) {
                return -10000;
            }
            if (opponentScore == 4) {
                return -1000000;
            }
        } else if (opponentScore == 0) {
            if (myScore == 1) {
                return 1;
            }
            if (myScore == 2) {
                return 100;
            }
            if (myScore == 3) {
                return 10000;
            }
            if (myScore == 4) {
                return 1000000;
            }
        }
        return 0;
    }

    public static int evaluateScore(GameState state) {
        int totalScore = 0;
        for (int row = 0; row < 76; row++) {
            int myScore = 0;
            int opponentsScore = 0;
            for (int column = 0; column < 4; column++) {
                if (state.at(winningCombinations[row][column]) == me) {
                    myScore++;
                } else if (state.at(winningCombinations[row][column]) == opponent) {
                    opponentsScore++;
                }
            }
            totalScore = totalScore + scoreRewards( myScore , opponentsScore);
        }
        return totalScore;
    }

    public static int bestPlayerValue(GameState state, int alpha, int beta, int depth) {
        if (state.isEOG() || depth == 0)
            return evaluateScore(state);
        int v = Integer.MIN_VALUE;
        Vector<GameState> nextStates = new Vector<>();
        state.findPossibleMoves(nextStates);
        for (GameState gamestate : nextStates) {
            v = Math.max(v, bestOpponentValue(gamestate, alpha, beta, depth - 1));
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }

    public static int bestOpponentValue(GameState state, int alpha, int beta, int depth) {
        if (state.isEOG() || depth == 0)
            return evaluateScore(state);
        int v = Integer.MAX_VALUE;  
        Vector<GameState> nextStates = new Vector<>();
        state.findPossibleMoves(nextStates);
        for (GameState gamestate : nextStates) {
            v = Math.min(v, bestPlayerValue(gamestate, alpha, beta, depth - 1));
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

    public static int findBestPossibleMove(GameState state) {
        int v = bestPlayerValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
        return v;
    }
}