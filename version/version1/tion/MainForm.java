package tion;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	private JTextField     loginAccount;
	private JPasswordField loginPass;
	
	public MainForm(){
	
		super("数字中南自动登陆");
		this.setSize(250,300);
		this.setResizable(false);
		this.setLayout(null);
		
		//设置窗体居中
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象  
		Dimension frameSize = this.getSize(); // 获得窗口大小对象  
		if (frameSize.width > displaySize.width)  
				frameSize.width = displaySize.width;           // 窗口的宽度不能大于显示器的宽度  
		this.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2); // 设置窗口居中显示器显示  
		
		
		//账号框和密码框显示和输入
		this.accountLabel  = new JLabel("账号：");
		this.accountLabel.setBounds(30, 120, 100, 20);
		this.passwordLabel = new JLabel("密码：");
		this.passwordLabel.setBounds(30, 150, 100, 20);
		this.loginAccount = new JTextField();
		this.loginAccount.setBounds(120, 120, 100, 20);
		this.loginPass = new JPasswordField();
		this.loginPass.setBounds(120, 150, 100, 20);
		
		
		//使用绝对布局setBounds()方法
		this.loginButton=new JButton("登入");
		this.loginButton.setBounds(10, 200, 100, 30);
		
		this.loginButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				account=loginAccount.getText();
				passward = RSA.getRSAPsd( new String(loginPass.getPassword()));
				Login();	
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
		
		this.add(loginButton);
		this.add(logoutButton);
		
		//设置窗体可见 -- 最后声明显示，先构建再显示
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public  void Login(){
		
		String location="";
	
		//向www.baidu.com发送 GET 请求,获取重定向URL,同时获取IP地址和接入设备地址两个参数
		location=HttpRequest.sendGet("http://www.baidu.com:80","",HttpRequest.GET_LOCATION);
		System.out.println("Location is:" + location);
		
		//已经连上网络或者非数字中南连接
		if(location==null||location.equals("EXCEPTION")){
			  JOptionPane.showConfirmDialog(null, "已连接上网或者非数字中南连接！", "提示", JOptionPane.YES_OPTION);
			  return;
		}
				
		if(location.equals("OFFLINE")){
			JOptionPane.showConfirmDialog(null, "未连接网络！", "提示", JOptionPane.YES_OPTION);
			return;
		}
		
		String s1=location.substring(location.indexOf("bas")+4);
		       brasAddress=s1.substring(0,s1.indexOf('?'));
		       
		String s2=s1.substring(s1.indexOf("wlanuserip")+11);
		       userIntranetAddress=s2.substring(0,s2.indexOf('&'));
		
		//发送 GET 请求,获取cookie
		cookie=HttpRequest.sendGet(location,"",HttpRequest.GET_COOKIE);
		
	    cookie=cookie.substring(0,cookie.indexOf(';'));
	    System.out.println("The Cookie being sent:"+cookie);  
		
	    String stringupload = "accountID="+account+"%40zndx.inter&password="+passward+"&brasAddress="
		           +brasAddress+"&userIntranetAddress="
		           +userIntranetAddress;
	    
	    //向重定向URL发送POST 请求,尝试登入        
	    String s3=HttpRequest.sendPost("http://61.137.86.87:8080/portalNat444/AccessServices/login",
	    	   stringupload,
	           cookie);        
	    System.out.println(s3);
	 }
	
	 public void  Logout(){
		 String s3=HttpRequest.sendPost("http://61.137.86.87:8080/portalNat444/AccessServices/logout?",
				"brasAddress="+brasAddress+"&userIntranetAddress="+userIntranetAddress,cookie);        
		 System.out.println(s3);
	 }
	 
	 public static void main(String[] args) {
		new MainForm();		
	 }
}