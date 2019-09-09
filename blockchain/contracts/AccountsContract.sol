pragma solidity >=0.4.22 <0.6.0;
contract AccountsContract {

    //VARIABLES
    address owner;
    AppContractInterface appContract;
    enum userRoles {inexistent, developer, technician, user} //inexistent: 0; developer: 1; technician: 2; user: 3
    struct User {
        userRoles role;
        mapping (uint128 => App) appRegister;
        uint balance;
    }
    struct App {
        uint subscriptionExpirationTime;
    }
    mapping (address => User) users;


    //CONSTRUCTOR
    constructor() public {
        owner = msg.sender;
    }


    //MODIFIERS
    modifier exists(address userAddress) {
       require(users[userAddress].role != userRoles.inexistent, "User is not registered");
        _;
    }
    modifier existsSelf() {
       require(users[msg.sender].role != userRoles.inexistent, "User is not registered");
        _;
    }
    modifier appExists(uint128 appGuid) {
        require(appContract.appExists(appGuid), "App does not exist");
        _;
    }


    //FUNCTIONS
    function registerUser(address userAddress, userRoles role) public {
        require(msg.sender == owner, "Not allowed to register users");
        require(users[userAddress].role == userRoles.inexistent, "User is registered already");
        User memory user;
        user.role = role;
        users[userAddress] = user;
    }
    function activateSubscription(uint128 appGuid) public payable appExists(appGuid) {
        require(users[msg.sender].appRegister[appGuid].subscriptionExpirationTime <= now, "Subscription still valid");
        uint price = appContract.getSubscriptionPrice(appGuid);

        //execute activation
        require(price == msg.value, "Wrong amount");
        users[appContract.getDeveloper(appGuid)].balance += msg.value;
        uint32 appExpiration = appContract.getSubscriptionDuration(appGuid);
        users[msg.sender].appRegister[appGuid].subscriptionExpirationTime = now + appExpiration;
    }
    function cancelSubscription(uint128 appGuid) public {
        users[msg.sender].appRegister[appGuid].subscriptionExpirationTime = 0;
    }
    function withdrawBalance() public {
        uint amount = users[msg.sender].balance;
        users[msg.sender].balance = 0;
        msg.sender.transfer(amount);
    }


    //GETTERS
    function getUserRole(address userAddress) private view exists(userAddress) returns(userRoles) {
        return users[userAddress].role;
    }
    function getAppExpiration(address userAddress, uint128 appGuid) public view exists(userAddress) appExists(appGuid) returns(uint) {
        return users[userAddress].appRegister[appGuid].subscriptionExpirationTime;
    }
    function getBalance() public view returns(uint) {
        return users[msg.sender].balance;
    }
    //get user role of calling account
    function identifySelf() public view existsSelf() returns(userRoles) {
        return getUserRole(msg.sender);
    }
    //read other users' roles
    function identifyUser(address userAddress) public view exists(userAddress) returns(userRoles) {
        return getUserRole(userAddress);
    }


    //SETTERS
    function setAppContractAddress(address appContractAddress) public {
        require(msg.sender == owner, "Not allowed");
        appContract = AppContractInterface(appContractAddress);
    }
}


//CONTRACT INTERFACES
contract AppContractInterface {
    function getSubscriptionDuration(uint128 appGuid) public view returns(uint32);
    function getSubscriptionPrice(uint128 appGuid) public view returns(uint);
    function getDeveloper(uint128 appGuid) public view returns(address);
    function appExists(uint128 appGuid) public view returns(bool);
    function setAccountsContractAddress(address accountsContractAddress, address caller) public;
}
