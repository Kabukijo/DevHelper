package br.com.zidorocomp.devhelper;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {

		String option = null;
		Scanner sc = new Scanner(System.in);

		while (option == null || !option.equals("3")) {

			System.out.println("Welcome dev, choose one option:");
			System.out.println("1 - Purge WAS");
			System.out.println("2 - Easy Deploy");
			System.out.println("3 - Exit");

			sc.reset();
			option = Utils.readInput();

			switch (option) {

			case "1":
				new WASFriend().purge();
				break;

			case "2":
				new WASFriend().deployApp();
				break;

			case "3":
				System.out.println("Ok, bye!");
				break;
			}
		}
		sc.close();
	}
}
