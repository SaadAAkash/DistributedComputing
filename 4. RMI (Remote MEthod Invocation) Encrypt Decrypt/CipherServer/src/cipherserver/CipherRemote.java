/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cipherserver;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 *
 * @author tishpish
 */
public class CipherRemote  extends UnicastRemoteObject implements Cipher
{
    char data[][]= new char[26][26];
    String input;
    String text = "GORDIAN";
    CipherRemote() throws Exception
    {
        FileReader fp = new FileReader("Cipher.txt");
        Scanner sc = new Scanner(fp);
        this.input = sc.nextLine();
        FileReader newfp = new FileReader("TabulaRecta.txt");
        Scanner newSc = new Scanner(newfp);
        int count =0;
        while(newSc.hasNextLine())
        {
            String line = newSc.nextLine();
            for (int i=0;i<line.length();i++)
            {
                data[count][i]=line.charAt(i);
            }
            count++;
        }
    }
    
    @Override
    public String Caesar() throws RemoteException
    {
        String cipa = text;
        String cip="";
        for (int i=0;i<cipa.length();i++)
        {
            if (!cip.contains(cipa.charAt(i)+""))
            {
                cip+=(char) cipa.charAt(i);
            }
        }
        for (int i=0;i<26;i++)
        {
            if (!cip.contains((char) (i+65)+""))
            {
                cip+=(char) (i+65);
            }
        }
        
        
        System.out.println( cip);
        System.out.println(input);
        String result ="";
        
        for (int i=0;i<input.length();i++)
        {
            //System.out.println("Input: "+input);
            char c = (char) (cip.indexOf(input.charAt(i))+65);
            result+=c;
            //result+=(cip.charAt(input.charAt(i)%65));
        }
        return result;
        //System.out.println("Vigenere cipher ended");
    }

    @Override
    public String Vigenere() throws RemoteException
    {

        while (input.length()!=text.length())
        {
            if (text.length()>input.length())
                text = text.substring(0,input.length());
            else
                text+=text;
        }
        
        
        
        
        System.out.println( text);
        System.out.println(input);
        String result ="";
        for (int i=0;i<input.length();i++)
        {
            int row = (int)(text.charAt(i))%65;
            //System.out.println("key: "+text.charAt(i)+" ("+row+") ");
            //System.out.print(input.charAt(i));
            int col = 0;
            for (int h=0;h<26;h++)
            {
                if (data[row][h]==input.charAt(i))
                {
                    col = h;
                    break;
                }
            }
            
            //int column = (int)( input.indexOf(text)-65+row)%65;
            //System.out.println(" Column: "+col);
            
            result+= (char)(col+65);
            
        }
        return result;
    }
    
}



        //System.out.println(input);
        //System.out.println(text);
        /*
        int start = (int)'A';
        
        char data[][]= new char[26][26];
        for (int i=0;i<26;i++)
        {
            int mid = start;
            for (int j=0;j<26;j++)
            {
                if (mid>= (int)'A'+26)
                {
                    mid=65;
                }
                data[i][j]=(char)mid++;
                ///System.out.print((char)mid++);
            }
            start++;
        }
        
        for (int i=0;i<26;i++)
        {
            for (int j=0;j<26;j++)
            {
                System.out.print(data[i][j]);
            }
            System.out.println();
        }
        */