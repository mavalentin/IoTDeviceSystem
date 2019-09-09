package it.manuelvalentin.iotdevicemanager;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.kenai.jffi.Main;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    private CameraSource cameraSource;
    private static final String TAG = MainActivity.class.getName();
    private static final int CAMERA_REQUEST_CODE = 200;
    private SurfaceHolder holder;
    public BlockchainHandler blockchainHandler;
    private BarcodeDetector barcodeDetector;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    public static InstanceData instanceData = new InstanceData();
    public DeviceHandler deviceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppsActivity.instanceData = instanceData;
        SurfaceView surfaceView;
        super.onCreate(savedInstanceState);
        blockchainHandler = new BlockchainHandler(MainActivity.this, this);
        blockchainHandler.setCurrentActivity(this);
        instanceData.setBlockchainHandler(blockchainHandler);
        deviceHandler = new DeviceHandler(MainActivity.this, this, blockchainHandler);
        instanceData.setDeviceHandler(deviceHandler);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (blockchainHandler.getLoginStatus().getState()) startBarcodeDetector();
            else Snackbar.make(MainActivity.this.findViewById(R.id.activitiMainCoordinatorLayout), "Login required", Snackbar.LENGTH_LONG)
                    .show();
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        surfaceView = findViewById(R.id.cameraPreview);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                MainActivity.this.holder = holder;
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                }
                setupCamera();

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        initializeNavDrawerTitle();
        //initializeCompatibleAppsMenu();
        initializeNavDrawerSubtitle();

    }

    @Override
    protected void onStop() {
        super.onStop();
        blockchainHandler.shutdownConnection();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apps:
                Intent intent = new Intent(this, AppsActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        TextView navTitle = this.findViewById(R.id.nav_header_title);
        switch (item.getItemId()) {
            case R.id.user:
                login(getString(R.string.userPrivateKey));
                return true;
            case R.id.technician:
                login(getString(R.string.technicianPrivateKey));
                return true;
            case R.id.developer:
                login(getString(R.string.developerPrivateKey));
                return true;
            case R.id.logout:
                blockchainHandler.logout();
                return true;
            case R.id.apps:
                Intent intent = new Intent(this, AppsActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    public void showUserMenu(View v) {
        PopupMenu userMenu = new PopupMenu(this, v);
        userMenu.setOnMenuItemClickListener(this);
        MenuInflater inflater = userMenu.getMenuInflater();
        inflater.inflate(R.menu.user_menu, userMenu.getMenu());
        userMenu.show();
    }

    public void setupCamera() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                cameraSource.start(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startBarcodeDetector() {
        if (barcodeDetector.isOperational()) {
            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                    if(qrCodes.size() != 0) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                stopBarcodeDetector();
                            }
                        });
                        blockchainHandler.getDeviceAddress(new BigInteger(qrCodes.valueAt(0).displayValue, 16)); //send guid in this hex format: 96315C0C3C453B1EC65791DAF307845A
                        blockchainHandler.getCompatibleApps(new BigInteger(qrCodes.valueAt(0).displayValue, 16));
                    }
                }
            });
        }
        fab.hide();
    }

    public void stopBarcodeDetector() {
        barcodeDetector.release();
        fab.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupCamera();
                } else {

                }
                return;
            }
        }
    }

    private void login(String key) {
        blockchainHandler.setPrivateKey(key);
        try {
            blockchainHandler.identifyUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeCompatibleAppsMenu() {
        /*Menu navigationVievMenu = navigationView.getMenu();
        SubMenu installableAppsMenu = navigationVievMenu.findItem(R.id.installableApps).getSubMenu();
        installableAppsMenu.clear();
        ArrayList<String> apps = instanceData.getCompatibleApps();
        ArrayList<String> installedApps = instanceData.getInstalledApps();
        apps.removeAll(installedApps);
        if(apps!=null) {
            for(String app : apps) {
                installableAppsMenu.add(Menu.NONE, Menu.NONE, Menu.NONE, app);
            }
        }*/
    }

    public void initializeNavDrawerTitle() {
        TextView navTitle = navigationView.getHeaderView(0).findViewById(R.id.nav_header_title);
        BlockchainHandler.LoginStatus loginStatus = blockchainHandler.getLoginStatus();
        if(loginStatus.getState()) {
            String userRole;
            switch (loginStatus.getRole()) {
                case "developer":
                    userRole = getResources().getString(R.string.developer);
                    break;
                case "technician":
                    userRole = getResources().getString(R.string.technician);
                    break;
                case "user":
                    userRole = getResources().getString(R.string.user);
                    break;
                default:
                    userRole = getResources().getString(R.string.no_login);
                    break;
            }
            navTitle.setText(userRole);
        } else navTitle.setText(getResources().getString(R.string.no_login));
    }

    public void initializeNavDrawerSubtitle() {
        TextView navSubtitle = navigationView.getHeaderView(0).findViewById(R.id.nav_subtitle);
        if(instanceData.getDeviceAddress() != null) {
            navSubtitle.setText(getResources().getString(R.string.connected_device, instanceData.getDeviceAddress()));
        } else {
            navSubtitle.setText(getResources().getString(R.string.no_connected_device));
        }
    }
}
