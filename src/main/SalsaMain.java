package main;
import keygens.*;
import java.util.Scanner;

public class SalsaMain {
	
	public static void main(String[] args)
	{
		String IPKey, output;;
		int length;
		SalsaGen salsa;
		salsa = new SalsaGen();
		Scanner sc = new Scanner(System.in);
		System.out.println("\nEnter the key to Salsafy");
		IPKey = sc.nextLine();
		salsa.setUserKey(IPKey);
		System.out.println("Enter the output key length");
		length = sc.nextInt();
		salsa.setLength(length);
		salsa.setUserKey(IPKey);
		salsa.generateSalsa();
		output = salsa.getOutput();
		System.out.println("Encoded string is " + output);
		sc.close();
	}

}
