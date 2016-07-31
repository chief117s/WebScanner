package view;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import renderer.CheckBoxTreeCellRenderer;
import component.CheckBoxTree;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import listener.CheckBoxTreeSelectionListener;

public class JPanelWebSiteList extends JPanel{
	
	private JButton bu_add;
	private JButton bu_import;
	private JPanel jp_b;	
	private JTree websitetree;
	private CheckBoxTree cbtroot;
	private CheckBoxTree cbtdefaultsort;
	
	private JPopupMenu jpm;
	private JMenuItem jpmi_edit,jpmi_del;
	
	private JPopupMenu wlist_jpm;
	private JMenuItem wlist_jpm_del,wlist_jpm_edit;
	
	private ArrayList<String> sortArr;
	private Document doc;
	private Element root;
	private URL urlpath;
	
	public JPanelWebSiteList(LayoutManager layout)
	{
		super(layout);
		
		bu_add=new JButton("���");
		bu_import=new JButton("��������");
		jp_b=new JPanel(new GridLayout(1,2,15,5));
		initWebSiteTree();	
		this.setBorder(new TitledBorder("��վ�б�"));
		this.add(jp_b,BorderLayout.SOUTH);
		
		jp_b.add(bu_add);
		jp_b.add(bu_import);
		
	}
	
	public void addWebSite(ArrayList<String> strArr,String sortstr)
	{
		
	}
	
	public void initWebSiteTree()
	{
		urlpath=getClass().getResource("/websitelist.xml");
		
		if(urlpath==null)				//Ĭ�ϵ���վ�б����ڣ�����Ĭ����վ�б�
		{
			cbtroot=new CheckBoxTree("��վ�б�");
			cbtdefaultsort=new CheckBoxTree("Ĭ�Ϸ���");
			cbtroot.add(cbtdefaultsort);
		}
		else							//Ĭ�ϵ���վ�б����,���xml�ļ��ж�ȡ�б���
		{
				readTree();
		}

		websitetree=new JTree(cbtroot);
		DefaultTreeModel model=new DefaultTreeModel(cbtroot);
		websitetree.addMouseListener(new CheckBoxTreeSelectionListener());
		websitetree.setModel(model);
		websitetree.setCellRenderer(new CheckBoxTreeCellRenderer());
		websitetree.setShowsRootHandles(true);
		websitetree.setRootVisible(false);
		
		this.add(new JScrollPane(websitetree));
		try {
			test();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readTree()
	{
		try
		{
			SAXReader saxReader=new SAXReader();
			doc=saxReader.read(urlpath);
			root=doc.getRootElement();
			cbtroot=new CheckBoxTree(new String(root.getName()));
			readTreeNodeNames(root,cbtroot);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void readTreeNodeNames(Element e,CheckBoxTree node)
	{
		Iterator iter=e.elementIterator();
		while(iter.hasNext())
		{
			Element childEle=(Element)iter.next();
			CheckBoxTree child;
			if(childEle.getName()!="leaf")				//���û��ȡ��Ҷ��
			{
				child=new CheckBoxTree(new String(childEle.getName()));
				node.add(child);			
			}
			else										//�����ȡ��Ҷ��
			{
				child=new CheckBoxTree(new String(childEle.getText().toString()));
				node.add(child);	
			}
			if(childEle.nodeCount()==0)
				continue;
			else
				readTreeNodeNames(childEle, child);
		}
	}
	
	public void saveWebSiteList()
	{


	}
	
	public void test() throws FileNotFoundException
	{
//		String path="/websitelist/Ĭ���б�/www.baidu.com";
//		Element list;
//		try
//		{
//			if(doc.selectSingleNode(path)==null)
//				System.out.println("null");
//			else
//			{
//				list=(Element)doc.selectSingleNode(path);
//				System.out.println(list.toString());	
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		Element newnode=doc.getRootElement().addElement("test");
		newnode.setText("456789");
		
		File xmlFile=new File("test.xml");
		Writer osWrite=new OutputStreamWriter(new FileOutputStream(xmlFile));//���������  
		OutputFormat format = OutputFormat.createPrettyPrint();  //��ȡ�����ָ����ʽ    
		format.setEncoding("UTF-8");//���ñ��� ��ȷ��������xmlΪUTF-8��ʽ  
		XMLWriter writer = new XMLWriter(osWrite,format);//XMLWriter ָ������ļ��Լ���ʽ    
		try {
			writer.write(doc);
			writer.flush();  
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//��documentд��xmlFileָ�����ļ�(����Ϊ���������ļ������´������ļ�)    

	}
	
	public void saveasWebSiteList()
	{
		int retval;
		JFileChooser jfc=new JFileChooser();
		File filename=new File("websitelist.xml");
		String filepath;
		jfc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setDialogTitle("����");
		jfc.setSelectedFile(filename);
		retval=jfc.showSaveDialog(jfc);	
		if(retval==JFileChooser.APPROVE_OPTION)
		{
			filepath=jfc.getSelectedFile().toString();
			
			OutputStream fileOut;
			try {
				fileOut = new FileOutputStream(filepath);
				BufferedOutputStream bufOut = new BufferedOutputStream(fileOut);  
		        XMLEncoder encoder = new XMLEncoder(bufOut);  
		        encoder.writeObject(websitetree);  
		        encoder.close();  
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	
			
		}
			

	}
	
	
	
	public JButton getAddBut() 
	{
		return bu_add;
	}
	
	public JButton getImportBut()
	{
		return bu_import;
	}
	
	public JTree getTree()
	{
		return websitetree;
	}
	

}
