package com.app.barber.util.quickblox.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;

import java.util.ArrayList;

/**
 * Created by Sunny on 3/26/2017.
 */

public class SubscribeToNotification {


    static SubscribeToNotification subscribeToNotification;

    public static SubscribeToNotification getInstance() {
        if (subscribeToNotification == null) {
            subscribeToNotification = new SubscribeToNotification();
        }
        return subscribeToNotification;
    }

    public SubscribeToNotification() {
    }

    public void setSubscribeToNotification(Context context) {
        Log.e("subscribe", "test1");
        QBSubscription subscription = new QBSubscription(QBNotificationChannel.GCM);
        Log.e("subscribe", "test2");
        subscription.setEnvironment(QBEnvironment.DEVELOPMENT);
        Log.e("subscribe", "test3");
        //
        String deviceId;
        try {
            deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        } catch (Exception e) {
            deviceId = "COULD NOT GET UDID";
            e.printStackTrace();
        }

        subscription.setDeviceUdid(deviceId);
        Log.e("subscribe", "test7");
        //
        String registrationID = FirebaseInstanceId.getInstance().getToken();
        Log.e("subscribe", "test9");
        subscription.setRegistrationID(registrationID);
        Log.e("subscribe", "test10");
        QBPushNotifications.createSubscription(subscription).performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {

            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {
                Log.e("subscrib", ">>> Subscription: " + subscriptions.toString());
            }

            @Override
            public void onError(QBResponseException errors) {
                handleErrors(errors);
            }
        });
    }

    public void handleErrors(QBResponseException exc) {
        String message = String.format("[ERROR] Request has been completed with errors: %s", exc.getErrors()
                + ", code: " + exc.getHttpStatusCode());

        // print
        Log.e("error", message);
    }

    public void unSubcribToNotfication(final Context context) {

        /*QBPushNotifications.getSubscriptions().performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {

                Log.e("sucesssss", "done unsubcreb");
                for (QBSubscription subscription : subscriptions) {
                    if (subscription.getDeviceUdid()!=null) {
                        if (subscription.getDeviceUdid().equalsIgnoreCase(getUDID(context))) {
                            QBPushNotifications.deleteSubscription(subscription.getId()).performAsync(new QBEntityCallback<Void>() {
                                @Override
                                public void onSuccess(Void aVoid, Bundle bundle) {
                                    Log.e("sucess", "done unsubcreb");
                                }

                                @Override
                                public void onError(QBResponseException e) {
                                    Log.e("error", e.getMessage());
                                }
                            });
                        }
                    }

                }
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.e("error", errors.getMessage());

            }
        });*/
    }


    boolean check=false;
    public boolean isSubscribeToNotification(final Context context) {

        QBPushNotifications.getSubscriptions().performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {


                for (QBSubscription subscription : subscriptions) {
                    if (subscription!=null) {
                        String deviceId = getUDID(context);
                        Log.e("deviceIddddd", deviceId );
                        if (deviceId!=null && subscription.getDeviceUdid()!=null) {
                            if (subscription.getDeviceUdid().equalsIgnoreCase(deviceId)) {
                                check=true;
                                break;
                            } else {
                                check =false;
                            }
                        } else {
                            check =false;
                        }

                    }

                    else
                        check=false;

                }
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.e("error", errors.getMessage());

            }
        });

        return check;
    }

    private String getUDID(Context context) {
        String device_UDID="";
        try {
            device_UDID = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            Log.e("DevideId", "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "";
            e.printStackTrace();
            Log.e("DevideId", "Failed to complete device UDID");
        }
        return device_UDID;
    }



//    public void unSubcribToNotfication(){
//
//        QBPushNotifications.deleteSubscription()
//    }
}

