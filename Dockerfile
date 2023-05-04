FROM ghcr.io/alexandreladeira/riscv-simulator:base

COPY test /test/input/
WORKDIR /test/input
RUN RISCV_TOOLCHAIN_PREFIX=riscv32-unknown-elf make

COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradle /app/gradle/
COPY src /app/src/

WORKDIR /app

RUN --mount=type=cache,target=/app/.gradle --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon jar
RUN mv build/libs/*.jar /riscv-simulator.jar

WORKDIR /

CMD java -jar riscv-simulator.jar /test/input /test/output