FROM alpine
ARG APP_VERSION=0.0.1
ARG SHIFTLEFT_ORG_ID
ARG SHIFTLEFT_UPLOAD_TOKEN
ENV SHIFTLEFT_ORG_ID ${SHIFTLEFT_ORG_ID}
ENV SHIFTLEFT_UPLOAD_TOKEN ${SHIFTLEFT_UPLOAD_TOKEN}

RUN apk --update --no-cache add curl openjdk8

COPY target/hello-shiftleft-${APP_VERSION}.jar hello-shiftleft.jar

# Install ShiftLeft CLI
RUN curl https://www.shiftleft.io/download/sl-latest-linux-x64.tar.gz | tar xvz -C /usr/local/bin

#RUN sl auth
#COPY config.json /.shiftleft/config.json

# Run ShiftLeft code analysis
RUN sl analyze --wait --app hsl hello-shiftleft.jar
EXPOSE 8081
# Run target app with ShiftLeft Microagent
CMD sl run -- java -jar hello-shiftleft.jar
