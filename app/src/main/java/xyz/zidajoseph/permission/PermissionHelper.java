package xyz.zidajoseph.permission;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class PermissionHelper implements ActivityCompat.OnRequestPermissionsResultCallback{

    private final Activity activity;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int CALL_PERMISSION_CODE = 102;
    private static final int NOTIFICATION_PERMISSION_CODE = 103;
    private static final int READ_CONTACT_PERMISSION_CODE = 104;

    public PermissionHelper(@NonNull Activity activity) {
        this.activity = activity;
    }

    public boolean isCallAllowed(){
        return ContextCompat
                .checkSelfPermission(
                        activity,
                        android.Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestCallPermission() {
        activity.requestPermissions(
                new String[]{android.Manifest.permission.CALL_PHONE},
                CALL_PERMISSION_CODE
        );
    }

    public boolean isCameraAllowed(){
        return ContextCompat
                .checkSelfPermission(
                        activity,
                        Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestCameraPermission() {
        activity.requestPermissions(
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_CODE
        );
    }

    public boolean isReadContactAllowed(){
        return ContextCompat
                .checkSelfPermission(
                        activity,
                        Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestReadContactPermission() {
        activity.requestPermissions(
                new String[]{Manifest.permission.READ_CONTACTS},
                READ_CONTACT_PERMISSION_CODE
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public boolean isNotificationAllowed(){
        return ContextCompat
                .checkSelfPermission(
                        activity,
                        android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void requestNotificationPermission() {
        requestPermissions(
                activity,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                NOTIFICATION_PERMISSION_CODE
        );
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showDialog(shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE);
            }
        }else if (requestCode == CALL_PERMISSION_CODE) {
            if (!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showDialog(shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.CALL_PHONE), CALL_PERMISSION_CODE);
            }
        } else if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    showDialog(shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_CODE);
                }
            }
        } else if (requestCode == READ_CONTACT_PERMISSION_CODE) {
            if (!(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showDialog(shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_CONTACTS), READ_CONTACT_PERMISSION_CODE);
            }
        }
    }

    public void showDialog(boolean ShowRequestPermission, int requestCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View customLayout = activity.getLayoutInflater() .inflate( R.layout.dialog_show_request,null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        TextView textViewDesc = customLayout.findViewById(R.id.textViewDesc);
        customLayout.findViewById(R.id.btCloseDialog).setOnClickListener(view -> {
            dialog.dismiss();
        });
        MaterialButton positiveButton = customLayout.findViewById(R.id.positiveButton);
        MaterialButton negativeButton = customLayout.findViewById(R.id.negativeButton);

        if (ShowRequestPermission) {
            positiveButton.setText("Autoriser");
            textViewDesc.setText("L'application a besoin de cette autorisation pour " +
                    "fonctionner correctement. Veuillez cliquer sur AUTORISER.");
        }
        else {
            positiveButton.setText("Aller aux paramètres");
            textViewDesc.setText("L'application a besoin de cette autorisation pour " +
                    "fonctionner correctement. Vous pouvez les accorder dans les " +
                    "paramètres de l'application.");
        }
        positiveButton.setOnClickListener(view -> {
            if (ShowRequestPermission) {
                PermissionHelper helper = new PermissionHelper(activity);
                switch (requestCode){
                    case CAMERA_PERMISSION_CODE :
                        helper.requestCameraPermission();
                        break;
                    case CALL_PERMISSION_CODE :
                        helper.requestCallPermission();
                        break;
                    case NOTIFICATION_PERMISSION_CODE :
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            helper.requestNotificationPermission();
                        }
                        break;
                    case READ_CONTACT_PERMISSION_CODE :
                        helper.requestReadContactPermission();
                        break;
                }
            } else {openSettings();}
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(view -> {
            dialog.dismiss();
        });
        // show the alert dialog
        dialog.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
