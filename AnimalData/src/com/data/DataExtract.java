package com.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import com.data.*;

/**
 * ���ݴ����������� 
 * 1���������ݣ����浽map��
 * 2�����������������ݿ��Է�Ϊ���ٸ������磬�����ӵĽڵ㼴��Ϊͬһ������
 * 3����������������Ƿ���DAG���磨�����޻�ͼ��
 * 4�����������ݽ���GUI����չʾ��
 * 
 * @author yefengzhichen
 *
 */
public class DataExtract {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataExtract de = new DataExtract();
		String path = "C:\\Users\\Administrator\\Desktop\\animaldata\\test1.txt";
		// ���нڵ����ָ��ڵ㣬ÿ���ڵ�һ����¼
		Map<Integer, AnimalNode> map = new HashMap<>();
		// ������ͼ��ÿ����ͼһ�������ʾ
		ArrayList<ArrayList<Integer>> arr = new ArrayList<ArrayList<Integer>>();
		// ��ȡ����
		de.readTxt(path, map);
		// �����ͼ��������ͼ����
		int numberGraph = de.detectNumberGraph(map, arr);
		System.out.println("��ͼ����Ϊ��" + numberGraph);
		for (int i = 0; i < arr.size(); ++i) {
			System.out.println("��ͼ " + i + " �ڵ����Ϊ��" + arr.get(i).size());
		}
		System.out.println("�г��ȵĽڵ����Ϊ�� " + map.size());
		// �ж�ÿ����ͼ�Ƿ��������޻�
		ArrayList<Integer> circlePath = new ArrayList<>();
		boolean isDAG = de.detectDAG(arr, map, circlePath);
		System.out.println("�ǲ��������޻��� " + isDAG);
		// ��ʾ��ͼ����ͼ
		Path.graphPath(map, arr, 10);
	}

	// �ж��Ƿ������޻�
	public boolean detectDAG(ArrayList<ArrayList<Integer>> arr, Map<Integer, AnimalNode> map,
			ArrayList<Integer> circlePath) {
		boolean resu = true;
		ArrayList<AnimalNode> access = new ArrayList<>();
		for (Map.Entry<Integer, AnimalNode> entry : map.entrySet()) {
			int key = entry.getKey();
			// �����л�ʱ��·��
			circlePath.add(key);
			AnimalNode value = entry.getValue();
			// �����ѷ��ʹ��Ľڵ�
			access.add(value);
			ArrayList<Integer> aa = value.getNeighbors();
			for (int j = 0; j < arr.size(); ++j) {
				int k = aa.get(j);
				circlePath.add(k);
				AnimalNode an = map.get(k);
				if (!process(an, access, value, map, circlePath)) {
					resu = false;
					// System.out.println("������Ľڵ�Ϊ��" + k);
					break;
				}
				circlePath.remove(circlePath.size() - 1);
			}
			if (resu == false) {
				return false;
			}
			// if (i % 10 == 0) {
			// System.out.println("�ڵ����Ϊ��" + i);
			// }
		}
		return true;
	}

	public boolean process(AnimalNode a, ArrayList<AnimalNode> access, AnimalNode b, Map<Integer, AnimalNode> map,
			ArrayList<Integer> circlePath) {
		if (a == null) {
			return true;
		}
		if (a == b) {
			System.out.println("�л���·�����ȣ�" + circlePath.size());
			System.out.println(circlePath);
			return false;
		}
		if (access.indexOf(a) != -1) {
			return true;
		}
		boolean resu = true;
		access.add(a);
		ArrayList<Integer> arr = a.getNeighbors();
		for (int i = 0; i < arr.size(); ++i) {
			int k = arr.get(i);
			circlePath.add(k);
			AnimalNode an = map.get(k);
			if (an != null && !process(an, access, b, map, circlePath)) {
				resu = false;
				// System.out.println("������Ľڵ�Ϊ��" + k);
				break;
			}
			circlePath.remove(circlePath.size() - 1);
		}
		return resu;
	}

	// �����ͼ�������Լ�ÿ����ͼ���нڵ�
	public int detectNumberGraph(Map<Integer, AnimalNode> map, ArrayList<ArrayList<Integer>> arr) {

		int numberGraph = 0;
		// �ڵ���ʱ�־
		Map<Integer, Boolean> isdetected = new HashMap<>();
		for (Map.Entry<Integer, AnimalNode> entry : map.entrySet()) {
			ArrayList<Integer> graph = new ArrayList<Integer>();
			int id = entry.getKey();
			// �жϴ˽ڵ��Ƿ��ѷ��ʣ�����������
			if (isdetected.containsKey(id)) {
				continue;
			}
			// ��¼�˴�ѭ���� �ڵ������ǰ�����Щ��ͼ�й�
			Set<Integer> set = new HashSet<>();
			graph.add(id);
			// ÿ���ڵ����һ���ڵ�
			ArrayList<Integer> nerghbors = entry.getValue().getNeighbors();
			int len = nerghbors.size();
			for (int i = 0; i < len; ++i) {
				getGraph(graph, arr, nerghbors.get(i), map, isdetected, set);
			}
			// �˴��ǹؼ�
			// ����set��Ϊ�գ�����Ҫ����graph���Լ�set�е���ͼΪһ����ͼ
			// System.out.println(set);

			if (set.size() > 0) {
				int[] arrSet = new int[set.size()];
				int length = 0;
				for (int i : set) {
					arrSet[length++] = i;
				}
				int low = arrSet[0];
				for (int i = length - 1; i > 0; --i) {
					int index = arrSet[i];
					ArrayList<Integer> tmp = arr.remove(index);
					for (int j : tmp) {
						arr.get(low).add(j);
					}
				}
				for (int j : graph) {
					arr.get(low).add(j);
				}
			} else {
				arr.add(graph);
			}
			// ���ÿһ����ͼ������Ԫ�ص�
			// int mm = arr.size() - 1;
			// System.out.println("��ͼ " + mm + " ����Ϊ��" + arr.get(mm).size());
			for (int i = 0; i < graph.size(); ++i) {
				isdetected.put(graph.get(i), true);
			}
		}
		System.out.println("�ڵ����Ϊ��" + isdetected.size());
		numberGraph = arr.size();
		return numberGraph;
	}

	public void getGraph(ArrayList<Integer> graph, ArrayList<ArrayList<Integer>> arr, int id,
			Map<Integer, AnimalNode> map, Map<Integer, Boolean> isdetected, Set<Integer> set) {
		if (graph.indexOf(id) != -1) {
			return;
		}
		if (isdetected.containsKey(id)) {
			for (int i = 0; i < arr.size(); ++i) {
				ArrayList<Integer> tmp = arr.get(i);
				if (tmp.contains(id)) {
					set.add(i);
					return;
				}
			}
		}
		graph.add(id);
		AnimalNode an = map.get(id);
		if (an == null) {
			return;
		}
		ArrayList<Integer> nerghbors = an.getNeighbors();
		int len = nerghbors.size();
		for (int i = 0; i < len; ++i) {
			getGraph(graph, arr, nerghbors.get(i), map, isdetected, set);
		}
	}

	// ��ȡ�ı��е����ݣ��浽map�У�ÿ���ڵ�һ����¼��������������ָ��Ľڵ�
	public void readTxt(String path, Map<Integer, AnimalNode> map) {
		try {
			// ���ط�ʽһ
			// File file = new File(path);
			// if (file.isFile() && file.exists()) { // �ж��ļ��Ƿ����
			// InputStreamReader read = new InputStreamReader(new
			// FileInputStream(file));// ���ǵ������ʽ
			// ���ط�ʽ��
			InputStream is = this.getClass().getResourceAsStream("/test1.txt");
			InputStreamReader read = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(read);
			String lineTxt = null;
			int id;
			int next;
			int count = 0;
			while ((lineTxt = br.readLine()) != null) {
				String[] content = lineTxt.split(" ");
				id = Integer.parseInt(content[0]);
				next = Integer.parseInt(content[1]);
				if (map.containsKey(id)) {
					AnimalNode an = map.get(id);
					an.addNeighbor(next);
					map.put(id, an);
				} else {
					AnimalNode an = new AnimalNode(id, next);
					map.put(id, an);
				}
				count++;
			}
			read.close();
			System.out.println("��ȡ���ݼ�¼������" + count);
			// }
		} catch (Exception e) {
			System.out.println("�Ҳ���ָ�����ļ�");
			e.printStackTrace();
		}
	}

}
