package tion;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShowPanel extends JPanel {

	/**
	 * ��ʾ��½��Ϣ
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

	//���������õǳ���ť
	JButton logout;

	ShowPanel() {

		this.setLayout(null);

		this.sum_liu = new JLabel("����������:");
		this.sum_liu.setBounds(40, 40, 90, 30);
		this.sum_liu_num = new JLabel("null");
		this.sum_liu_num.setBounds(130, 40, 90, 30);

		this.used_liu = new JLabel("��ʹ������:");
		this.used_liu.setBounds(40, 80, 90, 30);
		this.used_liu_num = new JLabel("null");
		this.used_liu_num.setBounds(130, 80, 90, 30);

		this.left_liu = new JLabel("ʣ �� ��  ��:");
		this.left_liu.setBounds(40, 120, 90, 30);
		this.left_liu_num = new JLabel("null");
		this.left_liu_num.setBounds(130, 120, 90, 30);

		this.left_money = new JLabel("ʣ �� ��  ��:");
		this.left_money.setBounds(40, 160, 90, 30);
		this.left_money_num = new JLabel("null");
		this.left_money_num.setBounds(130, 160, 90, 30);

		this.logout = new JButton("�ǳ�");
		this.logout.setBounds(74, 210, 90, 30);

		this.add(left_liu);
		this.add(left_liu_num);
		this.add(left_money);
		this.add(left_money_num);
		this.add(sum_liu);
		this.add(sum_liu_num);
		this.add(used_liu);
		this.add(used_liu_num);
		this.add(logout);
	}
}