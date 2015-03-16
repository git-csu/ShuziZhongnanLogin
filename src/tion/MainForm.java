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
	private JCheckBox loginRem;//�ṩ��ס���빦��
	
	private JTextField     loginAccount;
	private JPasswordField loginPass;
	
	private boolean jizhumimaOrNot;
	
	public MainForm(){
	
		super("���������Զ���½");
		this.setSize(250,300);
		this.setResizable(false);
		this.setLayout(null);
		
		
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); 
		Dimension frameSize = this.getSize();
		if (frameSize.width > displaySize.width)  
				frameSize.width = displaySize.width;
		this.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2); // ���ô��ھ�����ʾ����ʾ  
		

		this.accountLabel  = new JLabel("��      �ţ�");
		this.accountLabel.setBounds(30, 110, 100, 20);
		
		this.passwordLabel = new JLabel("��      �룺");
		this.passwordLabel.setBounds(30, 140, 100, 20);
		
		this.loginTip = new JLabel("");
	    this.loginTip.setBounds(130, 175, 100, 20);
		
	    this.loginRemtip = new JLabel("��ס����");
	    this.loginRemtip.setBounds(30,175, 60, 20);
	      
		this.loginAccount = new JTextField();
		this.loginAccount.setBounds(120, 110, 100, 20);

		this.loginPass = new JPasswordField();
		this.loginPass.setBounds(120, 140, 100, 20);
		
		LoadConfig();
		
		this.loginRem = new JCheckBox("��ס����",jizhumimaOrNot);
		    this.loginRem.setBounds(90, 175, 20, 20);
		
		//ʹ�þ��Բ���setBounds()����
		this.loginButton=new JButton("����");
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

		
		this.logoutButton=new JButton("�ǳ�");
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
			    loginTip.setText("�˺�Ϊ��!");
			    return false;
			}
			if(loginPass.getPassword().length!=0)
				passward = RSA.getRSAPsd( new String(loginPass.getPassword()));
			else{
				loginTip.setForeground(Color.RED);
			    loginTip.setText("����Ϊ��!");
			    return false;
			}
			return true;
	 }
	
	 //���˺ţ�������Ƿ��¼�˺��������ַ�����ʽ�洢�����У�д���ļ��ٶ�ȡ
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
	 //��������
	 public boolean Login(){
		
		String location="";
	
		//��www.baidu.com���� GET ����,��ȡ�ض���URL,ͬʱ��ȡIP��ַ�ͽ����豸��ַ��������
		location=HttpRequest.sendGet("http://www.baidu.com:80","",HttpRequest.GET_LOCATION);
		//System.out.println("Location is:" + location);
		
		String cookietest = HttpRequest.sendGet("http://www.baidu.com:80","",HttpRequest.GET_COOKIE);
		//System.out.println("cookietest is " + cookietest);
		
		//���û�л��Locationȴ�������µ�cookie,��˵���Ѿ�����������
		if(location==null&&cookietest!=null){
			loginTip.setForeground(Color.RED);
			loginTip.setText("����������");
			return false;
		}
		
		//����Ȳ��ܻ��location�ֲ��ܻ��cookie��˵��û��������
		if(location==null&&cookietest==null){
			loginTip.setForeground(Color.RED);
			loginTip.setText("δ���ӵ�����");
			return false;
		}
		
		String s1=location.substring(location.indexOf("bas")+4);
		       brasAddress=s1.substring(0,s1.indexOf('?'));
		       
		String s2=s1.substring(s1.indexOf("wlanuserip")+11);
		       userIntranetAddress=s2.substring(0,s2.indexOf('&'));
		
		//���� GET ����,��ȡcookie
		cookie=HttpRequest.sendGet(location,"",HttpRequest.GET_COOKIE);
		
	    cookie=cookie.substring(0,cookie.indexOf(';'));
	   // System.out.println("The Cookie being sent:"+cookie);  
		
	    String stringupload = "accountID="+account+"%40zndx.inter&password="+passward+"&brasAddress="
		           +brasAddress+"&userIntranetAddress="
		           +userIntranetAddress;
	    
	    //���ض���URL����POST ����,���Ե���        
	    String s3=HttpRequest.sendPost("http://61.137.86.87:8080/portalNat444/AccessServices/login",
	    	   stringupload,
	           cookie); 
	    if(s3.charAt(s3.indexOf("resultCode")+13)=='0')
	    {
	    	loginTip.setForeground(Color.RED);
	        loginTip.setText("��½�ɹ�");
	        return true;
	    }else{
	    	loginTip.setForeground(Color.RED);
	    	loginTip.setText("��½ʧ��");
	        return false;
	    }
	 }
	 //�ǳ�����
	 public void  Logout(){
		 String s3=HttpRequest.sendPost("http://61.137.86.87:8080/portalNat444/AccessServices/logout?",
				"brasAddress="+brasAddress+"&userIntranetAddress="+userIntranetAddress,cookie);    
		 if(s3.charAt(s3.indexOf("resultCode")+13)=='0')
			 loginTip.setText("�ǳ��ɹ�");
		 else
			 loginTip.setText("�ǳ�ʧ��");
	 }
	 
	 public static void main(String[] args) {
		new MainForm();		
	 }
}