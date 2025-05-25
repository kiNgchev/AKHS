FROM eclipse-temurin:21.0.2_13-jdk-jammy AS extract

ARG JAR_FILE
WORKDIR /build

ADD $JAR_FILE application.jar
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

FROM eclipse-temurin:21.0.2_13-jdk-jammy

RUN addgroup spring-boot-group && adduser --ingroup spring-boot-group spring-boot
USER spring-boot:spring-boot-group
VOLUME /tmp
WORKDIR /application

COPY --from=extract /build/extracted/dependencies .
COPY --from=extract /build/extracted/spring-boot-loader .
COPY --from=extract /build/extracted/snapshot-dependencies .
COPY --from=extract /build/extracted/application .

ENTRYPOINT exec java ${JAVA_OPTS} org.springframework.boot.loader.launch.JarLauncher ${0} ${@}