package com.cjc.mylibrary.tools;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * 
 ***************************************************** 
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>Fragment页面父类</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午3:08:32</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * 
 * @author aa1000777 - Email:aa1000777@qq.com
 ***************************************************** 
 */
public class BaseFragment extends Fragment {
	public static FragmentManager fragmentManager;
	public static Context context;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = this.getActivity();
		fragmentManager = this.getFragmentManager();
	}

}
