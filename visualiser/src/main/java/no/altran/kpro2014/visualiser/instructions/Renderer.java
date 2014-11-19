package no.altran.kpro2014.visualiser.instructions;

public interface Renderer {
    void notify(Instruction instruction, long beat);
}
