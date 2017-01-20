/*
Implementation of the RC4 algorithm in order to generate a random passkey for the program.
For some reason I believe this could be used to generate a random password (By using a random value), or work as a password "database",
by using a key and an output length [This helps the user create their own key, in case they want a low-security alternative to LastPass or KeePass].
By just remembering the main word and the password length, the user can retrieve their password.
*/

import java.util.Scanner;
import java.math.*;

class Key
{
    private String key = "";
    private StringBuilder output;
    private int[] sArray;
    private int i, j, length, outLength;

    //Getters and setters

    public void SetKey (String s)
    {
        key = s;
        length = key.length();
    }

    public void SetOPLength (int x)
    {
        outLength = x;
    }

    public String GetKey() {return key;}


    public String GetOutput () {return output.toString();}

    //Other Functions and Constructor

    public Key()
    {
        output = new StringBuilder();
        sArray = new int[128];
        i = 0;
        j = 0;
        length = 0;
        for (;i < 128;i++)
        {
            sArray[i] = i;
        }
        return;
    }

    public void Swap (int i, int j)
    {
        sArray[i] ^= sArray[j];
        sArray[j] ^= sArray[i];
        sArray[i] ^= sArray[j];
        return;
    }

    public void ScheduleKey()
    {
        j = 0;
        for (i = 0;i < 128;i++)
        {
            j = (j + sArray[i] + (int) key.charAt(i % length)) % 128;
            Swap (i, j);
        }
        return;
    }

    public void AppendOutput (char c)
    {
        output.append(c);
    }

    public void GeneratePseudoRandom()
    {
        char retChar = '\0';
        int count = 0, value = 0;
        i = 0;
        j = 0;
        for(;count < outLength;count++)
        {
            i = (i + 1) % 128;
            j = (j + sArray[i]) % 128;
            Swap (i, j);
            value = (sArray[(sArray[i] + sArray[j]) % 128]) % 94 + 33;
            retChar = (char) value;
            AppendOutput (retChar);
        }
        return;
    }

}


public class RC4Gen
{
    public static void main (String[] args)
    {
        int choice = 0;
        Scanner sc = new Scanner(System.in);
        Key userKey = new Key();
        System.out.println ("==================================================");
        System.out.println ("||\tPress 1 to generate random password\t||\n||\tPress 2 to enter custom key to encode\t||");
        System.out.println ("==================================================");
        System.out.print ("Enter Choice: ");
        choice = sc.nextInt();
        sc.nextLine();
        switch (choice)
        {
            case 1 : System.out.println ("\nGenerating key...");
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
                     return;
        }
        userKey.ScheduleKey();
        userKey.GeneratePseudoRandom();
        System.out.println ("\nThe output key is " + userKey.GetOutput());
        return;
    }
}