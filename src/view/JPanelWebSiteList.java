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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.InvalidXPathException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.omg.CORBA.CTX_RESTRICT_SCOPE;

import renderer.CheckBoxTreeCellRenderer;
import component.CheckBoxTree;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import listener.CheckBoxTreeSelectionListener;

public class JPanelWebSiteList extends JPanel{
	
	private JButton bu_add;
	private JButton bu_import;
	private JButton bu_newsort;
	private JPanel jp_b;	
	private JTree websitetree;
	private CheckBoxTree cbtroot;
	private CheckBoxTree cbtdefaultsort;
	
	private JPopupMenu wlist_jpm;
	private JMenuItem wlist_jpm_del,wlist_jpm_edit;
	
	private ArrayList<String> sortArr=new ArrayList<String>();
	private Document doc;
	private Element root;
	private URL urlpath;
	
	public JPanelWebSiteList(LayoutManager layout)
	{
		super(layout);
		
		wlist_jpm=new JPopupMenu();
		wlist_jpm_del=new JMenuItem("删除");
		wlist_jpm.add(wlist_jpm_del);
		wlist_jpm_edit=new JMenuItem("编辑");
		wlist_jpm.add(wlist_jpm_edit);
		
		bu_newsort=new JButton("新建分类");
		bu_add=new JButton("添加");
		bu_import=new JButton("批量导入");
		jp_b=new JPanel(new GridLayout(1,3,10,5));
		initWebSiteTree();	
		
		this.setBorder(new TitledBorder("网站列表"));
		this.add(jp_b,BorderLayout.SOUTH);
		
		jp_b.add(bu_add);
		jp_b.add(bu_import);
		jp_b.add(bu_newsort);
		
	}
	
	public void addWebSite()
	{
		CheckBoxTree selectednode=(CheckBoxTree)websitetree.getLastSelectedPathComponent();
		if(selectednode==null)
			return;
		String newurl=(String)JOptionPane.showInputDialog(null,"请输入网址:\n","网址",JOptionPane.PLAIN_MESSAGE,null,null,null);
		CheckBoxTree newnode=new CheckBoxTree(newurl);
		newnode.setAllowsChildren(false);
		if(selectednode.getAllowsChildren()==true)
		{
			selectednode.add(newnode);
		}
		else
		{
			CheckBoxTree p=(CheckBoxTree) selectednode.getParent();
			p.add(newnode);
		}	
		websitetree.updateUI();
	}
	
	public void newSort()
	{
		String newsort=(String)JOptionPane.showInputDialog(null,"请输入新的分类:\n","分类",JOptionPane.PLAIN_MESSAGE,null,null,null);
		if(sortArr.contains(newsort))
		{
			JOptionPane.showMessageDialog(null, "分类已存在");
			return;
		}
		else
		{
			sortArr.add(newsort);
			CheckBoxTree newnode=new CheckBoxTree(newsort);
			cbtroot.add(newnode);
		}
		websitetree.updateUI();
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
				readTree(urlpath);
		}

