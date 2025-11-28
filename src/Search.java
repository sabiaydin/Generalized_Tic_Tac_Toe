import java.util.Comparator;
import java.util.List;

// Minimax and Alpha-Beta search algorithms
public class Search {

    // Plain Minimax: returns best move for current player.
    // Mainly used as an oracle on 3x3 for correctness checks.
    public static Move minimax(State s) {
        if (GameEngine.terminal(s)) {
            throw new IllegalArgumentException("minimax called on terminal state");
        }

        char player = s.playerToMove;
        List<Move> moves = GameEngine.actions(s);
        sortLexicographically(moves);

        int bestValue = (player == 'X') ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Move bestMove = moves.get(0);

        for (Move mv : moves) {
            State ns = GameEngine.result(s, mv);
            int value = minimaxValue(ns);
            if (player == 'X') {
                if (value > bestValue) {
                    bestValue = value;
                    bestMove = mv;
                }
            } else {
                if (value < bestValue) {
                    bestValue = value;
                    bestMove = mv;
                }
            }
        }
        return bestMove;
    }

    // Recursive value for Minimax.
    private static int minimaxValue(State s) {
        Integer u = GameEngine.utility(s);
        if (u != null) {
            return u;
        }

        char player = s.playerToMove;
        List<Move> moves = GameEngine.actions(s);
        sortLexicographically(moves);

        if (player == 'X') {
            int value = Integer.MIN_VALUE;
            for (Move mv : moves) {
                value = Math.max(value, minimaxValue(GameEngine.result(s, mv)));
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;
            for (Move mv : moves) {
                value = Math.min(value, minimaxValue(GameEngine.result(s, mv)));
            }
            return value;
        }
    }

    // Minimax with Alpha-Beta pruning, no depth limit.
    //  For 3x3 this is optimal play.
    public static Move minimaxAB(State s) {
        if (GameEngine.terminal(s)) {
            throw new IllegalArgumentException("minimaxAB called on terminal state");
        }

        char player = s.playerToMove;
        List<Move> moves = orderedMoves(s);

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Move bestMove = moves.get(0);
        int bestValue = (player == 'X') ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move mv : moves) {
            State ns = GameEngine.result(s, mv);
            int value = abValue(ns, alpha, beta);
            if (player == 'X') {
                if (value > bestValue) {
                    bestValue = value;
                    bestMove = mv;
                }
                alpha = Math.max(alpha, bestValue);
            } else {
                if (value < bestValue) {
                    bestValue = value;
                    bestMove = mv;
                }
                beta = Math.min(beta, bestValue);
            }
            if (beta <= alpha) {
                break; //prune
            }
        }
        return bestMove;
    }

    //Recursive alpha-beta value without depth limit
    private static int abValue(State s, int alpha, int beta) {
        Integer u = GameEngine.utility(s);
        if (u != null) {
            return u;
        }

        char player = s.playerToMove;
        List<Move> moves = orderedMoves(s);

        if (player == 'X') {
            int value = Integer.MIN_VALUE;
            for (Move mv : moves) {
                value = Math.max(value, abValue(GameEngine.result(s, mv), alpha, beta));
                alpha = Math.max(alpha, value);
                if (beta <= alpha) break;
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;
            for (Move mv : moves) {
                value = Math.min(value, abValue(GameEngine.result(s, mv), alpha, beta));
                beta = Math.min(beta, value);
                if (beta <= alpha) break;
            }
            return value;
        }
    }

    //Depth-limited alpha-beta:
    //depthLimit: max depth from root (root = depthLimit).
    //evalFn: heuristic used when depth reaches 0 on non-terminal nodes.
    public static Move search(State s, int depthLimit, Heuristics.EvaluationFunction evalFn) {
        if (GameEngine.terminal(s)) {
            throw new IllegalArgumentException("search called on terminal state");
        }

        char player = s.playerToMove;
        List<Move> moves = orderedMoves(s);

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        Move bestMove = moves.get(0);
        int bestValue = (player == 'X') ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Move mv : moves) {
            State ns = GameEngine.result(s, mv);
            int value = abDLValue(ns, depthLimit - 1, alpha, beta, evalFn);
            if (player == 'X') {
                if (value > bestValue) {
                    bestValue = value;
                    bestMove = mv;
                }
                alpha = Math.max(alpha, bestValue);
            } else {
                if (value < bestValue) {
                    bestValue = value;
                    bestMove = mv;
                }
                beta = Math.min(beta, bestValue);
            }
            if (beta <= alpha) break;
        }
        return bestMove;
    }
    //Recursive depth-limited alpha-beta with heuristic evaluation.
    private static int abDLValue(State s, int depth, int alpha, int beta,
                                 Heuristics.EvaluationFunction evalFn) {
        Integer u = GameEngine.utility(s);
        if (u != null) {
            return u;
        }
        if (depth == 0) {
            return evalFn.evaluate(s);
        }

        char player = s.playerToMove;
        List<Move> moves = orderedMoves(s);

        if (moves.isEmpty()) {
            return 0;
        }

        if (player == 'X') {
            int value = Integer.MIN_VALUE;
            for (Move mv : moves) {
                value = Math.max(value, abDLValue(GameEngine.result(s, mv),
                        depth - 1, alpha, beta, evalFn));
                alpha = Math.max(alpha, value);
                if (beta <= alpha) break;
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;
            for (Move mv : moves) {
                value = Math.min(value, abDLValue(GameEngine.result(s, mv),
                        depth - 1, alpha, beta, evalFn));
                beta = Math.min(beta, value);
                if (beta <= alpha) break;
            }
            return value;
        }
    }
    /* Move ordering helpers */

    // Lexicographic ordering (row, col) for deterministic tie-breaking.
    private static void sortLexicographically(List<Move> moves) {
        moves.sort(Comparator.<Move>comparingInt(mv -> mv.row)
                .thenComparingInt(mv -> mv.col));
    }
    /**
     * Move ordering:
     *  - prefer moves closer to the board center (good pruning).
     *  - break ties lexicographically for determinism.
     */
    private static List<Move> orderedMoves(State s) {
        List<Move> moves = GameEngine.actions(s);
        final double center = (s.m - 1) / 2.0;

        moves.sort(Comparator
                .comparingDouble((Move mv) -> {
                    double dr = mv.row - center;
                    double dc = mv.col - center;
                    return dr * dr + dc * dc; // squeared distance from center
                })
                .thenComparingInt(mv -> mv.row)
                .thenComparingInt(mv -> mv.col));
        return moves;
    }
}
