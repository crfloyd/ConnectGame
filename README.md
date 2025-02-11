# Connect M

Connect M is a Java implementation of a configurable board game similar to Connect Four. In this game, a human player competes against an AI that uses adversarial search with alpha-beta pruning to select moves.

## Features

- **Customizable Board:** Play on an N x N grid where N is between 3 and 10.
- **Flexible Win Condition:** Set the number of contiguous disks (M) needed to win, with 2 ≤ M ≤ N.
- **Adversarial AI:** The computer uses a minimax algorithm with alpha-beta pruning to decide its moves.
- **Graphical Interface:** The game features a modern Swing-based GUI using FlatLaf for styling.

## Prerequisites

- **Java:** JDK 17 or later.
- **Gradle:** Either a local Gradle installation or use the provided Gradle Wrapper.

## Build Instructions

1. **Clone the Repository:**

   ```bash
   git clone <repository-url>
   cd <repository-directory>
   
2. **Build the Project:**
   ```bash
   ./gradlew build
   ```
   or using Gradle Wrapper:
   ```bash
   ./gradlew build
    ```
   The build process will compile your code and package it into a JAR file located in the build/libs/ directory.

3. **Run the Game:**
   The application requires three command-line arguments:

- **N** - The size of the board (both columns and rows). Must be between 3 and 10.
- **M** - The number of contiguous disks required to win. Must be between 2 and N.
- **H** - A flag indicating which player goes first:
  - 1 means the human goes first.
  - 0 means the AI goes first.
  
**Example**
  To run an 8x8 board game where 4 contiguous disks are needed to win and the human makes the first move, execute:
```bash
java -jar build/libs/ConnectM-1.0-SNAPSHOT.jar 8 4 1
```
or using Gradle:
```bash
./gradlew run --args="8 4 1"
```

## Additional Notes

**AI Difficulty**: The AI’s difficulty can be adjusted by modifying the search depth in AIPlayer.java.
