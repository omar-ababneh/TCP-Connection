package ServerSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket)
    {
        this.serverSocket=serverSocket;
   }

    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket=new ServerSocket(4444);
        Server server=new Server(serverSocket);
        server.StartServer();
    }

    public void StartServer()
    {
           try {
               while (!serverSocket.isClosed()){
               Socket socket = serverSocket.accept();
               System.out.println("A new client has created !!");
               ClientHandler clientHandler=new ClientHandler(socket);
               Thread thread=new Thread(clientHandler);
               thread.start();
               }
           }
           catch (IOException ioException) {
               CloseServer();
               ioException.printStackTrace();
           }
       }

    public void CloseServer()
    {
       try{
           if(serverSocket!=null){
               serverSocket.close();
           }
       }
       catch (IOException ioException){
           ioException.printStackTrace();
       }
    }

}
