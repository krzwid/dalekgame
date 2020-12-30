package command;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainApp.MainApp;

public class CommandRegistry {
    private final ObservableList<Command> commandStack = FXCollections.observableArrayList();
    IntegerBinding stackSizeProperty = Bindings.size(commandStack);

    public void executeCommand(Command command) {
        if(command.execute()) {
            if(MainApp.INITIAL_REWINDS <= commandStack.size()) commandStack.remove(0);
            commandStack.add(command);
            System.out.println("STACKSIZE: " + commandStack.size());
        }
    }

    public void undo() {
        if(!commandStack.isEmpty()) {
            var index = commandStack.size() - 1;
            Command command = commandStack.get(index);
            commandStack.remove(index);
            command.undo();
        }
    }

    public void clearCommandStack() {
        commandStack.clear();
    }

    public IntegerBinding getStackSizeProperty() {
        return stackSizeProperty;
    }
}
