package com.IR.search.controllers;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LuceneSearch {

    String indexPath;
    public LuceneSearch(String indexPath) {
        this.indexPath = indexPath;
    }

    public ArrayList<String> searchIndex(String input) throws IOException, ParseException {
        ArrayList<String> documentnames = new ArrayList<String>();
        try {

            Directory dir = FSDirectory.open(Paths.get(this.indexPath));
            DirectoryReader dirReader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(dirReader);

            Analyzer analyzer2 = new StandardAnalyzer();

            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                    new String[] {"tag", "title","desc","color"},
                    analyzer2);


            Query query = queryParser.parse(input);

            ScoreDoc[] hits = searcher.search(query, 1000000).scoreDocs;
            for(int i=0;i<hits.length;i++) {
                Document hitDoc = searcher.doc(hits[i].doc);
                documentnames.add(hitDoc.get("idname"));
             //   System.out.println(hitDoc.get("idname"));
            }
            dirReader.close();
            dir.close();
            analyzer2.close();
        }
        catch(Exception e) {
            System.err.println(e.getStackTrace());
        }
        return documentnames;
    }

}
