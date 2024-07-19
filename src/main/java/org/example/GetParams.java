package org.example;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GetParams {
 public static  Map<String,String> ReadFile()
 {
     HashMap<String,String> params = new HashMap<>();
     try{
     File file = new File("path");
     Scanner sc = new Scanner(file);

     while (sc.hasNextLine())
     {
         String line = sc.nextLine();
         if(line.isEmpty())
         {
             continue;
         }
         String [] parts = line.split(":",2);
         if(parts.length!=2)
         {
                 System.err.println("Invalid format: " + line);
                 continue;
         }
         String key = parts[0].trim();
         String value = parts[1].trim().replaceAll("\"","");
         if (key.equals("requete"))
         {
            StringBuilder query = new StringBuilder(value);
            while (sc.hasNextLine())
            {
                String nextline = sc.nextLine();
                if (nextline.trim().isEmpty())
                {
                    break;
                }
                query.append(" ").append(nextline.trim());
            }
            value=query.toString();
         }
         params.put(key,value);
     }
     }
     catch (Exception e)
     {
         System.out.println("Error");
         e.printStackTrace();
     }
     return params;
 }

}
