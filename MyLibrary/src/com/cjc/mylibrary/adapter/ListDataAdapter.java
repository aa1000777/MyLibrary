package com.cjc.mylibrary.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wechatsample.R;
/**
 * 列表Adapter
 *****************************************************
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>列表Adapter</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午2:54:25</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * @author aa1000777 - Email:aa1000777@qq.com
 *****************************************************
 */
public class ListDataAdapter extends BaseAdapter {
	private Context context;
	private List<String> listdata;
	public ListDataAdapter(Context context, List<String> listdata) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listdata=listdata;
	}

	@Override
	public int getCount() {
		return listdata.size();
	}

	@Override
	public Object getItem(int position) {
		return listdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewgroup) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_data_adapter, viewgroup, false);
			holder.liat_data_adpter_Title_TV = (TextView) convertView
					.findViewById(R.id.liat_data_adpter_Title_TV);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (listdata!=null) {
			holder.liat_data_adpter_Title_TV.setText(listdata.get(position));
		}
		return convertView;

	}

	class ViewHolder {
		TextView liat_data_adpter_Title_TV;
	}

}
