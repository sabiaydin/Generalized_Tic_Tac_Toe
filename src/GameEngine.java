import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    public static State initialState(int m, int k) {
        return new State(m, k);
    }

    public static char player(State s) {
        return s.playerToMove;
    }

    public static List<Move> actions(State s) {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < s.m; r++) {
            for (int c = 0; c < s.m; c++) {
                if (s.board[r][c] == '.') {
                    moves.add(new Move(r, c));
                }
            }
        }
        return moves;
    }

    public static State result(State s, Move a) {
        return s.applyMove(a);
    }

    public static Character winner(State s) {
        int m = s.m;
        int k = s.k;
        char[][] b = s.board;

        int[][] dirs = {
                {0, 1},
                {1, 0},
                {1, 1},
                {1, -1}
        };

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < m; c++) {
                char ch = b[r][c];
                if (ch == '.') continue;
                for (int[] d : dirs) {
                    int dr = d[0];
                    int dc = d[1];
                    int count = 1;
                    int rr = r + dr;
                    int cc = c + dc;
                    while (rr >= 0 && rr < m && cc >= 0 && cc < m && b[rr][cc] == ch) {
                        count++;
                        if (count == k) {
                            return ch;
                        }
                        rr += dr;
                        cc += dc;
                    }
                }
            }
        }
        return null;
    }

    public static boolean terminal(State s) {
        if (winner(s) != null) return true;

        for (int r = 0; r < s.m; r++) {
            for (int c = 0; c < s.m; c++) {
                if (s.board[r][c] == '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public static Integer utility(State s) {
        Character w = winner(s);
        if (w == null) {
            if (!terminal(s)) return null;
            return 0;
        }
        return (w == 'X') ? 1 : -1;
    }

    public static void printBoard(State s) {
        System.out.print("  ");
        for (int c = 0; c < s.m; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        for (int r = 0; r < s.m; r++) {
            System.out.print(r + " ");
            for (int c = 0; c < s.m; c++) {
                System.out.print(s.board[r][c] + " ");
            }
            System.out.println();
        }
    }
}