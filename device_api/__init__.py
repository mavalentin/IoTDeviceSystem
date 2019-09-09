from flask import Flask, request
from web3.auto import w3
from web3 import Web3
from Crypto.Hash import keccak
import json
import uuid
import subprocess
import wget
import hashlib
import os
import shutil
import docker
import time
# from IoTDeviceAPI
import blockchainHandler

app = Flask(__name__)
rnd = {}  # store random numbers sent out as {user_address:device_rnd}
client = docker.from_env()


@app.route('/installedApps', methods=['POST'])
def get_installed_apps():
    data = request.get_json()

    # check authentication
    auth = verify_auth(data['user_rnd'], data['signed_device_rnd'], data['user_address'])
    if auth:
        accounts_contract = blockchainHandler.AccountsContract()
        role = accounts_contract.identify_user(Web3.toChecksumAddress(data['user_address']))
        if role is None:
            return "Contract error", 502
        # roles: inexistent: 0; developer: 1; technician: 2; user: 3
        elif role != 0:
            installed_apps = []
            out = subprocess.Popen(['docker', 'images', '-f', 'label=project=IoTDeviceManager'],
                                   stdout=subprocess.PIPE)
            t = out.stdout.read()
            images_array = t.split(b"\n")
            # remove first element (titles) and last element (empty line)
            images_array.pop(0)
            images_array.pop()

            for l in images_array:
                o = subprocess.Popen(['docker', 'inspect', '--format', '"{{ index .Config.Labels \"guid\"}}"',
                                      l.split()[0]], stdout=subprocess.PIPE)
                r = o.stdout.read().decode("utf-8").split()[0].replace("\"", "")
                installed_apps.append(r)

            reply = {"installedApps": installed_apps}
            return json.dumps(reply), 200  # return JSON of app IDs
    return "Forbidden", 403


@app.route('/deployApp', methods=['POST'])
def deploy_app():
    data = request.get_json()

    # check authentication
    auth = verify_auth(data['user_rnd'], data['signed_device_rnd'], data['user_address'])
    if auth:
        accounts_contract = blockchainHandler.AccountsContract()
        role = accounts_contract.identify_user(Web3.toChecksumAddress(data['user_address']))
        if role is None:
            return "Contract error", 502
        # roles: inexistent: 0; developer: 1; technician: 2; user: 3
        elif role == 1:
            action = data['action']
            app_guid = int(data['app_guid'], 16)
            app_contract = blockchainHandler.AppContract()
            json_reply = ""

            if action == "install":
                app_url = data['app_url']
                if os.path.isdir('/tmp/iotapp/'): shutil.rmtree('/tmp/iotapp/')
                os.mkdir('/tmp/iotapp/');
                wget.download(app_url + 'docker_package.zip', '/tmp/iotapp/')
                wget.download(app_url + 'appinfo.json', '/tmp/iotapp/')

                with open('/tmp/iotapp/appinfo.json') as json_file:
                    appinfo = json.load(json_file)
                    app_name = appinfo['name'].lower().replace(" ", "")
                official_checksum = hex(app_contract.get_checksum(app_guid))
                calculated_checksum = '0x' + hashlib.sha256(open("/tmp/iotapp/"+'docker_package.zip', 'rb').read()).hexdigest()

                if official_checksum == calculated_checksum:
                    subprocess.call(['unzip', 'docker_package.zip'], cwd='/tmp/iotapp/')
                    subprocess.call(['docker', 'build', '-t', app_name, '.'], cwd='/tmp/iotapp/'+app_name)
                    shutil.rmtree('/tmp/iotapp/')
                    run_app(app_name)
                    json_reply = {"deployedAppGuid": data['app_guid']}
                else:
                    shutil.rmtree('/tmp/iotapp/')
                    return "Bad checksum", 550
            elif action == "uninstall":
                # TODO: replace following part with function find_appname_guid()
                o = subprocess.Popen(['docker', 'images', '-f', 'label=guid='+data['app_guid']], stdout=subprocess.PIPE)
                o.wait()
                t = o.stdout.read()
                images_array = t.split(b"\n")
                # remove first element (titles) and last element (empty line)
                images_array.pop(0)
                images_array.pop()
                if images_array.__len__() == 1:
                    app_name = images_array[0].split()[0].decode("utf-8")
                    #remove containers with selected image
                    try:
                        client.containers.get(app_name).stop()
                    except docker.errors.NotFound:
                        print("no containers existing. proceeding with image removal")
                    p1 = subprocess.Popen(['docker', 'ps', '-a'], stdout=subprocess.PIPE)
                    p2 = subprocess.Popen(['awk', "{print $1,$2}"], stdin=p1.stdout, stdout=subprocess.PIPE)
                    p3 = subprocess.Popen(['grep', app_name], stdin=p2.stdout, stdout=subprocess.PIPE)
                    p4 = subprocess.Popen(['awk', "{print $1}"], stdin=p3.stdout, stdout=subprocess.PIPE)
                    p5 = subprocess.Popen(['xargs', '-I', '{}', 'docker', 'rm', '{}'], stdin=p4.stdout, stdout=subprocess.PIPE)
                    p5.communicate()
                    print("Containers removed")
                    #remove image
                    subprocess.call(['docker', 'rmi', app_name])
                    print("image removed")
                    json_reply = {"uninstalledAppGuid": data['app_guid']}
                elif images_array.__len__() >= 1:
                    return "Image isn't unique", 551
            return json.dumps(json_reply), 200
    return "Forbidden", 403

