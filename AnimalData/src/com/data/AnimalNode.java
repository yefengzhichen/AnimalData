package com.data;

import java.util.ArrayList;



/**
 * 抽象的动物节点，表示群集网络中的一类动物
 * @author yefengzhichen
 * 2016年7月2日
 */
public class AnimalNode {
	//标识
	private int id;
	//此动物的邻居(下一级)，即它指向的动物集合。
	private ArrayList<Integer> neighbors = new ArrayList<>();
	//此动物的上一级，即指向它的动物集合(代码中暂时未用)
	private ArrayList<Integer> pre = new ArrayList<>();
	
	
	public AnimalNode(int id) {
		this.id = id;
	}

	public AnimalNode(int id, int next) {
		this.id = id;
		neighbors.add(next);
	}

	public void addNeighbor(int next) {
		neighbors.add(next);
	}

	public ArrayList<Integer> getNeighbors() {
		return neighbors;
	}
	
	public ArrayList<Integer> getPre() {
		return pre;
	}

	public void setPre(ArrayList<Integer> pre) {
		this.pre = pre;
	}
}
