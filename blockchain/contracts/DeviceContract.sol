pragma solidity >=0.4.22 <0.6.0;
contract DeviceContract {

    //VARIABLES
    address owner;
    struct Device {
        bool exists;
        string deviceAddress;
        uint128 deviceType;
        address deviceBlockchainAddress;
    }
    //typeGuid => appsList
    mapping (uint128 => uint128[]) appCompatibility;
    //deviceGuid => Device
    mapping (uint128 => Device) devices;


    //CONSTRUCTOR
    constructor() public {
        owner = msg.sender;
    }


    //MODIFIERS
    modifier exists(uint128 deviceGuid) {
       require(devices[deviceGuid].exists, "Device is not registered");
        _;
    }
    modifier onlyOwner() {
       require(msg.sender == owner, "Not allowed");
        _;
    }


    //FUNCTIONS
    function registerDevice(uint128 deviceGuid, address myDeviceBlockchainAddress, string memory myDeviceAddress, uint128 myType) public onlyOwner() {
        require(!devices[deviceGuid].exists, "Device is registered already");
        Device memory device;
        device.exists = true;
        device.deviceAddress = myDeviceAddress;
        device.deviceType = myType;
        device.deviceBlockchainAddress = myDeviceBlockchainAddress;
        devices[deviceGuid] = device;
    }
    function unregisterDevice(uint128 deviceGuid) public exists(deviceGuid) {
        devices[deviceGuid].exists = false;
        delete devices[deviceGuid].deviceAddress;
        delete devices[deviceGuid].deviceType;
        delete devices[deviceGuid].deviceBlockchainAddress;
    }
    function addCompatibleApp(uint128 typeGuid, uint128 appId) public onlyOwner() {
        uint existing = 0;
        for(uint i = 0; i < appCompatibility[typeGuid].length; i++) {
            if(appCompatibility[typeGuid][i] == appId) {
                existing = 1;
            }
        }
        require(existing != 1, "App is set already to this device type");
        appCompatibility[typeGuid].push(appId);
    }


    //SETTERS
    function setDeviceAddress(uint128 deviceGuid, string memory myDeviceAddress) public onlyOwner() exists(deviceGuid) {
        devices[deviceGuid].deviceAddress = myDeviceAddress;
    }
    function setDeviceType(uint128 deviceGuid, uint128 myType) public onlyOwner() exists(deviceGuid) {
        devices[deviceGuid].deviceType = myType;
    }
    function setDeviceBlockchainAddress(uint128 deviceGuid, address myDeviceBlockchainAddress) public onlyOwner() exists(deviceGuid) {
        devices[deviceGuid].deviceBlockchainAddress = myDeviceBlockchainAddress;
    }


    //GETTERS
    function getDeviceAddress(uint128 deviceGuid) public view exists(deviceGuid) returns(string memory) {
        return devices[deviceGuid].deviceAddress;
    }
    function getCompatibleApps(uint128 deviceGuid) public view exists(deviceGuid) returns(uint128[] memory) {
        return appCompatibility[devices[deviceGuid].deviceType];
    }

}
