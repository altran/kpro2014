package Interface;

import Interface.Instruction;

public interface Renderer {
    void notify(Instruction instruction, long beat);
}
