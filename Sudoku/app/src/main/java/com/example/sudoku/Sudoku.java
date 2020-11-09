package com.example.sudoku;

import java.util.ArrayList;

public class Sudoku {
	public static enum Difficulty {
		EASY(0),
		MEDIUM(1),
		HARD(2);

		private int val;

		Difficulty(int val) {
			this.val = val;
		}

		public int getVal() {
			return this.val;
		}

		public static Difficulty getDifficultyFromStr(String diff) {
			switch (diff.toLowerCase()) {
				case "easy":
					return EASY;
				case "medium":
					return MEDIUM;
				case "hard":
					return HARD;
				default:
					throw new IllegalArgumentException("Legal arguments are: \"easy\", \"medium\", \"hard\"");
			}
		}
	}

	private final String raw;
	int[][] numbers;

	public Sudoku() {
		this.raw = "";
		this.numbers = new int[9][9];
	}

	public Sudoku(String raw) {
		this.raw = raw;
		this.numbers = getNumbersFromString(raw);
	}

	public void setNumbers(String raw) {
		this.numbers = this.getNumbersFromString(raw);
	}

	private int[][] getNumbersFromString(String raw) {
		int[][] out = new int[9][9];
		for (int i = 0; i < raw.length(); ++i) {
			out[i / 9][i % 9] = Integer.parseInt(Character.toString(raw.charAt(i)));
		}
		return out;
	}

	public void setNumber(int i, int j, int number) {
		if (number <= 0) this.numbers[i][j] = 0;
		else if (number > 9) throw new IllegalArgumentException("Number must be in range 0-9");
		else this.numbers[i][j] = number;
	}

	public int getNumber(int i, int j) {
		return this.numbers[i][j];
	}

	public int[][] getNumbers() {
		return numbers;
	}

	@Override
	public String toString() {
		StringBuilder temp = new StringBuilder();
		for (int[] number : this.numbers) {
			for (int i : number) {
				temp.append(i);
			}
		}
		return temp.toString();
	}

	public boolean isSolved() {
		for (int[] number : this.numbers) {
			for (int i : number) {
				if (i == 0) return false;
			}
		}

		for (int[] number : this.numbers) {
			int sum = 0;
			for (int i : number) {
				sum += i;
			}
			if (sum != 45) return false;
		}

		for (int i = 0; i < numbers[0].length; i++) {
			int sum = 0;
			for (int[] number : numbers) {
				sum += number[i];
			}
			if (sum != 45) return false;
		}

		for (int i = 0; i < 9; i += 3) {
			for (int j = 0; j < 9; j += 3) {
				if (getSquareSum(i, i + 3, j, j + 3) != 45) return false;
			}
		}

		for (int[] number : this.numbers) {
			ArrayList<Integer> vals = new ArrayList<>();
			for (int i : number) {
				if (vals.contains(i)) return false;
				vals.add(i);
			}
		}

		for (int i = 0; i < numbers[0].length; i++) {
			ArrayList<Integer> vals = new ArrayList<>();
			for (int[] number : numbers) {
				if (vals.contains(number[i])) return false;
				vals.add(number[i]);
			}
		}

		for (int i = 0; i < 9; i += 3) {
			for (int j = 0; j < 9; j += 3) {
				if (!checkSquare(i, i + 3, j, j + 3)) return false;
			}
		}

		return true;
	}

	private int getSquareSum(int rowStart, int rowEnd, int colStart, int colEnd) {
		int sum = 0;
		for (int i = rowStart; i < rowEnd; i++) {
			for (int j = colStart; j < colEnd; j++) {
				sum += this.numbers[i][j];
			}
		}
		return sum;
	}

	private boolean checkSquare(int rowStart, int rowEnd, int colStart, int colEnd) {
		ArrayList<Integer> vals = new ArrayList<>();
		for (int i = rowStart; i < rowEnd; i++) {
			for (int j = colStart; j < colEnd; j++) {
				if (vals.contains(this.numbers[i][j])) return false;
				vals.add(this.numbers[i][j]);
			}
		}
		return true;
	}
}
