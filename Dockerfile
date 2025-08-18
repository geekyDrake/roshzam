FROM alpine:edge

# Install dependencies for wget if not already present
RUN apk add --no-cache wget

# Add Amazon Corretto's apk public key and repository
RUN wget -O /etc/apk/keys/amazoncorretto.rsa.pub https://apk.corretto.aws/amazoncorretto.rsa.pub \
    && echo "https://apk.corretto.aws/" >> /etc/apk/repositories \
    && apk update

# Install Amazon Corretto (choose the version you want)
RUN apk add amazon-corretto-21

# Install dependencies
RUN apk add --no-cache ffmpeg

ENV FFMPEG_PATH=/usr/bin/ffmpeg

COPY target/roshzam-0.0.1-SNAPSHOT.jar roshzam-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/roshzam-0.0.1-SNAPSHOT.jar"]

