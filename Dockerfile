FROM ghcr.io/alexandreladeira/riscv-simulator:base

COPY test /test/input/
WORKDIR /test/input
RUN make

COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradle /app/gradle/
COPY src /app/src/

WORKDIR /app

# Build jar if it is not present
RUN [ -f build/libs/*.jar ] && : || ./gradlew --no-daemon clean jar
RUN mv build/libs/*.jar /riscv-simulator.jar

WORKDIR /

CMD java -jar riscv-simulator.jar /test/input /test/output