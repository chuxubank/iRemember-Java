package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import models.Deck;
import models.Word;
import models.Card;
import models.DBConnection;
import javax.swing.JScrollPane;
import java.awt.event.WindowFocusListener;
import java.io.FileOutputStream;
import java.awt.event.WindowEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private List<Deck> DeckList;
	private DBConnection ir = null;
	private JPanel contentPane;
	private JTable tb_deck;
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
				 refreshtable();
			}
			public void windowLostFocus(WindowEvent arg0) {
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/iRemember.png"));
		setTitle("iRemember");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 535, 375);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// ʹ���ھ��п��ϲ�
		Toolkit kit = Toolkit.getDefaultToolkit(); // ���幤�߰�
		Dimension screenSize = kit.getScreenSize(); // ��ȡ��Ļ�ĳߴ�
		int screenWidth = screenSize.width / 2; // ��ȡ��Ļ�Ŀ�
		int screenHeight = screenSize.height / 2; // ��ȡ��Ļ�ĸ�
		int height = this.getHeight();
		int width = this.getWidth();
		setLocation(screenWidth - width / 2, screenHeight - height / 2);
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(SystemColor.windowBorder);
		panel1.setBorder(null);
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		contentPane.add(panel1,BorderLayout.NORTH);
		
		JButton btn_newcard = new JButton("\u65B0\u5361");
		btn_newcard.setEnabled(false);
		btn_newcard.setForeground(Color.WHITE);
		btn_newcard.setContentAreaFilled(false);
		btn_newcard.setFont(new Font("����", Font.PLAIN, 20));
		btn_newcard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				btn_newcard.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(btn_newcard.getForeground()!=SystemColor.activeCaption)
					btn_newcard.setForeground(Color.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btn_newcard.setForeground(SystemColor.activeCaption);
				refreshtable();
				if(DeckList.isEmpty())
					JOptionPane.showMessageDialog(null, "���ȴ���һ�����飡");
				else {
					new Creat();
				}
			}
		});
		panel1.add(btn_newcard);
		
		JButton btn_show = new JButton("\u6D4F\u89C8");
		btn_show.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btn_show.setForeground(SystemColor.activeCaption);
				new CardListView();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				btn_show.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(btn_show.getForeground()!=SystemColor.activeCaption)
					btn_show.setForeground(Color.WHITE);
			}
		});
		btn_show.setForeground(Color.WHITE);
		btn_show.setFont(new Font("����", Font.PLAIN, 20));
		btn_show.setEnabled(false);
		btn_show.setContentAreaFilled(false);
		btn_show.setBackground(Color.DARK_GRAY);
		panel1.add(btn_show);

		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem mntm_clear = new JMenuItem("\u6E05\u7A7A\u6570\u636E\u5E93");
		mntm_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ir = new DBConnection();
				ir.ExecuteSQL("drop table deck");
				ir.ExecuteSQL("drop table card");
			}
		});
		popupMenu.add(mntm_clear);
		JMenuItem mntm_fileout = new JMenuItem("\u5BFC\u51FA\u6240\u6709\u751F\u8BCD\u5230Excel");
		mntm_fileout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				creatExcel();
			}
		});
		popupMenu.add(mntm_fileout);
		
		
		JButton btn_setup = new JButton("\u8BBE\u7F6E");
		btn_setup.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btn_setup.setForeground(SystemColor.activeCaption);
				popupMenu.show(btn_setup, 5, btn_setup.getHeight()+5);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				btn_setup.setForeground(Color.CYAN);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(btn_setup.getForeground()!=SystemColor.activeCaption)
					btn_setup.setForeground(Color.WHITE);
			}
		});
		
		btn_setup.setForeground(Color.WHITE);
		btn_setup.setFont(new Font("����", Font.PLAIN, 20));
		btn_setup.setEnabled(false);
		btn_setup.setContentAreaFilled(false);
		btn_setup.setBackground(Color.DARK_GRAY);
		panel1.add(btn_setup);
		
		
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		contentPane.add(panel2, BorderLayout.CENTER);
		GridBagLayout gbl_panel2 = new GridBagLayout();
		gbl_panel2.columnWidths = new int[]{140, 140, 140, 140};
		gbl_panel2.rowHeights = new int[]{0, 0, 0};
		gbl_panel2.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0};
		gbl_panel2.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panel2.setLayout(gbl_panel2);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel2.add(scrollPane, gbc_scrollPane);
		
		tb_deck = new JTable();
		tb_deck.setFont(new Font("����", Font.PLAIN, 20));
		tb_deck.setRowHeight(25);
		scrollPane.setViewportView(tb_deck);
		
		JButton btn_newdeck = new JButton();
		btn_newdeck.setToolTipText("\u65B0\u5EFA\u5361\u7EC4");
		btn_newdeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				input DeckName
				String DeckName = JOptionPane.showInputDialog("�����뿨������");
				if((DeckName == null) || (DeckName.length() == 0)){
					return;
				}
				try {
					ir = new DBConnection();
					PreparedStatement stmt = ir.getConnection().prepareStatement("select * from deck where name=?");
					stmt.setObject(1, DeckName);
					ResultSet rs = stmt.executeQuery();
					if(rs.next()==false) {
						ir.ExecuteSQL("insert into deck (name) values(?)", DeckName);
					}
					else {
						JOptionPane.showMessageDialog(null,"�Ѵ��ڴ˿��飡");
					}
					ir.close();
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btn_newdeck.setContentAreaFilled(false);
		btn_newdeck.setIcon(new ImageIcon("img/plus.png"));
		GridBagConstraints gbc_btn_newdeck = new GridBagConstraints();
		gbc_btn_newdeck.insets = new Insets(0, 0, 0, 5);
		gbc_btn_newdeck.gridx = 0;
		gbc_btn_newdeck.gridy = 1;
		panel2.add(btn_newdeck, gbc_btn_newdeck);
		
		JButton btn_renamedeck = new JButton();
		btn_renamedeck.setToolTipText("\u91CD\u547D\u540D\u5361\u7EC4");
		btn_renamedeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tb_deck.getSelectedRow()!=-1) {
					String DeckName = JOptionPane.showInputDialog("�������¿�������");
					if((DeckName == null) || (DeckName.length() == 0)){
						return;
					}
					try {
						ir = new DBConnection();
						PreparedStatement stmt = ir.getConnection().prepareStatement("select * from deck where name=?");
						stmt.setObject(1, DeckName);
						ResultSet rs = stmt.executeQuery();
						if(rs.next()==false) {
							ir.ExecuteSQL("UPDATE Deck SET name = ? WHERE id = ?",DeckName,tb_deck.getValueAt(tb_deck.getSelectedRow(), 0));
						}
						else {
							JOptionPane.showMessageDialog(null,"�Ѵ��ڴ˿��飡");
						}
						ir.close();
					} catch (HeadlessException e1) {
						e1.printStackTrace();
					} catch (SQLException e1) {
						e1.printStackTrace();
					} 
				} else {
					JOptionPane.showMessageDialog(null,"����ѡ��һ�����飡");
				}
			}
		});
		btn_renamedeck.setContentAreaFilled(false);
		btn_renamedeck.setIcon(new ImageIcon("img/edit.png"));
		GridBagConstraints gbc_btn_renamedeck = new GridBagConstraints();
		gbc_btn_renamedeck.insets = new Insets(0, 0, 0, 5);
		gbc_btn_renamedeck.gridx = 1;
		gbc_btn_renamedeck.gridy = 1;
		panel2.add(btn_renamedeck, gbc_btn_renamedeck);
		
		JButton btn_delete = new JButton();
		btn_delete.setToolTipText("\u5220\u9664\u5361\u7EC4");
		btn_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tb_deck.getSelectedRow()!=-1) {
					if(JOptionPane.showConfirmDialog(null, "ȷ��ɾ�����飿")==0) {
						ir=new DBConnection();
						ir.ExecuteSQL("delete from Deck WHERE id = ?",tb_deck.getValueAt(tb_deck.getSelectedRow(), 0));
					}
				} else {
					JOptionPane.showMessageDialog(null,"����ѡ��һ�����飡");
				}
				
			}
		});
		btn_delete.setContentAreaFilled(false);
		btn_delete.setIcon(new ImageIcon("img/minus.png"));
		GridBagConstraints gbc_btn_delete = new GridBagConstraints();
		gbc_btn_delete.insets = new Insets(0, 0, 0, 5);
		gbc_btn_delete.gridx = 2;
		gbc_btn_delete.gridy = 1;
		panel2.add(btn_delete, gbc_btn_delete);
		
		JButton btn_start = new JButton();
		btn_start.setToolTipText("\u5F00\u59CB\u5B66\u4E60");
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(tb_deck.getSelectedRow()!=-1) {
					int i = tb_deck.getSelectedRow();
					if(DeckList.get(i).getNumofcard()==0)
						JOptionPane.showMessageDialog(null,"�ÿ��黹û�п�Ƭ��");
					else if(DeckList.get(i).getDonenumofcard()==DeckList.get(i).getNumofcard())
						JOptionPane.showMessageDialog(null,"�ÿ����Ѿ���������");
					else {
						new Review(DeckList.get(i).getId());
					}
//						int row = table.getSelectedRow();  
//					System.out.println(table.getValueAt(row, 0)); Ϊ�η��ص���Object����...
				} else {
					JOptionPane.showMessageDialog(null,"����ѡ��һ�����飡");
				}
			}
		});
		btn_start.setContentAreaFilled(false);
		btn_start.setIcon(new ImageIcon("img/play.png"));
		GridBagConstraints gbc_btn_start = new GridBagConstraints();
		gbc_btn_start.gridx = 3;
		gbc_btn_start.gridy = 1;
		panel2.add(btn_start, gbc_btn_start);
		
	}
	
	
	public void refreshtable() {
		DeckList = new ArrayList<Deck>();
		ResultSet rs = null;
		int i=0;
		String sql = "select * from Deck;";
		try {
			ir = new DBConnection();
			Statement stmt = ir.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
//			rs = ir.ExecuteQuery(sql); ��֪��Ϊ����ôдrs���ǿռ���
			while (rs.next()) {
				Deck tempd = new Deck(rs.getInt("id"),rs.getString("name"),-1,-1);
				DeckList.add(tempd);
			}
			rs.close();
			ir.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//��ѯ����ɺ�δ��ɵĿ�Ƭ����
		while(i<DeckList.size()) {
			int deckid = DeckList.get(i).getId();
			int numofcards = -1,donenumofcards = -1;
			try {
				ir = new DBConnection();
				numofcards = (int)ir.Select2Object("select count(*) from card where decknum=?",deckid);
				donenumofcards = (int)ir.Select2Object("select count(donetime) from card where decknum=?",deckid);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DeckList.get(i).setNumofcard(numofcards);
			DeckList.get(i).setDonenumofcard(donenumofcards);
			i++;
		}
		
		if(!DeckList.isEmpty()) {
			Vector<Vector<Object>> data = new Vector<>();
			for(Deck tempd : DeckList) {
				Vector<Object> record = new Vector<>();
				record.add(tempd.getId());
				record.add(tempd.getName());
				record.add(tempd.getNumofcard()-tempd.getDonenumofcard());
				record.add(tempd.getDonenumofcard());
				data.add(record);
			}
			Vector<String> columns = new Vector<>();
			columns.addAll(Arrays.<String> asList("���", "����" ,"δ����" ,"�����"));
			tb_deck.setModel(new DefaultTableModel(data, columns){
				private static final long serialVersionUID = 1L;
				//���ɱ༭����ѡ��
	            public boolean isCellEditable(int row, int column)
	            {
	                return false;
	            }
	            });
		} else {
			DefaultTableModel t = new DefaultTableModel();
			t.setRowCount(0);
			tb_deck.setModel(t);
			System.out.println("No Data");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void creatExcel() {
		List<Card> CardList = new ArrayList<Card>();
		ResultSet rs = null;
		try {
			ir = new DBConnection();
			Statement stmt = ir.getConnection().createStatement();
			rs = stmt.executeQuery("select * from card");
			while (rs.next()) {
				Word tempw = new Word(rs.getInt("id"),rs.getString("creattime"),rs.getString("front"),rs.getString("back"),rs.getInt("state"),rs.getString("donetime"),rs.getInt("decknum"),null);
				tempw.setDeckname((String)ir.Select2Object("select name from deck where id = ?", tempw.getDecknum()));
				CardList.add(tempw);
			}
			rs.close();
			ir.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(CardList.isEmpty()) {
			System.out.println("No Data");
		}
		
		// ��һ��������һ��webbook����Ӧһ��Excel�ļ�  
        @SuppressWarnings("resource")
		HSSFWorkbook wb = new HSSFWorkbook();  
        // �ڶ�������webbook�����һ��sheet,��ӦExcel�ļ��е�sheet  
        HSSFSheet sheet = wb.createSheet("����δ��ɿ�Ƭ�б�");  
        // ����������sheet����ӱ�ͷ��0��
        HSSFRow row = sheet.createRow((int) 0);  
        
        
        
        // ���Ĳ���������Ԫ�񣬲�����ֵ��ͷ ���ñ�ͷ����  
        HSSFCellStyle style = wb.createCellStyle();  
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // ����һ�����и�ʽ  //����û��Ч��
  
        HSSFCell cell = row.createCell(0);  
        cell.setCellValue("���");  
        cell.setCellStyle(style);  
        cell = row.createCell(1);  
        cell.setCellValue("����");  
        cell.setCellStyle(style);  
        cell = row.createCell(2);  
        cell.setCellValue("����");  
        cell.setCellStyle(style);  
        cell = row.createCell(3);  
        cell.setCellValue("��������");  
        cell.setCellStyle(style);  
        cell = row.createCell(4);  
        cell.setCellValue("���״̬");  
        cell.setCellStyle(style);  
        cell = row.createCell(5);  
        cell.setCellValue("���ڿ���");  
        cell.setCellStyle(style); 
        
        // ���岽��д��ʵ������
        for (int i = 0; i < CardList.size(); i++)  
        {  
            row = sheet.createRow((int) i + 1);  
            Card c = (Card) CardList.get(i);  
            // ������Ԫ�񣬲�����ֵ
            row.createCell(0).setCellValue((double)c.getId());  
            row.createCell(1).setCellValue(c.getFront());  
            row.createCell(2).setCellValue(c.getBack()); 
            row.createCell(3).setCellValue(c.getCreattime());
            row.createCell(4).setCellValue((double)c.getState()); 
            row.createCell(5).setCellValue(c.getDeckname()); 
        }  
        // �����������ļ��浽ָ��λ��  
        try  
        {  
            FileOutputStream fout = new FileOutputStream("undonecards.xls");  
            wb.write(fout);
            fout.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
    }  
	
}
