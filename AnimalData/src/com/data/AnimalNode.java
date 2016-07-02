package com.data;

import java.util.ArrayList;



/**
 * ����Ķ���ڵ㣬��ʾȺ�������е�һ�ද��
 * @author yefengzhichen
 * 2016��7��2��
 */
public class AnimalNode {
	//��ʶ
	private int id;
	//�˶�����ھ�(��һ��)������ָ��Ķ��Ｏ�ϡ�
	private ArrayList<Integer> neighbors = new ArrayList<>();
	//�˶������һ������ָ�����Ķ��Ｏ��(��������ʱδ��)
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
