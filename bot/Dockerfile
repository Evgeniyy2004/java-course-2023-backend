FROM openjdk:21-jdk-slim
VOLUME ./target
COPY ./target/bot.jar bot.jar
ENV APP_TELEGRAM_TOKEN ${APP_TELEGRAM_TOKEN}
ENTRYPOINT ["java","-jar","bot.jar"]
