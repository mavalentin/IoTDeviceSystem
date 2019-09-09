FROM alpine
LABEL project="IoTDeviceManager" guid="88407ec534384b2b976d724fd013e711"
COPY handle_user.sh /scripts/
COPY user_registry.txt /app/
RUN apk update && apk add bash
RUN chmod +x /scripts/handle_user.sh
	
CMD tail -f /dev/null