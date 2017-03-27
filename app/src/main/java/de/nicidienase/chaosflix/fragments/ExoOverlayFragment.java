package de.nicidienase.chaosflix.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.PlaybackFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.support.v17.leanback.widget.Row;

import de.nicidienase.chaosflix.PlaybackHelper;
import de.nicidienase.chaosflix.activities.DetailsActivity;
import de.nicidienase.chaosflix.entities.recording.Event;
import de.nicidienase.chaosflix.entities.recording.Recording;

/**
 * Created by felix on 26.03.17.
 */

public class ExoOverlayFragment extends android.support.v17.leanback.app.PlaybackOverlayFragment {

	private Recording mSelectedRecording;
	private Event mSelectedEvent;
	private PlaybackHelper mHelper;
	private PlaybackControlListener mCallback;
	private ArrayObjectAdapter mRowsAdapter;

	public interface PlaybackControlListener {
		void play();
		void pause();
		void playPause();
		void setVideoSource(String source);
		void skipForward(int sec);
		void skipBackward(int sec);
		void seekTo(int sec);
		boolean isMediaPlaying();
		long getCurrentPosition();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getActivity()
				.getIntent();
		mSelectedEvent = intent.getParcelableExtra(DetailsActivity.EVENT);
		mSelectedRecording = intent.getParcelableExtra(DetailsActivity.RECORDING);

		setBackgroundType(PlaybackFragment.BG_LIGHT);
		setFadingEnabled(false);

		mHelper = new PlaybackHelper(getActivity(),this,mSelectedEvent,mSelectedRecording);

		PlaybackControlsRowPresenter playbackControlsRowPresenter
				= mHelper.createControlsRowAndPresenter();
		PlaybackControlsRow controlsRow = mHelper.getControlsRow();

		ClassPresenterSelector ps = new ClassPresenterSelector();
		ps.addClassPresenter(PlaybackControlsRow.class, playbackControlsRowPresenter);
		ps.addClassPresenter(ListRow.class, new ListRowPresenter());
		mRowsAdapter = new ArrayObjectAdapter(ps);
		mRowsAdapter.add(controlsRow);
//		mRowsAdapter.add(getRelatedItems());
		setAdapter(mRowsAdapter);
	}

	private Row getRelatedItems() {
//		ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
//		TODO Add related items
//		HeaderItem header = new HeaderItem(0, getString(R.string.related_movies));
//		return new ListRow(header, listRowAdapter);
		return null;
	}

	public boolean isMediaPlaying() {
		if(mCallback != null){
			return mCallback.isMediaPlaying();
		}
		return false;
	}

	public int getCurrentPosition() {
		if(mCallback != null){
			return (int) mCallback.getCurrentPosition();
		}
		return 0;
	}

	@Override
	public void onStart() {
		super.onStart();
		mCallback.setVideoSource(mSelectedRecording.getRecordingUrl());
	}

	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);
		if(context instanceof PlaybackControlListener){
			mCallback = (PlaybackControlListener) context;
		} else {
			throw(new RuntimeException("Activity must implement PlaybackControlListener"));
		}
	}
}