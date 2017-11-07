/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knockknockserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class EchoThread extends Thread {
    protected Socket socket;
    BufferedReader is;
    PrintStream os;
    int currentJoke = -1;
    int currentLine=0;
    ArrayList<Joke> jokelist;
    KnockKnockServer knock;
    

    public EchoThread(Socket clientSocket, KnockKnockServer knock)
    {
        this.knock = knock;
        this.socket = clientSocket;
        initJokes();
    }
    
    public void run() 
    {
        //System.out.println("runnnusdfsdf");
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        
        System.out.println( "Connection established with: " + socket );
        try 
        {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintStream(socket.getOutputStream());
            
            startJoking(is, os);
            
            System.out.println( "Connection closed." );
            is.close();
            os.close();
            socket.close();
            
            
        } 
        catch (Exception e) 
        {
            System.out.println(e);
        }
        
       
        
    }
    
     
    public void startJoking(BufferedReader is, PrintStream ps) throws IOException
    {
        boolean test = true;
        System.out.println("joke list: "+ jokelist.size());
        while (test)
        {
            if (currentJoke== -1)
            {
                test = pickAJoke();
                //ps.println("Picking a joke:" + currentJoke);
                if (!test)
                    break;
            }
            else
            {
               // System.out.println("Current Joke"+ currentJoke);
            }
            
            Joke joke = jokelist.get(currentJoke);
            ps.println(""+joke.lines.get(currentLine));
            currentLine++;
            
            if (currentLine>=joke.lines.size())
            {
                jokelist.get(currentJoke).alreadySent = true;
                ps.println("Would you like to listen to another? (Y/N)");
                String xx = is.readLine();
                if (xx.matches("Y"))
                {
                    currentJoke = -1;
                    currentLine = 0;
                }
                else
                {
                  
                    break;
                }
                
                
                
                
            }
            else
            {
                System.out.println("Wait for client reply");
                String input = is.readLine();
                
                if (!joke.match(input, currentLine))
                {
                    ps.println("You are supposed to say, \""+joke.lines.get(currentLine)+"\" Let’s try again." );
                    currentLine = 0;
                }
                else
                {
                    currentLine++;
                }
            }
            
        }
        System.out.println("All jokes served");
        
    }
    
     public void initJokes()
    {
        jokelist = new ArrayList<Joke>();
        String data[]={"Knock Knock!\n" +
                        "Who's there?\n" +
                        "Will\n" +
                        "Will who?\n" +
                        "Will you let me in? It's freezing out here!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's There?\n" +
                        "Barbie\n" +
                        "Barbie Who?\n" +
                        "Barbie Q Chicken!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Figs\n" +
                        "Figs who?\n" +
                        "Figs the doorbell, it's broken!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Kiwi\n" +
                        "Kiwi who?\n" +
                        "Kiwi go to the store?\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Lettuce\n" +
                        "Lettuce who?\n" +
                        "Let us in, we're freezing!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Olive\n" +
                        "Olive who?\n" +
                        "Olive right next door to you.\n" +
                        "\n",
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Turnip\n" +
                        "Turnip who?\n" +
                        "Turnip the volume, it's quiet in here.\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Ice cream\n" +
                        "Ice cream who?\n" +
                        "Ice cream if you don't let me in !\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Orange\n" +
                        "Orange who?\n" +
                        "Orange you going to answer the door?\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Orange\n" +
                        "Orange who?\n" +
                        "Orange you glad to see me?\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Who\n" +
                        "Who who?\n" +
                        "Is there an owl in here?\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Cow-go\n" +
                        "Cow-go who?\n" +
                        "No, Cow go MOO!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Police\n" +
                        "Police who?\n" +
                        "Police (please) may I come in?\n" +
                        "\n",
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Water\n" +
                        "Water who?\n" +
                        "Water you doing in my house?\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Goat\n" +
                        "Goat who?\n" +
                        "Goat to the door and find out.\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Beef\n" +
                        "Beef who?\n" +
                        "Before I get cold, you'd better let me in!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Leaf\n" +
                        "Leaf Who?\n" +
                        "Leaf Me Alone!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Tank\n" +
                        "Tank who?\n" +
                        "You're welcome!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Wooden shoe\n" +
                        "Wooden shoe who?\n" +
                        "Wooden shoe like to hear another joke?\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Howard\n" +
                        "Howard who?\n" +
                        "Howard I know?\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Nobel\n" +
                        "Nobel who?\n" +
                        "No bell, that's why I knocked!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's There?\n" +
                        "Anita\n" +
                        "Anita who?\n" +
                        "Anita to borrow a pencil!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's There?\n" +
                        "Figs\n" +
                        "Figs who?\n" +
                        "Figs the doorbell, it's broken!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Yukon\n" +
                        "Yukon who?\n" +
                        "Yukon say that again!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Amarillo\n" +
                        "Amarillo who?\n" +
                        "Amarillo nice guy!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Tyrone\n" +
                        "Tyrone who?\n" +
                        "Tyrone shoelaces!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Abe\n" +
                        "Abe who?\n" +
                        "Abe C D E F G H…\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Ada\n" +
                        "Ada who?\n" +
                        "Ada burger for lunch!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Amy\n" +
                        "Amy who?\n" +
                        "Amy fraid I've forgotten!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Cash\n" +
                        "Cash who?\n" +
                        "No thanks, but I'd like some peanuts\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Alpaca\n" +
                        "Alpaca who?\n" +
                        "Alpaca the trunk, you pack the suitcase!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Alma\n" +
                        "Alma who?\n" +
                        "Alma not going to tell you!\n" +
                        "\n" ,
                        "Knock, knock\n" +
                        "Who's there?\n" +
                        "Ken\n" +
                        "Ken who?\n" +
                        "Ken I come in, it's freezing out here?"+
                        "\n" };
        
        for (int i=0;i<data.length;i++)
        {
            Joke joke = new Joke(data[i]);
            jokelist.add(joke);
        }
    }
    
    
    public boolean pickAJoke()
    {
        Random ran = new Random();
        int x = Math.abs(ran.nextInt()%jokelist.size());
        boolean allSent = true;
        for (int i=0;i<jokelist.size();i++)
        {
            Joke joke = jokelist.get(i);
            if (!joke.alreadySent)
            {
                allSent = false;
            }
        }
        
        if (!allSent)
        {
            if (jokelist.get(x).alreadySent)
                return pickAJoke();
            else
            {
                currentJoke = x;
                currentLine = 0;
                return true;
            }
        }
        else
            return false;
        
    }
}
