import java.util.*;

public class Game {
	private int gridSize;
	private int mineCount;
	private String[][] grid;

	private Status status = Status.IN_PROGRESS;
	private int moveCount = 0;
	private String gridHeader = null;

	/**
	 * Stores the location of mines.
	 * TreeMap - keys: row indexes, values: sets of column indexes
	 * TreeSet - column indexes
	 */
	private TreeMap<Integer, TreeSet<Integer>> mines = new TreeMap<Integer, TreeSet<Integer>>();

	public Game(int gridSize, int mineCount) {
		this.gridSize = gridSize;
		this.mineCount = mineCount;

		grid = new String[gridSize][gridSize];

		setupMines();
	}

	private void setupMines() {
		Random random = new Random();

		for (int i = 0; i < mineCount; i++) {
			int[] location = generateRandomMine(random);

			while (containsMine(location[0], location[1])) {
				location = generateRandomMine(random);
			}

			TreeSet<Integer> columnSet;
			if (mines.containsKey(location[0])) {
				columnSet = mines.get(location[0]);
			}
			else {
				columnSet = new TreeSet<Integer>();
				mines.put(location[0], columnSet);
			}

			columnSet.add(location[1]);
		}
	}

	private int[] generateRandomMine(Random random) {
		int[] location = new int[2];

		for (int i = 0; i < 2; i++) {
			location[i] = random.nextInt(gridSize);
		}

		return location;
	}

	private boolean containsMine(int rowIndex, int columnIndex) {
		if (!mines.containsKey(rowIndex)) {
			return false;
		}
		else {
			return mines.get(rowIndex).contains(columnIndex);
		}
	}

	public Status getStatus() {
		return status;
	}

	public void printGrid() {
		if (gridHeader == null) {
			StringBuilder stringBuilder1 = new StringBuilder();
			StringBuilder stringBuilder2 = new StringBuilder();

			for (int i = 0; i < gridSize; i++) {
				stringBuilder1.append(i + " ");
				stringBuilder2.append("- ");
			}

			gridHeader = "  " + stringBuilder1.toString() + "\n  " + stringBuilder2.toString();
		}

		System.out.println(gridHeader);

		for (int i = 0; i < gridSize; i++) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(i + "|");

			for (int j = 0; j < gridSize; j++) {
				if (grid[i][j] == null) {
					stringBuilder.append("X ");
				} else {
					stringBuilder.append(grid[i][j] + " ");
				}
			}

			System.out.println(stringBuilder.toString());
		}
	}

	private String getErrorMessageForMove(int rowIndex, int columnIndex) {
		String errorMessage = null;

		if (rowIndex >= gridSize) {
			errorMessage = "Row index is out of bound.";
		}
		else if (columnIndex >= gridSize) {
			errorMessage = "Column index is out of bound.";
		}
		else if (grid[rowIndex][columnIndex] != null) {
			errorMessage = "This cell has already been uncovered.";
		}

		return errorMessage;
	}

	public String move(int rowIndex, int columnIndex) {
		String errorMessage = getErrorMessageForMove(rowIndex, columnIndex);
		if (errorMessage != null) {
			return errorMessage;
		}

		if (containsMine(rowIndex, columnIndex)) {
			grid[rowIndex][columnIndex] = "M";
			status = Status.LOSE;
		}
		else {
			int surroundingMineCount = calculateSurroundingMineCount(rowIndex, columnIndex);

			if (surroundingMineCount > 0) {
				grid[rowIndex][columnIndex] = Integer.toString(surroundingMineCount);
			}
			else {
				grid[rowIndex][columnIndex] = ".";
			}

			moveCount++;

			if ((Math.pow(gridSize, 2) - moveCount) == mineCount) {
				status = Status.WIN;
			}
		}

		return null;
	}

	private int calculateSurroundingMineCount(int rowIndex, int columnIndex) {
		int surroundingMineCount = 0;

		if (rowIndex > 0 && mines.containsKey(rowIndex - 1)) {
			surroundingMineCount += calculateSurroundingMineCount(
				mines.get(rowIndex - 1),
				columnIndex,
				true
			);
		}

		if (mines.containsKey(rowIndex)) {
			surroundingMineCount += calculateSurroundingMineCount(
				mines.get(rowIndex),
				columnIndex,
				false
			);
		}

		if (rowIndex < (gridSize - 1) && mines.containsKey(rowIndex + 1)) {
			surroundingMineCount += calculateSurroundingMineCount(
				mines.get(rowIndex + 1),
				columnIndex,
				true
			);
		}

		return surroundingMineCount;
	}

	private int calculateSurroundingMineCount(TreeSet<Integer> columnSet, int columnIndex, boolean includeColumnIndex) {
		int surroundingMineCount = 0;

		if (columnIndex > 0 && columnSet.contains(columnIndex - 1)) {
			surroundingMineCount++;
		}

		if (includeColumnIndex && columnSet.contains(columnIndex)) {
			surroundingMineCount++;
		}

		if (columnIndex < (gridSize - 1) && columnSet.contains(columnIndex + 1)) {
			surroundingMineCount++;
		}

		return surroundingMineCount;
	}
}
