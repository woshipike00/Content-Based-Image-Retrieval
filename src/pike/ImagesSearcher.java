package pike;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
	
	public void search(String imgpath) throws IOException{
		File imgfile=new File(imgpath);
		if (!imgfile.exists()){
			System.out.println("image does not exist");
			System.exit(1);
		}
		
		BufferedImage image=ImageIO.read(imgfile);
		IndexReader indexReader=DirectoryReader.open(FSDirectory.open(new File(indexpath)));
		
		ImageSearcher searcher_0=ImageSearcherFactory.createCEDDImageSearcher(10);
		ImageSearcher searcher_1=ImageSearcherFactory.createFCTHImageSearcher(10);
		ImageSearcher searcher_2=ImageSearcherFactory.createGaborImageSearcher(10);
		ImageSearcher searcher_3=ImageSearcherFactory.createJCDImageSearcher(10);
		ImageSearcher searcher_4=ImageSearcherFactory.createTamuraImageSearcher(10);

		
		
		
		ImageSearchHits hits_0=searcher_0.search(image, indexReader);
		getHits(hits_0);
		
		System.out.println("----------------------------------------\n");
		
		ImageSearchHits hits_1=searcher_1.search(image, indexReader);
		getHits(hits_1);
		
		System.out.println("----------------------------------------\n");
		
		ImageSearchHits hits_2=searcher_2.search(image, indexReader);
		getHits(hits_2);
		
		System.out.println("----------------------------------------\n");
		
		ImageSearchHits hits_3=searcher_3.search(image, indexReader);
		getHits(hits_3);
		
		System.out.println("----------------------------------------\n");
		
		ImageSearchHits hits_4=searcher_4.search(image, indexReader);
		getHits(hits_4);
	}
	
	public void getHits(ImageSearchHits hits){
		for (int i=0;i<hits.length();i++){
			String filename=hits.doc(i).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
			String[] splits=filename.split("/");
			System.out.println(splits[splits.length-1]);
		}
	}
	
	public static void main(String[] args) throws IOException{
		//IndexCreator indexCreator=new IndexCreator("/Users/pike/Downloads/icpr2004.imgset/groundtruth", "index");
		//indexCreator.createIndex();
		ImagesSearcher searcher=new ImagesSearcher("index");
		
		//searcher.search("/Users/pike/Downloads/icpr2004.imgset/groundtruth/football/football08.jpg");
		//searcher.search("/Users/pike/Downloads/icpr2004.imgset/groundtruth/cherries/cherries05.jpg");
		searcher.search("/Users/pike/Downloads/icpr2004.imgset/groundtruth/swissmountains/swissmountains30.jpg");
		

	}
}
