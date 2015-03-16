package tion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private String cookie="";
	private String brasAddress;
	private String account;
	private String passward;
	private String userIntranetAddress;
	
	private JButton loginButton;
	private JButton logoutButton;
	
	private JLabel  accountLabel;
	private JLabel  passwordLabel;
	
	private JLabel  loginTip;
	
	private JLabel  loginRemtip;
	private JCheckBox loginRem;//提供记住密码功能
	
	private JTextField     loginAccount;
	private JPasswordField loginPass;
	
	private boolean jizhumimaOrNot;
	
	public MainForm(){
	
		super("数字中南自动登陆");
		this.setSize(250,300);
		this.setResizable(false);
		this.setLayout(null);
		
		
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); 
		Dimension frameSize = this.getSize();
		if (frameSize.width > displaySize.width)  
				frameSize.width = displaySize.width;
		this.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2); // 设置窗口居中显示器显示  
		

		this.accountLabel  = new JLabel("账      号：");
		this.accountLabel.setBounds(30, 110, 100, 20);
		
		this.passwordLabel = new JLabel("密      码：");
		this.passwordLabel.setBounds(30, 140, 100, 20);
		
		this.loginTip = new JLabel("");
	    this.loginTip.setBounds(130, 175, 100, 20);
		
	    this.loginRemtip = new JLabel("记住密码");
	    this.loginRemtip.setBounds(30,175, 60, 20);
	      
		this.loginAccount = new JTextField();
		this.loginAccount.setBounds(120, 110, 100, 20);

		this.loginPass = new JPasswordField();
		this.loginPass.setBounds(120, 140, 100, 20);
		
		LoadConfig();
		
		this.loginRem = new JCheckBox("记住密码",jizhumimaOrNot);
		    this.loginRem.setBounds(90, 175, 20, 20);
		
		//使用绝对布局setBounds()方法
		this.loginButton=new JButton("登入");
		this.loginButton.setBounds(10, 200, 100, 30);
		
		this.loginButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				    if(ReadFromInput())
				        Login();
				    new Thread(){
				    	public void run(){
				    	   RecordConfig();
				    	}
				    }.start();
			}
		});

		
		this.logoutButton=new JButton("登出");
		this.logoutButton.setBounds(120, 200, 100, 30);
		
		this.logoutButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				Logout();
			}
		});
			
		
		this.add(accountLabel);
		this.add(passwordLabel);
		
		this.add(loginAccount);
		this.add(loginPass);
		
		this.add(loginRemtip);
		this.add(loginRem);
		
		this.add(loginTip);
		
		this.add(loginButton);
		this.add(logoutButton);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	 }
	 public boolean ReadFromInput(){
		    if(!loginAccount.getText().equals(""))
			    account=loginAccount.getText();
			else{
				loginTip.setForeground(Color.RED);
			    loginTip.setText("账号为空!");
			    return false;
			}
			if(loginPass.getPassword().length!=0)
				passward = RSA.getRSAPsd( new String(loginPass.getPassword()));
			else{
				loginTip.setForeground(Color.RED);
			    loginTip.setText("密码为空!");
			    return false;
			}
			return true;
	 }
	
	 //将账号，密码和是否记录账号密码以字符串方式存储数据中，写入文件再读取
	 public void RecordConfig(){
	     if(loginRem.isSelected()){
	    	 String[] config =new String[3];
	    	 config[0]="true";
	    	 config[1]=account;
		     config[2]=new String(loginPass.getPassword());
		     try {
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("config"));
					oos.writeObject(config);
					oos.close();
				 } catch (IOException e) {
					e.printStackTrace();
			 }
	     }
	     else{
	    	 String[] config =new String[1];
	    	 config[0]="false";
	    	 try {
	 			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("config"));
	 			oos.writeObject(config);
	 			oos.close();
	 		 } catch (IOException e) {
	 			e.printStackTrace();
	 		 }
	     }
	 }
	 public void LoadConfig(){
		    String[] temp;
		    File confile = new File("config");
		    if(confile.exists()){
		 	  try {
				  ObjectInputStream ois = new ObjectInputStream(new FileInputStream("config"));
				  temp =(String[])ois.readObject();
				  ois.close();
				  for(int i=0;i<temp.length;i++)
					 System.out.println(temp[i]);
				  if(temp[0].equals("true")){
					  jizhumimaOrNot=true;
					  loginAccount.setText(temp[1]);
					  loginPass.setText(temp[2]);
				  }
				  else
					  jizhumimaOrNot=false;
			  }catch (Exception e) {
				  e.printStackTrace();
			  }
		    }else{
		    	return;
		    }
	 }
	 //登入网络
	 public boolean Login(){
		
		String location="";
	
		//向www.baidu.com发送 GET 请求,获取重定向URL,同时获取IP地址和接入设备地址两个参数
		location=HttpRequest.sendGet("http://www.baidu.com:80","",HttpRequest.GET_LOCATION);
		//System.out.println("Location is:" + location);
		
		String cookietest = HttpRequest.sendGet("http://www.baidu.com:80","",HttpRequest.GET_COOKIE);
		//System.out.println("cookietest is " + cookietest);
		
		//如果没有获得Location却产生了新的cookie,则说明已经连接上网。
		if(location==null&&cookietest!=null){
			loginTip.setForeground(Color.RED);
			loginTip.setText("网络已连接");
			return false;
		}
		
		//如果既不能获得location又不能获得cookie则说明没有联网。
		if(location==null&&cookietest==null){
			loginTip.setForeground(Color.RED);
			loginTip.setText("未连接到网络");
			return false;
		}
		
		String s1=location.substring(location.indexOf("bas")+4);
		       brasAddress=s1.substring(0,s1.indexOf('?'));
		       
		String s2=s1.substring(s1.indexOf("wlanuserip")+11);
		       userIntranetAddress=s2.substring(0,s2.indexOf('&'));
		
		//发送 GET 请求,获取cookie
		cookie=HttpRequest.sendGet(location,"",HttpRequest.GET_COOKIE);
		
	    cookie=cookie.substring(0,cookie.indexOf(';'));
	   // System.out.println("The Cookie being sent:"+cookie);  
		
	    String stringupload = "accountID="+account+"%40zndx.inter&password="+passward+"&brasAddress="
		           +brasAddress+"&userIntranetAddress="
		           +userIntranetAddress;
	    
	    //向重定向URL发送POST 请求,尝试登入        
	    String s3=HttpRequest.sendPost("http://61.137.86.87:8080/portalNat444/AccessServices/login",
	    	   stringupload,
	           cookie); 
	    if(s3.charAt(s3.indexOf("resultCode")+13)=='0')
	    {
	    	loginTip.setForeground(Color.RED);
	        loginTip.setText("登陆成功");
	        return true;
	    }else{
	    	loginTip.setForeground(Color.RED);
	    	loginTip.setText("登陆失败");
	        return false;
	    }
	 }
	 //登出网络
	 public void  Logout(){
		 String s3=HttpRequest.sendPost("http://61.137.86.87:8080/portalNat444/AccessServices/logout?",
				"brasAddress="+brasAddress+"&userIntranetAddress="+userIntranetAddress,cookie);    
		 if(s3.charAt(s3.indexOf("resultCode")+13)=='0')
			 loginTip.setText("登出成功");
		 else
			 loginTip.setText("登出失败");
	 }
	 
	 public static void main(String[] args) {
		new MainForm();		
	 }
}