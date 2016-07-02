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
 * 数据处理整体流程 
 * 1、加载数据，保存到map中
 * 2、计算整个动物数据可以分为多少个子网络，有连接的节点即作为同一个网络
 * 3、计算各个子网络是否是DAG网络（有向无环图）
 * 4、对网络数据进行GUI界面展示。
 * 
 * @author yefengzhichen
 *
 */
public class DataExtract {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataExtract de = new DataExtract();
		String path = "C:\\Users\\Administrator\\Desktop\\animaldata\\test1.txt";
		// 所有节点和其指向节点，每个节点一条记录
		Map<Integer, AnimalNode> map = new HashMap<>();
		// 所有子图，每个子图一个数组表示
		ArrayList<ArrayList<Integer>> arr = new ArrayList<ArrayList<Integer>>();
		// 读取数据
		de.readTxt(path, map);
		// 检测子图，返回子图个数
		int numberGraph = de.detectNumberGraph(map, arr);
		System.out.println("子图个数为：" + numberGraph);
		for (int i = 0; i < arr.size(); ++i) {
			System.out.println("子图 " + i + " 节点个数为：" + arr.get(i).size());
		}
		System.out.println("有出度的节点个数为： " + map.size());
		// 判断每个子图是否是有向无环
		ArrayList<Integer> circlePath = new ArrayList<>();
		boolean isDAG = de.detectDAG(arr, map, circlePath);
		System.out.println("是不是有向无环： " + isDAG);
		// 显示子图连接图
		Path.graphPath(map, arr, 10);
	}

	// 判断是否有向无环
	public boolean detectDAG(ArrayList<ArrayList<Integer>> arr, Map<Integer, AnimalNode> map,
			ArrayList<Integer> circlePath) {
		boolean resu = true;
		ArrayList<AnimalNode> access = new ArrayList<>();
		for (Map.Entry<Integer, AnimalNode> entry : map.entrySet()) {
			int key = entry.getKey();
			// 保存有环时的路径
			circlePath.add(key);
			AnimalNode value = entry.getValue();
			// 保存已访问过的节点
			access.add(value);
			ArrayList<Integer> aa = value.getNeighbors();
			for (int j = 0; j < arr.size(); ++j) {
				int k = aa.get(j);
				circlePath.add(k);
				AnimalNode an = map.get(k);
				if (!process(an, access, value, map, circlePath)) {
					resu = false;
					// System.out.println("不满足的节点为：" + k);
					break;
				}
				circlePath.remove(circlePath.size() - 1);
			}
			if (resu == false) {
				return false;
			}
			// if (i % 10 == 0) {
			// System.out.println("节点个数为：" + i);
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
			System.out.println("有环的路劲长度：" + circlePath.size());
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
				// System.out.println("不满足的节点为：" + k);
				break;
			}
			circlePath.remove(circlePath.size() - 1);
		}
		return resu;
	}

	// 检测子图个数，以及每个子图所有节点
	public int detectNumberGraph(Map<Integer, AnimalNode> map, ArrayList<ArrayList<Integer>> arr) {

		int numberGraph = 0;
		// 节点访问标志
		Map<Integer, Boolean> isdetected = new HashMap<>();
		for (Map.Entry<Integer, AnimalNode> entry : map.entrySet()) {
			ArrayList<Integer> graph = new ArrayList<Integer>();
			int id = entry.getKey();
			// 判断此节点是否已访问，访问则跳过
			if (isdetected.containsKey(id)) {
				continue;
			}
			// 记录此次循环中 节点出现在前面的哪些子图中过
			Set<Integer> set = new HashSet<>();
			graph.add(id);
			// 每个节点的下一个节点
			ArrayList<Integer> nerghbors = entry.getValue().getNeighbors();
			int len = nerghbors.size();
			for (int i = 0; i < len; ++i) {
				getGraph(graph, arr, nerghbors.get(i), map, isdetected, set);
			}
			// 此处是关键
			// 对于set不为空，则需要汇总graph、以及set中的子图为一个子图
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
			// 添加每一个子图的所有元素到
			// int mm = arr.size() - 1;
			// System.out.println("子图 " + mm + " 个数为：" + arr.get(mm).size());
			for (int i = 0; i < graph.size(); ++i) {
				isdetected.put(graph.get(i), true);
			}
		}
		System.out.println("节点个数为：" + isdetected.size());
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

	// 读取文本中的数据，存到map中，每个节点一条记录，保存了其所有指向的节点
	public void readTxt(String path, Map<Integer, AnimalNode> map) {
		try {
			// 加载方式一
			// File file = new File(path);
			// if (file.isFile() && file.exists()) { // 判断文件是否存在
			// InputStreamReader read = new InputStreamReader(new
			// FileInputStream(file));// 考虑到编码格式
			// 加载方式二
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
			System.out.println("提取数据记录条数：" + count);
			// }
		} catch (Exception e) {
			System.out.println("找不到指定的文件");
			e.printStackTrace();
		}
	}

}
