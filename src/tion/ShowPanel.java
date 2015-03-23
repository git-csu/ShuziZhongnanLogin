package tion;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShowPanel extends JPanel {

	/**
	 * 显示登陆信息
	 */
	private static final long serialVersionUID = 7516630583323609979L;

	JLabel sum_liu;
	JLabel sum_liu_num;

	JLabel used_liu;
	JLabel used_liu_num;

	JLabel left_liu;
	JLabel left_liu_num;

	JLabel left_money;
	JLabel left_money_num;
	
	JLabel logout_tip;

	//在这里设置登出按钮
	JButton logout;

	ShowPanel() {

		this.setLayout(null);
		this.setBackground(Color.WHITE);

		this.sum_liu = new JLabel("本月总流量:");
		this.sum_liu.setBounds(40, 40, 90, 30);
		this.sum_liu_num = new JLabel("null");
		this.sum_liu_num.setBounds(130, 40, 90, 30);

		this.used_liu = new JLabel("已使用流量:");
		this.used_liu.setBounds(40, 80, 90, 30);
		this.used_liu_num = new JLabel("null");
		this.used_liu_num.setBounds(130, 80, 90, 30);

		this.left_liu = new JLabel("剩 余 流  量:");
		this.left_liu.setBounds(40, 120, 90, 30);
		this.left_liu_num = new JLabel("null");
		this.left_liu_num.setBounds(130, 120, 90, 30);

		this.left_money = new JLabel("剩 余 金  额:");
		this.left_money.setBounds(40, 160, 90, 30);
		this.left_money_num = new JLabel("null");
		this.left_money_num.setBounds(130, 160, 90, 30);

		this.logout = new JButton("登出");
		this.logout.setBounds(74, 210, 90, 30);
		this.logout_tip = new JLabel();
		this.logout_tip.setForeground(Color.RED);
		this.logout_tip.setBounds(90, 240, 90, 30);

		this.add(left_liu);
		this.add(left_liu_num);
		this.add(left_money);
		this.add(left_money_num);
		this.add(sum_liu);
		this.add(sum_liu_num);
		this.add(used_liu);
		this.add(used_liu_num);
		this.add(logout);
		this.add(logout_tip);
	}
	public static void main(String[] args) {
		JFrame jf = new JFrame();
		jf.add(new ShowPanel());
		jf.setVisible(true);
		
	}
}