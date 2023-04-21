CROSS_COMPILER="riscv32-unknown-elf-gcc"
CROSS_OBJCOPY="riscv32-unknown-elf-objcopy"
TESTS = $(patsubst %.c,%,$(wildcard *.c))
CROSS_CFLAGS = -g -march=rv32im -std=gnu99 -mabi=ilp32
LINK_OPTS = -nostartfiles -lc -lm

build: $(TESTS)

clean: 
	rm -f *.elf
	rm -f *.bin
	rm -f *.o

$(TESTS):
	$(CROSS_COMPILER) -c crt.S $(CROSS_CFLAGS)
	$(CROSS_COMPILER) $(CROSS_CFLAGS) $@.c -o $@.elf -T test.ld $(CROSS_CFLAGS) $(LINK_OPTS)
	$(CROSS_OBJCOPY) -O binary $@.elf $@.bin