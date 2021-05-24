import java.io.*;
import java.net.*;
import java.util.*;

class StagServer
{
    Controller controller;
    public static void main(String args[])
    {
        if(args.length != 2){
            System.out.println("Usage: java StagServer <entity-file> <action-file>");
        }
        else{
            new StagServer(args[0], args[1], 8888);
        }
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        try {
            controller = new Controller(entityFilename, actionFilename);
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) acceptNextConnection(ss);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
    {
        String line = in.readLine();
        controller.doNextCommand(line);
        System.out.println(line);
        ArrayList<String> result = controller.getResult();
        String returnString = "";
        for(int i = 0; i < result.size(); i++) {
            returnString += result.get(i) + "\n";
        }
        out.write(returnString);
    }
}


