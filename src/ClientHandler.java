

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String ClientUserName;
    public ClientHandler(Socket socket)
    {
        try{
        this.socket=socket;
        this.bufferedReader=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        this.ClientUserName= bufferedReader.readLine();
        RedirectMessage(ClientUserName+" has entered the system !!");
        } catch (IOException ioException) {
            closeResources(socket,bufferedReader,bufferedWriter);
            ioException.printStackTrace();
        }
    }

    private void closeResources(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try {
            if(socket!=null)
                socket.close();
            if(bufferedReader!=null)
                bufferedReader.close();
            if(bufferedWriter!=null)
                bufferedWriter.close();
        }
        catch (IOException ioException)
        {
        ioException.printStackTrace();
        }
    }

    private void RedirectMessage(String Message)
    {
        try {
                Message=Message.toUpperCase();
                this.bufferedWriter.write("SERVER : "+Message);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
        }
        catch (NullPointerException nullPointerException){
            closeResources(socket,bufferedReader,bufferedWriter);
        }
        catch (IOException ioException){
            closeResources(socket,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run()
    {
    String clientMessage;
    while (!socket.isClosed()){
        try
        {
            clientMessage= bufferedReader.readLine();
            String finalClientMessage = clientMessage;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RedirectMessage(finalClientMessage);
                }
            }).start();

        }
        catch (IOException ioException)
        {
            closeResources(socket,bufferedReader,bufferedWriter);
            break;
        }

    }
    }
}
