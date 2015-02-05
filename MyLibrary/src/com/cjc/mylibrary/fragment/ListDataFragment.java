package com.cjc.mylibrary.fragment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.util.EncodingUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cjc.mylibrary.adapter.ListDataAdapter;
import com.cjc.mylibrary.entity.Xml;
import com.cjc.mylibrary.tools.BaseFragment;
import com.cjc.mylibrary.tools.Constants;
import com.cjc.mylibrary.tools.SAXPraserHelperContent;
import com.example.wechatsample.R;

/**
 * 列表页面
 ***************************************************** 
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>列表页面</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午2:55:36</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * 
 * @author aa1000777 - Email:aa1000777@qq.com
 ***************************************************** 
 */
public class ListDataFragment extends BaseFragment {
	private ListView list_data_fragment_list_LV;
	private ListDataAdapter adapter;
	private String MenuName = "";
	private String Name = "";
	private WebView contentWebView = null;
	private JSKit js;
	private List<String> listdata;
	private File file;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_data_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		MenuName = bundle.getString("MenuName");
		listdata = deepFile(context);
		initViews();
		initWebHtml();
	}

	private void initWebHtml() {
		View view = this.getView();
		// 实例化js对象
		js = new JSKit();
		contentWebView = (WebView) view.findViewById(R.id.web_html_webView);
		// 设置参数
		contentWebView.getSettings().setBuiltInZoomControls(true);
		// 内容的渲染需要webviewChromClient去实现，设置webviewChromClient基类，解决js中alert不弹出的问题和其他内容渲染问题
		contentWebView.setWebChromeClient(new WebChromeClient());
		// 启用javascript
		contentWebView.getSettings().setJavaScriptEnabled(true);
		// 把js绑定到全局的myjs上，myjs的作用域是全局的，初始化后可随处使用
		contentWebView.addJavascriptInterface(js, "myjs");
		// 从assets目录下面的加载html
		contentWebView.loadUrl("file:///android_asset/CreateHtml.html");
	}

	private void initViews() {
		// TODO Auto-generated method stub
		View view = this.getView();
		list_data_fragment_list_LV = (ListView) view.findViewById(R.id.list_data_fragment_list_LV);
		adapter = new ListDataAdapter(context, listdata);
		list_data_fragment_list_LV.setAdapter(adapter);
		list_data_fragment_list_LV.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Name = listdata.get(position);
				getData();
			}
		});
	}

	private void FragmentReplace(String name) {
		WebDataFragment fragment = new WebDataFragment();
		Bundle args = new Bundle();
		args.putString("MenuName", MenuName);
		args.putString("FileName", name);
		fragment.setArguments(args);
		fragmentManager.beginTransaction().addToBackStack(null).replace(R.id.slidingmenu_content, fragment).commit();
	}

	private void getData() {
		String fileName = MenuName + "/" + Name + ".html"; // 文件名字
		file = new File(Constants.pathStr + fileName);
		if (!file.exists()) {
			System.out.println("文件不存在");
			// 不存在先创建文件夹
			File pathcontent = new File(Constants.pathStr);
			if (pathcontent.mkdir()) {
				System.out.println("创建成功");
			} else {
				System.out.println("创建失败");
			}
			File path = new File(Constants.pathStr + MenuName);
			if (path.mkdir()) {
				System.out.println("创建成功");
			} else {
				System.out.println("创建失败");
			}
			// 取得assets中的文件数据
			String str = getDataText(Name);
			str = str.replaceAll("\r\n", "&#10;");// 去回车
			System.out.println("去掉回车后：" + str);
			// 创建并编译文件
			contentWebView.loadUrl("javascript:render('" + str + "')");
		} else {
			FragmentReplace(Name);
		}
	}

	// /data/data/com.example.wechatsample/content/activity/强制设置Actiity的显示方向为垂直方向.html

	/**
	 * 保存文件
	 */
	public class JSKit {
		public void showMsg(String msg) {
			System.out.println("回抛的值：" + msg);
			try {
				ByteArrayInputStream stream = new ByteArrayInputStream(msg.getBytes());
				// 用输出流写到SDcard上面
				FileOutputStream fos = new FileOutputStream(file);
				// 创建byte数组 用于1KB写一次
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = stream.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				// 最后关闭就可以了
				fos.flush();
				fos.close();
				stream.close();
				System.out.println("ok");
				FragmentReplace(Name);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 
	 ***************************************************** 
	 * 方法简介: 读取Assets中的txt代码文件
	 * 
	 * @return
	 ***************************************************** 
	 */
	private String getDataText(String name) {
		// TODO Auto-generated method stub
		File f = Environment.getExternalStorageDirectory();// 获取SD卡目录
		// 文件名字
		// String fileName = "content/" + MenuName + "/" + name + ".txt"; //
		// 读assets文件
		File fileName = new File(f, "MyLibrary/content/" + MenuName + "/" + name + ".txt");// 读sdcard文件
		String res = "";
		try {
			// 得到资源中的asset数据流
			// InputStream in =
			// getResources().getAssets().open(fileName);//读assets文件
			InputStream in = new FileInputStream(fileName);// 读sdcard文件
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			in.close();
			res = EncodingUtils.getString(buffer, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 ***************************************************** 
	 * 方法简介: 读取assets中html文件夹下的所有文件的文件名
	 ***************************************************** 
	 */
	public List<String> deepFile(Context context) {
		List<String> list = new ArrayList<String>();
		File f = Environment.getExternalStorageDirectory();// 获取SD卡目录
		File fileName = new File(f, "MyLibrary/content/" + MenuName);// 读sdcard文件
		String[] str = fileName.list();
		for (String string : str) {
			// 取得带后缀的文件名中的点的位置
			int i = string.indexOf(".") != -1 ? string.indexOf(".") : string.length();
			// 截取文件名并加入到List中
			list.add(string.substring(0, i));
			System.out.println("文件名：" + string.substring(0, i));
		}
		return list;
	}

}
