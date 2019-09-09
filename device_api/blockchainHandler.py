from web3 import Web3
import json
import configuration as cfg

web3 = Web3(Web3.HTTPProvider(cfg.rpc_address))

class DeviceContract:
    def __init__(self):
        with open('contracts/DeviceContract.json', 'r') as abi_definition:
            abi = json.load(abi_definition)
            contract_address = cfg.contracts['DeviceContract']['address']
            self.contract = web3.eth.contract(address=contract_address, abi=abi)


class AccountsContract:
    def __init__(self):
        with open('contracts/AccountsContract.json', 'r') as abi_definition:
            abi = json.load(abi_definition)
            contract_address = cfg.contracts['AccountsContract']['address']
            self.contract = web3.eth.contract(address=contract_address, abi=abi)

    def identify_user(self, address):
        try:
            role = self.contract.functions.identifyUser(address).call()
        except Exception:
            return None
        else:
            return role

    def get_appExpiration(self, address, app_guid):
        exp = self.contract.functions.getAppExpiration(address,app_guid).call()
        return exp


class AppContract:
    def __init__(self):
        with open('contracts/AppContract.json', 'r') as abi_definition:
            abi = json.load(abi_definition)
            contract_address = cfg.contracts['AppContract']['address']
            self.contract = web3.eth.contract(address=contract_address, abi=abi)

    def get_checksum(self, app_guid):
        checksum = self.contract.functions.getChecksum(app_guid).call()
        return checksum
