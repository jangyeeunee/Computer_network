package Socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

    public static String socketfromServer(String response,String request)
    {
        String complete = response.split(":")[0];
        if(complete.equalsIgnoreCase("Exception"))
        {
            return "Error message : " + response.split(":")[1];
        }else if(complete.equalsIgnoreCase("Account found"))
        {
            return request.split(" ")[1] + " has &" + response.split(":")[2];
        }else if(complete.equalsIgnoreCase("Accept")&&request.split(" ")[0].equals("TRANS"))
        {
            return request.split(" ")[1] + " now has &" + response.split(":")[3];
        }else
            return request.split(" ")[1] + " now has &" + response.split(":")[2];
    }


    public static void main(String[] args) throws Exception {
        String sentence;
        String response;

        String ServerFile = "environment.dat";

        int serverPort = 0;
        String serverIp = "";


        try{

            BufferedReader readServer = new BufferedReader(new FileReader(ServerFile));
            serverIp = readServer.readLine().split(":")[1];
            serverPort = Integer.parseInt(readServer.readLine().split(":")[1]);

        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Server file reading Error");
            System.exit(1);
        }


        while (true) {

            try {
                Socket clientSocket = new Socket(serverIp, serverPort);

                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                sentence = inFromUser.readLine();

                outToServer.writeBytes(sentence + '\n');

                response = inFromServer.readLine();

                System.out.println(socketfromServer(response,sentence));

                clientSocket.close();

            } catch (SocketException e) {
                System.out.println("Socket exception: " + e);
            }
        }
    }

}
