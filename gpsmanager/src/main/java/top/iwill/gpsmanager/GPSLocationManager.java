package top.iwill.gpsmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.lang.ref.WeakReference;

/**
 * Comment: // GPS定位的管理类
 *
 * @author Jax.zhou in Make1
 * @date 2018/5/24
 * Company:Make1
 * Email:Jax.zhou@make1.cn
 */
public class GPSLocationManager {
    private static final String GPS_LOCATION_NAME = LocationManager.GPS_PROVIDER;
    private static GPSLocationManager gpsLocationManager;
    private static final Object objLock = new Object();
    private boolean isGpsEnabled;
    private String mLocateType;
    private WeakReference<Context> mContext;
    private LocationManager locationManager;
    private GPSLocation mGPSLocation;
    private boolean isOPenGps;
    private long mMinTime;
    private float mMinDistance;
    private String mLocationTypeNoGps;

    private GPSLocationManager(Context context) {
        initData(context);
    }

    private void initData(Context context) {
        this.mContext = new WeakReference<>(context);
        if (mContext.get() != null) {
            locationManager = (LocationManager) (mContext.get().getSystemService(Context.LOCATION_SERVICE));
        }
        //定位类型：GPS
        mLocateType = LocationManager.GPS_PROVIDER;
        //默认不强制打开GPS设置面板
        isOPenGps = false;
        //默认定位时间间隔为6s
        mMinTime = 0;
        //默认位置可更新的最短距离为0m
        mMinDistance = 0;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        mLocationTypeNoGps = locationManager.getBestProvider(criteria, true);
    }

    public static GPSLocationManager getInstances(Context context) {
        if (gpsLocationManager == null) {
            synchronized (objLock) {
                if (gpsLocationManager == null) {
                    gpsLocationManager = new GPSLocationManager(context);
                }
            }
        }
        return gpsLocationManager;
    }

    @SuppressLint("MissingPermission")
    public void switchLocationType(boolean isGPS) {
        //只有在GPS定位模式需要切换的时候才切换
        if (isGPS != mLocateType.equals(LocationManager.GPS_PROVIDER)) {
            locationManager.removeUpdates(mGPSLocation);
            mLocateType = isGPS ? LocationManager.GPS_PROVIDER : mLocationTypeNoGps;
            locationManager.requestLocationUpdates(mLocateType, mMinTime, mMinDistance, mGPSLocation);
        }

    }

    /**
     * 方法描述：设置发起定位请求的间隔时长
     *
     * @param minTime 定位间隔时长（单位ms）
     */
    public void setScanSpan(long minTime) {
        this.mMinTime = minTime;
    }

    /**
     * 方法描述：设置位置更新的最短距离
     *
     * @param minDistance 最短距离（单位m）
     */
    public void setMinDistance(float minDistance) {
        this.mMinDistance = minDistance;
    }

    /**
     * 方法描述：开启定位（默认情况下不会强制要求用户打开GPS设置面板）
     *
     * @param gpsLocationListener gps状态和坐标变化监听
     */
    public void start(GPSLocationListener gpsLocationListener) {
        this.start(gpsLocationListener, isOPenGps);
    }

    /**
     * 切换定位间隔
     *
     * @param interval 间隔 单位s
     */
    @SuppressLint("MissingPermission")
    public void switchLocationInterval(int interval) {
        locationManager.removeUpdates(mGPSLocation);
        mMinTime = (long) (interval * 1000);
        locationManager.requestLocationUpdates(mLocateType, mMinTime, mMinDistance, mGPSLocation);
    }

    /**
     * 方法描述：开启定位
     *
     * @param gpsLocationListener gps状态和坐标变化监听
     * @param isOpenGps           当用户GPS未开启时是否强制用户开启GPS
     */
    @SuppressLint("MissingPermission")
    public void start(GPSLocationListener gpsLocationListener, boolean isOpenGps) {
        this.isOPenGps = isOpenGps;
        if (mContext.get() == null) {
            return;
        }
        mGPSLocation = new GPSLocation(gpsLocationListener);
        isGpsEnabled = locationManager.isProviderEnabled(GPS_LOCATION_NAME);
        if (!isGpsEnabled && isOPenGps) {
            openGPS();
            return;
        }
        if (ActivityCompat.checkSelfPermission(mContext.get(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (mContext.get(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(mLocateType);
        if (lastKnownLocation == null) {
            requestNetLocation();
        } else {
            mGPSLocation.onLocationChanged(lastKnownLocation);
            //备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
            locationManager.requestLocationUpdates(mLocateType, mMinTime, mMinDistance, mGPSLocation);
        }

    }

    @SuppressLint("MissingPermission")
    public void requestNetLocation() {
        Location lastKnownLocation = locationManager.getLastKnownLocation(mLocationTypeNoGps);
        mLocateType = mLocationTypeNoGps;
        locationManager.requestLocationUpdates(mLocateType, mMinTime, mMinDistance, mGPSLocation);
        if (mGPSLocation != null) {
            mGPSLocation.onLocationChanged(lastKnownLocation);
        }

    }

    /**
     * 方法描述：没有打开GPS，开始网络定位
     */
    @SuppressLint("MissingPermission")
    public void openGPS() {
        mLocateType = mLocationTypeNoGps;
        locationManager.requestLocationUpdates(mLocateType, mMinTime, mMinDistance, mGPSLocation);

    }

    /**
     * 方法描述：终止GPS定位,该方法最好在onPause()中调用
     */
    @SuppressLint("MissingPermission")
    public void stop() {
        locationManager.removeUpdates(mGPSLocation);
    }
}
