package pike;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

public class ImagesSearcher {
	
	private String indexpath;
	
	public ImagesSearcher(String indexpath){
		this.indexpath=indexpath;
	}
	
	public ArrayList<String> search(String imgpath,int type) throws IOException{
		File imgfile=new File(imgpath);
		if (!imgfile.exists()){
			System.out.println("image does not exist");
			System.exit(1);
		}
		
		BufferedImage image=ImageIO.read(imgfile);
		IndexReader indexReader=DirectoryReader.open(FSDirectory.open(new File(indexpath)));
		
		ImageSearcher searcher=null;
		ImageSearchHits hits=null;
		switch(type){
		case 0:
			searcher=ImageSearcherFactory.createCEDDImageSearcher(9);
			hits=searcher.search(image, indexReader);
			break;
		case 1:
			searcher=ImageSearcherFactory.createFCTHImageSearcher(9);
			hits=searcher.search(image, indexReader);
			break;
		case 2:
			searcher=ImageSearcherFactory.createGaborImageSearcher(9);
			hits=searcher.search(image, indexReader);
			break;
		case 3:
			searcher=ImageSearcherFactory.createJCDImageSearcher(9);
			hits=searcher.search(image, indexReader);
			break;
		}
		
		return getHits(hits);
		
	}
	
	public ArrayList<String> getHits(ImageSearchHits hits){
		ArrayList<String> result=new ArrayList<String>();
		for (int i=0;i<hits.length();i++){
			String filename=hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
			result.add(filename);
			String[] splits=filename.split("/");
			System.out.println(splits[splits.length-1]);
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException{
		IndexCreator indexCreator=new IndexCreator("/Users/pike/Downloads/icpr2004.imgset/groundtruth", "index");
		indexCreator.createIndex();
		ImagesSearcher searcher=new ImagesSearcher("index");
		
		//searcher.search("/Users/pike/Downloads/icpr2004.imgset/groundtruth/football/football08.jpg");
		//searcher.search("/Users/pike/Downloads/icpr2004.imgset/groundtruth/cherries/cherries05.jpg");
		searcher.search("/Users/pike/Downloads/icpr2004.imgset/groundtruth/swissmountains/swissmountains30.jpg",0);
		

	}
}
