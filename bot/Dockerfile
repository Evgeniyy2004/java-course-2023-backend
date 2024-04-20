FROM openjdk:21-jdk-slim
ENV APP_TELEGRAM_TOKEN ${APP_TELEGRAM_TOKEN}
COPY target/bot.jar bot.jar
ENTRYPOINT ["java","-jar","bot.jar"]
