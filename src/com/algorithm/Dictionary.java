package com.algorithm;


import java.util.*;

public class Dictionary implements AbstactsDictionary{
    List<String> matcherWords;
    Map<String,List<Location>> findWords= new TreeMap<>();
    public Dictionary(String[] words){

        insert(words);
    }

    public void insert(String[] text){
        matcherWords= new ArrayList<>(text.length);
        for(int i=0;i< text.length;i++)
           matcherWords.add(text[i]);
    }

    public Map<String,List<Location>> searchInFullText(List<String> words) {
        int lineId=0;
        for(String element  : words) {
            search(element,lineId++);
        }
        return  findWords;
    }
    public Map<String,List<Location>> searchInFullText(String[] words) {
       List<String > text=  Arrays.asList(words);
        int lineId=0;
       for(String element  : text) {
          search(element, lineId++);
       }
       return  findWords;
    }
    @Override
    public void search(String text, int lineId) {
      String[]  elements=   text.split(" ") ;
      List<Location> location = new ArrayList<>();
      int worldPos=0;
      for(String word : elements) {
          worldPos++;
         if( isPartOfLst(word)) {
             Location point= new Location(lineId, worldPos);
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




