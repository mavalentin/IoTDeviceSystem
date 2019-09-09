package it.manuelvalentin.iotdevicemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import java.util.ArrayList;

public class AppsActivity extends AppCompatActivity implements AppFragment.OnListFragmentInteractionListener, PopupMenu.OnMenuItemClickListener {
    public static InstanceData instanceData;
    private AppFragment installedAppsFragment;
    private AppFragment installableAppsFragment;
    private static final String TAG = AppsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        instanceData.getBlockchainHandler().setCurrentActivity(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                deploymentReceiver, new IntentFilter("deploy_app"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                userActionReceiver, new IntentFilter("user_action"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                technicianActionReceiver, new IntentFilter("technician_action"));
        initializeApps();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(deploymentReceiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initializeApps() {
        ArrayList<InstanceData.App> installableApps = new ArrayList<>(instanceData.getCompatibleApps());
        //ArrayList<InstanceData.App> installedAppsTemp = new ArrayList<>(instanceData.getInstalledApps());
        ArrayList<InstanceData.App> installedApps = new ArrayList<>(instanceData.getInstalledApps());
        /*for(int i = 0; i < installedAppsTemp.size(); i++) {
            for(InstanceData.App a : installableApps) {
                if (a.getAppGuid().equals(installedAppsTemp.get(i).getAppGuid())) {
                    installedApps.add(a);
                }
            }
        }*/
        installableApps.removeAll(installedApps);

        //Installed Apps
        installedAppsFragment = new AppFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putParcelableArrayList("apps", installedApps);
        bundle1.putString("type", "installed");
        installedAppsFragment.setArguments(bundle1);

        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.installedAppsFrame, installedAppsFragment);
        ft1.commit();

        if(instanceData.getBlockchainHandler().getLoginStatus().getRole().equals("developer")) {
            //Installable Apps
            installableAppsFragment = new AppFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putParcelableArrayList("apps", installableApps);
            bundle2.putString("type", "installable");
            installableAppsFragment.setArguments(bundle2);

            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.installableAppsFrame, installableAppsFragment);
            ft2.commit();
        } else {
            findViewById(R.id.installableAppsTitle).setVisibility(View.GONE);
        }
    }

    /*public void showAppMenu(InstanceData.App app, View v) {
        PopupMenu appMenu = new PopupMenu(this, v);
        //appMenu.setIntent
        appMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = appMenu.getMenuInflater();
        inflater.inflate(R.menu.app_menu, appMenu.getMenu());
        appMenu.show();
    }*/

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.install:
                //instanceData.getDeviceHandler().deployApp(app);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onListFragmentInteraction(MyAppRecyclerViewAdapter.ViewHolder holder) {
        /*Snackbar.make(findViewById(R.id.mainLayout), holder.app.getAppName(), Snackbar.LENGTH_LONG)
                .show();*/
        //showAppMenu(app, view);
    }

    private BroadcastReceiver deploymentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int responseCode = intent.getIntExtra("responseCode", 0);
            if(responseCode == 200) {
                String deployedAppGuid = intent.getStringExtra("deployed_app_guid");
                String uninstalledAppGuid = intent.getStringExtra("uninstalled_app_guid");

                if(deployedAppGuid != null) {
                    ArrayList<InstanceData.App> compatibleApps = new ArrayList<>(instanceData.getCompatibleApps());
                    // TODO: here I might have to simply get installed apps from device again and refresh the activity
                    for (int i = 0; i < compatibleApps.size(); i++) {
                        if (deployedAppGuid.equals(compatibleApps.get(i).getAppGuid())) {
                            instanceData.getInstalledApps().add(compatibleApps.get(i));
                        }
                    }
            /*installedAppsFragment.dataChanged();
            installableAppsFragment.dataChanged();*/
                    initializeApps();
                    Snackbar.make(findViewById(R.id.mainLayout), "Deployed app " + deployedAppGuid, Snackbar.LENGTH_LONG).show();
                } else if(uninstalledAppGuid != null) {
                    instanceData.removeInstalledApp(uninstalledAppGuid);
                    initializeApps();
                    Snackbar.make(findViewById(R.id.mainLayout), "Uninstalled app " + uninstalledAppGuid, Snackbar.LENGTH_LONG).show();
                }
            } else if(responseCode == 550) {
                Snackbar.make(findViewById(R.id.mainLayout), "Checksum error", Snackbar.LENGTH_LONG).show();
                Log.e(TAG, "checksum error");
            } else if(responseCode == 403) {
                Snackbar.make(findViewById(R.id.mainLayout), "Forbidden", Snackbar.LENGTH_LONG).show();
                Log.e(TAG, "forbidden");
            }

        }
    };

    private BroadcastReceiver userActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int responseCode = intent.getIntExtra("responseCode", 0);
            if(responseCode == 200) {
                String registrationResult = intent.getStringExtra("registrationResult");
                String access = intent.getStringExtra("access");
                if(registrationResult != null) {
                    if(registrationResult.equals("1")) {
                        Log.d("end aregister", System.currentTimeMillis() + " ms");
                        Snackbar.make(findViewById(R.id.mainLayout), "Successfully registered", Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "Successfully registered");
                    } else if(registrationResult.equals("2")) {
                        Log.d("end aunregister", System.currentTimeMillis() + " ms");
                        Snackbar.make(findViewById(R.id.mainLayout), "Successfully unregistered", Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "Successfully unregistered");
                    }
                } else if(access != null) {
                    if(access.equals("1")) {
                        Snackbar.make(findViewById(R.id.mainLayout), "Data accessed", Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "Data has been sent by email");
                        AppDataFragment dataDialog = new AppDataFragment();
                        dataDialog.setMessage(intent.getStringExtra("data"));
                        dataDialog.show(getSupportFragmentManager(), "data");
                    } else if(access.equals("0")) {
                        Snackbar.make(findViewById(R.id.mainLayout), "Data access error", Snackbar.LENGTH_LONG).show();
                        Log.e(TAG, "Data access error");
                    }
                }
            } else if(responseCode == 409) {
                Snackbar.make(findViewById(R.id.mainLayout), "Can't register: already registered", Snackbar.LENGTH_LONG).show();
                Log.e(TAG, "Can't register: already registered");
            }
        }
    };

    private BroadcastReceiver technicianActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int responseCode = intent.getIntExtra("responseCode", 0);
            if(responseCode == 200) {
                String restarted = intent.getStringExtra("restarted");
                if(restarted != null) {
                    if(restarted.equals("1")) {
                        Snackbar.make(findViewById(R.id.mainLayout), "Container restarted", Snackbar.LENGTH_LONG).show();
                        Log.d(TAG, "Container restarted");
                    }
                }
            }
        }
    };

    public static class AppDataFragment extends DialogFragment {
        private String message;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            return builder.create();
        }

        public void setMessage(String m) {
            message = m;
        }
    }
}
