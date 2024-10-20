package Socket;

import java.net.*;
import java.io.*;

public class TCPServer  {

    static String ServerFile = "environment.dat";

    public static String Request(String request)
    {
        String method = request.split(" ")[0];
        Exception eLack = new Exception("Exception:Lack Of Arguments");
        Exception eSurplus = new Exception("Exception:Surplus Of Arguments");

        try
        {
            if(method.equalsIgnoreCase("check"))
            {
                if(request.split(" ").length < 2)
                {
                    throw eLack;
                }else if(request.split(" ").length > 2)
                {
                    throw eSurplus;
                }else
                    return check(request);
            }
            else if(method.equalsIgnoreCase("deposit"))
            {
                if(request.split(" ").length < 3)
                {
                    throw eLack;
                }else if(request.split(" ").length > 3)
                {
                    throw eSurplus;
                }else
                    return deposit(request);
            }
            else if(method.equalsIgnoreCase("withdraw"))
            {
                if(request.split(" ").length < 3)
                {
                    throw eLack;
                }else if(request.split(" ").length > 3)
                {
                    throw eSurplus;
                }else
                    return withdraw(request);
            }
            else if(method.equalsIgnoreCase("trans"))
            {
                if(request.split(" ").length < 4)
                {
                    throw eLack;
                }else if(request.split(" ").length > 4)
                {
                    throw eSurplus;
                }else
                    return transfer(request);
            }
            else
            {
                Exception e = new Exception("Exception:Invalid Method");
                throw e;
            }
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    public static void setBalance(int set, int balance, String name) throws Exception {
        String curLine = "";
        String output = "";

        if(set == 1)
        {

            boolean complete = false;

            try{
                BufferedReader readServer = new BufferedReader(new FileReader(ServerFile));


                while((curLine = readServer.readLine()) != null)
                {

                    if(curLine.split(" ")[0].equalsIgnoreCase(name))
                    {
                        output += name + ' ' + ((Integer.parseInt(curLine.split(" ")[1])) + balance) + '\n';
                        complete = true;
                    }
                    else
                    {
                        output += curLine + '\n';
                    }

                }
                readServer.close();

                if(!complete)
                {
                    Exception e = new Exception("Exception:Account not found");
                    throw e;
                }

                try
                {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(ServerFile));
                    writer.write(output);
                    writer.close();

                } catch (Exception e) {
                    throw e;
                }

            }catch(Exception e){
                throw e;
            }
        }

        if(set == 2)
        {
            boolean complete = false;
            try{
                BufferedReader readServer = new BufferedReader(new FileReader(ServerFile));


                while((curLine = readServer.readLine()) != null) {

                    if (curLine.split(" ")[0].equalsIgnoreCase(name)) {
                        output += name + ' ' + ((Integer.parseInt(curLine.split(" ")[1])) - balance) + '\n';
                        complete = true;
                    } else {
                        output += curLine + '\n';
                    }
                }

                readServer.close();
                if(!complete)
                {
                    Exception e = new Exception("Exception:Account not found");
                    throw e;
                }

                try
                {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(ServerFile));
                    writer.write(output);
                    writer.close();

                } catch (Exception e) {
                    throw e;
                }

            }catch(Exception e){
                throw e;
            }
        }
    }

