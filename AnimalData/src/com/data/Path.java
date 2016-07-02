package com.data;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static com.data.SwingConsole.*;



/**
 * GUI����Frame,���ý���Ԫ�ؼ��������
 * @author yefengzhichen
 *
 */
public class Path extends JFrame {

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	JPanel textPanel = new JPanel();
	private JLabel subGraphLabel = new JLabel("ָ���ڵ����");
	private JLabel idDAGLabel = new JLabel("�Ƿ��������޻�");
//	private JLabel userIndexLabel = new JLabel("ָ����ͼ����");
	private JTextField subGraphText = new JTextField(4);
	private JTextField idDAGText = new JTextField(4);
//	private JTextField userIndexText = new JTextField(20);
	private int randomThrow = 0;

	private static Map<Integer, AnimalNode> map = new HashMap<>();
	private static ArrayList<ArrayList<Integer>> arr = new ArrayList<ArrayList<Integer>>();
	private int subGraph = 0;
	
	public Path(Map<Integer, AnimalNode> map, ArrayList<ArrayList<Integer>> arr,int subGraph) {
		this.map = map;
		this.arr = arr;
		this.subGraph = subGraph;
		boolean isDAG = false;	
		PathGui pathGui = new PathGui(map, arr, subGraph);		
		add(pathGui);
		subGraphText.setText("" + subGraph);
		subGraphText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String user = ((JTextField) e.getSource()).getText();
				int sub = Integer.valueOf(user);
				pathGui.setSubGraph(sub);
			}
		});
		idDAGText.setText("false");	
		textPanel.setLayout(new FlowLayout());
		textPanel.add(subGraphLabel);
		textPanel.add(subGraphText);
		textPanel.add(idDAGLabel);
		textPanel.add(idDAGText);	
		add(BorderLayout.SOUTH, textPanel);
	}
	
	public static void graphPath(Map<Integer, AnimalNode> map, ArrayList<ArrayList<Integer>> arr,int subGraph) {
		Dimension screensize = tk.getScreenSize();
		int width = screensize.width - 100;
		int height = screensize.height - 50;
		
		run(new Path(map,arr,subGraph), width, height);
	}
	
}
