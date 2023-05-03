CROSS_OBJCOPY="riscv32-unknown-elf-objcopy"
TESTS = $(patsubst %.riscv,%,$(wildcard *.riscv))

build: $(TESTS)

clean: 
	rm -f *.elf
	rm -f *.bin
	rm -f *.o

$(TESTS):
	$(CROSS_OBJCOPY) -j .text -j .rodata -O binary $@.riscv $@.bin