CROSS_OBJCOPY="$(RISCV_TOOLCHAIN_PREFIX)-objcopy"
TESTS = $(patsubst %.riscv,%,$(wildcard *.riscv))

build: $(TESTS)

clean: 
	rm -f *.elf
	rm -f *.bin
	rm -f *.o

$(TESTS):
	$(CROSS_OBJCOPY) -j .text -j .rodata -O binary $@.riscv $@.bin