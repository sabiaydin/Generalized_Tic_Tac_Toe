Generalized Tic-Tac-Toe (m×m, k-in-a-row)
1)This project implements a fully-functional adversarial search agent that plays generalized Tic-Tac-Toe using:
-Minimax
-Minimax with Alpha–Beta pruning
-Depth-limited Alpha–Beta + heuristic evaluation
-Deterministic move ordering
-The agent is optimal on 3×3 (k=3) and scalable for larger boards such as 4×4 (k=3) and 5×5 (k=4).
2)Features
-> Complete Game Engine
-State representation
-Legal actions
-Transition model
-Winner detection for arbitrary m, k
-Utility and terminal checks
-> Three Search Algorithms
a)Plain Minimax – used only for 3×3 verification
b)Alpha–Beta Pruning – optimal for 3×3
c)Depth-Limited Alpha–Beta – used for m>3 boards
Heuristic Evaluator
-> The heuristic evaluates partial k-length lines:
- +1000 for (k−1)-in-a-row
- +100 for (k−2)-in-a-row
- +10 for open lines with ≥1 piece
- Opponent-containing lines ignored
Designed from X's perspective (Max).
-> Move Ordering
Moves ordered by:
- Distance to center (improves pruning)
- Lexicographic tie-breaking for determinism


Overall report:
1. Design Choises
-Separeted modules
Clean separation between game rules, search algorithms, state representation, and heuristics.

-Imutable state transitions
Each move creates a new State object → prevents mutation errors.

-Deterministic ordering
Required for reproducibilitu and grading.

-Move ordering by distanse to center
Highly improves Alpha–Beta in practise.

2. Evaluation Function
-The heuristic scores each k-length segment only if it contains no opponent pieces:

--Line Type	Score--
| Line Type            | Score |
| -------------------- | ----- |
| (k−1)-in-a-row       | +1000 |
| (k−2)-in-a-row       | +100  |
| any positive partial | +10   |

This ensures:
-urgent wins are prioritezed
-urgent blocks are detected
-long-range planning possible

3. Alpha–Beta Pruning Effectiveness
-Without ordering (minimax only)
Nodes expanded (3×3): 255,168

-With alpha-beta + ordering
Nodes expanded (3×3): ≈4,100–6,500

4. Performance (Depth-Limited)
| Board   | Depth | Time (ms) | Notes                         |
| ------- | ----- | --------- | ----------------------------- |
| 3×3 k=3 | full  | ~1 ms     | Solves entire game tree       |
| 4×4 k=3 | 5     | 15–40 ms  | Good threat detection         |
| 5×5 k=4 | 4     | 20–80 ms  | Detects immediate wins/blocks |

