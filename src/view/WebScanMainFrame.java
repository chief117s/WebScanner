package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import util.*;

//主窗口

public class WebScanMainFrame extends JFrame implements ActionListener 
{
	//定义菜单栏变量
	private JMenuBar jmb;
	private JMenu jm_project,jm_edit,jm_tool,jm_about;
	private JMenuItem jmi_p_new,jmi_p_open,jmi_p_save,jmi_p_saveat;
	
	//定义工具栏变量
	private JToolBar jtb;
	private JButton jtbu_new;
	
	//定义主显示区域变量
	private JPanel jp_left,jp_right,jp_right_up,jp_right_down;
	private JSplitPane jp_center;
	
	//主窗口构造函数
	public WebScanMainFrame()
	{
		initMenu();
		initToolBar();
		initCenter();
		
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
		jmi_p_new=new JMenuItem("新建");
		jmi_p_new.addActionListener(this);
		
		jmi_p_open=new JMenuItem("打开");
		
		
		jmi_p_save=new JMenuItem("保存");
		
		
		jmi_p_saveat=new JMenuItem("另存为");
		
		
		
		//初始化编辑菜单
		jm_edit=new JMenu("编辑");
		
		//初始化工具菜单
		jm_tool=new JMenu("工具");
		
		//初始化关于菜单
		jm_about=new JMenu("关于");
		
		jmb.add(jm_project);
		jmb.add(jm_edit);
		jmb.add(jm_tool);
		jmb.add(jm_about);
		
		this.setJMenuBar(jmb);
		
	}
	
	//初始化工具栏
	private void initToolBar()
	{
		jtb=new JToolBar();
		
		jtbu_new=new JButton(new ImageIcon("res/tool_new.png"));
		
		jtb.add(jtbu_new);
		
		this.add(jtb,BorderLayout.NORTH);
		
	}
	
	//窗体内容部分初始化
	private void initCenter()
	{
		jp_left=new JPanel(new BorderLayout());
		jp_left.setBackground(Color.BLACK);
		jp_right=new JPanel(new BorderLayout());
		jp_right.setBackground(Color.BLUE);
		jp_center=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jp_left, jp_right);

		
		this.add(jp_center,BorderLayout.CENTER);
	//	setVisible(true);
		jp_center.setDividerLocation(DisplayAttribute.SW/5);
		jp_center.setDividerSize(2);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
		

	}

}
