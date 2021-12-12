package com.algorithm;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

import java.util.*;

public class Dictionary {
    List<String> matcherWords;
    @Setter @Getter
    private int offset;
    @Getter
    static Map<String,List<Location>> findWords= Collections.synchronizedMap(new TreeMap<>());
    public Dictionary(String[] keyWords){
       this(Arrays.asList(keyWords));
    }

    public Dictionary(List<String> matcherWords){
        this.matcherWords=matcherWords;
    }

    public  void searchInFullText(List<String> words) {
        int lineId=-1;
        for(String element  : words) {
            findWorld(element,++lineId);
        }
    }

    private void findWorld(String text, int lineId) {
      String[]  elements=   text.split(" ") ;
      int worldPos=0;
      for(String word : elements) {
          worldPos++;
         if( isPartOfLst(word)) {
             Location point= new Location(lineId+offset, worldPos);
             List<Location> prevPoints=  findWords.get(word);
             if(prevPoints==null) {
                 prevPoints= new ArrayList<>();
             }
             prevPoints.add(point);
             findWords.put(word,prevPoints);
         }
      }
    }

    private boolean isPartOfLst(String word) {
        for(String matchWord : matcherWords) {
            if(matchWord.equals(word)) {
                return true;
            }
        }
        return false;
    }

}

class Location {
    int line ;
    int wordPos;
    public Location(int line , int wordPos){
        this.line=line;
        this.wordPos= wordPos;
    }

    @Override
    public String toString(){
        return " line = " + line + " word Position= "+ wordPos;
    }
}




