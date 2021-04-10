package Cmds;

import Cmds.subcmds.Condition;
import Cmds.subcmds.GetRow;

import java.io.File;
import java.util.ArrayList;

public class Delete extends DBCmds{
    private Tokenizer tokens;
    private Database database;
    private boolean dbInited;
    private ArrayList<String> atts;
    private Table table;
    private ArrayList<String> rows;
    public Delete(Tokenizer tokens, Database database, boolean dbInited) {
        this.tokens = tokens;
        this.database = database;
        this.dbInited = dbInited;
        doDelete();
    }
    private void doDelete(){
        if(!dbInited){
            setResultError("Make sure your using a database");
            return;
        }
        if(!tokens.nextToken().toLowerCase().equals("from")){
            setResultError("Make sure from comes after Delete");
            return;
        }
        String tableName = tokens.nextToken() + ".tab";
        File fileToOpen = new File(database.getDBName() + File.separator + tableName);
        if(!fileToOpen.exists()){
            setResultError("Table doesn't exist.");
            return;
        }
        int tableNum = database.getTableNumFromName(tableName);
        table = database.getTable(tableNum);
        if(table.getNumRows() == 0){
            setResultError("Table is empty.");
            return;
        }
        if(!tokens.nextToken().toLowerCase().equals("where")){
            setResultError("Where comes after the table name.");
            return;
        }
        Condition condition = new Condition();
        condition.parseConditions(tokens);
        atts = condition.returnAtts();
        GetRow getrows = new GetRow();
        if(!getrows.getRows(atts, table, condition.isString())){
            setResultError("Error while getting rows to delete.");
            return;
        }
        if(!tokens.nextToken().equals(";")){
            setResultError("Missing closing ;");
            return;
        }
        rows = getrows.returnRows();
        for(int i = 0; i < rows.size(); i++){
            table.removeRow(rows.get(i));
        }
        table.overWriteTableFile(database.getDBName());
    }
}
