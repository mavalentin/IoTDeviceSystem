#!/bin/bash

action=$1
user_uid=$2
user_registry="/app/user_registry.txt"

if [ "$action" == "register" ]
then
	if grep -Fxq $user_uid $user_registry
	then
		echo "User already registered"
		exit 1
	else
		echo $user_uid >> $user_registry
		exit 0
	fi
elif [ "$action" == "unregister" ]
then
	if grep -Fxq $user_uid $user_registry
	then
		sed -i "/$user_uid/d" $user_registry
		exit 0
	else
		echo "User not registered"
		exit 2
	fi
fi