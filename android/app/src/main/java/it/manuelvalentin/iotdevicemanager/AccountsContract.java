package it.manuelvalentin.iotdevicemanager;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
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
public class AccountsContract extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b50336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506114e9806100606000396000f3fe608060405260043610610099576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806312065fe01461009e5780631d5dbd8b146100c957806343d3b1861461014a5780634529e8f21461019b5780635fd8c710146101d457806389ef9b97146101eb5780639e9fa09714610238578063a16b41c614610296578063bcd1208e14610309575b600080fd5b3480156100aa57600080fd5b506100b3610349565b6040518082815260200191505060405180910390f35b3480156100d557600080fd5b50610134600480360360408110156100ec57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080356fffffffffffffffffffffffffffffffff169060200190929190505050610393565b6040518082815260200191505060405180910390f35b34801561015657600080fd5b506101996004803603602081101561016d57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061065c565b005b3480156101a757600080fd5b506101b0610764565b604051808260038111156101c057fe5b60ff16815260200191505060405180910390f35b3480156101e057600080fd5b506101e9610851565b005b3480156101f757600080fd5b506102366004803603602081101561020e57600080fd5b8101908080356fffffffffffffffffffffffffffffffff16906020019092919050505061092a565b005b34801561024457600080fd5b506102946004803603604081101561025b57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803560ff1690602001909291905050506109ad565b005b3480156102a257600080fd5b506102e5600480360360208110156102b957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610bf3565b604051808260038111156102f557fe5b60ff16815260200191505060405180910390f35b6103476004803603602081101561031f57600080fd5b8101908080356fffffffffffffffffffffffffffffffff169060200190929190505050610ce5565b005b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060020154905090565b600082600060038111156103a357fe5b600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff1660038111156103fe57fe5b14151515610474576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f55736572206973206e6f7420726567697374657265640000000000000000000081525060200191505060405180910390fd5b82600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166312afa330826040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b15801561052857600080fd5b505afa15801561053c573d6000803e3d6000fd5b505050506040513d602081101561055257600080fd5b810190808051906020019092919050505015156105d7576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b600260008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001016000856fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff168152602001908152602001600020600001549250505092915050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610720576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f4e6f7420616c6c6f77656400000000000000000000000000000000000000000081525060200191505060405180910390fd5b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600080600381111561077257fe5b600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff1660038111156107cd57fe5b14151515610843576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f55736572206973206e6f7420726567697374657265640000000000000000000081525060200191505060405180910390fd5b61084c3361135f565b905090565b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206002015490506000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600201819055503373ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f19350505050158015610926573d6000803e3d6000fd5b5050565b6000600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001016000836fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206000018190555050565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610a71576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601d8152602001807f4e6f7420616c6c6f77656420746f20726567697374657220757365727300000081525060200191505060405180910390fd5b60006003811115610a7e57fe5b600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff166003811115610ad957fe5b141515610b4e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f55736572206973207265676973746572656420616c726561647900000000000081525060200191505060405180910390fd5b610b56611498565b8181600001906003811115610b6757fe5b90816003811115610b7457fe5b8152505080600260008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008201518160000160006101000a81548160ff02191690836003811115610bdc57fe5b021790555060208201518160020155905050505050565b60008160006003811115610c0357fe5b600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff166003811115610c5e57fe5b14151515610cd4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f55736572206973206e6f7420726567697374657265640000000000000000000081525060200191505060405180910390fd5b610cdd8361135f565b915050919050565b80600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166312afa330826040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b158015610d9957600080fd5b505afa158015610dad573d6000803e3d6000fd5b505050506040513d6020811015610dc357600080fd5b81019080805190602001909291905050501515610e48576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f41707020646f6573206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b42600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001016000846fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206000015411151515610f3a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f537562736372697074696f6e207374696c6c2076616c6964000000000000000081525060200191505060405180910390fd5b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166343c8501b846040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b158015610fef57600080fd5b505afa158015611003573d6000803e3d6000fd5b505050506040513d602081101561101957600080fd5b8101908080519060200190929190505050905034811415156110a3576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600c8152602001807f57726f6e6720616d6f756e74000000000000000000000000000000000000000081525060200191505060405180910390fd5b3460026000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166396fd274b876040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b15801561115b57600080fd5b505afa15801561116f573d6000803e3d6000fd5b505050506040513d602081101561118557600080fd5b810190808051906020019092919050505073ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600201600082825401925050819055506000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16636fa87db7856040518263ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040180826fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b15801561129557600080fd5b505afa1580156112a9573d6000803e3d6000fd5b505050506040513d60208110156112bf57600080fd5b810190808051906020019092919050505090508063ffffffff164201600260003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206001016000866fffffffffffffffffffffffffffffffff166fffffffffffffffffffffffffffffffff1681526020019081526020016000206000018190555050505050565b6000816000600381111561136f57fe5b600260008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff1660038111156113ca57fe5b14151515611440576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260168152602001807f55736572206973206e6f7420726567697374657265640000000000000000000081525060200191505060405180910390fd5b600260008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160009054906101000a900460ff16915050919050565b6040805190810160405280600060038111156114b057fe5b815260200160008152509056fea165627a7a723058205872ca31c352944df2e15add881863702d88f5bbc1f103f8c925540a8c8236180029";

    public static final String FUNC_IDENTIFYSELF = "identifySelf";

    public static final String FUNC_IDENTIFYUSER = "identifyUser";

    public static final String FUNC_REGISTERUSER = "registerUser";

    public static final String FUNC_ACTIVATESUBSCRIPTION = "activateSubscription";

    public static final String FUNC_CANCELSUBSCRIPTION = "cancelSubscription";

    public static final String FUNC_WITHDRAWBALANCE = "withdrawBalance";

    public static final String FUNC_GETAPPEXPIRATION = "getAppExpiration";

    public static final String FUNC_GETBALANCE = "getBalance";

    public static final String FUNC_SETAPPCONTRACTADDRESS = "setAppContractAddress";

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
        _addresses.put("3", "0xDF3F7b2321ae802Cc1a77fecC26064E1bBF7dF05");
    }

    @Deprecated
    protected AccountsContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected AccountsContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected AccountsContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected AccountsContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<BigInteger> identifySelf() {
        final Function function = new Function(FUNC_IDENTIFYSELF, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> identifyUser(String userAddress) {
        final Function function = new Function(FUNC_IDENTIFYUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userAddress)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> registerUser(String userAddress, BigInteger role) {
        final Function function = new Function(
                FUNC_REGISTERUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userAddress), 
                new org.web3j.abi.datatypes.generated.Uint8(role)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> activateSubscription(BigInteger appGuid, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_ACTIVATESUBSCRIPTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> cancelSubscription(BigInteger appGuid) {
        final Function function = new Function(
                FUNC_CANCELSUBSCRIPTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdrawBalance() {
        final Function function = new Function(
                FUNC_WITHDRAWBALANCE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getAppExpiration(String userAddress, BigInteger appGuid) {
        final Function function = new Function(FUNC_GETAPPEXPIRATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userAddress), 
                new org.web3j.abi.datatypes.generated.Uint128(appGuid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getBalance() {
        final Function function = new Function(FUNC_GETBALANCE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setAppContractAddress(String appContractAddress) {
        final Function function = new Function(
                FUNC_SETAPPCONTRACTADDRESS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(appContractAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static AccountsContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccountsContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static AccountsContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new AccountsContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static AccountsContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new AccountsContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static AccountsContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new AccountsContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<AccountsContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AccountsContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<AccountsContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(AccountsContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<AccountsContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AccountsContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<AccountsContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(AccountsContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }
}
