package ClientSide;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;




public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    public Client(Socket socket, String username)
    {
        try
        {
            this.socket=socket;
            this.username=username;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException ioException)
        {
         closeResources(socket,bufferedReader,bufferedWriter);
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

    public void sendMessage()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    bufferedWriter.write(username);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    Scanner scanner=new Scanner(System.in);
                    while (!socket.isClosed())
                    {
                        String messageToMessage=scanner.nextLine();
                        if(messageToMessage.equalsIgnoreCase("Exist")){
                            System.out.println("Good Bay !!");
                            throw new IOException();
                        }
                        bufferedWriter.write(messageToMessage);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }

                }
                catch (IOException ioException)
                {
                    closeResources(socket,bufferedReader,bufferedWriter);
                }


            }}).start();
    }

    public void listenForMessage()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (!socket.isClosed()){
                    try {
                        msgFromGroupChat= bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }
                    catch (IOException ioException)
                    {
                        closeResources(socket,bufferedReader,bufferedWriter);
                    }

                }

            }
        }).start();
    }

    public static void main(String[] args) throws IOException
    {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Please enter user name for system :");
        String username=scanner.nextLine();
        if(username.equalsIgnoreCase("Exist")){
            System.out.println("Good Bay !!");
            throw new IOException();
        }
        Socket socket=new Socket("localhost",4444);
        Client client=new Client(socket,username);
        client.listenForMessage();
        client.sendMessage();
    }
}


