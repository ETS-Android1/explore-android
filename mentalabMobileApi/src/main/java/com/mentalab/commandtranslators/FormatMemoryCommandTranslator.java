package com.mentalab.commandtranslators;

public class FormatMemoryCommandTranslator extends twoByteCommandTranslator {

  public FormatMemoryCommandTranslator(int opcode, int argument) {
    super(opcode, argument);
  }

  @Override
  public byte[] translateCommand(int argument) {
    return convertIntegerToByteArray();

    // return new int[]{this.pId, this.count,
  }
}