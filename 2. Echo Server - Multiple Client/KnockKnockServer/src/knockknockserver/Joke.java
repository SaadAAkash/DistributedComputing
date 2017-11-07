/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knockknockserver;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class Joke 
{
    ArrayList<String> lines;//<String>();
    boolean alreadySent = false;
    Joke(String data)
    {
        lines = new ArrayList<String>();
        String lies[] = data.split("\\r?\\n");
        for (int i=0;i<lies.length;i++)
        {
            lines.add(lies[i]);
        }
        //System.out.println("Joke size: "+ lines.length);
        
    }
    
    public String print(int pos)
    {
        return lines.get(pos);
    }
    
    public boolean match(String in, int pos)
    {
        String ser = lines.get(pos).toLowerCase();
        in = in.toLowerCase();
        
        System.out.println("Matching: "+ser+"   "+in);
        
        if (in.equals(ser))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
}
