package no.lau.vdvil.instruction.no.altran;

import no.lau.vdvil.instruction.Instruction;
import no.lau.vdvil.renderer.Renderer;
import org.junit.Test;

/**
 * Created by stiglau on 29/09/14.
 */
public class TestPlaying {
    @Test
    public void testSomething() throws InterruptedException {
        Instruction[] instructions = new Instruction[] {
                new ShoutInstruction(1000, 18, "ARG"),
                new ShoutInstruction(5000, 1000, "FSCK")};

        Renderer myRenderer = new ShoutRenderer();
        long start = now();
        while(true) {
            for (Instruction ins : instructions) {
                if(ins.start() <= (now() - start)) {
                    myRenderer.notify(ins, now()-start);
                } else {
                    System.out.println("-");
                }
            }
            Thread.sleep(500);
        }
    }

    long now() {
        return System.currentTimeMillis();
    }
}
