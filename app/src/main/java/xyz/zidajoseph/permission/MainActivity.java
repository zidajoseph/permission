package xyz.zidajoseph.permission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button requestCamera;
    private Button requestCall;
    private Button requestReadContacts;
    private Button requestPostNotification;
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestCamera = findViewById(R.id.requestCamera);
        requestCall = findViewById(R.id.requestCall);
        requestReadContacts = findViewById(R.id.requestReadContacts);
        requestPostNotification = findViewById(R.id.requestPostNotification);

        permissionHelper = new PermissionHelper(MainActivity.this);

        requestCamera.setOnClickListener(view -> {
            if (permissionHelper.isCameraAllowed()){
                Toast.makeText(this, "Permission allowed", Toast.LENGTH_SHORT).show();
            } else {
                permissionHelper.requestCameraPermission();
            }
        });
        requestCall.setOnClickListener(view -> {
            if (permissionHelper.isCallAllowed()){
                Toast.makeText(this, "Permission allowed", Toast.LENGTH_SHORT).show();
            } else {
                permissionHelper.requestCallPermission();
            }
        });
        requestReadContacts.setOnClickListener(view -> {
            if (permissionHelper.isReadContactAllowed()){
                Toast.makeText(this, "Permission allowed", Toast.LENGTH_SHORT).show();
            } else {
                permissionHelper.requestReadContactPermission();
            }
        });
        requestPostNotification.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (permissionHelper.isNotificationAllowed()){
                    Toast.makeText(this, "Permission allowed", Toast.LENGTH_SHORT).show();
                } else {
                    permissionHelper.requestNotificationPermission();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}