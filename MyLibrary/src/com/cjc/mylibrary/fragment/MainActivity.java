package com.cjc.mylibrary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.cjc.mylibrary.menu.lib.SlidingMenu;
import com.cjc.mylibrary.tools.Global;
import com.example.wechatsample.R;

/**
 * 菜单框架初始化页面
 ***************************************************** 
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>菜单框架初始化页面</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午2:55:48</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * 
 * @author aa1000777 - Email:aa1000777@qq.com
 ***************************************************** 
 */
public class MainActivity extends FragmentActivity {
	private SlidingMenu menu;// 菜单
	private Context context;// 上下文对象
	private long exitTime = 0;// 按两次后退的记时
	public static boolean isForeground = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidingmenu_main);
		context = this;
		int i = getResources().getDisplayMetrics().widthPixels;
		menu = new SlidingMenu(context);
		menu.setBehindWidth(i / 10 * 7);// 设置SlidingMenu菜单的宽度
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
		menu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		// menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this,
				SlidingMenu.SLIDING_CONTENT);
		menu.setContent(R.layout.slidingmenu_content);
		menu.setMenu(R.layout.slidingmenu_menu);
		menu.setMode(SlidingMenu.LEFT);
		Global.menu = menu;
		// 要跳转的页面
		ListDataFragment fragment = new ListDataFragment();
		Bundle args = new Bundle();// 参数
		args.putString("MenuName", "activity");
		fragment.setArguments(args);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.slidingmenu_content, fragment)
				.commit();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.slidingmenu_menu,
						new MainLeftFragment()).commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (menu.isMenuShowing()) {
				menu.toggle();
				return false;
			} else if (Global.ExitToHomeFlag) {
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(R.anim.push_left_in,
						R.anim.push_left_out,
						R.anim.push_right_in,
						R.anim.push_right_out);
				Global.ExitToHomeFlag = false;
				break;
			} else if (Global.UpdateFlag) {
				this.moveTaskToBack(true);
				return false;
			} else if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				boolean boolflag = getSupportFragmentManager()
						.popBackStackImmediate();
				if (!boolflag) {
					if ((System.currentTimeMillis() - exitTime) > 2000) {
						Toast.makeText(
								getApplicationContext(),
								"Click the exit program again",
								Toast.LENGTH_SHORT).show();
						exitTime = System
								.currentTimeMillis();
					} else {
						finish();
						System.exit(0);
					}
				}
				return true;
			}
			break;

		case KeyEvent.KEYCODE_MENU:
			if (!Global.UpdateFlag) {
				menu.toggle();
			} else {
				return false;
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
