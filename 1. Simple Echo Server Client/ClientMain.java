/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echo.server.client;


import java.net.*; 
import java.io.*;
import java.math.*;
import java.util.*;
import java.security.*;
/**
 *
 * @author Akash
 */
public class ClientMain extends EchoServerClient {

    
    public static void main(String[] args) throws IOException 
    {
	BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in));
	
        BufferedReader in = null;
        PrintWriter out = null;
        
        String serverHostname = new String ("127.0.0.1");
        
        System.out.print ("YOUR INPUT PLEASE?: ");
        while( true )
        {
            userInput = stdIn.readLine();
            if( userInput.equals("BEGIN") )
            {
                break;
            }
            else{
                System.out.println(userInput);
                System.out.print("YOUR INPUT PLEASE?: ");
            }
        }
        
        if (args.length > 0)
           serverHostname = args[0];
        System.out.println ("Connecting to host " +serverHostname + " on port " + port);

        try {
            echoSocket = new Socket(serverHostname, port);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) 
        {
            System.err.println("Error in host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Not getting I/O for " + serverHostname);
            System.exit(1);
        }

        while( true )
        {
            if( userInput.equals("BEGIN") )
            {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
                System.out.print ("input: ");
                
                while ((userInput = stdIn.readLine()) != null) 
                {
                    out.println(userInput);
                    if( (serverResponse = in.readLine()) != null ){
                        System.out.println("echo: " + serverResponse);
                    }
                    else{
                        System.out.println("echo: " + serverResponse);
                    }
                    if( serverResponse.equals("Conn ended")){
                        break;
                    }
                    System.out.print ("YOUR INPUT PLEASE?: ");
                }
            }
            System.out.print ("YOUR INPUT PLEASE? ");
            userInput = stdIn.readLine();
            System.out.println (userInput);
            if( userInput.equals("DONE"))
                break;
        }

	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();
    }


}
