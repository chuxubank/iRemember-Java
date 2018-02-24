package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import models.Card;
import models.DBConnection;
import models.Word;

import javax.swing.JProgressBar;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.SystemColor;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Review extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DBConnection ir;
	private JPanel contentPane;
	private int i=0;
	private List<Card> CardList ;
	private int deckid;
	private JCheckBox chkbx;
	private HttpURLConnection connToSpeech=null;
	private String langFrom="en";
	private JProgressBar progressBar;
	private JLabel lbl1,lbl2;
	private ConnThread connThread=null;
	private boolean know;
	
	
	/**
	 * Create the frame.
	 */
	public Review(int deckid) {
		this.deckid = deckid;
//		添加获取发音链接线程
		connThread = new ConnThread();
		connThread.start();
		
		getCardList();
		setTitle("\u590D\u4E60\u5361\u7247");
		setIconImage(Toolkit.getDefaultToolkit().getImage("img/iRemember.png"));
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 527);
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
		panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		chkbx = new JCheckBox("");
		chkbx.setFont(new Font("等线", Font.PLAIN, 20));
		chkbx.setForeground(Color.WHITE);
		chkbx.setBackground(SystemColor.windowBorder);
		chkbx.setSelected(true);
		panel1.add(chkbx);
		
		JButton btn_pronounce = new JButton("\u53D1\u97F3");
		btn_pronounce.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(connToSpeech!=null){
		       		System.out.println("Got connToSpeech!");
		       		playURL(connToSpeech);
		       		System.out.println("Play Successfully!");
		       	}
				else {
					System.out.println("No connToSpeech!");
				}
			}
		});
		btn_pronounce.setForeground(Color.WHITE);
		btn_pronounce.setContentAreaFilled(false);
		btn_pronounce.setFont(new Font("等线", Font.PLAIN, 20));
		panel1.add(btn_pronounce);
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		contentPane.add(panel2, BorderLayout.CENTER);
		GridBagLayout gbl_panel2 = new GridBagLayout();
		gbl_panel2.columnWidths = new int[]{147, 147};
		gbl_panel2.rowHeights = new int[]{0, 185, 185, 0, 0};
		gbl_panel2.columnWeights = new double[]{1.0, 1.0};
		gbl_panel2.rowWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		panel2.setLayout(gbl_panel2);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setFont(new Font("等线", Font.PLAIN, 20));
		progressBar.setBackground(Color.WHITE);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.anchor = GridBagConstraints.NORTH;
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 2;
		gbc_progressBar.insets = new Insets(0, 0, 5, 0);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 0;
		progressBar.setMaximum(CardList.size());
		panel2.add(progressBar, gbc_progressBar);
		
		lbl1 = new JLabel("");
		ir=new DBConnection();
		String text = null;
		text = CardList.get(0).getFront();
		lbl1.setText(text);
		lbl1.setFont(new Font("等线", Font.PLAIN, 25));
		GridBagConstraints gbc_lbl1 = new GridBagConstraints();
		gbc_lbl1.gridwidth = 2;
		gbc_lbl1.insets = new Insets(0, 0, 5, 0);
		gbc_lbl1.gridx = 0;
		gbc_lbl1.gridy = 1;
		panel2.add(lbl1, gbc_lbl1);
		
		lbl2 = new JLabel("\u5355\u51FB\u663E\u793A\u53CD\u9762");
		lbl2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lbl2.setText(CardList.get(i).getBack());
			}
		});
		lbl2.setFont(new Font("等线", Font.PLAIN, 25));
		GridBagConstraints gbc_lbl2 = new GridBagConstraints();
		gbc_lbl2.gridwidth = 2;
		gbc_lbl2.insets = new Insets(0, 0, 5, 0);
		gbc_lbl2.gridx = 0;
		gbc_lbl2.gridy = 2;
		panel2.add(lbl2, gbc_lbl2);
		
		JButton btn_know = new JButton();
		btn_know.setToolTipText("\u8BA4\u8BC6\u6B64\u5361\u7247");
		btn_know.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ir.getConnection();
					int state = CardList.get(i).getState()+1;
					if(CardList.get(i).getState()<3) {
						ir.ExecuteSQL("update card set state=? where id=?",state,CardList.get(i).getId());
					} else {
						ir.ExecuteSQL("update card set state=? , donetime = ? where id=?",state,refFormatNowDate(),CardList.get(i).getId());
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				know=true;
				showNextCard();
				i++;
				//get connection to GoogleSpeech in new Thread
				connToSpeech=null;
				if(chkbx.isSelected()) {
					connThread = new ConnThread();
					connThread.start();
				}
				i--;
			}
		});
		btn_know.setContentAreaFilled(false);
		btn_know.setIcon(new ImageIcon("img/ok_normal.png"));
		GridBagConstraints gbc_btn_know = new GridBagConstraints();
		gbc_btn_know.insets = new Insets(0, 0, 0, 5);
		gbc_btn_know.gridx = 0;
		gbc_btn_know.gridy = 3;
		panel2.add(btn_know, gbc_btn_know);
		btn_know.setContentAreaFilled(false);
		
		JButton btn_dontknow = new JButton();
		btn_dontknow.setToolTipText("\u4E0D\u8BA4\u8BC6\u6B64\u5361\u7247");
		btn_dontknow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				know=false;
				showNextCard();
				i++;
				//get connection to GoogleSpeech in new Thread
				connToSpeech=null;
				if(chkbx.isSelected()) {
					connThread = new ConnThread();
					connThread.start();
				}
				i--;
			}
		});
		btn_dontknow.setContentAreaFilled(false);
		btn_dontknow.setIcon(new ImageIcon("img/cancel_normal.png"));
		GridBagConstraints gbc_btn_dontknow = new GridBagConstraints();
		gbc_btn_dontknow.gridx = 1;
		gbc_btn_dontknow.gridy = 3;
		panel2.add(btn_dontknow, gbc_btn_dontknow);
		
	}

	private static class PauseThread extends Thread {
		int pause_time;
		public PauseThread(int arg_pause_time){
			pause_time=arg_pause_time;
		}
	    public void run(){ 	
        	try {
				sleep(pause_time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	         	
	    }
	}
	
	
	public void getCardList() {
		CardList = new ArrayList<Card>();
		ResultSet rs = null;
		try {
			ir = new DBConnection();
			PreparedStatement stmt = ir.getConnection().prepareStatement("select * from card where decknum=? and donetime is null;");
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
			JOptionPane.showMessageDialog(this, "No Data");
		}
	}
	
	public String refFormatNowDate() {
		  Date nowTime = new Date(System.currentTimeMillis());
		  SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
		  String retStrFormatNowDate = sdFormatter.format(nowTime);
		  return retStrFormatNowDate;
	}
	
//	Get Connection to Google Speech in the separate Tread

	private class ConnThread extends Thread {
	    public void run(){
			if(chkbx.isSelected()&&CardList.size()>i){
				connToSpeech=getConnToSpeech(CardList.get(i).getFront(),langFrom);
				System.out.println("ConnThread runing");
			}
			else
				System.out.println("out of CardList");
	    }
	}
	
//	get connection to Google speech mp3 stream for word
	
	public HttpURLConnection getConnToSpeech(String word,String langFrom)  { 
		try {
			//	Configure url
			String url = "https://translate.googleapis.com/translate_tts?ie=UTF-8"+
			"&q=" + URLEncoder.encode(word, "UTF-8")+
			"&tl=" + langFrom + 
			"&total=1&idx=0&textlen="+word.length()+
			"&client=gtx";
			System.out.println(url);
			  
			//	set connection
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
		 
		    // get content length 
		    long len = con.getContentLengthLong(); 
		    if(len == -1) 
		      System.out.println("Content length unavailable."); 
		    else 
		      System.out.println("Content-Length: " + len); 
		 
		    if(len != 0) { 
			    return con;
		    } 
		    else { 
		      System.out.println("No content available.");		      
		    } 
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}
	
//	play mp3 stream located at connection
	
	public void playURL(HttpURLConnection con){
	    try {
			InputStream input;
			input = con.getInputStream(); 			    
	        Player playMP3 = new Player(input);
	        playMP3.play();
	        input.close();
	        System.out.println("Playing...");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}		
		catch (JavaLayerException e) {
			e.printStackTrace();
		}	
	}
	
	private void showNextCard() {
		i++;
//		Play word if connection to Google answer exist
       	if(connToSpeech!=null){
       		System.out.println("Got connToSpeech!");
       		if(chkbx.isSelected()) {
       			playURL(connToSpeech);
       			System.out.println("Play URL Successfully!");
       		}
       	}
       	else if(connThread!=null){
       		connThread.interrupt();
       		connThread=null;
       		System.out.println("connThread killed!");
       	}
       	else {
       		System.out.println("No connToSpeech!");
       	}
       	
       	if(!know) {
//       	Pause 1 second
       		this.setEnabled(false);
	    	PauseThread pause_thread = new PauseThread(1000);
	    	pause_thread.start();  
	    	try {
				pause_thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
			this.setEnabled(true);
			System.out.println("Pause 1 second!");
       	}
       	
		progressBar.setValue(i);
		if(i>=CardList.size()) {
			JOptionPane.showMessageDialog(null, "背完了！");
			dispose();
		}
		else {
			lbl2.setText("\u5355\u51FB\u663E\u793A\u53CD\u9762");
			lbl1.setText(CardList.get(i).getFront());
		}
	}
}
