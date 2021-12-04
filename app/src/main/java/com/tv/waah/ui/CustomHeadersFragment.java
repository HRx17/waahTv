package com.tv.waah.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.app.HeadersFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnChildSelectedListener;
import androidx.leanback.widget.VerticalGridView;

import com.tv.waah.R;
import com.tv.waah.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * For fancy Design in future- 30/10/21
 **/

public class CustomHeadersFragment extends HeadersFragment {

	private ArrayObjectAdapter adapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		customSetBackground(R.color.fastlane_background);
		setHeaderAdapter();

		setCustomPadding();

		VerticalGridView gridView = ((MainActivity) getActivity()).getVerticalGridView(this);
		gridView.setOnChildSelectedListener(new OnChildSelectedListener() {
			@Override
			public void onChildSelected(ViewGroup viewGroup, View view, int i, long l) {
				Object obj = ((ListRow) getAdapter().get(i)).getAdapter().get(0);
				getFragmentManager().beginTransaction().replace(R.id.rows_container, (Fragment) obj).commit();
				//((MainActivity) getActivity()).updateCurrentFragment((Fragment) obj);
			}
		});
	}

	private void setHeaderAdapter() {
		adapter = new ArrayObjectAdapter();

		LinkedHashMap<Integer, androidx.fragment.app.Fragment> fragments = ((MainActivity) getActivity()).getFragments();

		int id = 0;
		for (int i = 0; i < fragments.size(); i++) {
			HeaderItem header = new HeaderItem(id, "Category " + i);
			ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
			innerAdapter.add(fragments.get(i));
			adapter.add(id, new ListRow(header, innerAdapter));
			id++;
		}

		setAdapter(adapter);
	}

	private void setCustomPadding() {
		getView().setPadding(0, Utils.convertDpToPixel(getActivity(), 128), Utils.convertDpToPixel(getActivity(), 48), 0);
	}

//	private OnItemSelectedListener getDefaultItemSelectedListener() {
//		return new OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(Object o, Row row) {
//				Object obj = ((ListRow) row).getAdapter().get(0);
//				getFragmentManager().beginTransaction().replace(R.id.rows_container, (Fragment) obj).commit();
//				((MainActivity) getActivity()).updateCurrentFragment((Fragment) obj);
//			}
//		};
//	}

	private void customSetBackground(int colorResource) {
		try {
			Class clazz = HeadersFragment.class;
			Method m = clazz.getDeclaredMethod("setBackgroundColor", Integer.TYPE);
			m.setAccessible(true);
			m.invoke(this, getResources().getColor(colorResource));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
