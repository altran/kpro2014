/**
 * Created by shimin on 10/1/2014.
 */


public class TableRender implements Renderer{

    @Override
    public void notify(Instruction instruction, long beat) {
        if(instruction instanceof ColumnInstruction){
            ColumnInstruction columnInstruction = new ColumnInstruction(((ColumnInstruction) instruction).getColumn(),
                    ((ColumnInstruction) instruction).getValue());
            System.out.println(columnInstruction.getColumn());
            columnInstruction.getColumn().setCellValueFactory(columnInstruction.getValue());

        }
    }
}
