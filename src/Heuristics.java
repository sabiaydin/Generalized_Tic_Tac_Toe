// Heuristic evaluation functions for depth-limited search.
public class Heuristics {
    /**
     * Functional interface for evaluation.
     * Returns a score from X's perspective (higher is better for X).
     */
    public interface EvaluationFunction {
        int evaluate(State s);
    }
    /**
     * A domainspecific heristic:
     * - Large positive for strong X positions.
     * - Large negative for strong O positions.
     * - Uses open k-length segments and partial sequences.
     */
    public static class DefaultHeuristic implements EvaluationFunction {

        @Override
        public int evaluate(State s) {
            Integer u = GameEngine.utility(s);
            if (u != null) {
                return u * 1_000_000;
            }
            int scoreX = scoreForPlayer(s, 'X');
            int scoreO = scoreForPlayer(s, 'O');
            return scoreX - scoreO;
        }
       //Score for a particular player based on potential k-length lines.
        private int scoreForPlayer(State s, char player) {
            char opponent = (player == 'X') ? 'O' : 'X';
            int m = s.m;
            int k = s.k;
            char[][] b = s.board;

            int[][] dirs = {
                    {0, 1},
                    {1, 0},
                    {1, 1},
                    {1, -1}
            };

            int score = 0;

            for (int r = 0; r < m; r++) {
                for (int c = 0; c < m; c++) {
                    for (int[] d : dirs) {
                        int dr = d[0];
                        int dc = d[1];
                        int endR = r + (k - 1) * dr;
                        int endC = c + (k - 1) * dc;

                        // ensuring segment stays within boar
                        if (endR < 0 || endR >= m || endC < 0 || endC >= m) continue;

                        int countPlayer = 0;
                        int countOpp = 0;

                        for (int i = 0; i < k; i++) {
                            int rr = r + i * dr;
                            int cc = c + i * dc;
                            char ch = b[rr][cc];
                            if (ch == player) countPlayer++;
                            else if (ch == opponent) countOpp++;
                        }
                        // Only care about lines without opponent pieces
                        if (countOpp == 0 && countPlayer > 0) {
                            if (countPlayer == k - 1) {
                                score += 1000;
                            } else if (countPlayer == k - 2) {
                                score += 100;
                            } else {
                                score += 10;
                            }
                        }
                    }
                }
            }
            return score;
        }
    }
}
