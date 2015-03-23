package tion;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MainForm extends JFrame {
	private static final long serialVersionUID = 1L;

	// 程序中用到的一些字符串
	private String cookie = "";
	private String brasAddress;
	private String userIntranetAddress;

	private LoginPanel loginPanel;
	private ShowPanel showPanel;

	// 最小化到托盘
	private TrayIcon trayIcon;// 托盘图标
	private SystemTray systemTray;// 系统托盘

	MainForm() {

		super("数字中南自动登陆");
		this.setSize(250, 300);
		this.setResizable(false);
		this.setLayout(null);
		
		ImageIcon icon = new ImageIcon("login2.jpg");
		this.setIconImage(icon.getImage());
		
		//制造拖动效果
		MouseEventListener mouselister = new MouseEventListener(this);
		

		// 最小化到托盘
		this.systemTray = SystemTray.getSystemTray();
		trayIcon = new TrayIcon(new ImageIcon("login2.jpg").getImage());
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		this.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				dispose();// 窗口最小化时dispose该窗口
			}
		});
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)// 双击托盘窗口再现
					setExtendedState(Frame.NORMAL);
				setVisible(true);
			}
		});

		this.loginPanel = new LoginPanel();
		this.loginPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.loginPanel.setVisible(true);
		this.loginPanel.addMouseListener(mouselister);
		this.loginPanel.addMouseMotionListener(mouselister);
		this.loginPanel.loginButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (loginPanel.ReadFromInput())
					Login();
				new Thread() {
					public void run() {
						loginPanel.RecordConfig();
					}
				}.start();
			}
		});
		this.add(loginPanel);

		this.showPanel = new ShowPanel();
		this.showPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.showPanel.setVisible(false);
		this.showPanel.addMouseListener(mouselister);
		this.showPanel.addMouseMotionListener(mouselister);
		this.showPanel.logout.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Logout();
			}
		});
		this.add(showPanel);

		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.width > displaySize.width)
			frameSize.width = displaySize.width;
		this.setLocation((displaySize.width - frameSize.width) / 2,
				(displaySize.height - frameSize.height) / 2); // 设置窗口居中显示器显示

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public boolean Login() {
		String location = HttpRequest.sendGet("http://www.baidu.com:80", "",
				HttpRequest.GET_LOCATION);
		String cookietest = HttpRequest.sendGet("http://www.baidu.com:80", "",
				HttpRequest.GET_COOKIE);
		if (location == null ) {
			if(cookietest != null){
    			this.loginPanel.loginTip.setForeground(Color.RED);
	    		this.loginPanel.loginTip.setText("网络已连接");
		    	return false;
			}else {
				this.loginPanel.loginTip.setForeground(Color.RED);
				this.loginPanel.loginTip.setText("未连接到网络");
				return false;
			}
		}
		
		String s1 = location.substring(location.indexOf("bas") + 4);
		brasAddress = s1.substring(0, s1.indexOf('?'));

		String s2 = s1.substring(s1.indexOf("wlanuserip") + 11);
		userIntranetAddress = s2.substring(0, s2.indexOf('&'));

		cookie = HttpRequest.sendGet(location, "", HttpRequest.GET_COOKIE);
		cookie = cookie.substring(0, cookie.indexOf(';'));

		String stringupload = "accountID=" + this.loginPanel.account
				+ "%40zndx.inter&password=" + this.loginPanel.passward
				+ "&brasAddress=" + brasAddress + "&userIntranetAddress="
				+ userIntranetAddress;

		String s3 = HttpRequest.sendPost(
				"http://61.137.86.87:8080/portalNat444/AccessServices/login",
				stringupload, cookie);
		char flag = s3.charAt(s3.indexOf("resultCode") + 13);
		
		if (flag == '0') {
			new Thread(){
				public void run(){
					ShowData();
				    loginPanel.setVisible(false);
					showPanel.setVisible(true);
					loginPanel.LoginSuccess = true;
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			}.start();
			return true;
		} else if(flag == '2') {
			this.loginPanel.loginTip.setForeground(Color.RED);
			this.loginPanel.loginTip.setText("用户已在线");
			this.loginPanel.LoginSuccess = false;
			return false;
		} else {
			this.loginPanel.loginTip.setForeground(Color.RED);
			this.loginPanel.loginTip.setText("登陆失败");
			this.loginPanel.LoginSuccess = false;
			return false;
		}
	}

	public void ShowData() {
		
		String s4 = HttpRequest.sendGet(
				"http://61.137.86.87:8080/portalNat444/main2.jsp", cookie,
				HttpRequest.GET_HTMLSTR);
		
		this.showPanel.sum_liu_num.setText(getLogInfo("本月总流量", s4));
		this.showPanel.used_liu_num.setText(getLogInfo("本月已用流量", s4));
		this.showPanel.left_liu_num.setText(getLogInfo("本月剩余流量", s4));

		Pattern pattern = Pattern.compile("当前剩余金额[\\D]*[\\d\\.]+元");
		Matcher matcher = pattern.matcher(s4);
		if (matcher.find())
			this.showPanel.left_money_num.setText(matcher.group(0).replaceAll(
					"[^\\d\\.元]", ""));

	}

	private String getLogInfo(String info, String htmlstr) {
		String result = "";
		Pattern pattern4 = Pattern.compile(info + "[\\D]*[\\d]+[GMK]B");
		Matcher matcher4 = pattern4.matcher(htmlstr);
		if (matcher4.find())
			result = matcher4.group(0).replaceAll("[^\\dGMKB]", "");
		if (result == null) {
			result = "null";
		}
		return result;
	}

	// 登出网络
	public void Logout() {
		String s3 = HttpRequest.sendPost(
				"http://61.137.86.87:8080/portalNat444/AccessServices/logout?",
				"brasAddress=" + brasAddress + "&userIntranetAddress="
						+ userIntranetAddress, cookie);

		if (s3.charAt(s3.indexOf("resultCode") + 13) == '0') {
			loginPanel.loginTip.setText("登出成功");
			//界面进行切换
			new Thread(){
				public void run(){
					showPanel.setVisible(false);
					loginPanel.setVisible(true);
					
					loginPanel.LoginSuccess = false;
					setDefaultCloseOperation(EXIT_ON_CLOSE);
				}
			}.start();
		} else{
			this.showPanel.logout_tip.setText("登出失败");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}

	public static void main(String[] args) {
		new MainForm();
	}
}