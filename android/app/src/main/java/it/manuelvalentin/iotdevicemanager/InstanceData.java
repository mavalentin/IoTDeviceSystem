package it.manuelvalentin.iotdevicemanager;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class InstanceData {
    private String deviceAddress;
    private ArrayList<App> compatibleApps = new ArrayList<>();
    private ArrayList<App> installedApps = new ArrayList<>();
    private BlockchainHandler blockchainHandler;
    private DeviceHandler deviceHandler;

    public void removeInstalledApp(String guid) {
        for(App app : installedApps) {
            installedApps.remove(app);
        }
    }
    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public ArrayList<App> getCompatibleApps() {
        return compatibleApps;
    }

    public void setCompatibleApps(ArrayList<App> compatibleApps) {
        this.compatibleApps = compatibleApps;
    }

    public ArrayList<App> getInstalledApps() {
        return installedApps;
    }

    public void setInstalledApps(ArrayList<App> apps) {
        this.installedApps = apps;
    }

    public void setBlockchainHandler(BlockchainHandler blockchainHandler) {
        this.blockchainHandler = blockchainHandler;
    }

    public BlockchainHandler getBlockchainHandler() {
        return blockchainHandler;
    }

    public void setDeviceHandler(DeviceHandler deviceHandler) {
        this.deviceHandler = deviceHandler;
    }

    public DeviceHandler getDeviceHandler() {
        return deviceHandler;
    }

    public static class App implements Parcelable {
        private String serverAddress;
        private String appName;
        private String appGuid;

        /*public App(String serverAddress, String appName, String appGuid) {
            this.serverAddress = serverAddress;
            this.appName = appName;
            this.appGuid = appGuid;
        }*/

        public App() {

        }

        protected App(Parcel in) {
            serverAddress = in.readString();
            appName = in.readString();
            appGuid = in.readString();
        }

        public static final Creator<App> CREATOR = new Creator<App>() {
            @Override
            public App createFromParcel(Parcel in) {
                return new App(in);
            }

            @Override
            public App[] newArray(int size) {
                return new App[size];
            }
        };

        public String getServerAddress() {
            return serverAddress;
        }

        public void setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppGuid() {
            return appGuid;
        }

        public void setAppGuid(String appGuid) {
            this.appGuid = appGuid;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(serverAddress);
            dest.writeString(appName);
            dest.writeString(appGuid);
        }
    }
}
