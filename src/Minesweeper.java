import java.util.Scanner;

public class Minesweeper {
	private static final String GRID_SIZE = "grid size";
	private static final String MINE_COUNT = "mine count";

	public static void main(String[] args) {
		if (args.length > 0 && args[0].toLowerCase().equals("solver")) {
			// Run solver
		}
		else {
			playGame();
		}
	}

	private static void playGame() {
		Scanner scanner = new Scanner(System.in);

		int gridSize = promptInput(GRID_SIZE, scanner, 0);
		int mineCount = promptInput(MINE_COUNT, scanner, gridSize);

		Game game = new Game(gridSize, mineCount);
		game.printGrid();

		while (game.getStatus() == Status.IN_PROGRESS) {
			int rowIndex = promptInput("row index", scanner, gridSize);
			int columnIndex = promptInput("column index", scanner, gridSize);

			game.move(rowIndex, columnIndex);
			game.printGrid();
		}

		if (game.getStatus() == Status.WIN) {
			System.out.println("You win!");
		}
		else if (game.getStatus() == Status.LOSE) {
			System.out.println("You lose!");
		}

		scanner.close();
	}

	private static int promptInput(String type, Scanner scanner, int gridSize) {
		int input = 0;
		boolean valid = false;

		while (!valid) {
			System.out.print("Please enter " + type + ": ");

			if (scanner.hasNextInt()) {
				input = scanner.nextInt();

				if (type.equals(GRID_SIZE)) {
					valid = isValidGridSize(input);
				}
				else if (type.equals(MINE_COUNT)) {
					valid = isValidMineCount(input, Math.pow(gridSize, 2));
				}
				else {
					valid = isValidIndex(input, gridSize, type);
				}
			}
			else {
				scanner.next();
				System.out.println("Input is not an integer.");
			}
		}

		return input;
	}

	private static boolean isValidGridSize(int gridSize) {
		boolean valid = false;

		if (gridSize <= 1) {
			System.out.println("Grid size should be bigger than 1.");
		}
		else {
			valid = true;
		}

		return valid;
	}

	private static boolean isValidMineCount(int mineCount, double cellCount) {
		boolean valid = false;

		if (mineCount <= 0) {
			System.out.println("Mine count should be bigger than 0.");
		}
		else if (mineCount >= cellCount) {
			System.out.println("Mine count should be less than " + String.format("%d", (long)cellCount) + ".");
		}
		else {
			valid = true;
		}

		return valid;
	}

	private static boolean isValidIndex(int index, int gridSize, String type) {
		boolean valid = false;
		String capitalizedType = Character.toUpperCase(type.charAt(0)) + type.substring(1);

		if (index < 0) {
			System.out.println(capitalizedType + " should be equal to or bigger than 0.");
		}
		else if (index >= gridSize) {
			System.out.println(capitalizedType + " should be less than " + gridSize + ".");
		}
		else {
			valid = true;
		}

		return valid;
	}
}
