package top.iwill.gpsmanager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

/**
 * Comment: //location的统一回调
 *
 * @author Jax.zhou in Make1
 * @date 2018/5/24
 * Company:Make1
 * Email:Jax.zhou@make1.cn
 */
public class GPSLocation implements LocationListener {

    private GPSLocationListener mGpsLocationListener;


    public GPSLocation(GPSLocationListener gpsLocationListener) {
        this.mGpsLocationListener = gpsLocationListener;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mGpsLocationListener.onUpdateLocation(location);
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        mGpsLocationListener.onUpdateStatus(provider, status, extras);
        switch (status) {
            case LocationProvider.AVAILABLE:
                mGpsLocationListener.onUpdateGpsProviderStatus(GPSProviderStatus.GPS_AVAILABLE);
                break;
            case LocationProvider.OUT_OF_SERVICE:
                mGpsLocationListener.onUpdateGpsProviderStatus(GPSProviderStatus.GPS_OUT_OF_SERVICE);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                mGpsLocationListener.onUpdateGpsProviderStatus(GPSProviderStatus.GPS_TEMPORARILY_UNAVAILABLE);
                break;
            default:
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        mGpsLocationListener.onUpdateGpsProviderStatus(GPSProviderStatus.GPS_ENABLED);
    }

    @Override
    public void onProviderDisabled(String provider) {
        mGpsLocationListener.onUpdateGpsProviderStatus(GPSProviderStatus.GPS_DISABLED);
    }
}
