package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import models.DBConnection;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JTextPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class Creat extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DBConnection ir = null;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Creat() {
		setTitle("\u65B0\u5EFA\u5361\u7247");
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/iRemember.png"));
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 433, 453);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
//		this.setResizable(false);
		// 使窗口居中靠上部
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高
		int height = this.getHeight();
		int width = this.getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);
		this.setVisible(true);
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(SystemColor.windowBorder);
		panel1.setBorder(null);
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		contentPane.add(panel1,BorderLayout.NORTH);
		
		JLabel lbl1 = new JLabel("\u9009\u62E9\u5361\u7EC4\uFF1A");
		lbl1.setForeground(Color.WHITE);
		lbl1.setFont(new Font("等线", Font.PLAIN, 20));
		panel1.add(lbl1);

		Vector<String> DeckList = new Vector<String>();
		ResultSet rs = null;
		String sql = "select name from Deck;";
		try {
			ir = new DBConnection();
			Statement stmt = ir.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				DeckList.addElement(rs.getString("name"));
			}
			rs.close();
			ir.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JComboBox<String> comboBox = new JComboBox<String>(DeckList);
		comboBox.setFont(new Font("等线 Light", Font.PLAIN, 18));
		panel1.add(comboBox);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		contentPane.add(panel2, BorderLayout.CENTER);
		GridBagLayout gbl_panel2 = new GridBagLayout();
		gbl_panel2.columnWidths = new int[]{218, 203, 0};
		gbl_panel2.rowHeights = new int[]{0, 152, 0, 170, 0, 0};
		gbl_panel2.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel2.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel2.setLayout(gbl_panel2);
		
		JLabel lbl2 = new JLabel("\u6B63\u9762\uFF1A");
		lbl2.setForeground(Color.BLACK);
		lbl2.setBackground(Color.LIGHT_GRAY);
		lbl2.setFont(new Font("等线 Light", Font.PLAIN, 18));
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.fill = GridBagConstraints.VERTICAL;
		gbc_lbl2.gridwidth = 2;
		gbc_lbl2.anchor = GridBagConstraints.WEST;
		gbc_lbl2.insets = new Insets(0, 0, 5, 0);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 0;
		panel2.add(lbl2, gbc_lbl2);
		
		JTextPane textPane1 = new JTextPane();
		textPane1.setFont(new Font("等线", Font.PLAIN, 20));
		GridBagConstraints gbc_textPane1 = new GridBagConstraints();
		gbc_textPane1.gridwidth = 2;
		gbc_textPane1.insets = new Insets(0, 0, 5, 0);
		gbc_textPane1.fill = GridBagConstraints.BOTH;
		gbc_textPane1.gridx = 0;
		gbc_textPane1.gridy = 1;
		panel2.add(textPane1, gbc_textPane1);
		
		JLabel label = new JLabel("\u53CD\u9762\uFF1A");
		label.setForeground(Color.BLACK);
		label.setFont(new Font("等线 Light", Font.PLAIN, 18));
		label.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.VERTICAL;
		gbc_label.anchor = GridBagConstraints.WEST;
		gbc_label.gridwidth = 2;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 2;
		panel2.add(label, gbc_label);
		
		JTextPane textPane2 = new JTextPane();
		textPane2.setFont(new Font("等线", Font.PLAIN, 20));
		GridBagConstraints gbc_textPane2 = new GridBagConstraints();
		gbc_textPane2.gridwidth = 2;
		gbc_textPane2.insets = new Insets(0, 0, 5, 0);
		gbc_textPane2.fill = GridBagConstraints.BOTH;
		gbc_textPane2.gridx = 0;
		gbc_textPane2.gridy = 3;
		panel2.add(textPane2, gbc_textPane2);
		
		JButton btn_ok = new JButton();
		btn_ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textPane1.getText().length()!=0 && textPane2.getText().length()!=0) {
					try {
						ir.getConnection();
						String tablename = comboBox.getSelectedItem().toString();
						int decknum = (int)ir.Select2Object("select id from deck where name = '"+tablename+"'");
						String date = refFormatNowDate();
						ir.ExecuteSQL("insert into card (FRONT,BACK,CREATTIME,STATE,DECKNUM)"
								+ "values(?,?,?,0,?)", textPane1.getText(),textPane2.getText(),date,decknum);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					//清空留作下一次输入
					textPane1.setText("");
					textPane2.setText("");
				} else {
					JOptionPane.showMessageDialog(null, "请输入内容！");
				}
			}
		});
		btn_ok.setContentAreaFilled(false);
		btn_ok.setIcon(new ImageIcon("img/ok_normal.png"));
		GridBagConstraints gbc_btn_ok = new GridBagConstraints();
		gbc_btn_ok.insets = new Insets(0, 0, 0, 5);
		gbc_btn_ok.gridx = 0;
		gbc_btn_ok.gridy = 4;
		panel2.add(btn_ok, gbc_btn_ok);
		
		JButton btn_cancel = new JButton();
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
			}
		});
		btn_cancel.setContentAreaFilled(false);
		btn_cancel.setIcon(new ImageIcon("img/cancel_normal.png"));
		GridBagConstraints gbc_btn_cancel = new GridBagConstraints();
		gbc_btn_cancel.gridx = 1;
		gbc_btn_cancel.gridy = 4;
		panel2.add(btn_cancel, gbc_btn_cancel);
	}
	
	public String refFormatNowDate() {
		  Date nowTime = new Date(System.currentTimeMillis());
		  SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
		  String retStrFormatNowDate = sdFormatter.format(nowTime);
		  return retStrFormatNowDate;
	}
}
