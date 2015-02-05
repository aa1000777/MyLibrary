package com.cjc.mylibrary.adapter;

import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjc.mylibrary.entity.Menu;
import com.example.wechatsample.R;

/**
 * menuAdapter
 ***************************************************** 
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>menuAdapter</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午2:53:57</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * @author aa1000777 - Email:aa1000777@qq.com
 ***************************************************** 
 */
public class LeftListAdpter extends BaseAdapter {
	private Context context;
	private List<Menu> Gmenu;
	private LayoutInflater mInflater;
	private int mLcdWidth = 0;
	private float mDensity = 0;

	public LeftListAdpter(Context context, List<Menu> Gmenu) {
		mInflater = LayoutInflater.from(context);
		this.Gmenu = Gmenu;
		this.context = context;
	}

	@Override
	public int getCount() {
		return Gmenu.size();
	}

	@Override
	public Object getItem(int arg0) {
		return Gmenu.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		TextView GroupTV;
		LinearLayout footer;
		ImageView GmenuImage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		convertView = mInflater.inflate(R.layout.expand_item, null);
		holder = new ViewHolder();
		holder.GmenuImage = (ImageView) convertView.findViewById(R.id.GmenuImage);
		// 通过GmenuImage设置菜单图标

		holder.GroupTV = (TextView) convertView.findViewById(R.id.GroupTV);
		holder.footer = (LinearLayout) convertView.findViewById(R.id.footer);
		getSystemInfo();
		holder.GroupTV.setText(Gmenu.get(position).Value);

		switch (Integer.valueOf(Gmenu.get(position).MenuNO)) {
		case 1:// ESSILOR NEWS
			holder.GmenuImage.setImageResource(R.drawable.muneicon_03);
			break;
		case 2:// SPEAKERS
			holder.GmenuImage.setImageResource(R.drawable.muneicon_06);
			break;
		case 3:// PROGRAMME
			holder.GmenuImage.setImageResource(R.drawable.muneicon_08);
			break;
		case 4:// FLOORPLAN
			holder.GmenuImage.setImageResource(R.drawable.muneicon_10);
			break;
		case 5:// DELEGATES OF THE WORLD
			holder.GmenuImage.setImageResource(R.drawable.muneicon_17);
			break;
		case 6:// ESSILOR ALL YOUR LIFE
			holder.GmenuImage.setImageResource(R.drawable.muneicon_19);
			break;
		case 7:// CONFERENCE SLIDER DOWNLOAD
			holder.GmenuImage.setImageResource(R.drawable.muneicon_21);
			break;
		case 8:// SURVEY
			holder.GmenuImage.setImageResource(R.drawable.muneicon_23);
			break;
		case 9:// HOME
			holder.GmenuImage.setImageResource(R.drawable.muneicon_home_03);
			break;
		default:
			break;
		}
		// get footer height
		int widthSpec = MeasureSpec.makeMeasureSpec((int) (mLcdWidth - 10 * mDensity), MeasureSpec.EXACTLY);
		holder.footer.measure(widthSpec, 0);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.footer.getLayoutParams();
		params.bottomMargin = -holder.footer.getMeasuredHeight();
		holder.footer.setVisibility(View.GONE);
		return convertView;
	}

	private void getSystemInfo() {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mLcdWidth = dm.widthPixels;
		mDensity = dm.density;
	}
}
