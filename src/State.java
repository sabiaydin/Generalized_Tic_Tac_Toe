import java.util.Arrays;
public class State {
    public final int m;
    public final int k;
    public final char[][] board;
    public final char playerToMove;


    public State(int m, int k) {
        this.m = m;
        this.k = k;
        this.board = new char[m][m];
        for (int i = 0; i < m; i++) {
            Arrays.fill(this.board[i], '.');
        }
        this.playerToMove = 'X';
    }
    public State(int m, int k, char[][] board, char playerToMove) {
        this.m = m;
        this.k = k;
        this.board = board;
        this.playerToMove = playerToMove;
    }

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

