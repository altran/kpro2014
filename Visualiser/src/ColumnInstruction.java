import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by shimin on 10/1/2014.
 */
public class ColumnInstruction implements Instruction{

    private TableColumn Column;
    private PropertyValueFactory value;

    public ColumnInstruction(TableColumn column, PropertyValueFactory value){
        this.Column = column;
        this.value = value;
    }

    public TableColumn getColumn(){
        return Column;
    }

    public PropertyValueFactory getValue(){
        return value;
    }

    public void setValue(PropertyValueFactory value){
        this.value = value;
    }

    @Override
    public long start() {
        return 0;
    }

    @Override
    public long length() {
        return 0;
    }


    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
