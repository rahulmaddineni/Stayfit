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

/**
 * Created by sunny on 24-Apr-16.
 */
public class Fragment1 extends Fragment implements YouTubePlayer.OnInitializedListener{

    private int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayer mPlayer1;
    String mVideoId1;
    YouTubePlayerSupportFragment playerFragment1;


    public static Fragment1 newInstance(){
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();

        return fragment;
    }
    public Fragment1(){
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView1;


        //getActivity().setTitle("Movie Show");

        rootView1=inflater.inflate(R.layout.tab_fragment_1, container, false);
        TextView title1 = (TextView) rootView1.findViewById(R.id.title1);
        title1.setVisibility(View.VISIBLE);

        title1.setText(R.string.tab1Text);
        mVideoId1 = getString(R.string.tab1VideoID);
        Log.d("mVideoId1 is ", mVideoId1);

        playerFragment1 = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.moviePlayer1);
        playerFragment1.setUserVisibleHint(true);
        //playerFragment1.initialize(getString(R.string.google_maps_key), this);

        return rootView1;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser && mPlayer1 != null) {
            mPlayer1.release();
        }
        if (isVisibleToUser && playerFragment1 != null) {
            playerFragment1.initialize(getString(R.string.google_maps_key), this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer1, boolean restored) {

        mPlayer1=youTubePlayer1;

        //Here we can set some flags on the player

        //This flag tells the player to switch to landscape when in fullscreen, it will also return to portrait
        //when leaving fullscreen
        mPlayer1.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);


        //This flag tells the player to automatically enter fullscreen when in landscape. Since we don't have
        //landscape layout for this activity, this is a good way to allow the user rotate the video player.
        mPlayer1.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);


        //This flag controls the system UI such as the status and navigation bar, hiding and showing them
        //alongside the player UI
        mPlayer1.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);



        if (mVideoId1 != null) {
            if (restored) {
                mPlayer1.play();
            } else {
                mPlayer1.loadVideo(mVideoId1);
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

