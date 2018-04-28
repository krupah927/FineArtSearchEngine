/*

Author: Krupa Hegde
implements rest api for search engine to get data from backend

 */

package com.IR.search.controllers;
import com.IR.search.models.Document;
//import com.IR.search.models.DocumentRepository;
import com.IR.search.models.Paintings;
import com.IR.search.models.Search;
import com.IR.search.repositories.DocumentRepository;
import com.IR.search.repositories.SearchRepository;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class SearchController {

    @Autowired
    SearchRepository searchRepository; //for index
    @Autowired
    DocumentRepository docRepository; //for database

    @GetMapping("/searches")
    public List<Search> getAllSearch() {
      //  Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
        System.out.println(searchRepository.findAll());
        return searchRepository.findAll();
    }

    @GetMapping("/lucenesearch/{word}")
    public List<Paintings> luceneSearch(@PathVariable("word") String word) throws IOException, ParseException {
        LuceneSearch luceneObj=new LuceneSearch("lucenk");
       ArrayList<String> filenames=luceneObj.searchIndex(word);
        System.out.println(searchRepository.findAll());
        List<Paintings> datafiles = docRepository.findByIdname(filenames.get(0));

        //add everything to the list datafiles
        for(int i=1;i<filenames.size();i++){
          //  System.out.println("inLucene "+filenames.get(i));
            List<Paintings> k= docRepository.findByIdname(filenames.get(i));
            for(Paintings ps: k)
                datafiles.add(ps);
        }

        return datafiles;

    }

    @GetMapping("/searching/{word}")
    public List<Paintings> geSearch(@PathVariable("word") String word) {
        //  Split keywords on special chars and spaces
        String [] keywords= word.split("[^a-zA-Z0-9]");
        //search for keyword in index
        List<Search> dataObjcts = searchRepository.findByWord(keywords[0]);
        for(int i=1;i<keywords.length;i++){

            List<Search> dataObjct=searchRepository.findByWord(keywords[i]);
            for(Search obj:dataObjct){
                dataObjcts.add(obj);
            }
        }
     //docnames will contain the documents to be returned
        ArrayList<String> docnames=new ArrayList<String>();
        Map<String,Double> docScores = new HashMap<>();
               Ranker rank =new Ranker();
        for(Search model : dataObjcts) {
            //get  relevant document objects
            Document[] doc = model.getDocument();
            for (Document x : doc) {
                //compute score for each doc
                double score=rank.computeScore(x);
                docScores.put(x.getId(),score);

            }
        }
        //sort documents by score
        docScores=rank.sortByValue(docScores);

        //extract document name from map to an arraylist
        docScores.forEach((key, value) ->{
           System.out.println(key + "/" + value);
            docnames.add(key);

    });



        int length=docnames.size();
         //fetch jsondata from database
         List<Paintings> datafiles = docRepository.findByIdname(docnames.get(length-1));

        //add everything to the list datafiles
        for(int i=length-2;i>=0;i--){
            System.out.println("kkk "+docnames.get(i));
            List<Paintings> k= docRepository.findByIdname(docnames.get(i));
            for(Paintings ps: k)
            datafiles.add(ps);
        }

       return datafiles;

    }


}
