package pike;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.utils.FileUtils;

public class IndexCreator {
	
	private ChainedDocumentBuilder docbuilder;
	private String pic_directory_path;
	private String index_path;
	
	public IndexCreator(String picpath,String indexpath){
		this.pic_directory_path=picpath;
		this.index_path=indexpath;
	}
	
	public void createIndex() throws IOException{
		
		File indexfile=new File(index_path);
		if(indexfile.exists()){
			System.out.println("index has been created");
			return;
		}
		
		
		File imgfile=new File(pic_directory_path);
		if(imgfile.exists() && imgfile.isDirectory())
			System.out.println("get images in "+pic_directory_path);		
		else
			System.out.println(pic_directory_path+" is not a directory or does not exist");
		
		//get all images 
		ArrayList<String> imagelist=FileUtils.getAllImages(imgfile, true);
		//System.out.println(imagelist.size());
		
		
		
		docbuilder=getDocumentBuilder();
		IndexWriterConfig config=new IndexWriterConfig(Version.LUCENE_CURRENT, new SimpleAnalyzer(Version.LUCENE_CURRENT));
		IndexWriter indexWriter=new IndexWriter(FSDirectory.open(new File(index_path)), config);
		
		for (Iterator<String> itr=imagelist.iterator();itr.hasNext();){
			String imgpath=itr.next();
			System.out.println(imgpath);
			BufferedImage image=ImageIO.read(new FileInputStream(imgpath));
			Document document=docbuilder.createDocument(image, imgpath);
			indexWriter.addDocument(document);
		}
		
		indexWriter.close();
		System.out.println("index finished");
		
	}
	
	
	
	
	public ChainedDocumentBuilder getDocumentBuilder(){
		ChainedDocumentBuilder docbuilder=new ChainedDocumentBuilder();
		docbuilder.addBuilder(DocumentBuilderFactory.getCEDDDocumentBuilder());
		docbuilder.addBuilder(DocumentBuilderFactory.getFCTHDocumentBuilder());
		docbuilder.addBuilder(DocumentBuilderFactory.getGaborDocumentBuilder());
		docbuilder.addBuilder(DocumentBuilderFactory.getJCDDocumentBuilder());
		docbuilder.addBuilder(DocumentBuilderFactory.getTamuraDocumentBuilder());


		return docbuilder;
	}
	
	public String getIndexPath(){
		return index_path;
	}
	
	public static void main(String[] args) throws IOException{
		//IndexCreator indexCreator=new IndexCreator();
		//indexCreator.createIndex();
	}

}
