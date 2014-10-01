package no.lau.vdvil.instruction.no.altran;

import no.lau.vdvil.instruction.Instruction;
import no.lau.vdvil.renderer.Renderer;

/**
 * Created by stiglau on 29/09/14.
 */
public class ShoutRenderer implements Renderer {

    @Override
    public void notify(Instruction instruction, long beat) {
        if (instruction instanceof ShoutInstruction) {
            ShoutInstruction shoutInstruction = (ShoutInstruction) instruction;
            System.out.println("shoutInstruction = " + shoutInstruction.getProfanity());
        }

    }
}
