/**
 * Created by shimin on 10/1/2014.
 */


public class ColumnRender implements Renderer{

    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof ColumnInstruction){
            ColumnInstruction columnInstruction = new ColumnInstruction(((ColumnInstruction) instruction).getColumn(),
                    ((ColumnInstruction) instruction).getValue());
            columnInstruction.getColumn().setCellValueFactory(columnInstruction.getValue());

        }
    }
}
