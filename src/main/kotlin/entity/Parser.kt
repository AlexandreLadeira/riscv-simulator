package entity

import entity.instruction.BranchInstruction
import entity.instruction.BranchInstructionType
import entity.instruction.BreakInstruction
import entity.instruction.ImmediateInstruction
import entity.instruction.ImmediateInstructionType
import entity.instruction.Instruction
import entity.instruction.JumpInstruction
import entity.instruction.RegisterInstruction
import entity.instruction.RegisterInstructionType
import entity.instruction.StoreInstruction
import entity.instruction.StoreInstructionType
import entity.instruction.UpperInstruction
import entity.instruction.UpperInstructionType
import extensions.branchImmediate
import extensions.funct3
import extensions.funct7
import extensions.immediate
import extensions.jumpImmediate
import extensions.opcode
import extensions.rd
import extensions.rs1
import extensions.rs2
import extensions.shamtImmediate
import extensions.storeImmediate
import extensions.toBinaryString
import extensions.toHexString
import extensions.upperImmediate

object Parser {

    private const val IMMEDIATE_INSTRUCTION_OPCODE = 0b0010011
    private const val STORE_INSTRUCTION_OPCODE = 0b0100011
    private const val LOAD_INSTRUCTION_OPCODE = 0b0000011
    private const val JALR_INSTRUCTION_OPCODE = 0b1100111
    private const val BRANCH_INSTRUCTION_OPCODE = 0b1100011
    private const val JUMP_INSTRUCTION_OPCODE = 0b1101111
    private const val LUI_INSTRUCTION_OPCODE = 0b0110111
    private const val AUIPC_INSTRUCTION_OPCODE = 0b0010111
    private const val REGISTER_INSTRUCTION_OPCODE = 0b0110011
    private const val SYSTEM_INSTRUCTION_OPCODE = 0b1110011

    fun parseFromRawInstruction(rawInstruction: Int): Instruction {
        return when (rawInstruction.opcode()) {
            IMMEDIATE_INSTRUCTION_OPCODE,
            LOAD_INSTRUCTION_OPCODE,
            JALR_INSTRUCTION_OPCODE -> buildImmediateInstruction(rawInstruction)

            STORE_INSTRUCTION_OPCODE -> buildStoreInstruction(rawInstruction)
            BRANCH_INSTRUCTION_OPCODE -> buildBranchInstruction(rawInstruction)
            JUMP_INSTRUCTION_OPCODE -> buildJumpInstruction(rawInstruction)
            LUI_INSTRUCTION_OPCODE -> buildUpperInstruction(UpperInstructionType.LUI, rawInstruction)
            AUIPC_INSTRUCTION_OPCODE -> buildUpperInstruction(UpperInstructionType.AUIPC, rawInstruction)
            REGISTER_INSTRUCTION_OPCODE -> buildRegisterInstruction(rawInstruction)
            SYSTEM_INSTRUCTION_OPCODE -> BreakInstruction()
            else -> throw IllegalArgumentException("Unknown instruction: ${rawInstruction.toHexString()}")
        }
    }

    private fun buildImmediateInstruction(rawInstruction: Int): ImmediateInstruction {
        val type = getImmediateInstructionType(
            rawInstruction.opcode(),
            rawInstruction.funct3(),
            rawInstruction.funct7()
        )
        return ImmediateInstruction(
            type = type,
            immediate = when (type) {
                ImmediateInstructionType.SLLI,
                ImmediateInstructionType.SRLI,
                ImmediateInstructionType.SRAI -> rawInstruction.shamtImmediate()

                else -> rawInstruction.immediate()
            },
            rs1 = rawInstruction.rs1(),
            rd = rawInstruction.rd()
        )
    }

