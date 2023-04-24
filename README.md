# RISC-V Simulator

Simulador de um processador RISC-V em Kotlin.

## Execução

Para buildar o arquivo docker, utilize o seguinte comando:

```
docker build -t riscv-simulator .
```

Então, é possível rodar a imagem construída, mapeando o volume `/test/output/` para uma pasta local onde serão escritos
os logs de execução:

```
docker run -v $PWD/output:/test/output riscv-simulator
```