    public static void setBalance(int balance, String sender, String receiver) throws Exception  {
        String curLine = "";
        String output = "";
        try{
            boolean completeSender = false;
            boolean completeReceiver = false;

            BufferedReader readServer = new BufferedReader(new FileReader(ServerFile));


            while((curLine = readServer.readLine()) != null)
            {

                if(curLine.split(" ")[0].equalsIgnoreCase(sender))
                {
                    completeSender = true;
                    output += sender + ' ' + ((Integer.parseInt(curLine.split(" ")[1])) - balance) + '\n';

                } else if(curLine.split(" ")[0].equalsIgnoreCase(receiver))
                {
                    completeReceiver = true;
                    output += receiver + ' ' + ((Integer.parseInt(curLine.split(" ")[1])) + balance) + '\n';
                }
                else {
                    output += curLine + '\n';
                }

            }

            if(!completeReceiver || !completeSender)
            {
                Exception e = new Exception("Exception:Account not found");
                readServer.close();
                throw e;
            }

            readServer.close();
            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(ServerFile));
                writer.write(output);
                writer.close();

            } catch (Exception e) {
                throw e;
            }


        }catch(Exception e){
           throw e;
        }
    }

    public static String check(String request) throws Exception {
        String curLine ;
        String name = request.split(" ")[1];

        try{

            BufferedReader readServer = new BufferedReader(new FileReader(ServerFile));

            while((curLine = readServer.readLine())!=null)
            {
                if(name.equalsIgnoreCase(curLine.split(" ")[0]))
                {
                    readServer.close();
                    return "Account found:Balance:"+curLine.split(" ")[1];
                }
            }
            readServer.close();

            Exception e = new Exception("Exception:No client");
            throw e;

        } catch (Exception e) {
            throw e;
        }

    }

    public static String deposit(String request) throws Exception {
        String curLine ;
        String name = request.split(" ")[1];

        int depositBalance = Integer.parseInt(request.split(" ")[2]);

        try{
            setBalance(1,depositBalance,name);

            BufferedReader readServer = new BufferedReader(new FileReader(ServerFile));

            while((curLine=readServer.readLine())!=null)
            {

                if(name.equalsIgnoreCase(curLine.split(" ")[0]))
                    break;
            }

            readServer.close();

            return "Accept:Balance"+curLine.split(" ")[1];

        } catch (Exception e) {
            throw e;
        }

    }

    public static String withdraw (String request) throws Exception {
        String curLine ;
        String name = request.split(" ")[1];

        int withdrawBalance = Integer.parseInt(request.split(" ")[2]);

        try{

            if((Integer.parseInt(check(request).split(":")[2]) - withdrawBalance ) <0)
            {
                Exception e = new Exception("Exception:Insufficient Balance");
                throw e;
            }

            setBalance(2,withdrawBalance,name);

            BufferedReader readServer = new BufferedReader(new FileReader(ServerFile));

            while((curLine=readServer.readLine())!=null)
            {

                if(name.equalsIgnoreCase(curLine.split(" ")[0]))
                    break;
            }
            readServer.close();

            return "Accept:Balance:"+curLine.split(" ")[1];

        } catch (Exception e) {
            throw e;
        }

    }

    public static String transfer(String request) throws Exception {
        String curLine = "";
        String resultSender = "";
        String resultReceiver = "";
        String sender = request.split(" ")[1];
        String receiver = request.split(" ")[2];
        int transferAmount = Integer.parseInt(request.split(" ")[3]);

        try{

            if((Integer.parseInt(check(request).split(":")[2]) - transferAmount )< 0)
            {
                Exception e = new Exception("Exception:Insufficient Balance");
                throw e;
            }

            setBalance(transferAmount,sender,receiver);

            BufferedReader readServer = new BufferedReader(new FileReader(ServerFile));

            while((curLine=readServer.readLine())!=null)
            {

                if(sender.equalsIgnoreCase(curLine.split(" ")[0]))
                {
                    resultSender = "sBalance:"+curLine.split(" ")[1];
                }
                if(receiver.equalsIgnoreCase(curLine.split(" ")[0]))
                {
                    resultReceiver = "rBalance:"+curLine.split(" ")[1];
                }
            }
            readServer.close();
            return "Accept:"+ resultSender + ":" + resultReceiver;

        } catch (Exception e) {
            throw e;
        }

    }

    public static void main(String[] args) throws Exception {

        String clientSentence;
        String capitalizedSentence;

        ServerSocket welcomeSocket = new ServerSocket(6789);

        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            clientSentence = inFromClient.readLine();

            capitalizedSentence = Request(clientSentence) + '\n';

            outToClient.writeBytes(capitalizedSentence);

       }


    }
}
