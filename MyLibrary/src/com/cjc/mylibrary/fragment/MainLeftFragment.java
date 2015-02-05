package com.cjc.mylibrary.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cjc.mylibrary.adapter.LeftListAdpter;
import com.cjc.mylibrary.entity.Menu;
import com.cjc.mylibrary.menu.lib.SlidingMenu;
import com.cjc.mylibrary.tools.BaseFragment;
import com.cjc.mylibrary.tools.Constants;
import com.cjc.mylibrary.tools.Global;
import com.cjc.mylibrary.tools.SAXPraserHelperMenu;
import com.example.wechatsample.R;

/**
 * 菜单界面
 ***************************************************** 
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>菜单界面</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午2:55:56</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * 
 * @author aa1000777 - Email:aa1000777@qq.com
 ***************************************************** 
 */
public class MainLeftFragment extends BaseFragment {
	private List<Menu> Gmenu;
	private ListView exListView;
	private LeftListAdpter adapter;

	public MainLeftFragment() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.my_main_left, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Gmenu = getChannelList();
		this.initViews();
	}

	private void initViews() {
		View parent = this.getView();
		// 设置图片路径
		exListView = (ListView) parent.findViewById(R.id.list);
		exListView.setDivider(getResources().getDrawable(R.color.divder_color));
		exListView.setDividerHeight(1);
		adapter = new LeftListAdpter(context, Gmenu);
		exListView.setAdapter(adapter);
		exListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 清除数据
				if ("clear".equals(Gmenu.get(position).Name)) {
					Global.menu.toggle();
					boolean flag = deleteDirectory(Constants.pathStr);
					if (flag) {
						System.out.println("成功清除数据！");
					}
				} else {
					// 菜单选种上色
					for (int i = 0; i < parent.getCount(); i++) {
						if (parent.getChildAt(i) != null) {
							parent.getChildAt(i).setBackgroundColor(Color.parseColor("#555555"));
						}
					}
					view.setBackgroundColor(Color.parseColor("#0082CB"));
					view.setSelected(true);
					View footer = view.findViewById(R.id.footer);
					footer.setVisibility(View.GONE);
					Global.menu.toggle();
					Global.menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					ListDataFragment fragment = new ListDataFragment();
					Bundle args = new Bundle();
					args.putString("MenuName", Gmenu.get(position).Name);
					fragment.setArguments(args);
					fragmentManager.beginTransaction().replace(R.id.slidingmenu_content, fragment).commit();
				}
			}
		});
	}

	/**
	 * 
	 ***************************************************** 
	 * 方法简介: xml解析
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 ***************************************************** 
	 */
	private List<Menu> getChannelList() {
		try {
			File f = Environment.getExternalStorageDirectory();// 获取SD卡目录
			// 文件名字
			File fileName = new File(f, "MyLibrary/menus/menu.xml");// 读sdcard文件
			// String fileName = "menus/menu.xml"; // 读assets文件
			// 实例化一个SAXParserFactory对象
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser;
			// 实例化SAXParser对象，创建XMLReader对象，解析器
			parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			// 实例化handler，事件处理器
			SAXPraserHelperMenu helperHandler = new SAXPraserHelperMenu();
			// 解析器注册事件
			xmlReader.setContentHandler(helperHandler);
			// 读取文件流
			// InputStream stream =
			// getResources().getAssets().open(fileName);//读assets文件
			InputStream stream = new FileInputStream(fileName);// 读sdcard文件
			InputSource is = new InputSource(stream);
			// 解析文件
			xmlReader.parse(is);
			return helperHandler.getList();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return Gmenu;
	}

	/**
	 * 
	 ***************************************************** 
	 * @方法简介： 删除目录(包括：目录里的所有文件)
	 * @参数列表： @param dirName
	 * @返回值： boolean
	 ***************************************************** 
	 */

	public static boolean deleteDirectory(String dirName) {
		boolean status;
		SecurityManager checker = new SecurityManager();
		if (!dirName.equals("")) {
			File newPath = new File(dirName);
			System.out.println("文件夹：" + newPath.toString());
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + File.separator + listfile[i].toString());
						checker.checkDelete(deletedFile.toString());
						if (deletedFile.isDirectory()) {
							String[] listfiles = deletedFile.list();
							for (int j = 0; j < listfiles.length; j++) {
								File deletedFiles = new File(deletedFile.toString() + File.separator
										+ listfiles[i].toString());
								System.out.println("文件：" + deletedFiles.toString());
								deletedFiles.delete();
							}
						}
						deletedFile.delete();
					}
					newPath.delete();
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}
			} else {
				status = false;
			}
		} else {
			status = false;
		}
		return status;
	}
}
