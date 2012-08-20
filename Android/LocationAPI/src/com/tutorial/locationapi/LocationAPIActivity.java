package com.tutorial.locationapi;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationAPIActivity extends Activity {
	LocationManager mLocationManager;
	String mProvider;
	EditText mConsole;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_api);
                        
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        /*
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        */               
        
        /*
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        	mProvider = LocationManager.GPS_PROVIDER;
        } else {
        	mProvider = mLocationManager.getBestProvider(new Criteria(), true);
        }
        */
        
        mProvider = mLocationManager.getBestProvider(new Criteria(), true);
        
        Toast.makeText(this, "Best provider: " + mProvider, Toast.LENGTH_SHORT).show();
        
        mConsole = (EditText)findViewById(R.id.console);
                                
        Button getProvider = (Button)findViewById(R.id.get_provider);
        getProvider.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				List<String> providers = mLocationManager.getProviders(true);
				for (String provider : providers) {
					mConsole.append(provider + "\n");
				}
			}
		});
        
        Button getLocation = (Button)findViewById(R.id.get_location);
        getLocation.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				mLocationManager.requestLocationUpdates(mProvider, 0, 0, mListener);
			}
		});
    }
    
    public String getLocation(double latitude, double longitude) {
    	String location = new String();
    	Geocoder geocoder = new Geocoder(this, Locale.KOREA);
    	
    	try {
    		List<Address> address = geocoder.getFromLocation(latitude, longitude, 1);        	
        	if (address.size() > 0) {
        		location = address.get(0).getCountryName() + " " + address.get(0).getLocality()
        				+ " " + address.get(0).getThoroughfare() + " " + address.get(0).getFeatureName();
        	}        	
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	    	
    	return location;
    }
    
    LocationListener mListener = new LocationListener() {
    	public void onStatusChanged(String provider, int status, Bundle extras) {
    		switch (status) {
    		case LocationProvider.OUT_OF_SERVICE:
    			mConsole.setText(provider + " Out of service");
    			break;
    		case LocationProvider.TEMPORARILY_UNAVAILABLE:
    			mConsole.setText(provider + "Temporarily unavailable");
    			break;
    		case LocationProvider.AVAILABLE:
    			mConsole.setText(provider + "Available");
    			break;
    		}
    	}
    	
    	public void onProviderEnabled(String provider) {
    		mConsole.setText(provider + "enabled");
    	}
    	
    	public void onProviderDisabled(String provider) {
    		mConsole.setText(provider + "disabled");
    	}
    	
    	public void onLocationChanged(Location location) {
    		mLocationManager.removeUpdates(mListener);
    		
    		String currentLocation = String.format("latitude: %f\nlongitude: %f ", location.getLatitude(), location.getLongitude());
    		mConsole.append(currentLocation);
    		
    		Calendar calendar = new GregorianCalendar();
    		String now = String.format("%d-%d-%d-%d-%d-%d\n", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
    				calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    		mConsole.append(now);    		
    		mConsole.append(getLocation(location.getLatitude(), location.getLongitude()));
    	}
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_location_api, menu);
        return true;
    }
}
