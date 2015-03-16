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
	
		super("���������Զ���½");
		this.setSize(250,300);
		this.setResizable(false);
		this.setLayout(null);
		
		//���ô������
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // �����ʾ����С����  
		Dimension frameSize = this.getSize(); // ��ô��ڴ�С����  
		if (frameSize.width > displaySize.width)  
				frameSize.width = displaySize.width;           // ���ڵĿ�Ȳ��ܴ�����ʾ���Ŀ��  
		this.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2); // ���ô��ھ�����ʾ����ʾ  
		
		
		//�˺ſ���������ʾ������
		this.accountLabel  = new JLabel("�˺ţ�");
		this.accountLabel.setBounds(30, 120, 100, 20);
		this.passwordLabel = new JLabel("���룺");
		this.passwordLabel.setBounds(30, 150, 100, 20);
		this.loginAccount = new JTextField();
		this.loginAccount.setBounds(120, 120, 100, 20);
		this.loginPass = new JPasswordField();
		this.loginPass.setBounds(120, 150, 100, 20);
		
		
		//ʹ�þ��Բ���setBounds()����
		this.loginButton=new JButton("����");
		this.loginButton.setBounds(10, 200, 100, 30);
		
		this.loginButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				account=loginAccount.getText();
				passward = RSA.getRSAPsd( new String(loginPass.getPassword()));
				Login();	
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
		
		this.add(loginButton);
		this.add(logoutButton);
		
		//���ô���ɼ� -- ���������ʾ���ȹ�������ʾ
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public  void Login(){
		
		String location="";
	
		//��www.baidu.com���� GET ����,��ȡ�ض���URL,ͬʱ��ȡIP��ַ�ͽ����豸��ַ��������
		location=HttpRequest.sendGet("http://www.baidu.com:80","",HttpRequest.GET_LOCATION);
		System.out.println("Location is:" + location);
		
		//�Ѿ�����������߷�������������
		if(location==null||location.equals("EXCEPTION")){
			  JOptionPane.showConfirmDialog(null, "�������������߷������������ӣ�", "��ʾ", JOptionPane.YES_OPTION);
			  return;
		}
				
		if(location.equals("OFFLINE")){
			JOptionPane.showConfirmDialog(null, "δ�������磡", "��ʾ", JOptionPane.YES_OPTION);
			return;
		}
		
		String s1=location.substring(location.indexOf("bas")+4);
		       brasAddress=s1.substring(0,s1.indexOf('?'));
		       
		String s2=s1.substring(s1.indexOf("wlanuserip")+11);
		       userIntranetAddress=s2.substring(0,s2.indexOf('&'));
		
		//���� GET ����,��ȡcookie
		cookie=HttpRequest.sendGet(location,"",HttpRequest.GET_COOKIE);
		
	    cookie=cookie.substring(0,cookie.indexOf(';'));
	    System.out.println("The Cookie being sent:"+cookie);  
		
	    String stringupload = "accountID="+account+"%40zndx.inter&password="+passward+"&brasAddress="
		           +brasAddress+"&userIntranetAddress="
		           +userIntranetAddress;
	    
	    //���ض���URL����POST ����,���Ե���        
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