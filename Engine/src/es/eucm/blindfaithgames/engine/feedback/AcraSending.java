package es.eucm.blindfaithgames.engine.feedback;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(formKey = "dE54QkpHMDBFNmRqVmJ5UnlBcjZlWFE6MQ",
customReportContent = { ReportField.PACKAGE_NAME, ReportField.APP_VERSION_CODE, ReportField.ANDROID_VERSION, ReportField.BRAND, ReportField.PHONE_MODEL, 
		ReportField.INSTALLATION_ID, ReportField.CUSTOM_DATA, ReportField.SHARED_PREFERENCES, ReportField.IS_SILENT,
		ReportField.STACK_TRACE })
public class AcraSending extends Application {
	@Override
	public void onCreate() {
		// The following line triggers the initialization of ACRA
		ACRA.init(this);
		super.onCreate();
	}
}
