import java.util.*;
// I am player 1 , opponent is player 2!
// https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
// http://www.ntu.edu.sg/home/ehchua/programming/java/javagame_tictactoe_ai.html
// https://www.hackerearth.com/blog/artificial-intelligence/minimax-algorithm-alpha-beta-pruning/

public class AlphaBetaPruning {

    public static int[][] winningCombinations = { { 0, 1, 2, 3 }, { 4, 5, 6, 7 }, { 8, 9, 10, 11 }, { 12, 13, 14, 15 },
            { 0, 4, 8, 12 }, { 1, 5, 9, 13 }, { 2, 6, 10, 14 }, { 3, 7, 11, 15 }, { 0, 5, 10, 15 }, { 3, 6, 9, 12 } };

    public static int[][] scoreRewards = { { 0, -1, -100, -10000, -1000000 }, { 1, 0, 0, 0, 0 }, { 100, 0, 0, 0, 0 },
            { 10000, 0, 0, 0, 0 }, { 1000000, 0, 0, 0, 0 } };
    public static int me = 1;
    public static int opponent = 2;
    public static int depth = 2;

    public static int evaluateScore(GameState state) {
        int totalScore = 0;
        for (int row = 0; row < 10; row++) {
            int myScore = 0;
            int opponentsScore = 0;
            for (int column = 0; column < 4; column++) {
                if (state.at(winningCombinations[row][column]) == me) {
                    myScore++;
                } else if (state.at(winningCombinations[row][column]) == opponent) {
                    opponentsScore++;
                }
            }
            totalScore = totalScore + scoreRewards[myScore][opponentsScore];
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