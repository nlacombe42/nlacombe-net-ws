FROM openjdk:13.0.2
COPY startApp.sh /app/startApp.sh
RUN chmod u+x /app/startApp.sh
ENTRYPOINT ["/app/startApp.sh"]
ENV PORT 8007

ADD *.jar /app/app.jar
