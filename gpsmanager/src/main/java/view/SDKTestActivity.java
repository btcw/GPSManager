package view;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import top.iwill.gpsmanager.GPSLocationListener;
import top.iwill.gpsmanager.GPSLocationManager;
import top.iwill.gpsmanager.R;

import static top.iwill.gpsmanager.GPSProviderStatus.GPS_AVAILABLE;
import static top.iwill.gpsmanager.GPSProviderStatus.GPS_DISABLED;
import static top.iwill.gpsmanager.GPSProviderStatus.GPS_ENABLED;
import static top.iwill.gpsmanager.GPSProviderStatus.GPS_OUT_OF_SERVICE;

public class SDKTestActivity extends AppCompatActivity implements View.OnClickListener, GPSLocationListener {

    private TextView mLogText;

    private Button mStartLocateBtn;

    private Button mEndLocateBtn;

    private ScrollView mLogScrollView;

    private GPSLocationManager mGpsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdktest);
        findViews();
        initListeners();
        initData();
    }

    private void findViews() {
        mLogText = findViewById(R.id.text_log);
        mStartLocateBtn = findViewById(R.id.btn_start_locate);
        mEndLocateBtn = findViewById(R.id.btn_end_locate);
        mLogScrollView = findViewById(R.id.scroll_view_log);
    }

    private void initListeners() {
        mStartLocateBtn.setOnClickListener(this);
        mEndLocateBtn.setOnClickListener(this);
    }


    private void initData() {
        mGpsManager = GPSLocationManager.getInstances(this);
        mGpsManager.setScanSpan(6000);
        mGpsManager.setMinDistance(0);
    }


    private void log(String logText) {
        mLogText.append(logText);
        mLogText.append("\r\n");
        mLogScrollView.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_start_locate) {
            mGpsManager.start(this);
        } else if (i == R.id.btn_end_locate) {
            mGpsManager.stop();
        }
    }

    @Override
    public void onUpdateLocation(Location location) {
        log("onUpdateLocation,location:" + location);
    }

    @Override
    public void onUpdateStatus(String provider, int status, Bundle extras) {

    }

    @Override
    public void onUpdateGpsProviderStatus(int gpsStatus) {
        log("onUpdateGpsProviderStatus,provider:" + gpsStatus);
        switch (gpsStatus) {
            case GPS_DISABLED:
            case GPS_OUT_OF_SERVICE:
                mGpsManager.switchLocationType(false);
                break;
            case GPS_ENABLED:
            case GPS_AVAILABLE:
                mGpsManager.switchLocationType(true);
                break;
            default:
        }
    }
}
