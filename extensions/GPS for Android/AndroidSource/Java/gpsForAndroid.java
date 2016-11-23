//
// GPS Location for Android
// By Florian van Strien
// Free to use and edit for non-commercial use.
//

package ${YYAndroidPackageName};

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import java.lang.System;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.app.IntentService;

import android.util.Log;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;

import ${YYAndroidPackageName}.RunnerActivity;
import com.yoyogames.runner.RunnerJNILib;

import android.provider.Settings;

public class gpsForAndroid
{
	LocationManager locationManager;
	LocationListener locationListener;

	double gpsStatus = 0.0;
	double gpsIsEnabled = 1.0;
	boolean autoOffWhenDeviceIsSleeping = true;
	boolean isCurrentlyUpdating = false;
	boolean shouldResumeUpdating = false;
	Location mostRecentLocation;

	public double device_has_gps()
	{
		PackageManager packageManager = RunnerActivity.CurrentActivity.getPackageManager();
		return packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS) ? 1.0 : 0.0;
	}

	public void location_init()
	{
		Log.i("yoyo", "Location Init");
		locationManager = (LocationManager) RunnerActivity.CurrentActivity.getSystemService(Context.LOCATION_SERVICE);
		mostRecentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locationListener = new LocationListener()
		{
			public void onLocationChanged(Location location)
			{
		    	Log.i("yoyo", "Location Found! Timestamp: " + String.valueOf(location.getTime()));
		    	gpsStatus = 1.0;
		    	mostRecentLocation = location;
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras)
		    {
		    	if (provider.equals(LocationManager.GPS_PROVIDER))
				{
					Log.i("yoyo", "GPS Provider status changed!");
					if (status == LocationProvider.AVAILABLE)
					{
						Log.i("yoyo", "GPS Provider available!");
						gpsStatus = 1.0;
					}
					else if (status == LocationProvider.OUT_OF_SERVICE)
					{
						Log.i("yoyo", "GPS Provider out of service!");
						gpsStatus = 0.0;
					}
					else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE)
					{
						Log.i("yoyo", "GPS Provider temp unavailable!");
						gpsStatus = 0.0;
					}
				}
		    }

		    public void onProviderEnabled(String provider)
		    {
		    	if (provider.equals(LocationManager.GPS_PROVIDER))
				{
					Log.i("yoyo", "GPS Enabled!");
					gpsStatus = 1.0;
					gpsIsEnabled = 1.0;
				}
		    }

		    public void onProviderDisabled(String provider)
		    {
		    	if (provider.equals(LocationManager.GPS_PROVIDER))
				{
					Log.i("yoyo", "GPS Disabled!");
					gpsStatus = 0.0;
					gpsIsEnabled = 0.0;
				}
		    }
		};

		if (autoOffWhenDeviceIsSleeping)
		{
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);

			BroadcastReceiver screenOffReceiver = new BroadcastReceiver()
			{
				@Override public void onReceive(Context context, Intent intent)
				{
					if (autoOffWhenDeviceIsSleeping)
					{
						if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
						{
							if (isCurrentlyUpdating)
							{
								location_update_stop();
								shouldResumeUpdating = true;
							}
						}
						else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
						{
							if ((! isCurrentlyUpdating) && (shouldResumeUpdating))
							{
								location_update_start();
								shouldResumeUpdating = false;
							}
						}
					}
			    }
			};

			RunnerActivity.CurrentActivity.registerReceiver(screenOffReceiver, filter);
		}
	}

	public void location_update_start()
	{
		RunnerActivity.ViewHandler.post( new Runnable()
		{
			public void run() 
			{
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
				Log.i("yoyo", "Location listener started");
			}
		});
		isCurrentlyUpdating = true;
	}

	public void location_update_stop()
	{
		RunnerActivity.ViewHandler.post( new Runnable()
		{
			public void run() 
			{
				locationManager.removeUpdates(locationListener);
				Log.i("yoyo", "Location listener stopped");
			}
		});
		isCurrentlyUpdating = false;
	}

	public double location_is_updating()
	{
		return isCurrentlyUpdating ? 1.0 : 0.0;
	}

	public double location_get_status()
	{
		return gpsStatus;
	}

	public double location_is_enabled()
	{
		try
		{
			return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ? 1.0 : 0.0;
		}
		catch (Exception ex)
		{
			return gpsIsEnabled;
		}
	}

	public void location_open_settings()
	{
		Intent androidSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        RunnerActivity.CurrentActivity.startActivity(androidSettings);
	}

	public double location_is_available()
	{
		return (mostRecentLocation != null) ? 1.0 : 0.0;
	}

	public double location_has_bearing()
	{
		return mostRecentLocation.hasBearing() ? 1.0 : 0.0;
	}

	public double location_has_speed()
	{
		return mostRecentLocation.hasSpeed() ? 1.0 : 0.0;
	}

	public double location_has_accuracy()
	{
		return mostRecentLocation.hasAccuracy() ? 1.0 : 0.0;
	}

	public double location_has_altitude()
	{
		return mostRecentLocation.hasAltitude() ? 1.0 : 0.0;
	}

	//In ms
	public double location_get_time()
	{
		return (double) mostRecentLocation.getTime();
	}

	//Needs newer Android API version
	/*public double location_get_age()
	{
		return ((double) (SystemClock.elapsedRealtimeNanos() - mostRecentLocation.elapsedRealtimeNanos())) / 1000000;
	}*/

	//In ms
	public double location_get_age()
	{
		double age = (double) (System.currentTimeMillis() - mostRecentLocation.getTime());
		if (age < 0.0)
			return 0.0;
		else
			return age;
	}

	public double location_get_bearing()
	{
		return (double) mostRecentLocation.getBearing();
	}

	public double location_get_latitude()
	{
		return mostRecentLocation.getLatitude();
	}

	public double location_get_longitude()
	{
		return mostRecentLocation.getLongitude();
	}

	public double location_get_speed()
	{
		return mostRecentLocation.getSpeed();
	}

	public double location_get_accuracy()
	{
		return mostRecentLocation.getAccuracy();
	}

	public double location_get_altitude()
	{
		return mostRecentLocation.getAltitude();
	}

	//When always_update is true, the location will even be collected when the screen is off.
	public void location_update_always(double always_update)
	{
		if (always_update == 0.0)
			autoOffWhenDeviceIsSleeping = true;
		else
			autoOffWhenDeviceIsSleeping = false;
	}

	//Returns distance between points in meters
	public double point_distance_earth(double latitude1, double longitude1, double latitude2, double longitude2)
	{
		float[] resultArray;
		resultArray = new float[1];
		Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, resultArray);
		return (double) resultArray[0];
	}
}