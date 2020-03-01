FROM alpine 

RUN apk add curl jq openjdk8 wget git go

COPY actionanalysis.sh /actionanalysis.sh
RUN chmod +x /actionanalysis.sh
ENTRYPOINT ["/actionanalysis.sh"]
