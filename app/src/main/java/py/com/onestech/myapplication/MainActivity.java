package py.com.onestech.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import py.com.onestech.minipos.model.PaymentResponse;
import py.com.onestech.minipos.ui.DeviceFactory;
import py.com.onestech.minipos.ui.MposUi;

public class MainActivity extends AppCompatActivity implements MposUi.OnDeviceListener {
    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static final int REQUEST_ENABLE_BT = 3;

    private AlertDialog mDialog;

    private Button mButtonCharge;
    private Button mButtonConnect;
    private Button mButtonDisconnect;

    private String mTextHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.layout_loading_dialog);
        mDialog = builder.create();


        mButtonConnect = findViewById(R.id.btn_connect);
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.show();
                init();
            }
        });

        mButtonCharge = findViewById(R.id.btn_charge);
        mButtonCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dialogText = (TextView) mDialog.findViewById(R.id.dialog_label);
                if (dialogText != null) {
                    dialogText.setText(getString(R.string.swipe_card_title));
                }
                mDialog.show();
                MposUi.getInstance().createTransaction(MainActivity.this, 1.0);
            }
        });
    }

    private void init() {
        MposUi.initialize(DeviceFactory.DeviceMode.MIURA,
                BuildConfig.MERCHANT_ACCOUNT,
                BuildConfig.MERCHANT_KEY,
                "+595971500500",
                MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDialog.dismiss();
        if (requestCode == DeviceFactory.REQUEST_ENABLE_BT) {
            Toast.makeText(this, getString(R.string.bt_enabled), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeviceDetected(String serial, String name) {
        TextView textView = findViewById(R.id.main_text);
        textView.setText(serial);
    }

    @Override
    public void onError() {
        mDialog.dismiss();
        Toast.makeText(this, getString(R.string.wait_dialog_title), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String message) {
        mDialog.dismiss();
        TextView textView = findViewById(R.id.main_text);
        textView.setText(message);
    }

    @Override
    public void onFallback(String message) {
        TextView textView = findViewById(R.id.main_text);
        textView.setText(message);
        MposUi.getInstance().continueWithFallback(this);
    }

    @Override
    public void onRequestEnableBluetooth() {
        mDialog.dismiss();
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onAuthFailed() {
        mDialog.dismiss();
        Toast.makeText(this, getString(R.string.err_not_authorized), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthDeviceFailed() {
        mDialog.dismiss();
        TextView textView = findViewById(R.id.main_text);
        textView.setText(getString(R.string.err_device_not_authorized));
    }

    @Override
    public void onSessionExpired() {
        mDialog.dismiss();
        init();
    }

    @Override
    public void onSuccess() {
        mDialog.dismiss();

        mTextHelper = mButtonConnect.getText().toString();

        TextView textView = findViewById(R.id.main_text);
        textView.setText(textView.getText() + " OK");
        mButtonCharge.setEnabled(true);
        mButtonConnect.setText(textView.getText());
        mButtonConnect.setEnabled(false);
        mButtonDisconnect = findViewById(R.id.btn_disconnect);
        mButtonDisconnect.setEnabled(true);
        mButtonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Desconectar dispositivo
                mDialog.show();
                MposUi.getInstance().disconnect();
            }
        });
    }

    @Override
    public void onDisconnected() {
        mDialog.dismiss();
        mButtonConnect.setEnabled(true);
        mButtonConnect.setText(mTextHelper);
        mButtonCharge.setEnabled(false);
        mButtonConnect.setEnabled(true);
        mButtonDisconnect.setEnabled(false);
    }

    @Override
    public void onTransactionData(PaymentResponse response) {
        mDialog.dismiss();
        TextView textView = findViewById(R.id.main_text);
        String textConcat = textView.getText() + "\nAuthCode: " + response.getAuthorizationCode();
        textConcat = textConcat + "\nTicketNumber: " + response.getTicketNumber();
        textConcat = textConcat + "\nMerchantCode: " + response.getMerchantCode();
        textView.setText(textConcat);

        //enviamos voucher por correo
        MposUi.getInstance().sendEmailVoucher(response.getId(), "arrobasoft@gmail.com");
        //enviamos voucher por sms
        MposUi.getInstance().sendSmsVoucher(response.getId(), "0981833415");
    }

    @Override
    public void onCVVRequired() {
        mDialog.dismiss();
        MposUi.getInstance().continueTransaction("203");
    }

    @Override
    public void onNotificationSuccess() {
        Toast.makeText(this, getString(R.string.email_success), Toast.LENGTH_LONG).show();
    }
}
