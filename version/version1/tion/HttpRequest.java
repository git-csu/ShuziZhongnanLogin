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
	     * 向指定URL发送GET方法的请求
	     * 
	     * @param url
	     *            发送请求的URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return URL 所代表远程资源的响应结果
	     */
	    public static String sendGet(String url,String sentCookie,int getFlag)  {
	    	
	    	String result="";
	        String htmlstr = "";
	        String getCookie="";
	        String getlLocation="";
	        BufferedReader in = null;
	        
	        try {	          
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
	        
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("cookie", sentCookie);
	            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.2; Win64;");
	            connection.setConnectTimeout(2000);
	            
	            //非常重要，一定要为false.
	            connection.setInstanceFollowRedirects(false);
	            
	            //这两个表示什么呢，我现在好烦好烦呢？
	            getCookie = connection.getHeaderField("Set-Cookie");
	            System.out.println("GetCookie:"+getCookie);
	            getlLocation=connection.getHeaderField("LOCATION");	          
	            System.out.println("GetLocation:"+getlLocation);
	             
	            
	            // 建立实际的连接
	            connection.connect();
	            // 获取cookie
	             	           
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));	                   
	            String line;
	            while ((line = in.readLine()) != null) {
	                htmlstr += line;
	            }            
	        }catch (UnknownHostException e) {	        		
	        	result= "OFFLINE" ;           
	        }catch (Exception e) {
	        	JOptionPane.showConfirmDialog(null, "未连接网络！", "提示", JOptionPane.YES_NO_OPTION);				    		
	        	result= "EXCEPTION" ;           
	        }
	        // 使用finally块来关闭输入流
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
	     * 向指定 URL 发送POST方法的请求
	     * 
	     * @param url
	     *            发送请求的 URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return 所代表远程资源的响应结果
	     */
	    public static String sendPost(String url, String param,String cookie) {
	    	
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            
	            // 设置通用的请求属性,模拟浏览器的登陆
	            conn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setRequestProperty("Cookie", cookie);//POST到登入登出界面都需要cookie
	            conn.setRequestProperty("Referer", "http://61.137.86.87:8080/portalNat444/main2.jsp");	            
	            conn.setRequestProperty("User-Agent",  "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.2; Win64");	                  
	            conn.setConnectTimeout(2000);
	            
	            // 发送POST请求必须设置如下两行 sendPost()与sendGet()函数
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	           
	            // 定义BufferedReader输入流来读取URL的响应,如何保证不乱码，这是一个问题
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));	                 	            
	            String line;
	            while ((line = in.readLine()) != null) {
	            	result += line;
	            }

	        } catch (Exception e) {	           
	            e.printStackTrace();
	            result="EXCEPTION";
	        }
	        //使用finally块来关闭输出流、输入流
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