package tion;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {
	private static final long serialVersionUID = -3456170773198057720L;

	JButton loginButton;
	
	JLabel accountLabel;
	JLabel passwordLabel;
	
	JLabel loginTip;
	JLabel loginRemtip;
	
	JCheckBox loginRem;// 提供记住密码功能
	JTextField loginAccount;
	JPasswordField loginPass;

	// 判断状态的一些变量
	boolean jizhumimaOrNot;
	boolean LoginSuccess;
	
	String account;
	String passward;

	public LoginPanel() {

		this.LoginSuccess = false;
		this.setLayout(null);
		
 		this.setBackground(Color.WHITE);

		this.accountLabel = new JLabel("    账  号:");
		this.accountLabel.setBounds(35,  97, 100, 25);
		
		this.loginAccount = new JTextField();
		this.loginAccount.setBounds(110, 100, 100, 20);

		this.passwordLabel = new JLabel("    密  码:");
		this.passwordLabel.setBounds(35, 127, 100, 25);
		this.loginPass = new JPasswordField();
		this.loginPass.setBounds(110, 130, 100, 20);

		this.loginTip = new JLabel();
		this.loginTip.setForeground(Color.RED);
		this.loginTip.setBounds(90, 188, 100, 25);

		this.loginRemtip = new JLabel("记 住 密 码");
		this.loginRemtip.setBounds(80, 160, 70, 30);

		this.loginRem = new JCheckBox();
		this.loginRem.setBackground(Color.WHITE);
		this.loginRem.setBounds(145, 160, 20, 30);

		LoadConfig();

		this.loginButton = new JButton("登入");
		this.loginButton.setBounds(70, 220, 100, 30);

		this.add(accountLabel);
		this.add(passwordLabel);

		this.add(loginAccount);
		this.add(loginPass);

		this.add(loginRemtip);
		this.add(loginRem);

		this.add(loginTip);

		this.add(loginButton);
	}

	public boolean ReadFromInput() {
		if (!loginAccount.getText().equals(""))
			account = loginAccount.getText();
		else {
			loginTip.setForeground(Color.RED);
			loginTip.setText("账号为空!");
			return false;
		}
		if (loginPass.getPassword().length != 0)
			passward = RSA.getRSAPsd(new String(loginPass.getPassword()));
		else {
			loginTip.setForeground(Color.RED);
			loginTip.setText("密码为空!");
			return false;
		}
		return true;
	}

	public void RecordConfig() {
		if (loginRem.isSelected()) {
			String[] config = new String[3];
			config[0] = "true";
			config[1] = account;
			config[2] = new String(loginPass.getPassword());
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream("config"));
				oos.writeObject(config);
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String[] config = new String[1];
			config[0] = "false";
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream("config"));
				oos.writeObject(config);
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void LoadConfig() {
		String[] temp;
		File confile = new File("config");
		if (confile.exists()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream("config"));
				temp = (String[]) ois.readObject();
				ois.close();

				if (temp[0].equals("true")) {
					jizhumimaOrNot = true;
					loginRem.setSelected(true);
					loginAccount.setText(temp[1]);
					loginPass.setText(temp[2]);
				} else
					jizhumimaOrNot = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return;
		}
	}
}