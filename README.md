# 15 Puzzle Solver
Author: John Gluzniewicz

## Description
This Java application implements several graph search algorithms to solve the classic 15 Puzzle game. It includes Breadth-First Search (BFS), Depth-First Search (DFS), and A-star (A*) algorithms with heuristic options. The project also features an interactive JavaFX GUI to visualize and interact with the puzzle-solving process.

The 15 Puzzle, also known as the sliding puzzle, consists of a 4x4 grid with 15 numbered tiles that can slide horizontally or vertically. The goal is to rearrange the tiles from a scrambled initial configuration into an ordered sequence.

## Features
- **BFS**: Breadth-First Search algorithm implementation for solving the 15 Puzzle.
- **DFS**: Depth-First Search algorithm implementation with a depth limit for solving the 15 Puzzle.
- **A-star**: A* algorithm implementation with both Manhattan Distance and Hamming Distance heuristics.
- **Interactive GUI**: JavaFX-based graphical user interface to visualize and control the puzzle-solving algorithms.
- **Random Board Generation**: Capability to generate random solvable 15 Puzzle boards for algorithm testing and demonstration.

## Usage
### Requirements
- Java Development Kit (JDK) 8 or higher
- JavaFX (included in JDK until JDK 10, separate installation required for JDK 11+)

### Example Usage
1. Launch the application.
2. Select an algorithm (BFS, DFS, A-star) from the Algorithm dropdown menu.
3. Choose a heuristic (for A-star) or move order (for BFS and DFS) from the corresponding dropdown menus.
4. Click "Generate" to create a random solvable 15 Puzzle board.
5. Click "Play" to start the algorithm to solve the puzzle. Watch as the solution steps are demonstrated on the board.

#### Choosing Parameters
- **BFS and DFS**: Select a move order parameter (e.g., RUDL) to specify the preferred order of moves.
- **A-star**: Choose between "MANH" (Manhattan Distance) and "HAMM" (Hamming Distance) heuristics.

## Installation and Running
### Setting Up the Project
1. Clone the repository:
   ```
   git clone https://github.com/your_username/graph-search-algorithms.git
   cd 15-puzzle-solver
   ```
2. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
3. Build and run the `MainWindow.java` class to start the JavaFX application.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
