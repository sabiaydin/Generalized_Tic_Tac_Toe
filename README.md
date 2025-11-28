1)Introduction: The goal is to design an intelligent agent using:
-Minimax search
-Minimax with Alpha–Beta pruning
-Depth-limited search with a heuristic evaluator
-Deterministic move ordering

The agent must play optimally on a standard 3×3 (k = 3) board and perform strongly on larger boards like 4×4 (k = 3) and 5×5 (k = 4) under depth constraints.

2)System Architecture
The system is divided into six modules, each with a clear responsibility:
| Module       | Responsibility                                                  |
| ------------ | --------------------------------------------------------------- |
| `State`      | Represents the board and active player                          |
| `Move`       | Encapsulates a move (row, col)                                  |
| `GameEngine` | Rules: legal moves, winner detection, terminal tests, utilities |
| `Search`     | Minimax, Alpha–Beta, depth-limited Alpha–Beta                   |
| `Heuristics` | Evaluation function for non-terminal search states              |
| `Main`       | Console-based user interface                                    |

3)Game Engine Design

The game engine implements all rules needed for adversarial search:
3.1 State Representation
A State object contains:
-board char[][]
-size m
-win condition k
-current player X or O
State transitions are immutable:
Every move generates a new state → avoids mutation bugs.

3.2 Legal Move Generation
actions(state) returns all empty cells.
This guarantees correctness and ensures Minimax explores all possible actions.

3.3 Transition Model
result(state, action) → returns a new state with the move applied.

3.4 Winner Detection
The engine scans:
-all rows
-all columns
-main diagonals
-anti-diagonals

This detection is generalized for any m and k.
Works for 3×3, 4×4, 5×5, and any board shape.

3.5 Terminal States
A state is terminal if:
-X wins
-O wins
-the board is full (draw)

3.6 Utility Function
+1 → X wins
–1 → O wins
0 → draw
null → non-terminal

This utility drives Minimax and Alpha–Beta.

4)Search Algorithms
The AI agent implements three adversarial search algorithms.

4.1 Plain Minimax
A full Minimax recursion exploring the entire tree:
-uses utility values
-returns optimal move
-lexicographically breaks ties for determinism

This is used as a correctness oracle for 3×3.

4.2 Alpha–Beta Pruning
Improves Minimax by pruning branches that cannot influence the final decision.
Key improvements:
-reduces node exploration drastically
-produces exactly the same move as Minimax on 3×3
-uses ordered moves for deeper pruning

4.3 Depth-Limited Alpha–Beta

For larger boards, full Minimax is impossible.
Therefore, depth-limited Alpha–Beta is used.
When depth reaches 0:    return heuristic.evaluate(state)

5.Heuristic Evaluation Function
Designed to evaluate boards from X’s perspective.
5.1 Scoring Rules
Only segments of length k that contain no opponent pieces are evaluated:
| Segment Pattern       | Score |
| --------------------- | ----- |
| k − 1 in a row        | +1000 |
| k − 2 in a row        | +100  |
| any partial open line | +10   |
This ensures:
-urgent wins are prioritized
-urgent blocks are recognized
-long-term planning exists

The heuristic is symmetric:
O’s strength is subtracted from X’s to compute net score.

6.Move Ordering Strategy
Before Alpha–Beta search, moves are sorted by:
-Distance to the board center (center moves explored first)
-Lexicographic order for reproducibility

This dramatically increases pruning efficiency.

7.Performance Evaluation

Experiments were performed to compare:
-Minimax vs Alpha–Beta
-Alpha–Beta with vs without move ordering
-Depth-limited performance on larger boards

7.1 Minimax vs Alpha–Beta (3×3)
| Search Type | Nodes Explored |
| ----------- | -------------- |
| Minimax     | ~255,168       |
| Alpha–Beta  | ~4,100 – 6,500 |
-> ≈40× improvement
Both algorithms choose exactly the same moves.
7.2 Depth-Limited Performance
| Board      | Depth     | Time (ms) | Notes                 |
| ---------- | --------- | --------- | --------------------- |
| 3×3 (full) | unlimited | ~1 ms     | Optimal               |
| 4×4 (k=3)  | 5         | 15–40 ms  | Good threat detection |
| 5×5 (k=4)  | 4         | 20–80 ms  | Detects wins & blocks |
The agent remains fast enough for interactive play.

The project includes a full JUnit test suite covering:

8.Testing and Validation

The project includes a full JUnit test suite covering:
 Winner detection
 Legal moves
 Minimax correctness
 Alpha–Beta correctness
 Minimax = Alpha–Beta on 3×3
 Depth-limited threat blocking
 Immediate win detection
All tests pass successfully.
9.How to Run
Running with IntelliJ
Open project
Mark src as Sources Root
Open Main.java
Click Run

