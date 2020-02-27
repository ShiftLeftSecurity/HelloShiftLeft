FROM alpine 

RUN apk add curl jq openjdk8 wget git go

COPY entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
