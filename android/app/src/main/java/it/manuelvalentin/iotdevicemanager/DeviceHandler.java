package it.manuelvalentin.iotdevicemanager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import static java.nio.charset.StandardCharsets.UTF_8;

class DeviceHandler {
    private static final String TAG = DeviceHandler.class.getName();
    private String deviceAddress;
    private DeviceResponse deviceResponse;
    private MainActivity mainActivity;
    private Context context;
    private JSONObject deviceRndNr;
    private BlockchainHandler blockchainHandler;

    DeviceHandler(Context myContext, MainActivity myMainActivity, BlockchainHandler myBlockchainHandler) {
        this.mainActivity = myMainActivity;
        this.context = myContext;
        this.blockchainHandler = myBlockchainHandler;
    }

    void setAddress(String deviceAddress) {
        //get device address and transform it to URL
        //TODO: for production deployment, user correct address and https setup with certificate
        this.deviceAddress = "http://" + deviceAddress + ":5000/";
    }

    private void authToDevice(String thenAction, HashMap<String, String> thenData) {
        //generate random number to pass as user_rnd
        Log.d("installedapps start", System.currentTimeMillis() + " ms");
        String rnd = UUID.randomUUID().toString().replace("-", "");
        JSONObject user_rnd = new JSONObject();
        try {
            user_rnd.put("user_rnd", rnd);
            new DeviceConnection(deviceAddress + "randomNumber", "auth", thenAction, thenData, user_rnd, this).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void getAppsInfo() {
        for(InstanceData.App app : MainActivity.instanceData.getCompatibleApps()) {
            new DeviceConnection(app.getServerAddress() + "appinfo.json", "appInfo", null, null, null, this).execute();
        }
    }

    void getInstalledApps() {
        authToDevice("getInstalledApps", null);
    }

    void deployApp(InstanceData.App app) {
        HashMap<String, String> thenData = new HashMap<>();
        thenData.put("guid", app.getAppGuid());
        thenData.put("app_url", app.getServerAddress());
        thenData.put("action", "install");
        authToDevice("deployApp", thenData);
    }

    void uninstallApp(InstanceData.App app) {
        HashMap<String, String> thenData = new HashMap<>();
        thenData.put("guid", app.getAppGuid());
        thenData.put("action", "uninstall");
        authToDevice("deployApp", thenData);
    }

    void registerToApp(InstanceData.App app) {
        HashMap<String, String> thenData = new HashMap<>();
        thenData.put("guid", app.getAppGuid());
        thenData.put("userAddress", blockchainHandler.getLoginStatus().getAddress());
        thenData.put("action", "registerToApp");
        authToDevice("userAction", thenData);
    }

    void unregisterFromApp(InstanceData.App app) {
        HashMap<String, String> thenData = new HashMap<>();
        thenData.put("guid", app.getAppGuid());
        thenData.put("userAddress", blockchainHandler.getLoginStatus().getAddress());
        thenData.put("action", "unregisterFromApp");
        authToDevice("userAction", thenData);
    }

    void accessApp(InstanceData.App app) {
        HashMap<String, String> thenData = new HashMap<>();
        thenData.put("guid", app.getAppGuid());
        thenData.put("userAddress", blockchainHandler.getLoginStatus().getAddress());
        thenData.put("action", "accessApp");
        authToDevice("userAction", thenData);
    }

    void restartContainer(InstanceData.App app) {
        HashMap<String, String> thenData = new HashMap<>();
        thenData.put("guid", app.getAppGuid());
        thenData.put("userAddress", blockchainHandler.getLoginStatus().getAddress());
        thenData.put("action", "restartContainer");
        authToDevice("technicianAction", thenData);
    }

    //This object represents responses from the IoT device
    private static class DeviceResponse {
        private int responseCode;
        private String responseString;

        DeviceResponse(int responseCode, String responseString) {
            this.responseCode = responseCode;
            this.responseString = responseString;
        }
    }

    //This class connects and interacts with the IoT Device
    private static class DeviceConnection extends AsyncTask<Void, Void, DeviceResponse> {
        private String deviceURL;
        private DeviceHandler deviceHandler;
        private String action;
        private JSONObject postJSON;
        private String thenAction;
        private HashMap<String, String> thenData;
        private long startTime, endTime;

        DeviceConnection(String deviceURL, String action, String thenAction, HashMap<String, String> thenData, JSONObject postJSON, DeviceHandler deviceHandler) {
            this.deviceURL = deviceURL;
            this.deviceHandler = deviceHandler;
            this.action = action;
            this.postJSON = postJSON;
            this.thenAction = thenAction;
            this.thenData = thenData;
            this.startTime = System.currentTimeMillis();
        }

        protected DeviceResponse doInBackground(Void... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            DeviceResponse deviceResponse;

            try {
                URL url = new URL(deviceURL);
                connection = (HttpURLConnection)url.openConnection();

                if (action.equals("getInstalledApps") || action.equals("auth") ||
                        action.equals("appInfo") || action.equals("deployApp") || action.equals("userAction") || action.equals("technicianAction")) {
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");

                    if(postJSON != null) {
                        OutputStream outputStream = connection.getOutputStream();
                        outputStream.write(postJSON.toString().getBytes(UTF_8));
                        outputStream.close();
                    }
                }

                //connection.connect();
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuffer buffer = new StringBuffer();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    deviceResponse = new DeviceResponse(responseCode, buffer.toString());
                    return deviceResponse;
                } else {
                    deviceResponse = new DeviceResponse(responseCode, null);
                    return deviceResponse;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            deviceResponse = new DeviceResponse(0, null);
            return deviceResponse;
        }

        protected void onPostExecute(DeviceResponse dR) {
            switch(dR.responseCode) {
                case 200:
                    switch(action) {
                        case "getInstalledApps":
                            try {
                                JSONObject json = new JSONObject(dR.responseString);
                                JSONArray list = json.getJSONArray("installedApps");
                                ArrayList<InstanceData.App> temp = new ArrayList<>();
                                ArrayList<InstanceData.App> compatibleApps = new ArrayList<>(MainActivity.instanceData.getCompatibleApps());

                                for(int i = 0; i < list.length(); i++) {
                                    for(InstanceData.App a : compatibleApps) {
                                        if (list.getString(i).equals(a.getAppGuid())) {
                                            temp.add(a);
                                        }
                                    }
                                }

                                /*for(int i = 0; i<list.length(); i++) {
                                    InstanceData.App thisApp = new InstanceData.App();
                                    thisApp.setAppGuid(list.getString(i));
                                    temp.add(thisApp);
                                }*/

                                MainActivity.instanceData.setInstalledApps(temp);
                                deviceHandler.blockchainHandler.getAppsServerAddress();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "deployApp":
                            try {
                                JSONObject json = new JSONObject(dR.responseString);
                                Intent i = new Intent("deploy_app");
                                i.putExtra("responseCode", dR.responseCode);
                                if(json.has("deployedAppGuid")) i.putExtra("deployed_app_guid", json.getString("deployedAppGuid"));
                                else if (json.has("uninstalledAppGuid")) i.putExtra("uninstalled_app_guid", json.getString("uninstalledAppGuid"));
                                LocalBroadcastManager.getInstance(MainActivity.instanceData.getDeviceHandler().context).sendBroadcast(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "userAction":
                            try {
                                JSONObject json = new JSONObject(dR.responseString);
                                Intent i = new Intent("user_action");
                                i.putExtra("responseCode", dR.responseCode);
                                if(json.has("registrationResult")) i.putExtra("registrationResult", json.getString("registrationResult"));
                                else if(json.has("access")) {
                                    i.putExtra("access", json.getString("access"));
                                    if(json.has("data")) i.putExtra("data", json.getString("data"));
                                }
                                LocalBroadcastManager.getInstance(MainActivity.instanceData.getDeviceHandler().context).sendBroadcast(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "technicianAction":
                            try {
                                JSONObject json = new JSONObject(dR.responseString);
                                Intent i = new Intent("technician_action");
                                i.putExtra("responseCode", dR.responseCode);
                                if(json.has("restarted")) i.putExtra("restarted", json.getString("restarted"));
                                LocalBroadcastManager.getInstance(MainActivity.instanceData.getDeviceHandler().context).sendBroadcast(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "auth":
                            try {
                                //Sign device_rnd and create JSON with user_rnd and signed device_rnd object
                                JSONObject json = new JSONObject(dR.responseString);

                                //add user_rnd
                                json.put("user_rnd", postJSON.getString("user_rnd"));

                                String plainText = json.getString("device_rnd");
                                Sign.SignatureData signedMessage = deviceHandler.blockchainHandler.signCrypto(plainText);
                                JSONObject signed_deviceRnd = new JSONObject();
                                signed_deviceRnd.put("r",  Numeric.toHexString(signedMessage.getR()));
                                signed_deviceRnd.put("s",  Numeric.toHexString(signedMessage.getS()));
                                signed_deviceRnd.put("v", signedMessage.getV());

                                //add signed_device_rnd
                                json.put("signed_device_rnd", signed_deviceRnd);

                                String pubKey ="";
                                String address="";
                                try {
                                    pubKey = Sign.signedMessageToKey(plainText.getBytes(), signedMessage).toString(16);
                                    address = "0x" + Keys.getAddress(pubKey);
                                } catch (SignatureException e) {
                                    e.printStackTrace();
                                }

                                //add user_address
                                json.put("user_address", address);

                                json.remove("device_rnd"); // remove old unsigned device_rnd

                                switch(thenAction) {
                                    case "getInstalledApps":
                                        new DeviceConnection(deviceHandler.deviceAddress + "installedApps", "getInstalledApps", null, null, json, deviceHandler).execute();
                                        break;
                                    case "deployApp":
                                        json.put("app_guid", thenData.get("guid"));
                                        json.put("app_url", thenData.get("app_url"));
                                        json.put("action", thenData.get("action"));
                                        new DeviceConnection(deviceHandler.deviceAddress + "deployApp", "deployApp", null,null, json, deviceHandler).execute();
                                        break;
                                    case "userAction":
                                        json.put("app_guid", thenData.get("guid"));
                                        json.put("action", thenData.get("action"));
                                        json.put("user_address", thenData.get("userAddress"));
                                        new DeviceConnection(deviceHandler.deviceAddress + "userAction", "userAction", null,null, json, deviceHandler).execute();
                                        break;
                                    case "technicianAction":
                                        json.put("app_guid", thenData.get("guid"));
                                        json.put("action", thenData.get("action"));
                                        json.put("user_address", thenData.get("userAddress"));
                                        new DeviceConnection(deviceHandler.deviceAddress + "technicianAction", "technicianAction", null,null, json, deviceHandler).execute();
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "appInfo":
                            try {
                                JSONObject json = new JSONObject(dR.responseString);
                                String appName = json.getString("name");
                                String appGuid = json.getString("guid");
                                InstanceData.App thisApp;
                                for(InstanceData.App a : MainActivity.instanceData.getCompatibleApps()) {
                                    if (a.getAppGuid().equals(appGuid)) {
                                        thisApp = a;
                                        thisApp.setAppName(appName);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("installedapps end", System.currentTimeMillis() + " ms");
                            break;
                    }
                    break;
                case 550: //checksum error
                    switch(action) {
                        case "deployApp":
                            Intent i = new Intent("deploy_app");
                            i.putExtra("responseCode", dR.responseCode);
                            LocalBroadcastManager.getInstance(MainActivity.instanceData.getDeviceHandler().context).sendBroadcast(i);
                            break;
                    }
                    break;
                case 403:
                    switch(action) {
                        case "deployApp":
                            Intent i = new Intent("deploy_app");
                            i.putExtra("responseCode", dR.responseCode);
                            LocalBroadcastManager.getInstance(MainActivity.instanceData.getDeviceHandler().context).sendBroadcast(i);
                            break;
                    }
                    break;
                default:
                    Snackbar.make(deviceHandler.mainActivity.findViewById(R.id.activitiMainCoordinatorLayout), "An error occurred: " + dR.responseCode, Snackbar.LENGTH_LONG)
                            .show();
                    break;
            }
        }
    }
}

