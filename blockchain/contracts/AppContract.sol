pragma solidity >=0.4.22 <0.6.0;
contract AppContract {

    //VARIABLES
    address owner;
    AccountsContractInterface accountsContract;
    struct App {
        bool exists;
        string appServerAddress;
        uint256 checksum; //SHA3-256 hash
        uint32 subscriptionDuration; //seconds
        uint subscriptionPrice; //wei
        address developer;
    }
    mapping (uint128 => App) apps; //appGuid => App


    //CONSTRUCTOR
    constructor() public {
        owner = msg.sender;
    }


    //MODIFIERS
    modifier exists(uint128 appGuid) {
       require(apps[appGuid].exists, "App does not exist");
        _;
    }
    modifier onlyAppDeveloper(uint128 appGuid) {
        require(msg.sender == apps[appGuid].developer, "Not allowed");
        _;
    }


    //FUNCTIONS
    function registerApp(uint128 appGuid, string memory myAppServerAddress, uint256 myChecksum, uint32 mySubscriptionDuration, uint mySubscriptionPrice) public {
        require(!apps[appGuid].exists, "App is registered already");
        uint role = accountsContract.identifyUser(msg.sender);
        require(role == 1, "Not allowed to register apps"); //must be a developer
        App memory app;
        app.exists = true;
        app.appServerAddress = myAppServerAddress;
        app.checksum = myChecksum;
        app.subscriptionDuration = mySubscriptionDuration;
        app.subscriptionPrice = mySubscriptionPrice;
        app.developer = msg.sender;
        apps[appGuid] = app;
    }
    function eliminateApp(uint128 appGuid) public onlyAppDeveloper(appGuid) {
        apps[appGuid].exists = false;
        delete apps[appGuid].appServerAddress;
        delete apps[appGuid].checksum;
        delete apps[appGuid].subscriptionDuration;
        delete apps[appGuid].subscriptionPrice;
        delete apps[appGuid].developer;
    }


    //GETTERS
    function getDeveloper(uint128 appGuid) public view exists(appGuid) returns(address) {
        return apps[appGuid].developer;
    }
    function getServerAddress(uint128 appGuid) public view exists(appGuid) returns(string memory) {
        return apps[appGuid].appServerAddress;
    }
    function getChecksum(uint128 appGuid) public view exists(appGuid) returns(uint256) {
        return apps[appGuid].checksum;
    }
    function getSubscriptionDuration(uint128 appGuid) public view exists(appGuid) returns(uint32) {
        return apps[appGuid].subscriptionDuration;
    }
    function getSubscriptionPrice(uint128 appGuid) public view exists(appGuid) returns(uint) {
        return apps[appGuid].subscriptionPrice;
    }
    function appExists(uint128 appGuid) public view returns(bool) {
        return apps[appGuid].exists;
    }


    //SETTERS
    function setSubscriptionDuration(uint128 appGuid, uint32 duration) public exists(appGuid) onlyAppDeveloper(appGuid) {
        apps[appGuid].subscriptionDuration = duration;
    }
    function setSubscriptionPrice(uint128 appGuid, uint price) public exists(appGuid) onlyAppDeveloper(appGuid) {
        apps[appGuid].subscriptionPrice = price;
    }
    function setServerAddress(uint128 appGuid, string memory serverAddress) public exists(appGuid) onlyAppDeveloper(appGuid) {
        apps[appGuid].appServerAddress = serverAddress;
    }
    function setChecksum(uint128 appGuid, uint256 checksum) public exists(appGuid) onlyAppDeveloper(appGuid) {
        apps[appGuid].checksum = checksum;
    }
    function setAccountsContractAddress(address accountsContractAddress) public {
        require(msg.sender == owner, "Not allowed");
        accountsContract = AccountsContractInterface(accountsContractAddress);
    }
}


//CONTRACT INTERFACES
contract AccountsContractInterface {
    function identifyUser(address userAddress) public view returns(uint);
}
