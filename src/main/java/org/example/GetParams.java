package org.example;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GetParams {


        public static Map<String,String> ReadFile()
        {
            Map <String,String> params = new HashMap<>();

            try{
                File file = new File("M:\\Stage\\parametre.txt");
                Scanner sc = new Scanner(file);


                while (sc.hasNextLine())
                {

                    String line =  sc.nextLine();
                    if(line.isEmpty())
                    {
                        continue;
                    }
                    String [] parts = line.split(":",2);
                    if(parts.length!=2 )
                    {
                        System.err.println("invalid format"+line);
                        continue;
                    }

                    String key = parts[0].trim();
                    String value = parts[1].trim().replace("\"","");
                    params.put(key,value);

                }

            }
            catch ( Exception e)
            {
                System.out.println("error");
                e.printStackTrace();
            }
            return params;
        }


}
