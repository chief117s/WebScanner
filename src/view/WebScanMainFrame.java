package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

import component.CheckBoxTree;
import util.*;

//主窗口

public class WebScanMainFrame extends JFrame implements ActionListener 
{
	//定义菜单栏变量
	private JMenuBar jmb;
	private JMenu jm_project,jm_edit,jm_tool,jm_about;
	private JMenuItem jmi_p_add,jmi_p_open,jmi_p_save,jmi_p_saveat;
	
//	//定义工具栏变量
//	private JToolBar jtb;
//	private JButton jtbu_new;
//	
	//定义主显示区域变量
	private JPanel jp_left,jp_right_up,jp_right_down;
	private JSplitPane jp_center,jp_right;
	private JPanelWebSiteList jp_wsl;
	
	//定义选项卡
	JTabbedPane jtp_bf;
	JPanelBruteForceTab jp_bf;		//暴力破解模块的界面
	
	//定义输出模块
	JTableResultTable jtrt_result;
	JScrollPane jsp_result;
	
	
	//主窗口构造函数
	public WebScanMainFrame()
	{
		initMenu();
		//initToolBar();
		initCenter();
		loadModsTab();
		
		setTitle("WebScanner");
		setIconImage(Toolkit.getDefaultToolkit().getImage("res/logo.jpg"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		

		
	}
	
	//初始化菜单栏
	private void initMenu()
	{
		jmb=new JMenuBar();
		
		//初始化项目菜单
		jm_project= new JMenu("项目(P)");
		jm_project.setMnemonic('P');
		jmi_p_add=new JMenuItem("添加");
		jmi_p_add.addActionListener(this);
		jmi_p_open=new JMenuItem("打开");
		jmi_p_open.addActionListener(this);
		jmi_p_save=new JMenuItem("保存");
		jmi_p_save.addActionListener(this);
		jmi_p_saveat=new JMenuItem("另存为");
		jmi_p_saveat.addActionListener(this);
		jm_project.add(jmi_p_add);
		jm_project.add(jmi_p_open);
		jm_project.add(jmi_p_save);
		jm_project.add(jmi_p_saveat);
		
		//初始化编辑菜单
//		jm_edit=new JMenu("编辑(E)");
//		jm_edit.setMnemonic('E');
		
		
		//初始化工具菜单
//		jm_tool=new JMenu("工具");
		
		//初始化关于菜单
		jm_about=new JMenu("关于");
		
		jmb.add(jm_project);
//		jmb.add(jm_edit);
//		jmb.add(jm_tool);
		jmb.add(jm_about);
		
		this.setJMenuBar(jmb);
		
	}
	
//	//初始化工具栏
//	private void initToolBar()
//	{
//		jtb=new JToolBar();
//		
//		jtbu_new=new JButton(new ImageIcon("res/tool_new.png"));
//		
//		jtb.add(jtbu_new);
//		
//		this.add(jtb,BorderLayout.NORTH);
//		
//	}
	
	//窗体内容部分初始化
	private void initCenter()
	{
		//jp_left=new JPanel(new BorderLayout());
		//jp_left.setBackground(Color.BLACK);
		jp_right_up=new JPanel(new BorderLayout());
		//jp_right_up.setBackground(Color.BLUE);
		jp_right_down=new JPanel(new BorderLayout());
		//jp_right_down.setBackground(Color.YELLOW);
		jp_wsl=new JPanelWebSiteList(new BorderLayout());
		jp_wsl.setVisible(true);
		jp_wsl.getAddBut().addActionListener(this);
		jp_wsl.getImportBut().addActionListener(this);
		jp_wsl.getNewsortBut().addActionListener(this);
		jp_wsl.getDelMenuItem().addActionListener(this);
		jp_wsl.getEditMenuItem().addActionListener(this);
		
		jp_right=new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jp_right_up, jp_right_down);
		jp_center=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jp_wsl, jp_right);

		this.add(jp_center,BorderLayout.CENTER);
		//setVisible(true);
		
		jp_center.setDividerLocation(DisplayAttribute.SW/5);
		jp_center.setDividerSize(2);
		jp_right.setDividerLocation(DisplayAttribute.SH/5*3);
		jp_right.setDividerSize(2);	
		
	}
	
	public void loadModsTab()									//读取各个mod的选项卡
	{
		jp_bf=new JPanelBruteForceTab();
		jtp_bf=new JTabbedPane();
		
		jtp_bf.addTab("暴力破解", null,jp_bf);
		jp_right_up.add(jtp_bf);
		jp_bf.getStartBut().addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		 //TODO Auto-generated method stub
		if(e.getSource()==jmi_p_add||e.getSource()==jp_wsl.getAddBut())					//事件响应
		{
			jp_wsl.addWebSite();
		}
		if(e.getSource()==jmi_p_open)
		{
			jp_wsl.openTree();
		}
		if(e.getSource()==jmi_p_save)
		{
			jp_wsl.saveWebSiteList();
		}
		if(e.getSource()==jmi_p_saveat)
		{
				try {
				jp_wsl.saveasWebSiteList();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		}
		if(e.getSource()==jp_wsl.getImportBut())
		{
			
		}
		if(e.getSource()==jp_wsl.getNewsortBut())
		{
			jp_wsl.newSort();
		}
		if(e.getSource()==jp_wsl.getDelMenuItem())
		{
			
		}
		if(e.getSource()==jp_wsl.getEditMenuItem())
		{
			jp_wsl.editNodeName((CheckBoxTree)jp_wsl.getTree().getLastSelectedPathComponent());
		}
		if(e.getSource()==jp_bf.getStartBut())
		{
			jp_bf.setTargetWebList(jp_wsl.getTragetWebList());
			jp_bf.setMethod(jp_bf.getMethodBox().getSelectedIndex());
			jp_bf.startBruteForce();
			
			String[] h={"URL","Username","Password","StatusCode","Location","Content-Length"};
			Vector<String> head=new Vector<String>();
			for(int i=0;i<6;i++)
			{
				head.add(h[i]);			
			}
			jtrt_result=new JTableResultTable(jp_bf.getResult(), head);
			jtrt_result.setVisible(true);
			jsp_result=new JScrollPane(jtrt_result);
			jsp_result.setAutoscrolls(true);
			jp_right_down.add(jsp_result);

			jp_right_down.updateUI();
		}
	}
	


}
