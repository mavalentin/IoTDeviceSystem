package it.manuelvalentin.iotdevicemanager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint128;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.0.1.
 */
public class DeviceContract extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550611882806100606000396000f3fe60806040526004361061008e576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630980aa6814610093578063117c9fb3146101745780631581b6491461023a57806349384820146102dc5780637e796a6e146103fc5780638241ac9614610465578063c73d0cce146104b2578063cdd2ff911461051f575b600080fd5b34801561009f57600080fd5b50610172600480360360408110156100b657600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190803590602001906401000000008111156100ef57600080fd5b82018360208201111561010157600080fd5b8035906020019184602083028401116401000000008311171561012357600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600081840152601f19601f820116905080830192505050505050509192919290505050610603565b005b34801561018057600080fd5b506101bf6004803603602081101561019757600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190505050610717565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101ff5780820151818401526020810190506101e4565b50505050905090810190601f16801561022c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561024657600080fd5b506102856004803603602081101561025d57600080fd5b8101908080356fffffffffffffffffffffffffffffffff1690602001909291905050506108b1565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b838110156102c85780820151818401526020810190506102ad565b505050509050019250505060405180910390f35b3480156102e857600080fd5b506103fa600480360360808110156102ff57600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561035857600080fd5b82018360208201111561036a57600080fd5b8035906020019184600183028401116401000000008311171561038c57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929080356fffffffffffffffffffffffffffffffff169060200190929190505050610a98565b005b34801561040857600080fd5b506104636004803603604081101561041f57600080fd5b8101908080356fffffffffffffffffffffffffffffffff16906020019092919080356fffffffffffffffffffffffffffffffff169060200190929190505050610da8565b005b34801561047157600080fd5b506104b06004803603602081101561048857600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190505050610f9f565b005b3480156104be57600080fd5b5061051d600480360360408110156104d557600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506111af565b005b34801561052b57600080fd5b506106016004803603604081101561054257600080fd5b8101908080356fffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561057b57600080fd5b82018360208201111561058d57600080fd5b803590602001918460018302840111640100000000831117156105af57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506113ae565b005b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156106c7576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b8060016000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000209080519060200190610712929190611583565b505050565b60608160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff1615156107d6576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f446576696365206973206e6f742072656769737465726564000000000000000081525060200191505060405180910390fd5b60026000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108a45780601f10610879576101008083540402835291602001916108a4565b820191906000526020600020905b81548152906001019060200180831161088757829003601f168201915b5050505050915050919050565b60608160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610970576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f446576696365206973206e6f742072656769737465726564000000000000000081525060200191505060405180910390fd5b6001600060026000866fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060020160009054906101000a90046fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff168152602001908152602001600020805480602002602001604051908101604052809291908181526020018280548015610a8b57602002820191906000526020600020906000905b82829054906101000a90046fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019060100190602082600f01049283019260010382029150808411610a365790505b5050505050915050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610b5c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b60026000856fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151515610c19576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601c8152602001807f446576696365206973207265676973746572656420616c72656164790000000081525060200191505060405180910390fd5b610c21611657565b60018160000190151590811515815250508281602001819052508181604001906fffffffffffffffffffffffffffffffff1690816fffffffffffffffffffffffffffffffff168152505083816060019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250508060026000876fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548160ff0219169083151502179055506020820151816001019080519060200190610d179291906116aa565b5060408201518160020160006101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff16021790555060608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055509050505050505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610e6c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b8160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610f29576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f446576696365206973206e6f742072656769737465726564000000000000000081525060200191505060405180910390fd5b8160026000856fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060020160006101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff160217905550505050565b8060026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151561105c576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f446576696365206973206e6f742072656769737465726564000000000000000081525060200191505060405180910390fd5b600060026000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff02191690831515021790555060026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060010160006110f5919061172a565b60026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060020160006101000a8154906fffffffffffffffffffffffffffffffff021916905560026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060030160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611273576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b8160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515611330576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f446576696365206973206e6f742072656769737465726564000000000000000081525060200191505060405180910390fd5b8160026000856fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611472576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b8160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151561152f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f446576696365206973206e6f742072656769737465726564000000000000000081525060200191505060405180910390fd5b8160026000856fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff168152602001908152602001600020600101908051906020019061157d929190611772565b50505050565b828054828255906000526020600020906001016002900481019282156116465791602002820160005b8382111561160857835183826101000a8154816fffffffffffffffffffffffffffffffff02191690836fffffffffffffffffffffffffffffffff1602179055509260200192601001602081600f010492830192600103026115ac565b80156116445782816101000a8154906fffffffffffffffffffffffffffffffff0219169055601001602081600f01049283019260010302611608565b505b50905061165391906117f2565b5090565b6080604051908101604052806000151581526020016060815260200160006fffffffffffffffffffffffffffffffff168152602001600073ffffffffffffffffffffffffffffffffffffffff1681525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106116eb57805160ff1916838001178555611719565b82800160010185558215611719579182015b828111156117185782518255916020019190600101906116fd565b5b5090506117269190611831565b5090565b50805460018160011615610100020316600290046000825580601f10611750575061176f565b601f01602090049060005260206000209081019061176e9190611831565b5b50565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106117b357805160ff19168380011785556117e1565b828001600101855582156117e1579182015b828111156117e05782518255916020019190600101906117c5565b5b5090506117ee9190611831565b5090565b61182e91905b8082111561182a57600081816101000a8154906fffffffffffffffffffffffffffffffff0219169055506001016117f8565b5090565b90565b61185391905b8082111561184f576000816000905550600101611837565b5090565b9056fea165627a7a723058202dd1fab7718b5507e7a8236f26ff258d6b87fe0ed0bfbb2b4560de22dc9fbfc80029";

    public static final String FUNC_REGISTERDEVICE = "registerDevice";

    public static final String FUNC_UNREGISTERDEVICE = "unregisterDevice";

    public static final String FUNC_SETCOMPATIBLEAPPLICATIONS = "setCompatibleApplications";

    public static final String FUNC_SETDEVICEADDRESS = "setDeviceAddress";

    public static final String FUNC_SETDEVICETYPE = "setDeviceType";

    public static final String FUNC_SETDEVICEBLOCKCHAINADDRESS = "setDeviceBlockchainAddress";

    public static final String FUNC_GETDEVICEADDRESS = "getDeviceAddress";

    public static final String FUNC_GETCOMPATIBLEAPPS = "getCompatibleApps";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("3", "0x5a78f1af6385015BC0311C5B18990330D562EcbD");
    }

    @Deprecated
    protected DeviceContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DeviceContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DeviceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DeviceContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> registerDevice(BigInteger deviceGuid, String myDeviceBlockchainAddress, String myDeviceAddress, BigInteger myType) {
        final Function function = new Function(
                FUNC_REGISTERDEVICE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(deviceGuid), 
                new org.web3j.abi.datatypes.Address(myDeviceBlockchainAddress), 
                new org.web3j.abi.datatypes.Utf8String(myDeviceAddress), 
                new org.web3j.abi.datatypes.generated.Uint128(myType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> unregisterDevice(BigInteger deviceGuid) {
        final Function function = new Function(
                FUNC_UNREGISTERDEVICE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(deviceGuid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setCompatibleApplications(BigInteger typeGuid, List<BigInteger> appsList) {
        final Function function = new Function(
                FUNC_SETCOMPATIBLEAPPLICATIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(typeGuid), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint128>(
                        org.web3j.abi.Utils.typeMap(appsList, org.web3j.abi.datatypes.generated.Uint128.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setDeviceAddress(BigInteger deviceGuid, String myDeviceAddress) {
        final Function function = new Function(
                FUNC_SETDEVICEADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(deviceGuid), 
                new org.web3j.abi.datatypes.Utf8String(myDeviceAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setDeviceType(BigInteger deviceGuid, BigInteger myType) {
        final Function function = new Function(
                FUNC_SETDEVICETYPE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(deviceGuid), 
                new org.web3j.abi.datatypes.generated.Uint128(myType)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setDeviceBlockchainAddress(BigInteger deviceGuid, String myDeviceBlockchainAddress) {
        final Function function = new Function(
                FUNC_SETDEVICEBLOCKCHAINADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(deviceGuid), 
                new org.web3j.abi.datatypes.Address(myDeviceBlockchainAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getDeviceAddress(BigInteger deviceGuid) {
        final Function function = new Function(FUNC_GETDEVICEADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(deviceGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<List> getCompatibleApps(BigInteger deviceGuid) {
        final Function function = new Function(FUNC_GETCOMPATIBLEAPPS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(deviceGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint128>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    @Deprecated
    public static DeviceContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DeviceContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DeviceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DeviceContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DeviceContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DeviceContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DeviceContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DeviceContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DeviceContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DeviceContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<DeviceContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DeviceContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DeviceContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DeviceContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DeviceContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DeviceContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
