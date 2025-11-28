import java.util.Arrays;

// Representing a game state: board + whose turn + (m, k).
// X is Max, O is Min
public class State {
    public final int m;
    public final int k;
    public final char[][] board;
    public final char playerToMove;

    // Creating an empty m x m board, X to move.
    public State(int m, int k) {
        this.m = m;
        this.k = k;
        this.board = new char[m][m];
        for (int i = 0; i < m; i++) {
            Arrays.fill(this.board[i], '.');
        }
        this.playerToMove = 'X';
    }

    //Full constructor
    public State(int m, int k, char[][] board, char playerToMove) {
        this.m = m;
        this.k = k;
        this.board = board;
        this.playerToMove = playerToMove;
    }

    // Returnning a new State with the given move applied.
    public State applyMove(Move move) {
        if (board[move.row][move.col] != '.') {
            throw new IllegalArgumentException("Illegal move: cell not empty");
        }
        char[][] newBoard = new char[m][m];
        for (int i = 0; i < m; i++) {
            newBoard[i] = Arrays.copyOf(board[i], m);
        }
        newBoard[move.row][move.col] = playerToMove;
        char nextPlayer = (playerToMove == 'X') ? 'O' : 'X';
        return new State(m, k, newBoard, nextPlayer);
    }
}

