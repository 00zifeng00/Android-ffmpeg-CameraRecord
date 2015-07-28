package org.wysaid.filtervideocamera;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import org.wysaid.myUtils.Common;
import org.wysaid.view.FilterGLSurfaceView;


public class MainActivity extends ActionBarActivity {

    private Button mTakePicBtn;
    private Button mRecordBtn;
    private FilterGLSurfaceView mGLSurfaceView;
    private SeekBar mSeekBar;

    public final static String LOG_TAG = Common.LOG_TAG;

    public static MainActivity mCurrentInstance = null;
    public static MainActivity getInstance() {
        return mCurrentInstance;
    }

    public static final String FilterNames[] = {
            "波纹",
            "普通模糊",
            "浮雕",
            "查找边缘",
            "LerpBlur"
    };

    public static final FilterGLSurfaceView.FilterButtons[] FilterTypes = {
            FilterGLSurfaceView.FilterButtons.Filter_Wave,
            FilterGLSurfaceView.FilterButtons.Filter_Blur,
            FilterGLSurfaceView.FilterButtons.Filter_Emboss,
            FilterGLSurfaceView.FilterButtons.Filter_Edge,
            FilterGLSurfaceView.FilterButtons.Filter_BlurLerp
    };

    private View.OnClickListener mFilterSwitchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyButtons btn = (MyButtons)v;
            mGLSurfaceView.setFrameRenderer(btn.filterType);
        }
    };

    public class MyButtons extends Button {

        public FilterGLSurfaceView.FilterButtons filterType;

        public MyButtons(Context context) {
            super(context);
            setOnClickListener(mFilterSwitchListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTakePicBtn = (Button)findViewById(R.id.takeShotBtn);
        mRecordBtn = (Button)findViewById(R.id.recordBtn);
        mGLSurfaceView = (FilterGLSurfaceView)findViewById(R.id.myGLSurfaceView);
        mSeekBar = (SeekBar)findViewById(R.id.seekBar);

        mTakePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "Taking Picture...");
                mGLSurfaceView.post(new Runnable() {
                    @Override
                    public void run() {
                        mGLSurfaceView.takeShot();
                    }
                });
            }
        });

        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(LOG_TAG, "Start recording...");
                        if(mGLSurfaceView.isRecording())
                            break;
                        mGLSurfaceView.setClearColor(1.0f, 0.0f, 0.0f, 0.6f);
                        mGLSurfaceView.startRecording(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(LOG_TAG, "End recording...");
                        if(!mGLSurfaceView.isRecording())
                            break;
                        mGLSurfaceView.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                        mGLSurfaceView.endRecording();
                        Log.i(LOG_TAG, "End recording OK");
                        break;
                }
                return true;
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.menuLinearLayout);

        for(int i = 0; i != FilterTypes.length; ++i) {
            MyButtons button = new MyButtons(this);
            button.filterType = FilterTypes[i];
            button.setText(FilterNames[i]);
            layout.addView(button);
        }

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGLSurfaceView.setIntensity(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCurrentInstance = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
