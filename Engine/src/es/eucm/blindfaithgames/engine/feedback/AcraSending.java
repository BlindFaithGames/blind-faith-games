package es.eucm.blindfaithgames.engine.feedback;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(formKey = "dFNvb2VqZ09DNHhUN2IwZ0lpRkZMY2c6MQ",
customReportContent = { ReportField.PACKAGE_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.INSTALLATION_ID, 
		ReportField.CUSTOM_DATA, ReportField.SHARED_PREFERENCES, ReportField.STACK_TRACE })
public class AcraSending extends Application {
	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
		super.onCreate();
	}
}
