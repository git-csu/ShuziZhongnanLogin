package tion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
	
public class HttpRequest {
		
		public static final int GET_HTMLSTR=0;
		public static final int GET_COOKIE=1;
		public static final int GET_LOCATION=2;
		
	    /**
	     * ��ָ��URL����GET����������
	     * 
	     * @param url
	     *            ���������URL
	     * @param param
	     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	     * @return URL ������Զ����Դ����Ӧ���
	     */
	    public static String sendGet(String url,String sentCookie,int getFlag)  {
	    	
	    	String result="";
	        String htmlstr = "";
	        String getCookie="";
	        String getlLocation="";
	        BufferedReader in = null;
	        
	        try {	          
	            URL realUrl = new URL(url);
	            // �򿪺�URL֮�������
	            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
	        
	            // ����ͨ�õ���������
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("cookie", sentCookie);
	            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.2; Win64;");
	            connection.setConnectTimeout(2000);
	            
	            //�ǳ���Ҫ��һ��ҪΪfalse.
	            connection.setInstanceFollowRedirects(false);
	            
	            //��������ʾʲô�أ������ں÷��÷��أ�
	            getCookie = connection.getHeaderField("Set-Cookie");
	            System.out.println("GetCookie:"+getCookie);
	            getlLocation=connection.getHeaderField("LOCATION");	          
	            System.out.println("GetLocation:"+getlLocation);
	             
	            
	            // ����ʵ�ʵ�����
	            connection.connect();
	            // ��ȡcookie
	             	           
	            // ���� BufferedReader����������ȡURL����Ӧ
	            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));	                   
	            String line;
	            while ((line = in.readLine()) != null) {
	                htmlstr += line;
	            }            
	        }catch (UnknownHostException e) {	        		
	        	result= "OFFLINE" ;           
	        }catch (Exception e) {
	        	JOptionPane.showConfirmDialog(null, "δ�������磡", "��ʾ", JOptionPane.YES_NO_OPTION);				    		
	        	result= "EXCEPTION" ;           
	        }
	        // ʹ��finally�����ر�������
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        switch(getFlag){
	        	case GET_HTMLSTR:result=htmlstr;break;
	        	case GET_COOKIE:result=getCookie;;break;
	        	case GET_LOCATION:result=getlLocation;;break;
	        	default:result="";break;
	        }	
	        return result;
	    }

	    /**
	     * ��ָ�� URL ����POST����������
	     * 
	     * @param url
	     *            ��������� URL
	     * @param param
	     *            ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
	     * @return ������Զ����Դ����Ӧ���
	     */
	    public static String sendPost(String url, String param,String cookie) {
	    	
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // �򿪺�URL֮�������
	            URLConnection conn = realUrl.openConnection();
	            
	            // ����ͨ�õ���������,ģ��������ĵ�½
	            conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setRequestProperty("Cookie", cookie);//POST������ǳ����涼��Ҫcookie
	            conn.setRequestProperty("Referer", "http://61.137.86.87:8080/portalNat444/main2.jsp");	            
	            conn.setRequestProperty("User-Agent",  "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.2; Win64");	                  
	            conn.setConnectTimeout(2000);
	            
	            // ����POST������������������� sendPost()��sendGet()����
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            
	            // ��ȡURLConnection�����Ӧ�������
	            out = new PrintWriter(conn.getOutputStream());
	            // �����������
	            out.print(param);
	            // flush������Ļ���
	            out.flush();
	           
	            // ����BufferedReader����������ȡURL����Ӧ,��α�֤�����룬����һ������
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));	                 	            
	            String line;
	            while ((line = in.readLine()) != null) {
	            	result += line;
	            }

	        } catch (Exception e) {	           
	            e.printStackTrace();
	            result="EXCEPTION";
	        }
	        //ʹ��finally�����ر��������������
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    
	}