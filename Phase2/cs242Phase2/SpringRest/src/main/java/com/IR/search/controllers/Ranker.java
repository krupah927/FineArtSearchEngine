package com.IR.search.controllers;

import com.IR.search.models.Document;
import com.IR.search.models.FieldCount;

import java.util.*;

public class Ranker {


    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue));
        //list.sort();


        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
 double computeScore(Document doc) {

     FieldCount fields =doc.getFieldCount();
      int tags=fields.getTags();
      int title=fields.getTitle();
      int desc=fields.getDesc();
      int color=fields.getColor();
      int artist=fields.getArtist();
      int medium=fields.getMedium();
     double score= Math.log((title*10)+(tags*8)+(desc*4)+(artist*7)+(medium*5));
        return score;
 }

}
