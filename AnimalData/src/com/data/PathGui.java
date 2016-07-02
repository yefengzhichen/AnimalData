package com.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;

/**
 * 
 * 表示需要展示的每个节点的位置
 *
 */
class Location{
	int x;
	int y;
	public Location(int x,int y){
		this.x=x;
		this.y=y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}

/**
 * 绘图面板内容处理，包括进行节点位置产生，数据间的绘图连线等
 * @author yefengzhichen
 *
 */
public class PathGui extends JPanel {
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension screensize = tk.getScreenSize();
	private int width = screensize.width - 0;
	private int height = screensize.height - 0;
	private static final Color[] colors = { Color.BLACK, Color.BLUE, Color.GRAY, Color.GREEN, Color.RED };
	// Color.YELLOW,Color.MAGENTA, Color.CYAN,
	private static final int COLORTYPE = 5;
	// 宽：screensize.width
	// 高：screensize.height
	private static final int MAX = 10000;
	private static final int RADIUS = 6;
	private static Random random = new Random(47);
	//子图索引
	int subGraph = 0;
	
	private static Map<Integer, AnimalNode> map = new HashMap<>();
	private static ArrayList<ArrayList<Integer>> arr = new ArrayList<ArrayList<Integer>>();

	public PathGui(Map<Integer, AnimalNode> map, ArrayList<ArrayList<Integer>> arr,int subGraph) {
		// TODO Auto-generated constructor stub
		this.map = map;
		this.arr = arr;
		this.subGraph = subGraph;
	}

	public void setSubGraph(int subGraph) {
		this.subGraph = subGraph;
		// this.inputFlag = false;
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ArrayList<Integer> sub = arr.get(0);
		Map<Integer,Location> graphLoction = new HashMap<>();
		int num = 0;
		for(int id : sub){
			Location loc1;
			if(!graphLoction.containsKey(id)){
				loc1 = getRandLocation(width,height);
				graphLoction.put(id, loc1);
			} else {
				loc1 = graphLoction.get(id);
			}
			AnimalNode an = map.get(id);
			if(an == null){
				continue;
			}
			ArrayList<Integer> neighbors = an.getNeighbors();
			for(int key: neighbors){
				Location loc2;
				if(!graphLoction.containsKey(key)){
					loc2 = getRandLocation(width,height);
					graphLoction.put(key, loc2);
				} else {
					loc2 = graphLoction.get(key);
				}
				//画圆
				drawFillCircle(loc1,g);
				drawEmptyCircle(loc2,g);
				Graphics2D g2 = (Graphics2D) g;
				drawAL(loc1.getX(),loc1.getY(),loc2.getX(),loc2.getY(),g2);			
			}	
			num++;
			if(num>=subGraph){
				break;
			}
		}
		System.out.println("subGraph " + subGraph + " : The graph is shown!");		
	}
	
	public void drawEmptyCircle(Location loc1,Graphics g){
		int x1 = loc1.getX() - RADIUS;
		int y1 = loc1.getY() - RADIUS;
//		int x2 = loc1.getX() + RADIUS;
//		int y2 = loc1.getY() + RADIUS;
		g.drawOval(x1,y1,RADIUS,RADIUS);
//		g.fillOval(x1,y1,RADIUS,RADIUS);
	}
	
	public void drawFillCircle(Location loc1,Graphics g){
		int x1 = loc1.getX() - 2*RADIUS;
		int y1 = loc1.getY() - 2*RADIUS;
//		int x2 = loc1.getX() + RADIUS;
//		int y2 = loc1.getY() + RADIUS;
//		g.drawOval(x1,y1,RADIUS,RADIUS);
		g.fillOval(x1,y1,2*RADIUS,2*RADIUS);
	}
	
	public Location getRandLocation( int width, int height) {

		int	x = width/4 + random.nextInt(width/2);
		int	y = height/4 + random.nextInt(height/2);
		Location loc = new Location(x,y);
		
		return loc;
	}

	public void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2) {

		double H = 10; // 箭头高度
		double L = 4; // 底边的一半
		int x3 = 0;
		int y3 = 0;
		int x4 = 0;
		int y4 = 0;
		double awrad = Math.atan(L / H); // 箭头角度
		double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
		double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
		double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
		double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
		double y_3 = ey - arrXY_1[1];
		double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
		double y_4 = ey - arrXY_2[1];

		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();
		// 画线
		g2.drawLine(sx, sy, ex, ey);
		//
		GeneralPath triangle = new GeneralPath();
		triangle.moveTo(ex, ey);
		triangle.lineTo(x3, y3);
		triangle.lineTo(x4, y4);
		triangle.closePath();
		// 实心箭头
		g2.fill(triangle);
		// 非实心箭头
		// g2.draw(triangle);
	}

	public double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}

}

