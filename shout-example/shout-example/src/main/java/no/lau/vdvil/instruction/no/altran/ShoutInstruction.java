package no.lau.vdvil.instruction.no.altran;

import no.lau.vdvil.instruction.Instruction;

/**
 * Created by stiglau on 29/09/14.
 */
public class ShoutInstruction implements Instruction {
    final String profanity;
    final long start;
    final long length;

    public ShoutInstruction(long start, long length, String profanity) {
        this.profanity = profanity;
        this.start = start;
        this.length = length;
    }

    public String getProfanity() {
        return profanity;
    }

    public long start() { return start; }

    public long length() { return length; }

    public int compareTo(Object o) {
        return 0; //TODO compare starts and start+duration points
    }
}
