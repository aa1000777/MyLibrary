package com.cjc.mylibrary.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cjc.mylibrary.menulibrary.RayMenu;
import com.cjc.mylibrary.tools.BaseFragment;
import com.cjc.mylibrary.tools.Constants;
import com.cjc.mylibrary.tools.Global;
import com.example.wechatsample.R;

/**
 * web代码显示页面
 ***************************************************** 
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>web代码显示页面</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午2:56:11</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * 
 * @author aa1000777 - Email:aa1000777@qq.com
 ***************************************************** 
 */
public class WebDataFragment extends BaseFragment {
	/** 查找cssURL */
	public static final String URL_REG_EXPRESSION = "(https?://)([a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+)+(/*[A-Za-z0-9/\\-_&:?\\+=//.%]*).css";
	/** 查找css文件 */
	public static final String URL_REG_EXPRESSION2 = "(file:///android_asset/css/)(\\w*).css";
	/** 去掉网页中多余的代码 */
	public static final String DIV_REG_EXPRESSION = "(<div class=).*>?</a></span></div>";
	private WebView wView;
	/** 数据 */
	private String data = "";
	/** 文件名 */
	private String fileName = "";
	/** 菜单名 */
	private String MenuName = "";
	/** 用于控件css样式的显示 */
	private int page = 0;
	/** 菜单图片 */
	private static final int[] ITEM_DRAWABLES = { R.drawable.menu_about, R.drawable.menu_normal, R.drawable.menu_save };
	/** css文件 */
	private static final String[] DATA_CSS = { "shCoreDefault.css", "shCoreDjango.css", "shCoreEclipse.css",
			"shCoreEmacs.css", "shCoreFadeToGrey.css", "shCoreMDUltra.css", "shCoreMidnight.css", "shCoreRDark.css" };
	/** css文件对应的名称 */
	private static final String[] DATA_CSS_NAME = { "默认样式", "Django样式", "Eclipse样式", "Emacs样式", "FadeToGrey样式",
			"MDUltra样式", "Midnight样式", "RDark样式" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.web_data_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		MenuName = bundle.getString("MenuName");
		fileName = bundle.getString("FileName");
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		View view = this.getView();
		data = this.getData();
		// 去掉多余代码
		data = data.replaceAll(DIV_REG_EXPRESSION, "");
		// 正则表达式将url替换成本地文件路径
		Pattern p = Pattern.compile(URL_REG_EXPRESSION);
		Matcher m = p.matcher(data);
		while (m.find()) {
			data = data.replaceAll(m.group(), "file:///android_asset/css/" + DATA_CSS[page]);
		}
		wView = (WebView) view.findViewById(R.id.web_data_webView);
		wView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
		// 加载菜单并设置事件
		RayMenu rayMenu = (RayMenu) view.findViewById(R.id.ray_menu);
		final int itemCount = ITEM_DRAWABLES.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(context);
			item.setImageResource(ITEM_DRAWABLES[i]);
			final int position = i;
			rayMenu.addItem(item, new OnClickListener() {
				@Override
				public void onClick(View v) {

					switch (position) {
					case 0:
						openFileTxt(getTxtIntent(getDataText()));
						break;
					case 1:
						page++;
						if (page >= DATA_CSS.length) {
							page = 0;
						}
						// 根据page来跟改css文件
						if ("".equals(data)) {
							data = getData();
							data = data.replaceAll(DIV_REG_EXPRESSION, "");
							Pattern p = Pattern.compile(URL_REG_EXPRESSION);
							Matcher m = p.matcher(data);
							while (m.find()) {
								data = data.replaceAll(m.group(), "file:///android_asset/css/" + DATA_CSS[page]);
							}
						} else {
							Pattern p = Pattern.compile(URL_REG_EXPRESSION2);
							Matcher m = p.matcher(data);
							while (m.find()) {
								data = data.replaceAll(m.group(), "file:///android_asset/css/" + DATA_CSS[page]);
							}
						}
						wView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
						Toast.makeText(context, DATA_CSS_NAME[page], Toast.LENGTH_SHORT).show();
						break;
					case 2:
						// cory代码到剪切板
						copy(getDataText(), context);
						Toast.makeText(context, "代码以自制到剪切板", Toast.LENGTH_SHORT).show();
						break;
					}

				}
			});
		}
	}

	/**
	 * 
	 ***************************************************** 
	 * 方法简介: corpy代码到剪切板
	 * 
	 * @param content
	 * @param context
	 ***************************************************** 
	 */
	public static void copy(String content, Context context) {
		// 得到剪贴板管理器
		ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(content.trim());
	}

	/**
	 * 
	 ***************************************************** 
	 * 方法简介: 读取Assets中的html文件
	 * 
	 * @return
	 ***************************************************** 
	 */
	private String getData() {
		// TODO Auto-generated method stub
		String fileName = MenuName + "/" + this.fileName + ".html"; // 文件名字
		File file = new File(Constants.pathStr + fileName);
		String res = "";
		try {
			// 得到资源中的asset数据流
			InputStream in = new FileInputStream(file);
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
	 * 
	 ***************************************************** 
	 * 方法简介: 读取Assets中的txt代码文件
	 * 
	 * @return
	 ***************************************************** 
	 */
	private String getDataText() {
		// TODO Auto-generated method stub
		File f = Environment.getExternalStorageDirectory();// 获取SD卡目录
		// 文件名字
		File fileName = new File(f, "MyLibrary/content/" + MenuName + "/" + this.fileName + ".txt");// 读sdcard文件
		// String fileName = "content/" + MenuName + "/" + this.fileName +
		// ".txt"; // 读assets文件
		String res = "";
		try {
			// 得到资源中的asset数据流
			// InputStream in = getResources().getAssets().open(fileName);//
			// 读assets文件
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

	@Override
	public void onStart() {
		// 左菜单上锁
		Global.InterceptFlag = true;
		super.onStart();
	}

	@Override
	public void onStop() {
		// 左菜单解锁
		Global.InterceptFlag = false;
		super.onStop();
	}

	/**
	 * 分享跳转页面
	 * 
	 * @param intent
	 */
	void openFileTxt(Intent intent) {
		context.startActivity(intent);
	}

	private Intent getTxtIntent(String str) {
		String subject = "share subject";
		String text = str;
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		return intent;
	}
}