@app.route('/technicianAction', methods=['POST'])
def technician_action():
    data = request.get_json()

    # check authentication
    auth = verify_auth(data['user_rnd'], data['signed_device_rnd'], data['user_address'])
    if auth:
        accounts_contract = blockchainHandler.AccountsContract()
        role = accounts_contract.identify_user(Web3.toChecksumAddress(data['user_address']))
        if role is None:
            return "Contract error", 502
        # roles: inexistent: 0; developer: 1; technician: 2; user: 3
        elif role == 2:
            action = data['action']
            json_reply = ""

            if action == "restartContainer":
                app_name = find_appname_guid(data['app_guid'])
                if app_name is not None:
                    client.containers.get(app_name).restart()
                    json_reply = {"restarted": 1}
                return json.dumps(json_reply), 200

    return "Forbidden", 403

@app.route('/userAction', methods=['POST'])
def user_action():
    data = request.get_json()

    # check authentication
    auth = verify_auth(data['user_rnd'], data['signed_device_rnd'], data['user_address'])
    if auth:
        accounts_contract = blockchainHandler.AccountsContract()
        role = accounts_contract.identify_user(Web3.toChecksumAddress(data['user_address']))
        if role is None:
            return "Contract error", 502
        # roles: inexistent: 0; developer: 1; technician: 2; user: 3
        elif role == 3:
            action = data['action']
            json_reply = ""

            if action == "registerToApp":
                app_name = find_appname_guid(data['app_guid'])
                if app_name is not None:
                    client.containers.get(app_name).start()
                    run = client.containers.get(app_name).exec_run('/scripts/handle_user.sh register ' + data['user_address'])
                    if run[0] == 0:
                        json_reply = {"registrationResult": 1}
                    else:
                        json_reply = {"registrationResult": 0}
                return json.dumps(json_reply), 200
            elif action == "unregisterFromApp":
                app_name = find_appname_guid(data['app_guid'])
                if app_name is not None:
                    client.containers.get(app_name).start()
                    run = client.containers.get(app_name).exec_run(
                        '/scripts/handle_user.sh unregister ' + data['user_address'])
                    if run[0] == 0:
                        json_reply = {"registrationResult": 2}
                    else:
                        json_reply = {"registrationResult": -1}
                return json.dumps(json_reply), 200
            elif action == "accessApp":
                app_guid = int(data['app_guid'], 16)
                expiration = accounts_contract.get_appExpiration(Web3.toChecksumAddress(data['user_address']),app_guid)
                timestamp = time.time();
                if expiration > timestamp :
                    json_reply = {"access": 1, "data": "This is an example data message from the application"}
                else:
                    json_reply = {"access": 0}
                return json.dumps(json_reply), 200
    return "Forbidden", 403


@app.route('/randomNumber', methods=['POST'])
def get_random_number():
    data = request.get_json()
    user_rnd = data['user_rnd']
    device_rnd = uuid.uuid4().hex  # generate device_rnd
    rnd[user_rnd] = device_rnd
    return json.dumps({"device_rnd": device_rnd}), 200


def verify_auth(user_rnd, signed_device_rnd, user_address):
    if user_rnd in rnd:
        keccak_hash = keccak.new(digest_bits=256)
        keccak_hash.update(str.encode(rnd[user_rnd]))
        device_rnd_hash = keccak_hash.hexdigest()
        rnd.pop(user_rnd)
        return verify_signature(device_rnd_hash, signed_device_rnd, user_address)
    else:
        return False


def verify_signature(message_hash, signature, address):
    vrs_tuple = (signature["v"], signature["r"], signature["s"])
    calculated_address = w3.eth.account.recoverHash(message_hash, vrs=vrs_tuple)
    return int(calculated_address, 16) == int(address, 16)


def run_app(app_name):
    prune_containers()
    container = client.containers.run(app_name, name=app_name, volumes={app_name: {'bind': '/app', 'mode': 'rw'}}, detach=True)


def prune_containers():
    # delete stopped containers
    client.containers.prune()


def find_appname_guid(app_guid):
    o = subprocess.Popen(['docker', 'images', '-f', 'label=guid=' + app_guid], stdout=subprocess.PIPE)
    o.wait()
    t = o.stdout.read()
    images_array = t.split(b"\n")
    # remove first element (titles) and last element (empty line)
    images_array.pop(0)
    images_array.pop()
    if images_array.__len__() == 1:
        return images_array[0].split()[0].decode("utf-8")
    else:
        return False


if __name__ == '__main__':
    app.run(host='0.0.0.0')
