package com.entryPoint;

import com.algorithm.Dictionary;
import com.config.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Log4j2
public class Searcher {
    public static void main(String[] args) {
         Searcher searcher=new Searcher();
         searcher.start();
    }

    public void start() {
        String keys = "Arthur,James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher,Daniel,Paul,Mark,Donal\n" +
                "d,George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew,Gary,Timothy,Jose,Larry,Jeffrey,\n" +
                "Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas,H\n" +
                "Henry,Carl,Arthur,Ryan,Roger";

        Dictionary dictionary=new Dictionary(keys.split(","));

        List<String> fullText;
        long startTime = System.currentTimeMillis();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url=classLoader.getResource("config.yaml");
        if(url == null) {
            log.error("resource not found ");
            return;
        }
        File file = new File(url.getFile());
        String path = getConfigData(file);

        try(InputStream inputStream= new FileInputStream(path)) {
           Scanner sc= new Scanner(inputStream, StandardCharsets.UTF_8);{
           int matcherSize = inputStream.available()/2000;
           List<Thread> blockMatchers = new ArrayList<>();
           fullText= new ArrayList<>(matcherSize)   ;
           int count =0;
           while(sc.hasNextLine()) {
               String line = sc.nextLine();
               count++;
               fullText.add(line);
               if (count % matcherSize == 0) {
                   parallelSearch(dictionary, fullText, matcherSize, blockMatchers, count);
               }
           }
           parallelSearch(dictionary, fullText, matcherSize, blockMatchers, count);
           for(Thread t : blockMatchers)     {
               t.join();
           }
           Map<String,?> result=  Dictionary.getFindWords();
   //        System.setOut(new PrintStream(rootPath+ "/log/result.log"));
           System.out.println(" size =   "+fullText.size());
           for (String key :result.keySet()) {
               System.out.println("for key  "+ key + " location is " + result.get(key));
           }
           long duration = System.currentTimeMillis() - startTime ;
                System.setOut(System.out);
          log.debug(" number lines " +  " duration  = "  +  duration);
       }
     } catch (Exception e ) {
            log.error(e);
        }
    }

    @SneakyThrows
    private String getConfigData(File file)  {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        Parameters params = om.readValue(file, Parameters.class);

        return params.getPath();
    }

    private void parallelSearch(Dictionary dictionary, List<String> fullText, int matcherSize, List<Thread> blockMatchers, int count) {
        List<String> chunk = new ArrayList<>(fullText);
        fullText.clear();
        int offset = count / matcherSize * matcherSize - matcherSize;
        log.debug (" offset = {} ",offset);
        dictionary.setOffset(offset);
        BlockSearcher blockSearcher=new BlockSearcher(chunk, dictionary);
        Thread thread =new Thread(blockSearcher);
        blockMatchers.add(thread);
        thread.start();
    }
}


@Log4j2
class BlockSearcher implements Runnable{
    Dictionary dictionary;
    List<String>chunk;
    public BlockSearcher(List<String > chunk,Dictionary dictionary){
        this.chunk=chunk;
        this.dictionary=dictionary;
    }
    @Override
    public void run() {
        long startTask = System.currentTimeMillis();
        log.debug(" The  thread  {} started ", Thread.currentThread().getName());

        dictionary.searchInFullText(chunk);
        log.debug("The  thread {}  duration is {} ", Thread.currentThread().getName(), System.currentTimeMillis() - startTask);
    }
}
