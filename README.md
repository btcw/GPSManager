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
#### 使用
权限
````Java
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
````
动态获取
````Java
    ActivityCompat.requestPermissions(activity
                ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}
                ,requestCode);
````

正文
````Java
        //获取实例
        mGpsManager = GPSLocationManager.getInstances(this);
        //设置发起定位请求的间隔时长 10s
        mGpsManager.setScanSpan(10*1000);
        //设置位置更新的最短距离(10m)
        mGpsManager.setMinDistance(10);
        
        
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
