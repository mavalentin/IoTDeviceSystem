package it.manuelvalentin.iotdevicemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


class BlockchainHandler {

    private static final String TAG = BlockchainHandler.class.getName();
    private static final String accountsContractAddress = AccountsContract._addresses.get("3");
    private static final String deviceContractAddress = DeviceContract._addresses.get("3");
    private static final String appContractAddress = AppContract._addresses.get("3");
    private String myPrivateKey;
    private Credentials credentials;
    private BigInteger gasLimit = new BigInteger("3000000");
    private BigInteger gasPrice = new BigInteger("3000000000");
    private Context context;
    private Web3j web3j;
    private LoginStatus loginStatus = new LoginStatus();
    private MainActivity mainActivity;
    private Activity activity;

    BlockchainHandler(Context myContext, MainActivity myMainActivity) {
        context = myContext;
        mainActivity = myMainActivity;

        //Importing saved state
        SharedPreferences sharedPreferences = context.getSharedPreferences("blockchainHandler", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("loginState", false)) {
            loginStatus.setState(true);
            loginStatus.setRole(sharedPreferences.getString("userRole", "undefined"));
            loginStatus.setAddress(sharedPreferences.getString("userAddress", "undefined"));
            setPrivateKey(sharedPreferences.getString("privateKey", "undefined"));
        }

        web3j = Web3j.build(new HttpService(context.getString(R.string.nodeIP)));
        try {
            Web3ClientVersion clientVersion = web3j.web3ClientVersion().sendAsync().get();
            if(!clientVersion.hasError()){
                Log.d(TAG,"Connection to blockchain node successful");
            }
            else {
                Log.e(TAG, "Connection to blockchain node failed");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Connection to blockchain node failed");
        }
    }

    void setCurrentActivity(Activity a) {
        activity = a;
    }

    private static <T> void saveInPref(String key, T value, @NonNull BlockchainHandler blockchainHandler){
        SharedPreferences sharedPreferences = blockchainHandler.context.getSharedPreferences("blockchainHandler", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(value instanceof String) {
            editor.putString(key, (String)value);
        } else if(value.getClass().equals(Boolean.class)) {
            editor.putBoolean(key, (Boolean)value);
        }
        editor.apply();
    }

    private void saveLogin() {
        saveInPref("loginState", loginStatus.getState(), this);
        saveInPref("userRole", loginStatus.getRole(), this);
        saveInPref("userAddress", loginStatus.getAddress(), this);
        saveInPref("privateKey", myPrivateKey, this);
    }

    void identifyUser() {
        new IdentifyUser(web3j, credentials, gasPrice, gasLimit, this).execute();
    }

    private static void finalizeLogin(BigInteger role, BlockchainHandler blockchainHandler) {
        String userRole;
        LoginStatus loginStatus = blockchainHandler.getLoginStatus();
        Context context = blockchainHandler.context;
        loginStatus.setAddress(blockchainHandler.credentials.getAddress());
        switch (role.intValue()) {
            case 1:
                userRole = context.getResources().getString(R.string.developer);
                loginStatus.setState(true);
                loginStatus.setRole("developer");
                blockchainHandler.saveLogin();
                break;
            case 2:
                userRole = context.getResources().getString(R.string.technician);
                loginStatus.setState(true);
                loginStatus.setRole("technician");
                blockchainHandler.saveLogin();
                break;
            case 3:
                userRole = context.getResources().getString(R.string.user);
                loginStatus.setState(true);
                loginStatus.setRole("user");
                blockchainHandler.saveLogin();
                break;
            default:
                userRole = context.getResources().getString(R.string.no_login);
                loginStatus.setState(false);
                loginStatus.setRole("");
                blockchainHandler.saveLogin();
                break;
        }
        blockchainHandler.mainActivity.initializeNavDrawerTitle();
        if(loginStatus.getState()) {
            Snackbar.make(((Activity) context).findViewById(R.id.activitiMainCoordinatorLayout), "Logged in as: " + userRole, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            blockchainHandler.mainActivity.stopBarcodeDetector();
            Snackbar.make(((Activity)context).findViewById(R.id.activitiMainCoordinatorLayout), "Error logging in", Snackbar.LENGTH_LONG)
                    .show();
            Log.e(TAG, "Login error");
        }
    }

    void logout() {
        this.setPrivateKey("");
        this.getLoginStatus().clear();
        saveLogin();
        this.mainActivity.initializeNavDrawerTitle();
    }

    Sign.SignatureData signCrypto(String plainText) {
        Sign.SignatureData signatureData = Sign.signMessage(plainText.getBytes(), mainActivity.blockchainHandler.credentials.getEcKeyPair());

        return signatureData;
    }

    void shutdownConnection() {
        web3j.shutdown();
    }

    void getDeviceAddress(BigInteger guid) {
        new GetDeviceAddress(web3j, credentials, gasPrice, gasLimit, this).execute(guid);
    }

    void getCompatibleApps(BigInteger guid) {
        new GetCompatibleApps(web3j, credentials, gasPrice, gasLimit, this).execute(guid);
    }

    void activateSubscription(BigInteger guid) {
        new ActivateSubscription(web3j, credentials, gasPrice, gasLimit, this).execute(guid);
    }
    void deactivateSubscription(BigInteger guid) {
        new DeactivateSubscription(web3j, credentials, gasPrice, gasLimit, this).execute(guid);
    }

    void getAppsServerAddress() {
        int size = MainActivity.instanceData.getCompatibleApps().size();
        BigInteger[] guids = new BigInteger[size];

        for(int i = 0; i < size; i++) {
            guids[i] = new BigInteger(MainActivity.instanceData.getCompatibleApps().get(i).getAppGuid(), 16);
        }

        new GetAppServerAddress(web3j, credentials, gasPrice, gasLimit, this).execute(guids);
    }

    LoginStatus getLoginStatus() {
        return loginStatus;
    }

    void setPrivateKey(String key) {
        myPrivateKey = key;
        if(!key.equals("")) {
            credentials = Credentials.create(myPrivateKey);
        }
        else {
            credentials = null;
        }
    }

    private static class IdentifyUser extends AsyncTask<Void, Void, BigInteger> {
        private Web3j web3j;
        private Credentials credentials;
        private BigInteger gasPrice;
        private BigInteger gasLimit;
        private BlockchainHandler blockchainHandler;
        private long startTime;
        private long endTime;

        IdentifyUser(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BlockchainHandler blockchainHandler) {
            this.web3j = web3j;
            this.credentials = credentials;
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
            this.blockchainHandler = blockchainHandler;
            startTime = System.currentTimeMillis();
        }

        protected BigInteger doInBackground(Void... params) {
            AccountsContract accountsContract = AccountsContract.load(accountsContractAddress, web3j, credentials, gasPrice, gasLimit);
            BigInteger role = null;
            try {
                role = accountsContract.identifySelf().send();
            } catch (Exception e) {
                e.printStackTrace();
                finalizeLogin(new BigInteger("0"), blockchainHandler);
            }
            return role;
        }

        protected void onPostExecute(BigInteger role) {
            if(role!=null) {
                finalizeLogin(role, blockchainHandler);
                endTime = System.currentTimeMillis();
                Log.d("exec time idenfityUser", endTime-startTime + " ms");
            }
        }
    }

    private static class GetDeviceAddress extends AsyncTask<BigInteger, Void, String> {
        private Web3j web3j;
        private Credentials credentials;
        private BigInteger gasPrice;
        private BigInteger gasLimit;
        private BlockchainHandler blockchainHandler;
        private long startTime;
        private long endTime;

        GetDeviceAddress(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BlockchainHandler blockchainHandler) {
            this.web3j = web3j;
            this.credentials = credentials;
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
            this.blockchainHandler = blockchainHandler;
            this.startTime = System.currentTimeMillis();
        }

        protected String doInBackground(BigInteger... guid) {
            DeviceContract identifyDeviceContract = DeviceContract.load(deviceContractAddress, web3j, credentials, gasPrice, gasLimit);
            String deviceAddress = null;
            try {
                deviceAddress = identifyDeviceContract.getDeviceAddress(guid[0]).send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return deviceAddress;
        }

        protected void onPostExecute(String deviceAddress) {
            this.endTime = System.currentTimeMillis();
            MainActivity.instanceData.setDeviceAddress(deviceAddress);
            Snackbar.make(((Activity)blockchainHandler.context).findViewById(R.id.activitiMainCoordinatorLayout), "Device address: " + deviceAddress, Snackbar.LENGTH_LONG)
                    .show();
            blockchainHandler.mainActivity.initializeNavDrawerSubtitle();
            Log.d("exec time getDeviceAddr", endTime-startTime + " ms");
        }
    }

    private static class ActivateSubscription extends AsyncTask<BigInteger, Void, Pair<Boolean, BigInteger>> {
        private Web3j web3j;
        private Credentials credentials;
        private BigInteger gasPrice;
        private BigInteger gasLimit;
        private BlockchainHandler blockchainHandler;
        private View progressOverlay;

        ActivateSubscription(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BlockchainHandler blockchainHandler) {
            this.web3j = web3j;
            this.credentials = credentials;
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
            this.blockchainHandler = blockchainHandler;

        }

        protected void onPreExecute() {
            progressOverlay = blockchainHandler.activity.findViewById(R.id.progress_overlay);
            progressOverlay.setVisibility(View.VISIBLE);
        }

        protected Pair<Boolean, BigInteger> doInBackground(BigInteger... guid) {
            AccountsContract accountsContract = AccountsContract.load(accountsContractAddress, web3j, credentials, gasPrice, gasLimit);
            AppContract appContract = AppContract.load(appContractAddress, web3j, credentials, gasPrice, gasLimit);
            BigInteger price;
            boolean result = false;
            try {
                price = appContract.getSubscriptionPrice(guid[0]).send();
                TransactionReceipt transactionReceipt = accountsContract.activateSubscription(guid[0], price).send();
                if(transactionReceipt.getStatus().equals("0x1")) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Pair<>(result, guid[0]);
        }

        protected void onPostExecute(Pair<Boolean, BigInteger> result) {
            progressOverlay.setVisibility(View.INVISIBLE);
            if(result.first) {
                InstanceData.App app = new InstanceData.App();
                app.setAppGuid(result.second.toString(16));
                MainActivity.instanceData.getDeviceHandler().registerToApp(app);
            } else {
                Intent i = new Intent("user_action");
                i.putExtra("responseCode", "409");
                LocalBroadcastManager.getInstance(MainActivity.instanceData.getBlockchainHandler().context).sendBroadcast(i);
            }
        }
    }


    private static class DeactivateSubscription extends AsyncTask<BigInteger, Void, Pair<Boolean, BigInteger>> {
        private Web3j web3j;
        private Credentials credentials;
        private BigInteger gasPrice;
        private BigInteger gasLimit;
        private BlockchainHandler blockchainHandler;
        private View progressOverlay;

        DeactivateSubscription(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BlockchainHandler blockchainHandler) {
            this.web3j = web3j;
            this.credentials = credentials;
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
            this.blockchainHandler = blockchainHandler;

        }

        protected void onPreExecute() {
            progressOverlay = blockchainHandler.activity.findViewById(R.id.progress_overlay);
            progressOverlay.setVisibility(View.VISIBLE);
        }

        protected Pair<Boolean, BigInteger> doInBackground(BigInteger... guid) {
            AccountsContract accountsContract = AccountsContract.load(accountsContractAddress, web3j, credentials, gasPrice, gasLimit);
            AppContract appContract = AppContract.load(appContractAddress, web3j, credentials, gasPrice, gasLimit);
            boolean result = false;
            try {
                TransactionReceipt transactionReceipt = accountsContract.cancelSubscription(guid[0]).send();
                if(transactionReceipt.getStatus().equals("0x1")) {
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Pair<>(result, guid[0]);
        }

        protected void onPostExecute(Pair<Boolean, BigInteger> result) {
            progressOverlay.setVisibility(View.INVISIBLE);
            if(result.first) {
                InstanceData.App app = new InstanceData.App();
                app.setAppGuid(result.second.toString(16));
                MainActivity.instanceData.getDeviceHandler().unregisterFromApp(app);
            } else {
                Intent i = new Intent("user_action");
                i.putExtra("responseCode", "409");
                LocalBroadcastManager.getInstance(MainActivity.instanceData.getBlockchainHandler().context).sendBroadcast(i);
            }
        }
    }


    private static class GetCompatibleApps extends AsyncTask<BigInteger, Void, List<BigInteger>> {
        private Web3j web3j;
        private Credentials credentials;
        private BigInteger gasPrice;
        private BigInteger gasLimit;
        private BlockchainHandler blockchainHandler;
        private long startTime;
        private long endTime;

        GetCompatibleApps(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BlockchainHandler blockchainHandler) {
            this.web3j = web3j;
            this.credentials = credentials;
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
            this.blockchainHandler = blockchainHandler;
            this.startTime = System.currentTimeMillis();
        }

        protected List<BigInteger> doInBackground(BigInteger... guid) {
            DeviceContract deviceContract = DeviceContract.load(deviceContractAddress, web3j, credentials, gasPrice, gasLimit);
            List apps = null;
            try {
                apps = deviceContract.getCompatibleApps(guid[0]).send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return apps;
        }

        protected void onPostExecute(List<BigInteger> apps) {
            /*NavigationView navigationView = ((Activity)blockchainHandler.context).findViewById(R.id.nav_view);
            Menu navigationVievMenu = navigationView.getMenu();
            navigationVievMenu.removeGroup(R.id.compatibleApps);*/
            this.endTime = System.currentTimeMillis();
            Log.d("exec time getCompApps", endTime-startTime + " ms");
            ArrayList<InstanceData.App> temp = new ArrayList<>();
            for(BigInteger app : apps) {
                InstanceData.App thisApp = new InstanceData.App();
                thisApp.setAppGuid(app.toString(16));
                temp.add(thisApp);
            }
            MainActivity.instanceData.setCompatibleApps(temp);
            blockchainHandler.mainActivity.deviceHandler.setAddress(MainActivity.instanceData.getDeviceAddress());
            blockchainHandler.mainActivity.deviceHandler.getInstalledApps();
        }
    }

    private static class GetAppServerAddress extends AsyncTask<BigInteger, Void, InstanceData.App> {
        private Web3j web3j;
        private Credentials credentials;
        private BigInteger gasPrice;
        private BigInteger gasLimit;
        private BlockchainHandler blockchainHandler;

        GetAppServerAddress(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BlockchainHandler blockchainHandler) {
            this.web3j = web3j;
            this.credentials = credentials;
            this.gasPrice = gasPrice;
            this.gasLimit = gasLimit;
            this.blockchainHandler = blockchainHandler;
        }

        protected InstanceData.App doInBackground(BigInteger... guid) {
            AppContract appContract = AppContract.load(appContractAddress, web3j, credentials, gasPrice, gasLimit);
            for(BigInteger b : guid) {
                String address;
                InstanceData.App thisApp;
                try {
                    address = appContract.getServerAddress(b).send();
                    for(InstanceData.App a : MainActivity.instanceData.getCompatibleApps()) {
                        if (a.getAppGuid().equals(b.toString(16))) {
                            thisApp = a;
                            thisApp.setServerAddress(address);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(InstanceData.App app) {
            blockchainHandler.mainActivity.deviceHandler.getAppsInfo();
        }
    }


    class LoginStatus {
        private boolean state = false;
        private String role = "";
        private String address = "";

        void clear() {
            state = false;
            role = "";
            address = "";
        }

        boolean getState() {
            return state;
        }

        String getRole() {
            return role;
        }

        String getAddress() {
            return address;
        }

        void setState(boolean state) {
            this.state = state;
        }

        void setRole(String role) {
            this.role = role;
        }

        void setAddress(String address) {
            this.address = address;
        }
    }
}