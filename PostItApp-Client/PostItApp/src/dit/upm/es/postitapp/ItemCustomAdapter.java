package dit.upm.es.postitapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemCustomAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<ItemNavigationDrawer> items;

	public ItemCustomAdapter(Activity activity, ArrayList<ItemNavigationDrawer> items) {
		this.activity = activity;
		this.items = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View vi=contentView;

		if(contentView == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi = inflater.inflate(R.layout.item_list, null);
		}

		ItemNavigationDrawer item = items.get(position);

		ImageView image = (ImageView) vi.findViewById(R.id.icon);
		int imageResource = activity.getResources().getIdentifier(item.getPathImage(), null, activity.getPackageName());
		image.setImageDrawable(activity.getResources().getDrawable(imageResource));

		TextView nombre = (TextView) vi.findViewById(R.id.title_item);
		nombre.setText(item.getName());

		return vi;
	}
}
