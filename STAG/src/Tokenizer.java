public class Tokenizer {
    private String[] command;
    public Tokenizer(String line){
        command = line.split(" ");
    }

    public String[] getTokens(){
        return command;
    }
    //this function checks if the string word which contains the trigger word/s
    //matches the users commands and the returns true if so
    public boolean myContains(String word){
        int numTrig = 0;
        String[] trigger = word.split(" ");
        for(int i = 0; i < trigger.length; i++){
            for(int j = 0; j < command.length; j++){
                if(trigger[i].equals(command[j])){
                    numTrig++;
                }
            }
        }
        if(numTrig == trigger.length){
            return true;
        }
        return false;
    }
    //this is the same as above but only works for single works as well
    //as putting them all to lower case and is only used for the key words.
    public boolean myContainsLower(String word){
        for(int i = 0; i < command.length; i++){
            if(command[i].toLowerCase().equals(word)){
                return true;
            }
        }
        return false;
    }
}
