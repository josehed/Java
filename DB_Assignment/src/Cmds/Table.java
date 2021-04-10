package Cmds;

import java.io.*;
import java.util.ArrayList;

public class Table {
    private ArrayList<String> table;
    private String TableName;
    private static final int error = -1;
    public Table(String TName){
        TableName = TName;
    }

    public void ReadInTable(String DBname){
        table = new ArrayList<String>();
        File fileToOpen = new File(DBname + File.separator + TableName);
        table = new ArrayList();
        try {
            if(!fileToOpen.exists()){
                //if file doesn't exist throw error
                //fix/change this later
                throw new IOException("file doesn't exist");
            }
            else{
                //this is where we read from the file
                FileReader reader = new FileReader(fileToOpen);
                BufferedReader br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    // this will need to be changed to take table name
                    table.add(line);
                }
                br.close();
            }
        }catch(Exception read){
            return;
        }
    }
    public String getTableName(){
        return TableName;
    }
    public void overWriteTableFile(String DBname){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(DBname + File.separator + TableName));
            int tableSize = table.size();
            for(int i = 0; i < tableSize; i++){
                bw.write(table.get(i));
                bw.write("\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeToTable(String newRow){
        table.add(newRow);
    }
    public int getNumAttributes(){
        String firstLine = table.get(0);
        int cnt = 0;
        for(int i = 0; i < firstLine.length(); i++){
            if(firstLine.charAt(i) == '\t'){
                cnt++;
            }
        }
        return cnt;
    }
    public int getNumRows(){
        return table.size();
    }
    public String getRow(int rowNum){
        return table.get(rowNum);
    }
    public void removeRow(String row){
        for(int i = 0; i < table.size(); i++){
            if(table.get(i).equals(row)){
                table.remove(i);
            }
        }
    }
    public void setRow(String row, int rowNum){
        table.set(rowNum, row);
    }
    public boolean addColumn(String columnName){
        if(table.size() == 0){
            return false;
        }
        String topLine = table.get(0);
        topLine += columnName + "\t";
        table.set(0, topLine);
        for(int i = 1; i < table.size(); i++){
            String line = table.get(i) + " " + "\t";
            table.set(i, line);
        }
        return true;
    }
    public boolean removeColumn(String columnName){
        if(table.size() == 0){
            return false;
        }
        String topLine = table.get(0);
        String[] split = topLine.split("\t");
        int columnNum = error;
        for(int i = 0; i < split.length; i++){
            if(split[i].equals(columnName)){
                columnNum = i;
            }
        }
        if(columnNum == error){
            return false;
        }
        for(int i = 0; i < table.size(); i++){
            String[] splitLine = table.get(i).split("\t");
            ArrayList<String> listLine = new ArrayList<String>();
            String result = "";
            for(int j = 0; j < splitLine.length; j++){
                listLine.add(splitLine[j]);
            }
            listLine.remove(columnNum);
            for(int k = 0; k < listLine.size(); k++){
                result += listLine.get(k) + "\t";
            }
            table.set(i, result);
        }
        return true;
    }
}
