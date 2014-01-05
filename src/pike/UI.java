package pike;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class UI extends JFrame{
	private JPanel pics=new JPanel();
	private JPanel searcharea=new JPanel();
	private JTextField picurl=new JTextField("/Users/pike/Downloads/icpr2004.imgset/groundtruth/swissmountains/swissmountains30.jpg",30);
	
	private JLabel label=new JLabel("please enter the pic's url: ");
	private JButton searchbutton=new JButton("search");
	private JComboBox box=new JComboBox(new String[]{"CEDD","FCTH","Gabor","JCDI"});
	
	private IndexCreator creator;
	private ImagesSearcher searcher;
	
	private int searchtype=0;
	
	public UI() throws IOException{
		
		creator=new IndexCreator("/Users/pike/Downloads/icpr2004.imgset/groundtruth", "index");
		creator.createIndex();
		searcher=new ImagesSearcher("index");
		
		setLayout(new BorderLayout());
		
		
		searcharea.setLayout(new FlowLayout());
		searcharea.add(label);
		searcharea.add(picurl);
		searcharea.add(searchbutton);
		searcharea.add(box);
		
		pics.setLayout(new GridLayout(3,3,5,5));
		
		add(searcharea,BorderLayout.NORTH);
		add(pics);
		
		box.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				searchtype=box.getSelectedIndex();
			}
		});

		searchbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("jj");
				
				pics.removeAll();
				try {
					
					ArrayList<String> result=searcher.search(picurl.getText(),searchtype);
					ImageIcon[] img=new ImageIcon[9];
					
					
					for (int i=0;i<9;i++){
						img[i]=new ImageIcon(result.get(i));
						
						pics.add(new JLabel(img[i]));
						
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				pics.updateUI();
				

			}
		});
		
		
	}
	

	public static void main(String[] args) throws IOException{
				
		JFrame myFrame=new UI();
		myFrame.setTitle("CBIR");
		myFrame.setLocationRelativeTo(null);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setSize(800,700);
		myFrame.setVisible(true);
	}
}
