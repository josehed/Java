package Cmds;
import java.io.File;
import java.util.ArrayList;

public class Database {
    private ArrayList<Table> tablelst;
    private String DBName;
    private File dbFile;
    public Database(){
        DBName = null;
    }
    public void loadInDB(File dataBase, String DBName){
        this.DBName = DBName;
        tablelst = new ArrayList<Table>();
        String files[] = dataBase.list();
        for(int i = 0; i < files.length; i++){
            //f.list also picks up hidden files so this stops it
            tablelst.add(new Table(files[i]));
            tablelst.get(i).ReadInTable(DBName);
        }
    }

    public int getTableNumFromName(String tableName){
        int tableNum = 0;
        while(tableNum < tablelst.size()){
            if(tablelst.get(tableNum).getTableName().equals(tableName)){
                return tableNum;
            }
            tableNum++;
        }
        return -1;
    }
    public String getDBName(){
        return DBName;
    }
    public void setDBFile(File file){
        dbFile = file;
    }
    public File getDBFile(){
        return dbFile;
    }

    public void addToTable(String newRow, int tableNum){
        tablelst.get(tableNum).writeToTable(newRow);
        tablelst.get(tableNum).overWriteTableFile(getDBName());
    }

    public boolean tableExists(String tableName){
        for(int i = 0; i < tablelst.size(); i++){
            if(tablelst.get(i).getTableName().equals(tableName)){
                return true;
            }
        }
        return false;
    }
    public int getNumAttributes(int tableNum){
        return tablelst.get(tableNum).getNumAttributes();
    }

    public int getNumRowsFromTable(int tableNum){
        return tablelst.get(tableNum).getNumRows();
    }
    public Table getTable(int tableNum){
        return tablelst.get(tableNum);
    }
}
