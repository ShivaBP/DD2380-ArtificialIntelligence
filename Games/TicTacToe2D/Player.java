import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
         	return new GameState(gameState, new Move());
        } 
        Vector<Integer> stateValues = new Vector<>();
        for (GameState state: nextStates) {
            int v = AlphaBetaPruning.findBestPossibleMove(state);
            stateValues.add(v);
        }
        int bestValue = stateValues.elementAt(0);
        int position = 0;
        for (int i = 0; i< stateValues.size(); i++){
            if (stateValues.elementAt(i) > bestValue){
                bestValue = stateValues.elementAt(i);
                position= i;
            }
        }
        GameState result = nextStates.elementAt(position);
        return result;
    }    
}