		loadTree();
	}
	
	public void loadTree()
	{
		websitetree=new JTree(cbtroot);
		DefaultTreeModel model=new DefaultTreeModel(cbtroot);
		websitetree.setModel(model);
		websitetree.setCellRenderer(new CheckBoxTreeCellRenderer());
		websitetree.setShowsRootHandles(true);
		websitetree.setRootVisible(false);
		websitetree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{				
				JTree tree=(JTree)e.getSource();
				TreePath path = tree.getPathForLocation(e.getX(),e.getY());
				if(path!=null)
				{
					CheckBoxTree node=(CheckBoxTree)path.getLastPathComponent();
					if(node!=null&&e.getButton()!=MouseEvent.BUTTON3)
					{
						boolean isSelected=!node.isSelected();
						node.setSelected(isSelected);
						((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
					}
					if(e.getButton()==MouseEvent.BUTTON3)
					{
						wlist_jpm.show(e.getComponent(),e.getX(),e.getY()); 
					}
				}
			}
		});		
		this.add(new JScrollPane(websitetree));
	}
	
	public void readTree(URL url)
	{
		try
		{
			SAXReader saxReader=new SAXReader();
			doc=saxReader.read(url);
			//doc=saxReader.read(new BufferedReader(new InputStreamReader(new FileInputStream(url.getPath()), "UTF-8")));
			root=doc.getRootElement();
			cbtroot=new CheckBoxTree(new String(root.getName()));
//			System.out.println(cbtroot.toString());
			readTreeNodeNames(root,cbtroot);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void openTree()
	{
		int retval;
		JFileChooser jfc=new JFileChooser();
		FileNameExtensionFilter ff=new FileNameExtensionFilter("xml文档(*.xml)", "xml");
		jfc.setFileFilter(ff);
		jfc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setDialogTitle("保存");
		retval=jfc.showOpenDialog(null);
		File file=jfc.getSelectedFile();
		if(retval==JFileChooser.APPROVE_OPTION)
		{

			try {
				readTree(file.toURL());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			loadTree();
			websitetree.updateUI();
//			System.out.println(cbtroot.toString());
//			System.out.println(file.toString());
//			System.out.println(file.toURI().toString());
//			System.out.println("newtree:"+websitetree.toString());
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
				sortArr.add(childEle.getName());
			}
			else										//如果读取到叶子
			{
				child=new CheckBoxTree(new String(childEle.getText().toString()));
				child.setAllowsChildren(false);
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
		try {
			Document newdoc =  DocumentHelper.createDocument();	
			Element newroot= newdoc.addElement(cbtroot.toString());
			newdoc.appendContent(newroot);
			for(int i=0;i<cbtroot.getChildCount();i++)
			{
				CheckBoxTree sortnode=(CheckBoxTree) cbtroot.getChildAt(i);
				Element nowsort=newroot.addElement(sortnode.toString());
				newroot.appendContent(nowsort);
				for(int j=0;j<sortnode.getChildCount();j++)
				{
					CheckBoxTree urlnode=(CheckBoxTree) sortnode.getChildAt(j);
					Element url=nowsort.addElement("leaf");
					url.setText(urlnode.toString());
				}			
			}				
			File xmlFile=new File("websitelist.xml");
			Writer osWrite=new OutputStreamWriter(new FileOutputStream(xmlFile));//创建输出流  
			OutputFormat format = OutputFormat.createPrettyPrint();  //获取输出的指定格式    
			format.setEncoding("UTF-8");//设置编码 ，确保解析的xml为UTF-8格式  
			XMLWriter writer = new XMLWriter(osWrite,format);//XMLWriter 指定输出文件以及格式    
			try {
				writer.write(newdoc);
				writer.flush();  
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//把document写入xmlFile指定的文件(可以为被解析的文件或者新创建的文件)    
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		finally
		{
			JOptionPane.showMessageDialog(null, "保存成功！");
		}
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
//		Element newnode=doc.getRootElement().addElement("test");
//		newnode.setText("456789");
		
		File xmlFile=new File("test2.xml");
		Writer osWrite=new OutputStreamWriter(new FileOutputStream(xmlFile));//创建输出流  
		OutputFormat format = OutputFormat.createPrettyPrint();  //获取输出的指定格式    
		format.setEncoding("UTF-8");//设置编码 ，确保解析的xml为UTF-8格式  
		XMLWriter writer = new XMLWriter(osWrite,format);//XMLWriter 指定输出文件以及格式    
		try {
			writer.write(websitetree);
			writer.flush();  
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//把document写入xmlFile指定的文件(可以为被解析的文件或者新创建的文件)    

	}
	
	public void saveasWebSiteList() throws FileNotFoundException
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
			try {
				Document newdoc =  DocumentHelper.createDocument();	
				Element newroot= newdoc.addElement(cbtroot.toString());
				newdoc.appendContent(newroot);
				for(int i=0;i<cbtroot.getChildCount();i++)
				{
					CheckBoxTree sortnode=(CheckBoxTree) cbtroot.getChildAt(i);
					Element nowsort=newroot.addElement(sortnode.toString());
					newroot.appendContent(nowsort);
					for(int j=0;j<sortnode.getChildCount();j++)
					{
						CheckBoxTree urlnode=(CheckBoxTree) sortnode.getChildAt(j);
						Element url=nowsort.addElement("leaf");
						url.setText(urlnode.toString());
					}			
				}				
				File xmlFile=new File(filepath);
				Writer osWrite=new OutputStreamWriter(new FileOutputStream(xmlFile));//创建输出流  
				OutputFormat format = OutputFormat.createPrettyPrint();  //获取输出的指定格式    
				format.setEncoding("UTF-8");//设置编码 ，确保解析的xml为UTF-8格式  
				XMLWriter writer = new XMLWriter(osWrite,format);//XMLWriter 指定输出文件以及格式    
				try {
					writer.write(newdoc);
					writer.flush();  
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//把document写入xmlFile指定的文件(可以为被解析的文件或者新创建的文件)    
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
	}
	
	public ArrayList<String> getTragetWebList()
	{
		ArrayList<String> url=new ArrayList<String>();
		Enumeration e=cbtroot.depthFirstEnumeration();
		while(e.hasMoreElements())
		{
			CheckBoxTree temp=(CheckBoxTree)e.nextElement();
			if(temp.isLeaf()==true&&temp.isSelected()==true)
			{
				url.add(temp.toString());
			}
		}
		return url;
	}
	
	public void editNodeName(CheckBoxTree node)
	{
		String newname=(String)JOptionPane.showInputDialog(null,"请输入新的URL：","编辑",JOptionPane.PLAIN_MESSAGE,null,null,null);
		node.setUserObject(newname);
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
	
	public JButton getNewsortBut()
	{
		return bu_newsort;
	}
	
	public JMenuItem getDelMenuItem()
	{
		return wlist_jpm_del;
	}
	
	public JMenuItem getEditMenuItem()
	{
		return wlist_jpm_edit;
	}

}
