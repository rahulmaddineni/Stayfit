package com.example.maddi.fitness;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class Fragment2 extends Fragment implements YouTubePlayer.OnInitializedListener {

    private int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayer mPlayer2;
    YouTubePlayerSupportFragment playerFragment2;


    String mVideoId2;

    public static Fragment2 newInstance() {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();

        return fragment;
    }

    public Fragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView2;

        //getActivity().setTitle("Movie Show");
        rootView2 = inflater.inflate(R.layout.tab_fragment_2, container, false);
        TextView title2 = (TextView) rootView2.findViewById(R.id.title2);
        title2.setVisibility(View.VISIBLE);

        title2.setText(R.string.tab2Text);
        mVideoId2 = getString(R.string.tab2VideoID);
        Log.d("mVideoId2 is ", mVideoId2);

        playerFragment2 = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.moviePlayer2);
        playerFragment2.setUserVisibleHint(true);

        return rootView2;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser && mPlayer2 != null) {
            mPlayer2.release();
        }
        if (isVisibleToUser && playerFragment2 != null) {
            playerFragment2.initialize(getString(R.string.google_maps_key), this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer2, boolean restored) {

        mPlayer2 = youTubePlayer2;

        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        mPlayer2.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);


        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        mPlayer2.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);


        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        mPlayer2.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);


        if (mVideoId2 != null) {
            if (restored) {
                mPlayer2.play();
            } else {
                mPlayer2.loadVideo(mVideoId2);
            }
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST).show();
        } else {
            //Handle the failure
            Toast.makeText(getActivity(), "onInitializationFailure", Toast.LENGTH_LONG).show();
        }
    }
}