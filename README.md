# RISC-V Simulator

Simulador de um processador RISC-V em Kotlin.

## Execução com Dockerfile

Para buildar o arquivo docker, utilize o seguinte comando:

```
docker build -t riscv-simulator .
```

Então, é possível rodar a imagem construída, mapeando o volume `/test/output/` para uma pasta local onde serão escritos
os logs de execução:

```
docker run --rm -v $PWD/output:/test/output riscv-simulator
```

## Execução sem Dockerfile

Para a execução sem Dockerfile, é necessário ter compilado o riscv-gnu-toolchain e ter o Java 17 instalado.

```
cd test
RISCV_TOOLCHAIN_PREFIX=riscv32-unknown-elf make
cd ..
mkdir output
./gradlew run --args "test output"
```