    private fun getImmediateInstructionType(opcode: Int, funct3: Int, funct7: Int) =
        when (opcode) {
            IMMEDIATE_INSTRUCTION_OPCODE -> when (funct3) {
                0b000 -> ImmediateInstructionType.ADDI
                0b010 -> ImmediateInstructionType.SLTI
                0b100 -> ImmediateInstructionType.XORI
                0b011 -> ImmediateInstructionType.SLTIU
                0b110 -> ImmediateInstructionType.ORI
                0b111 -> ImmediateInstructionType.ANDI
                0b001 -> ImmediateInstructionType.SLLI
                0b101 -> if (funct7 == 0) ImmediateInstructionType.SRLI else ImmediateInstructionType.SRAI
                else -> null
            }

            LOAD_INSTRUCTION_OPCODE -> when (funct3) {
                0b000 -> ImmediateInstructionType.LB
                0b001 -> ImmediateInstructionType.LH
                0b010 -> ImmediateInstructionType.LW
                0b100 -> ImmediateInstructionType.LBU
                0b101 -> ImmediateInstructionType.LHU
                else -> null
            }

            JALR_INSTRUCTION_OPCODE -> ImmediateInstructionType.JALR

            else -> null
        } ?: error(
            "Unable to parse immediate instruction type, " +
                "opcode=${opcode.toBinaryString()}, funct3=${funct3.toBinaryString()}, funct7=${funct7.toBinaryString()}"
        )

    private fun buildStoreInstruction(rawInstruction: Int) = StoreInstruction(
        type = getStoreInstructionType(rawInstruction.funct3()),
        rs1 = rawInstruction.rs1(),
        rs2 = rawInstruction.rs2(),
        immediate = rawInstruction.storeImmediate()
    )

    private fun getStoreInstructionType(funct3: Int) =
        when (funct3) {
            0b000 -> StoreInstructionType.SB
            0b001 -> StoreInstructionType.SH
            0b010 -> StoreInstructionType.SW
            else -> error("Unable to parse store instruction type, funct3=${funct3.toBinaryString()}")
        }

    private fun buildBranchInstruction(rawInstruction: Int) = BranchInstruction(
        type = getBranchInstructionType(rawInstruction.funct3()),
        rs1 = rawInstruction.rs1(),
        rs2 = rawInstruction.rs2(),
        immediate = rawInstruction.branchImmediate()
    )

    private fun getBranchInstructionType(funct3: Int) =
        when (funct3) {
            0b000 -> BranchInstructionType.BEQ
            0b001 -> BranchInstructionType.BNE
            0b100 -> BranchInstructionType.BLT
            0b101 -> BranchInstructionType.BGE
            0b110 -> BranchInstructionType.BLTU
            0b111 -> BranchInstructionType.BGEU
            else -> error("Unable to parse branch instruction type, funct3=${funct3.toBinaryString()}")
        }

    private fun buildJumpInstruction(rawInstruction: Int) = JumpInstruction(
        rd = rawInstruction.rd(),
        immediate = rawInstruction.jumpImmediate()
    )

    private fun buildUpperInstruction(type: UpperInstructionType, rawInstruction: Int) = UpperInstruction(
        type = type,
        rd = rawInstruction.rd(),
        immediate = rawInstruction.upperImmediate()
    )

    private fun buildRegisterInstruction(rawInstruction: Int) = RegisterInstruction(
        type = getRegisterInstructionType(rawInstruction.funct3(), rawInstruction.funct7()),
        rs1 = rawInstruction.rs1(),
        rs2 = rawInstruction.rs2(),
        rd = rawInstruction.rd()
    )

    private fun getRegisterInstructionType(funct3: Int, funct7: Int) =
        when (funct7) {
            0 -> when (funct3) {
                0b000 -> RegisterInstructionType.ADD
                0b001 -> RegisterInstructionType.SLL
                0b010 -> RegisterInstructionType.SLT
                0b011 -> RegisterInstructionType.SLTU
                0b100 -> RegisterInstructionType.XOR
                0b101 -> RegisterInstructionType.SRL
                0b110 -> RegisterInstructionType.OR
                0b111 -> RegisterInstructionType.AND
                else -> null
            }

            1 -> when (funct3) {
                0b000 -> RegisterInstructionType.MUL
                0b001 -> RegisterInstructionType.MULH
                0b010 -> RegisterInstructionType.MULHSU
                0b011 -> RegisterInstructionType.MULHU
                0b100 -> RegisterInstructionType.DIV
                0b101 -> RegisterInstructionType.DIVU
                0b110 -> RegisterInstructionType.REM
                0b111 -> RegisterInstructionType.REMU
                else -> null
            }

            0b0100000 -> when (funct3) {
                0b000 -> RegisterInstructionType.SUB
                0b101 -> RegisterInstructionType.SRA
                else -> null
            }

            else -> null
        } ?: error(
            "Unable to parse register instruction type, " +
                "funct3=${funct3.toBinaryString()}, funct7=${funct7.toBinaryString()}"
        )
}