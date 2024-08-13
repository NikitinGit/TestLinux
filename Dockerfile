FROM openjdk:17-jdk-slim
ARG JAR_FILE='TestLinux-0.0.1-SNAPSHOT.jar'
COPY ${JAR_FILE} app.jar

# Установите переменные окружения для подключения к базе данных
# если есть в application.properties то это не надо начало
ENV SPRING_DATASOURCE_URL=jdbc:mysql://94.241.140.130:3306/test_sortition
ENV SPRING_DATASOURCE_USERNAME=nikitin
ENV SPRING_DATASOURCE_PASSWORD=123456789
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQL8Dialect
# если есть в application.properties то это не надо конец
ENTRYPOINT ["java", "-jar", "/app.jar"]