package com.mesoco.smsretrieved;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CapacitorPlugin(name = "AndroidSmsRetrieved")
public class AndroidSmsRetrievedPlugin extends Plugin {

    private AndroidSmsRetrieved implementation = new AndroidSmsRetrieved();
    public static final int RESULT_OK = -1;

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void unregisterSmsReceiver() {
        getActivity().unregisterReceiver(smsBroadcastReceiver);
    }

    @PluginMethod
    public void registerSmsReceiver() {
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @PluginMethod
    public void startSmsUserConsent(PluginCall call) {
        saveCall(call);
        call.setKeepAlive(true);
        SmsRetrieverClient client = SmsRetriever.getClient(getActivity());
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null);
    }

    BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), SmsRetriever.SMS_RETRIEVED_ACTION)) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        startActivityIntent.launch(messageIntent);
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        break;
                }
            }
        }
    };

    ActivityResultLauncher<Intent> startActivityIntent = getActivity()
        .registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onActivityResult);

    private void onActivityResult(ActivityResult result) {
        if ((result.getResultCode() == RESULT_OK) && (result.getData() != null)) {
            //That gives all message to us.
            // We need to get the code from inside with regex
            String message = result.getData().getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
            Pattern pattern = Pattern.compile("(|^)\\d{6}");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                JSObject ret = new JSObject();
                ret.put("otp", matcher.group(0));
                PluginCall call = getSavedCall();
                call.resolve(ret);
            }
        }
    }
}
