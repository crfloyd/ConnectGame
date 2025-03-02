# Connect M

Connect M is a Java implementation of a configurable board game similar to Connect Four. A human player competes against an AI opponent that uses adversarial search with alpha-beta pruning to select its moves. This project was developed as part of the CAP 4601 Intro to AI course in Spring 2025.

## Features

- Customizable Board: Play on an N x N grid where N is between 3 and 10.
- Flexible Win Condition: Set the number of contiguous disks (M) needed to win, with 2 ≤ M ≤ N.
- Adversarial AI: The AI uses a minimax algorithm with alpha-beta pruning for decision-making.
- Graphical Interface: Features a Swing-based GUI with animations and status messages.

## Prerequisites

- Java: JDK 17 or later.
- Optional - Gradle: The project includes the Gradle Wrapper, so you don’t need to install Gradle separately. However, Gradle is recommended for building and running the project.

## Project Structure

The source code is organized under src/main/java/com/connectm/:
- Main.java: Entry point for the application.
- ai/AIPlayer.java: Implements the AI using minimax with alpha-beta pruning.
- controller/GameController.java: Manages game flow and coordinates between model and view.
- model/: Contains Board.java, GameState.java, and Move.java for game state and logic.
- view/ConnectMView.java: Handles the graphical user interface and user input.

## Build Instructions

This project uses Gradle for building, which is the recommended approach. A manual javac compilation option is also provided as a fallback.

### Option 1: Build Using Gradle (Recommended)

1. Navigate to the Project Directory:
   Ensure you are in the root directory of the project (where build.gradle is located).

2. Build the Project:
   Use the Gradle Wrapper to compile and package the project into a JAR file:
   ./gradlew build
   The JAR file will be generated in build/libs/ as ConnectM-1.0-SNAPSHOT.jar.

Note: If the build fails, ensure JDK 17 is installed and properly configured in your environment (java -version should show version 17 or later).

### Option 2: Build Using javac (Manual Compilation)

1. Compile the Source Files:
   From the project root, run:
   mkdir -p build/classes
   javac -d build/classes src/main/java/com/connectm/*.java src/main/java/com/connectm/**/*.java
   This compiles all Java files into the build/classes/ directory.

2. Create a JAR File:
   Create a manifest file (e.g., manifest.txt) with the following content:
   Main-Class: com.connectm.Main
   Then, package the compiled classes into a JAR:
   jar cfm build/libs/ConnectM-1.0-SNAPSHOT.jar manifest.txt -C build/classes .

## Run Instructions

The game requires three command-line arguments:
- N: The size of the board (both columns and rows). Must be between 3 and 10.
- M: The number of contiguous disks required to win. Must be between 2 and N.
- H: A flag indicating which player goes first:
   - 1: Human goes first.
   - 0: AI goes first.

### If Built with Gradle

Option 1: Run Using Gradle:
./gradlew runApp --args="8 4 1"

Option 2: Run Using the JAR File:
java -jar build/libs/ConnectM-1.0-SNAPSHOT.jar 8 4 1

Example:
To play on an 8x8 board where 4 contiguous disks are needed to win, with the human moving first:
java -jar build/libs/ConnectM-1.0-SNAPSHOT.jar 8 4 1

Note: If invalid arguments are provided (e.g., N < 3 or M > N), the program will print an error message and exit.

## Adjusting AI Difficulty

The AI’s difficulty can be adjusted by changing the MAX_DEPTH constant in AIPlayer.java. The default value is 4:
- Higher values (e.g., 5 or 6) make the AI stronger but slower.
- Lower values (e.g., 2 or 3) make the AI faster but less strategic.

## Troubleshooting

- Build Fails with Gradle: Ensure JDK 17 is installed (java -version). Run ./gradlew build --stacktrace for detailed error information.
- Build Fails with javac: Ensure all source files are correctly specified in the javac command.
- Invalid Arguments: Double-check that N, M, and H meet the constraints (3 ≤ N ≤ 10, 2 ≤ M ≤ N, H = 0 or 1).
- Slow AI: The AI move delay is set to 200ms for a natural feel. Adjust AI_MOVE_DELAY_MS in GameController.java if needed (e.g., reduce to 100ms for faster turns).

## License

This project is unlicensed and intended for educational purposes as part of the CAP 4601 course. All rights reserved by the author.