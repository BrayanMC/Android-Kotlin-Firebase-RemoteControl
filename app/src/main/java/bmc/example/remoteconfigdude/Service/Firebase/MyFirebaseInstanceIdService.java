package bmc.example.remoteconfigdude.Service.Firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import bmc.example.remoteconfigdude.Utils.AppConstants;
import bmc.example.remoteconfigdude.Utils.Tools;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (AppConstants.IS_DEBUG) {
            Tools.logLine(this, "Refreshed token: " + token);
        }
    }
}
