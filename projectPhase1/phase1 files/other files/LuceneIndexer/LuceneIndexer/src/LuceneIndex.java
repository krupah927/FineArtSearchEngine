
public class LuceneIndex {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String indexPath = args[0];
		String jsonFolderPath = args[1];
		LuceneIndexWriter liw = new LuceneIndexWriter(indexPath, jsonFolderPath);
		liw.createIndex();
	}

}