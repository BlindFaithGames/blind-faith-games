package com.accgames.feedback;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(formKey = "dFNvb2VqZ09DNHhUN2IwZ0lpRkZMY2c6MQ")
public class AcraSending extends Application {
	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
		super.onCreate();
	}
}
