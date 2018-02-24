package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import models.Card;
import models.DBConnection;
import models.Word;

import java.awt.SystemColor;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CardListView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int deckid=1;
	private List<Card> CardList;
	private DBConnection ir;
	private JPanel contentPane;
	private JTable table;
	/**
	 * Create the frame.
	 */
	public CardListView() {
		setTitle("\u6D4F\u89C8\u5361\u7247");
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/iRemember.png"));
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 768, 493);
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
		panel1.setBorder(null);
		panel1.setBackground(SystemColor.windowBorder);
		contentPane.add(panel1, BorderLayout.NORTH);
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel label = new JLabel("\u9009\u62E9\u5361\u7EC4\uFF1A");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("等线", Font.PLAIN, 20));
		panel1.add(label);
		
		Vector<String> data = new Vector<String>();
		ResultSet rs = null;
		String sql = "select name from Deck;";
		try {
			ir = new DBConnection();
			Statement stmt = ir.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				data.addElement(rs.getString("name"));
			}
			rs.close();
			ir.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JComboBox<String> comboBox = new JComboBox<String>(data);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ir = new DBConnection();
					deckid=(int)ir.Select2Object("select id from deck where name ='"+comboBox.getSelectedItem().toString()+"'");
					refreshtable();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		comboBox.setFont(new Font("等线 Light", Font.PLAIN, 18));
		panel1.add(comboBox);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		contentPane.add(panel2, BorderLayout.CENTER);
		GridBagLayout gbl_panel2 = new GridBagLayout();
		gbl_panel2.columnWidths = new int[]{289, 289, 0};
		gbl_panel2.rowHeights = new int[]{0, 0, 0};
		gbl_panel2.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel2.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panel2.setLayout(gbl_panel2);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel2.add(scrollPane, gbc_scrollPane);
		
		table = new JTable();
		table.setFont(new Font("等线", Font.PLAIN, 20));
		table.setRowHeight(25);
		scrollPane.setViewportView(table);
		refreshtable();

//		JButton btn_save = new JButton();
//		btn_save.setContentAreaFilled(false);
//		btn_save.setIcon(new ImageIcon("img/ok_normal.png"));
//		GridBagConstraints gbc_btn_save = new GridBagConstraints();
//		gbc_btn_save.insets = new Insets(0, 0, 0, 5);
//		gbc_btn_save.gridx = 0;
//		gbc_btn_save.gridy = 1;
//		panel2.add(btn_save, gbc_btn_save);
		
		JButton btn_delete = new JButton();
		btn_delete.setToolTipText("\u5220\u9664\u5355\u8BCD");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow()!=-1) {
					int i = table.getSelectedRow();
					int id = CardList.get(i).getId();
					
					ir = new DBConnection();
					ir.ExecuteSQL("delete from card where id=?", id);
					refreshtable();
				} else {
					JOptionPane.showMessageDialog(null,"请先选择一张卡！");
				}
			}
		});
		btn_delete.setContentAreaFilled(false);
		btn_delete.setIcon(new ImageIcon("img/minus.png"));
		GridBagConstraints gbc_btn_delete = new GridBagConstraints();
		gbc_btn_delete.insets = new Insets(0, 0, 0, 5);
		gbc_btn_delete.gridx = 0;
		gbc_btn_delete.gridy = 1;
		panel2.add(btn_delete, gbc_btn_delete);
		
		JButton btn_cancel = new JButton();
		btn_cancel.setToolTipText("\u53D6\u6D88");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btn_cancel.setContentAreaFilled(false);
		btn_cancel.setIcon(new ImageIcon("img/cancel_normal.png"));
		GridBagConstraints gbc_btn_cancel = new GridBagConstraints();
		gbc_btn_cancel.gridx = 1;
		gbc_btn_cancel.gridy = 1;
		panel2.add(btn_cancel, gbc_btn_cancel);
	}

	public void refreshtable() {
		getCardList();
		if(!CardList.isEmpty()) {
			Vector<Vector<Object>> data = new Vector<>();
			for(Card tempc : CardList) {
				Vector<Object> record = new Vector<>();
				record.add(tempc.getId());
				record.add(tempc.getFront());
				record.add(tempc.getBack());
				record.add(tempc.getCreattime());
				record.add(tempc.getDonetime());
				record.add(tempc.getState());
				data.add(record);
			}
			Vector<String> columns = new Vector<>();
			columns.addAll(Arrays.<String> asList("编号", "正面" ,"反面" ,"创建时间","完成时间","状态"));
			table.setModel(new DefaultTableModel(data, columns));
		} else {
			System.out.println("No Data");
		}
	}
	
	public void getCardList() {
		CardList = new ArrayList<Card>();
		ResultSet rs = null;
		try {
			ir = new DBConnection();
			PreparedStatement stmt = ir.getConnection().prepareStatement("select * from card where decknum=?");
			stmt.setObject(1, deckid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Word tempw = new Word(rs.getInt("id"),rs.getString("creattime"),rs.getString("front"),rs.getString("back"),rs.getInt("state"),rs.getString("donetime"),rs.getInt("decknum"),null);
				CardList.add(tempw);
			}
			rs.close();
			ir.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(CardList.isEmpty()) {
			DefaultTableModel t = new DefaultTableModel();
			t.setRowCount(0);
			table.setModel(t);
			System.out.println("No Data");
		}
	}
}
