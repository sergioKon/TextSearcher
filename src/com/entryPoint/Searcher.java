package com.entryPoint;

import com.algorithm.Dictionary;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Searcher {

    public static void main(String[] args) {

        String keys = "Arthur,James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher,Daniel,Paul,Mark,Donal\n" +
                "d,George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew,Gary,Timothy,Jose,Larry,Jeffrey,\n" +
                "Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas,H\n" +
                "Henry,Carl,Arthur,Ryan,Roger";
        String[] text = { " Adam has talked with Anna ", " But he had no idea about Robert it  ", " one day before " };
        Dictionary dictionary=new Dictionary(keys.split(","));
        List<String> fullText;
        long startTime = System.currentTimeMillis();
        String path=  System.getProperty("user.dir")+ "/resources/origin.txt";//"/resources/big.txt";

        try(InputStream inputStream= new FileInputStream(path)) {
            Scanner sc= new Scanner(inputStream, StandardCharsets.UTF_8);{
           fullText= new ArrayList<>(inputStream.available()/200);
           int count =0;

           System.setOut(new PrintStream("/resources/result.log"));
           while(sc.hasNextLine()){
               String line = sc.nextLine();
               fullText.add(line);
               count++;
               if(count% (1000 *1000 *10 )==0) {
                   long duration = System.currentTimeMillis() - startTime ;
                   System.out.println(" <------- DEBUG ------> number lines " + count + " duration "+ duration/1000  + " seconds ");
               }
           }


           Map<String,?> result=  dictionary.searchInFullText(fullText);
           for (String key : result.keySet()) {
               System.out.println("for key  "+ key + "location is " + result.get(key));
           }
           long duration = System.currentTimeMillis() - startTime ;
           System.out.println(" number lines " + count + " duration "+ duration );
     }
     } catch (IOException e ) {
            e.printStackTrace();
        }
    }
}
