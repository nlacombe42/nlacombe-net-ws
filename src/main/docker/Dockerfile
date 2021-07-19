FROM openjdk:14.0.1

WORKDIR /app/

COPY startApp.sh startApp.sh
RUN chmod u+x startApp.sh
CMD ["bash", "./startApp.sh"]

ENV PORT 8007

COPY *.jar app.jar
