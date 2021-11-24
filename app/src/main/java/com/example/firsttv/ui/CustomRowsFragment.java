package com.example.firsttv.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.app.RowsFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.example.firsttv.R;
import com.example.firsttv.Utils;
import com.example.firsttv.data.VideoProvider;
import com.example.firsttv.model.Movie;
import com.example.firsttv.presenter.CardPresenter;
import com.example.firsttv.recommendation.UpdateRecommendationsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CustomRowsFragment extends RowsFragment implements
		LoaderManager.LoaderCallbacks<HashMap<String, List<Movie>>> {

	private ArrayObjectAdapter rowsAdapter;

	private static String mVideosUrl;

	// CustomHeadersFragment, scaled by 0.9 on a 1080p screen, is 600px wide.
	// This is the corresponding dip size.
	private static final int HEADERS_FRAGMENT_SCALE_SIZE = 300;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);

		int marginOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEADERS_FRAGMENT_SCALE_SIZE, getResources().getDisplayMetrics());
		ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
		params.rightMargin -= marginOffset;
		v.setLayoutParams(params);

		//v.setBackgroundColor(getRandomColor());
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadVideoData();
		setCustomPadding();
		setOnItemViewClickedListener(new OnItemViewClickedListener() {
			@Override
			public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
				Movie movie = (Movie) item;
				Intent intent = new Intent(getActivity(), SeriesDetailsActivity.class);

				Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
						getActivity(),
						((ImageCardView) itemViewHolder.view).getMainImageView(),
						SeriesDetailsActivity.SHARED_ELEMENT_NAME).toBundle();
				getActivity().startActivity(intent, bundle);
			}
		});
	}

	private void loadVideoData() {
		VideoProvider.setContext(getActivity());
		mVideosUrl = getActivity().getResources().getString(R.string.catalog_url);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<HashMap<String, List<Movie>>> onCreateLoader(int arg0, Bundle arg1) {
		return null;//return new VideoItemLoader(getActivity(), mVideosUrl);
	}

	@Override
	public void onLoadFinished(Loader<HashMap<String, List<Movie>>> arg0,
							   HashMap<String, List<Movie>> data) {

		rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
		CardPresenter cardPresenter = new CardPresenter();

		int i = 0;

		for (Map.Entry<String, List<Movie>> entry : data.entrySet()) {
			ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
			List<Movie> list = entry.getValue();

			for (int j = 0; j < list.size(); j++) {
				listRowAdapter.add(list.get(j));
			}
			HeaderItem header = new HeaderItem(i, entry.getKey());
			i++;
			rowsAdapter.add(new ListRow(header, listRowAdapter));
		}

		setAdapter(rowsAdapter);

		updateRecommendations();
	}

	@Override
	public void onLoaderReset(Loader<HashMap<String, List<Movie>>> arg0) {
		rowsAdapter.clear();
	}

	private void setCustomPadding() {
		getView().setPadding(Utils.convertDpToPixel(getActivity(), -24), Utils.convertDpToPixel(getActivity(), 128), Utils.convertDpToPixel(getActivity(), 48), 0);
	}

	private int getRandomColor() {
		Random rnd = new Random();
		return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}

	public void refresh() {
		getView().setPadding(Utils.convertDpToPixel(getActivity(), -24), Utils.convertDpToPixel(getActivity(), 128), Utils.convertDpToPixel(getActivity(), 300), 0);
	}

	private void updateRecommendations() {
		Intent recommendationIntent = new Intent(getActivity(), UpdateRecommendationsService.class);
		getActivity().startService(recommendationIntent);
	}
}
