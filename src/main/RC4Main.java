package main;

import java.util.Scanner;
import keygens.*;



public class RC4Main {
	/*public static void main (String[] args)
    {
        int choice = 0;
        boolean isRandom = false;
        Scanner sc = new Scanner(System.in);
        RC4Gen userKey = new RC4Gen();
        System.out.println ("==================================================");
        System.out.println ("||\tPress 1 to generate random password\t||\n||\tPress 2 to enter custom key to encode\t||");
        System.out.println ("==================================================");
        System.out.print ("Enter Choice: ");
        choice = sc.nextInt();
        sc.nextLine();
        switch (choice)
        {
            case 1 : isRandom = true;
            		 System.out.println ("\nGenerating key...");
                     int length = (int) Math.random() * (8) + 8, num = 0;
                     int i = 0;
                     StringBuilder randStr = new StringBuilder();
                     String inStr;
                     for (i=0;i<length;i++)
                     {
                        num = (int) (Math.random()*127);
                        if (num < 33)
                        {
                            num += 33;
                        }
                        randStr.append((char) num);
                     }
                     inStr = randStr.toString();
                     userKey.SetKey (inStr);
                     System.out.println ("\nInput Key generated.");
                     System.out.print ("\nEnter length of output key: ");
                     length = sc.nextInt();
                     userKey.SetOPLength (length);
                     break;

            case 2 : System.out.println ("\nEnter the key to encode\n");
                     userKey.SetKey (sc.nextLine());
                     System.out.println ("\nEnter the length of output string\n");
                     userKey.SetOPLength (sc.nextInt());
                     break;

            default: System.out.println ("\nWrong");
            		 sc.close();
                     return;
        }
        userKey.ScheduleKey();
        userKey.GeneratePseudoRandom(isRandom);
        System.out.println ("\nThe output key is " + userKey.GetOutput());
        sc.close();
        return;
    }*/

}
