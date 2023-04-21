FROM riscv-simulator-base:latest

COPY ./test /test/input
WORKDIR /test/input
RUN make

COPY . /app
WORKDIR /app

# Build jar if it is not present
RUN [ -f build/libs/*.jar ] && : || ./gradlew --no-daemon clean jar
RUN mv build/libs/*.jar /riscv-simulator.jar

WORKDIR /

CMD java -jar riscv-simulator.jar /test/input /test/output