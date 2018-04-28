import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.config.FieldConfig;
import org.apache.lucene.queryparser.surround.parser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LuceneIndexWriter {
	

	String indexPath;
	
	String jsonFolderPath;
	
	int objcount = 0;
	
	IndexWriter indexWriter = null;

	public LuceneIndexWriter(String indexPath, String jsonFolderPath) {
		this.indexPath = indexPath;
		this.jsonFolderPath = jsonFolderPath;
	}
	
	public void createIndex() {
		JSONArray jsonObj = null;
		double startTime =0;
		double endTime;
		startTime=System.nanoTime();
		//Check if directory path exists and read all files inside the directory
		if(Files.exists(Paths.get(this.jsonFolderPath))){
			File dir = new File(this.jsonFolderPath);
			
			//Find list of all files in the folder path
			File[] files = dir.listFiles();
			for( File f : files) {
				//Parse JSON file to find all json objects
				jsonObj = parseJSONFile1(f);
				
				//Create Index for the json objects
				openIndex();
				
				//Write index into doc for all the json objects after indexing required fields
				addDocs(jsonObj);
				
				//Close indexwriter
				terminate();
			}
			endTime=System.nanoTime();
			double totalTime=(endTime-startTime)/1000000000;
			System.out.println("Indexing Complete");
			System.out.println("Total Time take to index = "+totalTime+" seconds");
			System.out.println("");
			try{
				test();
			}
			catch(Exception e) {
				
			}
		}
	}
	
	public JSONArray parseJSONFile1(File f) {
		//Get Json file from path
		try {
			//Open insput stream for JSON file reading
			InputStream jsonFile = new FileInputStream(f);
			Reader readerJSON = new InputStreamReader(jsonFile);
			
			//Parse the json file using simple-json library
			Object fileObjects = JSONValue.parse(readerJSON);
			JSONArray arrayObjects = (JSONArray)fileObjects;
			
			return arrayObjects;			
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getStackTrace());
			return null;
		}
		
	}
	
	public boolean openIndex() {
		try {
			//Initialize analyzer, output directory and index writer 
			File file = new File(this.indexPath);
			if (! file.exists()){
		        file.mkdirs();
		    }
			Directory dir = FSDirectory.open(Paths.get(this.indexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			iwc.setOpenMode(OpenMode.CREATE);
			indexWriter = new IndexWriter(dir, iwc);
			
			return true;
		}
		catch(Exception e) {
			System.out.println("Error opening the index: "+e.getMessage());
		}
		return false;
	}
	
	//Function to add documents to index
	public void addDocs(JSONArray jsonObj) {
		try {
			for (JSONObject obj : (List<JSONObject>) jsonObj) {
				Document doc = new Document();
				objcount++;
			

				for(String attr : (Set<String>) obj.keySet()) {
					//Check if the value of a attr is null 
					if(obj.get(attr) == null) {
						doc.add(new TextField(attr, "", Store.YES));
					}
					else {
						//Find type of value of each attribute in the JSON Object
						Class type = obj.get(attr).getClass();
						
						
						if(type.equals(String.class)) {
							//Store only 32766 characters - limit used by lucene string field
							if((attr.toString()).equalsIgnoreCase("url")) {
								doc.add(new StringField(attr, (String)obj.get(attr), Store.YES));
							}
							else if((attr.toString()).equalsIgnoreCase("description")) {
								if(((String)obj.get(attr)).length() > 32765) {
									doc.add(new TextField(attr, ((String)obj.get(attr)).substring(0, 32740), Store.YES));
								}
								else {
									doc.add(new TextField(attr, (String)obj.get(attr), Store.YES));
								}
							}
							else if((attr.toString()).equalsIgnoreCase("medium")) {
								doc.add(new TextField(attr, (String)obj.get(attr), Store.YES));
							}
							else if((attr.toString()).equalsIgnoreCase("artist")) {
								doc.add(new TextField(attr, (String)obj.get(attr), Store.YES));
							}
							else if((attr.toString()).equalsIgnoreCase("title")) {
								doc.add(new TextField(attr, (String)obj.get(attr), Store.YES));
							}
							else {
								doc.add(new StoredField(attr, (String)obj.get(attr)));
							}
							
						}
						//In case the value is a JSON array and loop through each one to add to index 
						else {
							if((attr.toString()).equalsIgnoreCase("color")) {
								for (String x : (List<String>) obj.get(attr) ) {
									String y[] = x.split(" ");
									if(y.length > 0) {
										//Use hashset to detect duplicates
										HashSet<String> hs = new HashSet<String>();
										for(String z : y) {
											if(! hs.contains(z)) {
												hs.add(z);
											}
										}
										for(String val : hs) {
											doc.add(new TextField(attr, val.toString(), Store.YES));
										}
									}
									else {
										doc.add(new TextField(attr, "", Store.YES));
									}
									
								}
							}
							else if((attr.toString()).equalsIgnoreCase("tags")) {
								for (String x : (List<String>) obj.get(attr) ) {
									String z[] = x.split(" ");
									if(z.length > 0) {
										//Use hashset to detect duplicates
										HashSet<String> hs = new HashSet<String>();
										for(String y : z) {
											if(! hs.contains(z)) {
												hs.add(y);
											}
										}
										for(String val : hs) {
											doc.add(new TextField(attr, val.toString(), Store.YES));
										}
									}
									else {
										doc.add(new TextField(attr, "", Store.YES));
									}
									
								}
							}
						}
						
					}
				}
			
			try{
				indexWriter.addDocument(doc);
			}
			catch(IOException e) {
				System.err.println("Error adding documents to the index. " + e.getMessage());
				
			}
			catch(Exception e) {
				System.err.println("Error adding documents to the index. " + e.getMessage());
				
			}
		}
	}
	catch(Exception e) {
		//System.out.println(obj);
	}
	
}
	
	//Function used to commit the contents of Index write and close it
	public void terminate() {
		try {
			indexWriter.commit();
			indexWriter.close();
		}
		catch(IOException e) {
			System.err.println("Error adding documents to the index. " + e.getMessage());
		}
		catch(Exception e) {
			System.err.println("Error adding documents to the index. " + e.getMessage());
		}
	}
	
	public void test() throws IOException, ParseException {
		try {
			Directory dir = FSDirectory.open(Paths.get(this.indexPath));
			DirectoryReader dirReader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(dirReader);
			
			Analyzer analyzer2 = new StandardAnalyzer();
			
			//Parse simple query that searches for a word
			
			org.apache.lucene.queryparser.classic.QueryParser parser = new org.apache.lucene.queryparser.classic.QueryParser("tags", analyzer2);
			Query query = parser.parse("animals");
			System.out.println("Running test for Field: 'Tags' and Query: 'animals'");
			System.out.println("");
			ScoreDoc[] hits = searcher.search(query, 1000).scoreDocs;
			for(int i=0;i<hits.length;i++) {
				Document hitDoc = searcher.doc(hits[i].doc);
				System.out.println(hitDoc.get("url"));
			}
			dirReader.close();
			dir.close();
			analyzer2.close();
		}
		catch(Exception e) {
			System.err.println(e.getStackTrace());
		}
	}
	
}