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
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

import util.*;

//������

public class WebScanMainFrame extends JFrame implements ActionListener 
{
	//����˵�������
	private JMenuBar jmb;
	private JMenu jm_project,jm_edit,jm_tool,jm_about;
	private JMenuItem jmi_p_add,jmi_p_open,jmi_p_save,jmi_p_saveat;
	
//	//���幤��������
//	private JToolBar jtb;
//	private JButton jtbu_new;
//	
	//��������ʾ�������
	private JPanel jp_left,jp_right_up,jp_right_down;
	private JSplitPane jp_center,jp_right;
	private JPanelWebSiteList jp_wsl;
	
	//�����ڹ��캯��
	public WebScanMainFrame()
	{
		initMenu();
		//initToolBar();
		initCenter();
		
		setTitle("WebScanner");
		setIconImage(Toolkit.getDefaultToolkit().getImage("res/logo.jpg"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		
	}
	
	//��ʼ���˵���
	private void initMenu()
	{
		jmb=new JMenuBar();
		
		//��ʼ����Ŀ�˵�
		jm_project= new JMenu("��Ŀ(P)");
		jm_project.setMnemonic('P');
		jmi_p_add=new JMenuItem("���");
		jmi_p_add.addActionListener(this);
		jmi_p_open=new JMenuItem("��");
		jmi_p_open.addActionListener(this);
		jmi_p_save=new JMenuItem("����");
		jmi_p_save.addActionListener(this);
		jmi_p_saveat=new JMenuItem("���Ϊ");
		jmi_p_saveat.addActionListener(this);
		jm_project.add(jmi_p_add);
		jm_project.add(jmi_p_open);
		jm_project.add(jmi_p_save);
		jm_project.add(jmi_p_saveat);
		
		//��ʼ���༭�˵�
		jm_edit=new JMenu("�༭(E)");
		jm_edit.setMnemonic('E');
		
		
		//��ʼ�����߲˵�
		jm_tool=new JMenu("����");
		
		//��ʼ�����ڲ˵�
		jm_about=new JMenu("����");
		
		jmb.add(jm_project);
		jmb.add(jm_edit);
		jmb.add(jm_tool);
		jmb.add(jm_about);
		
		this.setJMenuBar(jmb);
		
	}
	
//	//��ʼ��������
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
	
	//�������ݲ��ֳ�ʼ��
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
		jp_right=new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jp_right_up, jp_right_down);
		jp_center=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jp_wsl, jp_right);

		this.add(jp_center,BorderLayout.CENTER);
		//setVisible(true);
		
		jp_center.setDividerLocation(DisplayAttribute.SW/5);
		jp_center.setDividerSize(2);
		jp_right.setDividerLocation(DisplayAttribute.SH/5*3);
		jp_right.setDividerSize(2);	
		
	}
	

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		 //TODO Auto-generated method stub
		if(e.getSource()==jmi_p_add)					//��Ŀ�˵��µ��¼���Ӧ
		{
			
		}
		if(e.getSource()==jmi_p_open)
		{
			
		}
		if(e.getSource()==jmi_p_save)
		{
		//	SaveWebSiteList();
		}
		if(e.getSource()==jmi_p_saveat)
		{
			
		}

	}
	


}
