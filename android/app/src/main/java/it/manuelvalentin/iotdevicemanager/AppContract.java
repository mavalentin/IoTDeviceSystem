package it.manuelvalentin.iotdevicemanager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
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
public class AppContract extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550611f02806100606000396000f3fe6080604052600436106100c5576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806312afa330146100ca5780632077018f1461012f578063273a38741461017c57806327b5f90c146101dd57806343c8501b1461022e57806345f32b081461028f5780635709c91c146102e65780636fa87db7146103ee57806396fd274b1461045b578063b41139b0146104e8578063b5eceb0a1461053f578063d45b593614610605578063fec70fb7146106e9575b600080fd5b3480156100d657600080fd5b50610115600480360360208110156100ed57600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190505050610746565b604051808215151515815260200191505060405180910390f35b34801561013b57600080fd5b5061017a6004803603602081101561015257600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190505050610797565b005b34801561018857600080fd5b506101c76004803603602081101561019f57600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190505050610a58565b6040518082815260200191505060405180910390f35b3480156101e957600080fd5b5061022c6004803603602081101561020057600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610b5a565b005b34801561023a57600080fd5b506102796004803603602081101561025157600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190505050610c62565b6040518082815260200191505060405180910390f35b34801561029b57600080fd5b506102e4600480360360408110156102b257600080fd5b8101908080356fffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610d64565b005b3480156102f257600080fd5b506103ec600480360360a081101561030957600080fd5b8101908080356fffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561034257600080fd5b82018360208201111561035457600080fd5b8035906020019184600183028401116401000000008311171561037657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190929190803563ffffffff16906020019092919080359060200190929190505050610f64565b005b3480156103fa57600080fd5b506104396004803603602081101561041157600080fd5b8101908080356fffffffffffffffffffffffffffffffff16906020019092919050505061131c565b604051808263ffffffff1663ffffffff16815260200191505060405180910390f35b34801561046757600080fd5b506104a66004803603602081101561047e57600080fd5b8101908080356fffffffffffffffffffffffffffffffff16906020019092919050505061142e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156104f457600080fd5b5061053d6004803603604081101561050b57600080fd5b8101908080356fffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050611550565b005b34801561054b57600080fd5b5061058a6004803603602081101561056257600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190505050611750565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156105ca5780820151818401526020810190506105af565b50505050905090810190601f1680156105f75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561061157600080fd5b506106e76004803603604081101561062857600080fd5b8101908080356fffffffffffffffffffffffffffffffff1690602001909291908035906020019064010000000081111561066157600080fd5b82018360208201111561067357600080fd5b8035906020019184600183028401116401000000008311171561069557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506118ea565b005b3480156106f557600080fd5b506107446004803603604081101561070c57600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190803563ffffffff169060200190929190505050611afa565b005b600060026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff169050919050565b8060026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610895576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b600060026000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160006101000a81548160ff02191690831515021790555060026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff168152602001908152602001600020600101600061092e9190611d14565b60026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206002016000905560026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060030160006101000a81549063ffffffff021916905560026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206004016000905560026000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060050160006101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690555050565b60008160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610b17576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b60026000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060020154915050919050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610c1e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60008160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610d21576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b60026000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060040154915050919050565b8160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515610e21576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b8260026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610f1f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b8260026000866fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206002018190555050505050565b60026000866fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151515611021576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260198152602001807f417070206973207265676973746572656420616c72656164790000000000000081525060200191505060405180910390fd5b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a16b41c6336040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b1580156110de57600080fd5b505afa1580156110f2573d6000803e3d6000fd5b505050506040513d602081101561110857600080fd5b81019080805190602001909291905050509050600181141515611193576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601c8152602001807f4e6f7420616c6c6f77656420746f20726567697374657220617070730000000081525060200191505060405180910390fd5b61119b611d5c565b60018160000190151590811515815250508581602001819052508481604001818152505083816060019063ffffffff16908163ffffffff168152505082816080018181525050338160a0019073ffffffffffffffffffffffffffffffffffffffff16908173ffffffffffffffffffffffffffffffffffffffff16815250508060026000896fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548160ff021916908315150217905550602082015181600101908051906020019061128d929190611db1565b506040820151816002015560608201518160030160006101000a81548163ffffffff021916908363ffffffff1602179055506080820151816004015560a08201518160050160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555090505050505050505050565b60008160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff1615156113db576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b60026000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060030160009054906101000a900463ffffffff16915050919050565b60008160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff1615156114ed576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b60026000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16915050919050565b8160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151561160d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b8260026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561170b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b8260026000866fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206004018190555050505050565b60608160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16151561180f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b60026000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156118dd5780601f106118b2576101008083540402835291602001916118dd565b820191906000526020600020905b8154815290600101906020018083116118c057829003601f168201915b5050505050915050919050565b8160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff1615156119a7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b8260026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611aa5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b8260026000866fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206001019080519060200190611af3929190611e31565b5050505050565b8160026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff161515611bb7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b8260026000826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060050160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611cb5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b8260026000866fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200190815260200160002060030160006101000a81548163ffffffff021916908363ffffffff16021790555050505050565b50805460018160011615610100020316600290046000825580601f10611d3a5750611d59565b601f016020900490600052602060002090810190611d589190611eb1565b5b50565b60c0604051908101604052806000151581526020016060815260200160008152602001600063ffffffff16815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff1681525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611df257805160ff1916838001178555611e20565b82800160010185558215611e20579182015b82811115611e1f578251825591602001919060010190611e04565b5b509050611e2d9190611eb1565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611e7257805160ff1916838001178555611ea0565b82800160010185558215611ea0579182015b82811115611e9f578251825591602001919060010190611e84565b5b509050611ead9190611eb1565b5090565b611ed391905b80821115611ecf576000816000905550600101611eb7565b5090565b9056fea165627a7a7230582099162e22b66b86bdccdba3038abeecb1d5d7a769979ee26ef21dba6f632f6b6e0029";

    public static final String FUNC_REGISTERAPP = "registerApp";

    public static final String FUNC_ELIMINATEAPP = "eliminateApp";

    public static final String FUNC_GETDEVELOPER = "getDeveloper";

    public static final String FUNC_GETSERVERADDRESS = "getServerAddress";

    public static final String FUNC_GETCHECKSUM = "getChecksum";

    public static final String FUNC_GETSUBSCRIPTIONDURATION = "getSubscriptionDuration";

    public static final String FUNC_GETSUBSCRIPTIONPRICE = "getSubscriptionPrice";

    public static final String FUNC_APPEXISTS = "appExists";

    public static final String FUNC_SETSUBSCRIPTIONDURATION = "setSubscriptionDuration";

    public static final String FUNC_SETSUBSCRIPTIONPRICE = "setSubscriptionPrice";

    public static final String FUNC_SETSERVERADDRESS = "setServerAddress";

    public static final String FUNC_SETCHECKSUM = "setChecksum";

    public static final String FUNC_SETACCOUNTSCONTRACTADDRESS = "setAccountsContractAddress";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("3", "0x579d3A18e08d419ADc81A6E2aD5c11AD9BC230b5");
    }

    @Deprecated
    protected AppContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AppContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AppContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AppContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> registerApp(BigInteger appGuid, String myAppServerAddress, BigInteger myChecksum, BigInteger mySubscriptionDuration, BigInteger mySubscriptionPrice) {
        final Function function = new Function(
                FUNC_REGISTERAPP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid), 
                new org.web3j.abi.datatypes.Utf8String(myAppServerAddress), 
                new org.web3j.abi.datatypes.generated.Uint256(myChecksum), 
                new org.web3j.abi.datatypes.generated.Uint32(mySubscriptionDuration), 
                new org.web3j.abi.datatypes.generated.Uint256(mySubscriptionPrice)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> eliminateApp(BigInteger appGuid) {
        final Function function = new Function(
                FUNC_ELIMINATEAPP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> getDeveloper(BigInteger appGuid) {
        final Function function = new Function(FUNC_GETDEVELOPER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> getServerAddress(BigInteger appGuid) {
        final Function function = new Function(FUNC_GETSERVERADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> getChecksum(BigInteger appGuid) {
        final Function function = new Function(FUNC_GETCHECKSUM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getSubscriptionDuration(BigInteger appGuid) {
        final Function function = new Function(FUNC_GETSUBSCRIPTIONDURATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getSubscriptionPrice(BigInteger appGuid) {
        final Function function = new Function(FUNC_GETSUBSCRIPTIONPRICE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> appExists(BigInteger appGuid) {
        final Function function = new Function(FUNC_APPEXISTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> setSubscriptionDuration(BigInteger appGuid, BigInteger duration) {
        final Function function = new Function(
                FUNC_SETSUBSCRIPTIONDURATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid), 
                new org.web3j.abi.datatypes.generated.Uint32(duration)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setSubscriptionPrice(BigInteger appGuid, BigInteger price) {
        final Function function = new Function(
                FUNC_SETSUBSCRIPTIONPRICE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid), 
                new org.web3j.abi.datatypes.generated.Uint256(price)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setServerAddress(BigInteger appGuid, String serverAddress) {
        final Function function = new Function(
                FUNC_SETSERVERADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid), 
                new org.web3j.abi.datatypes.Utf8String(serverAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setChecksum(BigInteger appGuid, BigInteger checksum) {
        final Function function = new Function(
                FUNC_SETCHECKSUM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid), 
                new org.web3j.abi.datatypes.generated.Uint256(checksum)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setAccountsContractAddress(String accountsContractAddress) {
        final Function function = new Function(
                FUNC_SETACCOUNTSCONTRACTADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(accountsContractAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static AppContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AppContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AppContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AppContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AppContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AppContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AppContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AppContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AppContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AppContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<AppContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AppContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<AppContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AppContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<AppContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AppContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
