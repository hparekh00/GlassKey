package com.example.glasskey;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (hasNfc()) {
            Toast.makeText(this, "NFC is available.", Toast.LENGTH_LONG).
                    show();
        } else {
            Toast.makeText(this, "NFC is not available on this device. " +
                            "This application may not work correctly.",
                    Toast.LENGTH_LONG).show();
        }


    }
    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Toast.makeText(this, action, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "NFC intent received", Toast.LENGTH_SHORT).
                show();

        super.onNewIntent(intent);
        if (!intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            return;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MainActivity.class).
                addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[] { };
        nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                intentFilter, null);
    }
    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    //hasNfc checks the phone to see whether it has a NFC chip and whether is enabled
    boolean hasNfc(){
        boolean hasFeature = getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
        boolean isEnabled = NfcAdapter.getDefaultAdapter(this).isEnabled();
        return hasFeature && isEnabled;
    }
}
