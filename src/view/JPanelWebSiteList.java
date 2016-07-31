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
		
		bu_add=new JButton("添加");
		bu_import=new JButton("批量导入");
		jp_b=new JPanel(new GridLayout(1,2,15,5));
		initWebSiteTree();	
		this.setBorder(new TitledBorder("网站列表"));
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
		
		if(urlpath==null)				//默认的网站列表不存在，创建默认网站列表
		{
			cbtroot=new CheckBoxTree("网站列表");
			cbtdefaultsort=new CheckBoxTree("默认分类");
			cbtroot.add(cbtdefaultsort);
		}
		else							//默认的网站列表存在,则从xml文件中读取列表树
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
			if(childEle.getName()!="leaf")				//如果没读取到叶子
			{
				child=new CheckBoxTree(new String(childEle.getName()));
				node.add(child);			
			}
			else										//如果读取到叶子
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
//		String path="/websitelist/默认列表/www.baidu.com";
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
		Writer osWrite=new OutputStreamWriter(new FileOutputStream(xmlFile));//创建输出流  
		OutputFormat format = OutputFormat.createPrettyPrint();  //获取输出的指定格式    
		format.setEncoding("UTF-8");//设置编码 ，确保解析的xml为UTF-8格式  
		XMLWriter writer = new XMLWriter(osWrite,format);//XMLWriter 指定输出文件以及格式    
		try {
			writer.write(doc);
			writer.flush();  
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//把document写入xmlFile指定的文件(可以为被解析的文件或者新创建的文件)    

	}
	
	public void saveasWebSiteList()
	{
		int retval;
		JFileChooser jfc=new JFileChooser();
		File filename=new File("websitelist.xml");
		String filepath;
		jfc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setDialogTitle("保存");
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
