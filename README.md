# GPSManager
An open source library based on Android GPS native code.
本项目是基于安卓系统GPS的封装
    
如果没有GPS定位就会自动调用网络定位，所以一般是一定能定到位的。
[github](https://github.com/btcw/GpsLocationManager)
    
#### 引入
maven
````Java
    compile 'top.iwill.gpsmanager:gpsmanager:1.0.0'
````
#### 权限
````Java
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
````
#### 动态获取GPS权限
````Java
    ActivityCompat.requestPermissions(activity
                ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}
                ,requestCode);
````

#### 使用
````Java
          mGpsManager = new GPSLocationManager.Builder(this)
                //最小更新距离
                .setMinDistance(0)
                //间隔6s
                .setMinTime(6000)
                .build();
        
        
        mGpsManager.start(new GPSLocationListener() {
            @Override
            public void onUpdateLocation(Location location) {
                //位置更新的回调
            }

            @Override
            public void onUpdateStatus(String provider, int status, Bundle extras) {
                //系统的GPS状态回调
            }

            @Override
            public void onUpdateGpsProviderStatus(int gpsStatus) {
                //GPS状态改变回调
            }
        });
        
         //停止定位
        mGpsManager.stop();
        
````

#### 关于onUpdateGpsProviderStatus(int gpsStatus)的参数
````Java
   /**
     *用户手动开启GPS
     */
    public static final int GPS_ENABLED = 0;
    /**
     *用户手动关闭GPS
     */
    public static final int GPS_DISABLED = 1;
    /**
     * 服务已停止，并且在短时间内不会改变
     */
    public static final int GPS_OUT_OF_SERVICE = 2;
    /**
     * 服务暂时停止，并且在短时间内会恢复
     */
    public static final int GPS_TEMPORARILY_UNAVAILABLE = 3;
    /**
     * 服务正常有效
     */
    public static final int GPS_AVAILABLE = 4;

````

#### 切换定位模式
````Java
    //true为GPS，false为网络定位
    mGpsManager.switchLocationType(true);
````

#### 处理回调样例
````Java

    @Override
    public void onUpdateGpsProviderStatus(int gpsStatus) {
        log("onUpdateGpsProviderStatus,provider:" + gpsStatus);
        switch (gpsStatus) {
            case GPS_DISABLED:
                //用户手动关闭GPS
                break;
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
````
