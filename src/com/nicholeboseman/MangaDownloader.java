package com.nicholeboseman;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;

public class MangaDownloader extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MangaDownloader frame = new MangaDownloader();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MangaDownloader() {
		MangaPanda mp = new MangaPanda();
		ArrayList<String> titles = mp.getMangaTitles();
		DefaultListModel listModel = new DefaultListModel();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new FlowLayout());
		setContentPane(contentPane);
		CapturePane capturePane = new CapturePane();
		JLabel label = new JLabel("New label");
		contentPane.add(label);
		
		 PrintStream ps = System.out;
         System.setOut(new PrintStream(new StreamCapturer("STDOUT", capturePane, ps)));

		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane, FlowLayout.LEFT);
		
		JList informationList = new JList(listModel);
		informationList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		scrollPane.setViewportView( informationList);
		
		for (String title : titles ) {
			listModel.addElement(title);
		}
		
	}

	 public class CapturePane extends JPanel implements Consumer {

	        private JTextArea output;

	        public CapturePane() {
	            setLayout(new FlowLayout());
	            output = new JTextArea();
	            add(new JScrollPane(output));
	        }

	        @Override
	        public void appendText(final String text) {
	            if (EventQueue.isDispatchThread()) {
	                output.append(text);
	                output.setCaretPosition(output.getText().length());
	            } else {

	                EventQueue.invokeLater(new Runnable() {
	                    @Override
	                    public void run() {
	                        appendText(text);
	                    }
	                });

	            }
	        }        
	    }

	    public interface Consumer {        
	        public void appendText(String text);        
	    }

	    public class StreamCapturer extends OutputStream {

	        private StringBuilder buffer;
	        private String prefix;
	        private Consumer consumer;
	        private PrintStream old;

	        public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
	            this.prefix = prefix;
	            buffer = new StringBuilder(128);
	            buffer.append("[").append(prefix).append("] ");
	            this.old = old;
	            this.consumer = consumer;
	        }

	        @Override
	        public void write(int b) throws IOException {
	            char c = (char) b;
	            String value = Character.toString(c);
	            buffer.append(value);
	            if (value.equals("\n")) {
	                consumer.appendText(buffer.toString());
	                buffer.delete(0, buffer.length());
	                buffer.append("[").append(prefix).append("] ");
	            }
	            old.print(c);
	        }        
	    }    
}


