package mods.BruteForce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CrawlerTest {
	
	 static String SendGet(String url) {
		  // ����һ���ַ��������洢��ҳ����
		  String result = "";
		  // ����һ�������ַ�������
		  BufferedReader in = null;
		  try {
		   // ��stringת��url����
		   URL realUrl = new URL(url);
		   // ��ʼ��һ�����ӵ��Ǹ�url������
		   URLConnection connection = realUrl.openConnection();
		   // ��ʼʵ�ʵ�����
		   connection.connect();
		   // ��ʼ�� BufferedReader����������ȡURL����Ӧ
		   in = new BufferedReader(new InputStreamReader(
		     connection.getInputStream()));
		   // ������ʱ�洢ץȡ����ÿһ�е�����
		   String line;
		   while ((line = in.readLine()) != null) {
		    // ����ץȡ����ÿһ�в�����洢��result����
		    result += line;
		   }
		  } catch (Exception e) {
		   System.out.println("����GET��������쳣��" + e);
		   e.printStackTrace();
		  }
		  // ʹ��finally���ر�������
		  finally {
		   try {
		    if (in != null) {
		     in.close();
		    }
		   } catch (Exception e2) {
		    e2.printStackTrace();
		   }
		  }
		  return result;
		 }
		 
		 static String RegexString(String targetStr, String patternStr) {
		  // ����һ����ʽģ�壬����ʹ��������ʽ����������Ҫץ������
		  // �൱�����������ƥ��ĵط��ͻ����ȥ
		  Pattern pattern = Pattern.compile(patternStr);
		  // ����һ��matcher������ƥ��
		  Matcher matcher = pattern.matcher(targetStr);
		  // ����ҵ���
		  while (matcher.find()) {
		   // ��ӡ�����
		   //return matcher.group(1);
			  System.out.println(matcher.group());
		  }
		  return "Nothing";
		 }
		 
		 
		 public static void main(String[] args) {
//		  // ���弴�����ʵ�����
//		  String url = "http://www.bilibili.com";
//		  // �������Ӳ���ȡҳ������
//		  String result = SendGet(url);
//		  // ʹ������ƥ��ͼƬ��src����
//		  String imgSrc = RegexString(result, "src=\"(.+?)\"");
//		  // ��ӡ���
//		  System.out.println(result);
			 
			 try {
				URL url=new URL("http://www.lightnovel.cn/forum.php");
				URLConnection conn=url.openConnection();
				
				Map headers=conn.getHeaderFields();
//		        Set<String> keys = headers.keySet();
//		        for( String key : keys ){
//		            String val = conn.getHeaderField(key);
//		            System.out.println(key+"    "+val);
//		        }
//		        System.out.println( conn.getLastModified() );
				System.out.println( headers );
			
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	 
		 }
}
