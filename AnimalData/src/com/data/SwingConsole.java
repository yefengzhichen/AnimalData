package com.data;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *静态执行方法，执行一些frame的默认设置，参考java编程思想一书
 * @author yefengzhichen
 * 2016年7月2日
 */
public class SwingConsole {
	public static void run(final JFrame f, final int width, final int height) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				f.setTitle(f.getClass().getSimpleName());
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setSize(width, height);
				f.setVisible(true);				
			}
		});
	}
}
