import java.io.*;
import java.net.*;
import java.util.ArrayList;

class DBServer
{
    public DBServer(int portNumber, DBController controller)
    {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) processNextConnection(serverSocket, controller);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextConnection(ServerSocket serverSocket, DBController controller)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter, controller);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter, DBController controller) throws IOException, NullPointerException
    {
        ArrayList<String> result = new ArrayList<String>();
        String incomingCommand = socketReader.readLine();
        //send command to controller
        controller.control(incomingCommand);
        result = controller.getResult();
        System.out.println("Received message: " + incomingCommand);
        for(int i = 0; i < result.size(); i++){
            socketWriter.write(result.get(i));
            if(i + 1 < result.size()){
                socketWriter.write("\n");
            }
        }
        socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    public static void main(String args[])
    {
        DBController controller = new DBController();
        DBServer server = new DBServer(8888, controller);
    }
}
