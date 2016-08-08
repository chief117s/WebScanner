package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import mods.BruteForce.BruteForce;

public class JPanelBruteForceTab extends JPanel implements ActionListener {
	
	JButton jb_start,jb_selectpwfile,jb_selectuserfile;
	JComboBox<String> jcb_method;
	JTextField jtf_pwfile,jtf_userfile,jtf_threadnum;
	//BruteForce bf=new BruteForce();
	JLabel jl_user,jl_pw,jl_method,jl_threadnum;
	JTextArea jta_userlist,jta_pwlist;
	JScrollPane jsp_userlist,jsp_pwlist;
	int threadnum;
	int method_flag;
	private Vector<Vector<String>> result=new Vector<Vector<String>>();

	ArrayList<String> urllist;
	public ArrayList<String> userDire=new ArrayList<String>();
	public ArrayList<String> passwordDire=new ArrayList<String>();
	
	public Vector<Vector<String>> getResult()
	{
		return result;
	}

	
	public void loadUserDire(String userfilepath)
	{
		try {
			BufferedReader reader=new BufferedReader(new FileReader(userfilepath));
			String temp;
			while((temp=reader.readLine())!=null)
			{
				userDire.add(temp);
			}
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadPasswordDire(String passwordfilepath)
	{
		try {
			BufferedReader reader=new BufferedReader(new FileReader(passwordfilepath));
			String temp;
			while((temp=reader.readLine())!=null)
			{
				passwordDire.add(temp);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startBruteForce()
	{
		threadnum=Integer.parseInt(jtf_threadnum.getText());
		if(threadnum>0)
		{
			ExecutorService exec=Executors.newFixedThreadPool(threadnum);
			Vector<Vector<String>> tasklist=new Vector<Vector<String>>(); 
			for(int m=0;m<urllist.size();m++)
			{
				for(int n=0;n<userDire.size();n++)
				{
					for(int j=0;j<passwordDire.size();j++)
					{
						Vector<String> temp=new Vector<String>();
						temp.add(urllist.get(m));
						temp.add(userDire.get(n));
						temp.add(passwordDire.get(j));
						tasklist.add(temp);
					}
				}
			}
			Vector<Future<Vector<String>>> vfr=new Vector<Future<Vector<String>>>();
			for(int j=0;j<tasklist.size();)
			{
				for(int i=0;i<threadnum;i++)
				{
					if(j>=tasklist.size())
						break;
					vfr.add(exec.submit(new BruteForce(tasklist.get(j).get(0), tasklist.get(j).get(1), tasklist.get(j).get(2), method_flag)));
					j++;
				}
			}
			for(Future<Vector<String>> fvs:vfr)
			{
				try {
					result.add(fvs.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			exec.shutdown();
			try {
				exec.awaitTermination(20, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			JOptionPane.showMessageDialog(null, "请正确输入线程数！");
	}
			
	
	public void setMethod(int method)
	{
		method_flag=method;
	}
	
	public JComboBox<String> getMethodBox()
	{
		return jcb_method;
	}
	
	public JPanelBruteForceTab()
	{
		jtf_userfile=new JTextField();
		jb_selectuserfile=new JButton("浏览...");
		jl_user=new JLabel("请选择用户字典：");
		jb_selectpwfile=new JButton("浏览...");
		jtf_pwfile=new JTextField();
		jl_pw=new JLabel("请选择密码字典：");		
		jta_userlist=new JTextArea();
		jta_pwlist=new JTextArea();
		jb_start=new JButton("开始");
		jsp_userlist=new JScrollPane(jta_userlist);
		jsp_userlist.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jta_userlist.setEditable(false);
		jsp_pwlist=new JScrollPane(jta_pwlist);
		jsp_pwlist.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jta_pwlist.setEditable(false);
		jl_method=new JLabel("请选择提交方式：");
		jcb_method=new JComboBox<String>();
		jcb_method.addItem("GET");
		jcb_method.addItem("POST");
		jtf_threadnum=new JTextField(5);
		jl_threadnum=new JLabel("请输入线程数：");
		
		GridBagLayout layout=new GridBagLayout();
		GridBagConstraints gbc=new GridBagConstraints();
		//JPanel jp_firstline=new JPanel();
		this.setLayout(layout);
		//this.add(jp_firstline);
				
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.weightx=0;
		gbc.weighty=0;
		gbc.insets=new Insets(3, 3, 3, 3);
		gbc.fill=GridBagConstraints.BOTH;
		
		layout.setConstraints(jl_user, gbc);		
		this.add(jl_user);	
		
		gbc.gridx=1;
		gbc.gridwidth=4;
		gbc.weightx=1;
		layout.setConstraints(jtf_userfile, gbc);		
		this.add(jtf_userfile);
		
		gbc.gridx=5;
		gbc.gridwidth=1;
		gbc.weightx=0;
		layout.setConstraints(jb_selectuserfile, gbc);		
		this.add(jb_selectuserfile);
		
		gbc.gridx=6;
		layout.setConstraints(jl_pw, gbc);		
		this.add(jl_pw);	
		
		gbc.gridx=7;
		gbc.gridwidth=4;
		gbc.weightx=1;
		layout.setConstraints(jtf_pwfile, gbc);			
		this.add(jtf_pwfile);
		
		gbc.gridx=11;
		gbc.gridwidth=1;
		gbc.weightx=0;
		layout.setConstraints(jb_selectpwfile, gbc);		
		this.add(jb_selectpwfile);

		gbc.gridx=0;
		gbc.gridy=1;
		gbc.gridheight=6;
		gbc.gridwidth=6;
		gbc.weightx=1;
		gbc.weighty=1;
		layout.setConstraints(jsp_userlist, gbc);
		this.add(jsp_userlist);
		
		gbc.gridx=6;
		layout.setConstraints(jsp_pwlist, gbc);
		this.add(jsp_pwlist);
		
		gbc.gridx=0;
		gbc.gridy=7;
		gbc.weightx=0;
		gbc.weighty=0;			
		gbc.gridwidth=1;
		gbc.gridheight=1;		
		layout.setConstraints(jl_method, gbc);
		this.add(jl_method);
		
		gbc.gridx=1;
		layout.setConstraints(jcb_method, gbc);
		this.add(jcb_method);
		
		gbc.gridx=2;
		layout.setConstraints(jl_threadnum, gbc);
		this.add(jl_threadnum);
		
		gbc.gridx=3;
		layout.setConstraints(jtf_threadnum, gbc);
		this.add(jtf_threadnum);
		
		gbc.gridx=11;
		layout.setConstraints(jb_start, gbc);
		this.add(jb_start);
		
		jb_selectuserfile.addActionListener(this);
		jb_selectpwfile.addActionListener(this);

	}
	
	public void setTargetWebList(ArrayList<String> url)
	{
		urllist=new ArrayList<String>(url);
	}
	
	public JButton getStartBut()
	{
		return jb_start;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==jb_selectpwfile)
		{
			JFileChooser jfc=new JFileChooser();
			FileNameExtensionFilter ff=new FileNameExtensionFilter("TXT文档(*.txt)", "txt");
			jfc.setFileFilter(ff);
			jfc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
			jfc.setDialogTitle("打开");
			int retval=jfc.showOpenDialog(null);
			File file=jfc.getSelectedFile();
			if(retval==JFileChooser.APPROVE_OPTION)
			{
				String pwpath=file.getPath();
				jtf_pwfile.setText(pwpath);
				this.loadPasswordDire(pwpath);
				for(int i=0;i<this.passwordDire.size();i++)
					jta_pwlist.append(this.passwordDire.get(i)+"\n");
			}
		}
		if(e.getSource()==jb_selectuserfile)
		{
			JFileChooser jfc=new JFileChooser();
			FileNameExtensionFilter ff=new FileNameExtensionFilter("TXT文档(*.txt)", "txt");
			jfc.setFileFilter(ff);
			jfc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
			jfc.setDialogTitle("打开");
			int retval=jfc.showOpenDialog(null);
			File file=jfc.getSelectedFile();
			if(retval==JFileChooser.APPROVE_OPTION)
			{
				String userpath=file.getPath();
				jtf_userfile.setText(userpath);
				this.loadUserDire(userpath);
				for(int i=0;i<this.userDire.size();i++)
					jta_userlist.append(this.userDire.get(i)+"\n");				
			}
		}
	}
}
