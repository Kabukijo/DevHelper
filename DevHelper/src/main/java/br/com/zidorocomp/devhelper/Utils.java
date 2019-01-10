package br.com.zidorocomp.devhelper;

import java.util.Scanner;

public class Utils {

	private static Scanner scanner = null;

	public static String readInput() {

		String input = null;

		if (scanner == null) {
			scanner = new Scanner(System.in);
		}

		scanner.reset();
		input = scanner.next();
		scanner.reset();

		return input;
	}
}
