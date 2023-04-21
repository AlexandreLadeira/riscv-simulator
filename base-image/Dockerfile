FROM debian:bullseye-slim as riscv-gnu-toolchain

RUN apt update
RUN apt install -y git
RUN apt install -y autoconf automake autotools-dev curl python3 libmpc-dev libmpfr-dev libgmp-dev gawk build-essential bison flex texinfo gperf libtool patchutils bc zlib1g-dev libexpat-dev ninja-build

RUN git clone https://github.com/riscv/riscv-gnu-toolchain
WORKDIR riscv-gnu-toolchain
RUN ./configure --prefix=/opt/riscv --with-arch=rv32im --with-abi=ilp32
RUN make

FROM openjdk:17-slim-bullseye

COPY --from=riscv-gnu-toolchain /opt/riscv /opt/riscv
ENV PATH="$PATH:/opt/riscv/bin"

RUN apt update
RUN apt install -y build-essential