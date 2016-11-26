package such.app.much.wow;


import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Log.d("App", "onCreate");
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("CRq5CS4PCh5FiXJCvIK3Bv4UznBA6P4p3PXymySe")
                .server("https://parseapi.back4app.com/")
                .clientKey("v5wmLtJyuZkdaQ7Ik3CJNdfsDnntbKks7pyyAcal")
                .build());

        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

    }
}
