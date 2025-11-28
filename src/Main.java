import java.util.Scanner;
/**
 * Simple console interface to play against the AI.
 * - Supports arbitrary m x m board and k-in-a-row.
 * - X is Max, O is Min.
 */
public class Main {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);

            System.out.print("Board size m (e.g., 3 or 4 or 5): ");
            int m = sc.nextInt();
            System.out.print("Win condition k (e.g., 3 or 4): ");
            int k = sc.nextInt();

            State state = GameEngine.initialState(m, k);
            Heuristics.EvaluationFunction heuristic = new Heuristics.DefaultHeuristic();

            System.out.print("Do you want to be X (first)? (y/n): ");
            boolean humanIsX = sc.next().trim().toLowerCase().startsWith("y");

            int depthLimit;
            if (m == 3 && k == 3) {
                depthLimit = 9;  // full search
            } else if (m == 4) {
                depthLimit = 5;
            } else if (m == 5) {
                depthLimit = 4;
            } else {
                depthLimit = 4;  //default for larger boards (tune if needed)
            }

            while (true) {
                GameEngine.printBoard(state);

                if (GameEngine.terminal(state)) {
                    Integer u = GameEngine.utility(state);
                    if (u != null) {
                        if (u > 0) {
                            System.out.println("X wins!");
                        } else if (u < 0) {
                            System.out.println("O wins!");
                        } else {
                            System.out.println("Draw!");
                        }
                    } else {
                        System.out.println("Game over.");
                    }
                    break;
                }

                char currentPlayer = state.playerToMove;
                System.out.println("Current player: " + currentPlayer);

                boolean humanTurn = (currentPlayer == 'X' && humanIsX)
                        || (currentPlayer == 'O' && !humanIsX);

                if (humanTurn) {
                    System.out.print("Enter your move as 'row col': ");
                    int r = sc.nextInt();
                    int c = sc.nextInt();

                    if (r < 0 || r >= m || c < 0 || c >= m || state.board[r][c] != '.') {
                        System.out.println("Invalid move, try again.");
                        continue;
                    }

                    Move mv = new Move(r, c);
                    state = GameEngine.result(state, mv);
                } else {
                    Move bestMove;
                    if (m == 3 && k == 3) {
                        // full alpha-beta for optimal play
                        bestMove = Search.minimaxAB(state);
                    } else {
                        // depth-limited alpha-beta with heuristic
                        bestMove = Search.search(state, depthLimit, heuristic);
                    }
                    System.out.println("AI plays: " + bestMove);
                    state = GameEngine.result(state, bestMove);
                }
            }

            sc.close();
        }
    }
