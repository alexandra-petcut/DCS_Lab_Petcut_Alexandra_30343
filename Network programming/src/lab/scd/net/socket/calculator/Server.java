package lab.scd.net.socket.calculator;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket ss=null;
        Socket s=null;

        try{
            String line="";
            ss = new ServerSocket(1900); //create a serversocket object
            System.out.println("Server is waiting for connections...");
            s = ss.accept(); //start waiting for connections at port 1900
            //When a client connects, `ss.accept()` returns.
            //a Socket object that identifies the connection

            //Create the input and output streams.
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);

            //extract the IP address and port from which the client connected
            InetSocketAddress remoteadr = (InetSocketAddress)s.getRemoteSocketAddress();
            String remotehost = remoteadr.getHostName();
            int remoteport = remoteadr.getPort();

            System.out.println("Client nou conectat: "+remotehost+":"+remoteport);

            while(!line.equals("END")){
                line = in.readLine();
                if (line == null || line.equals("END")) {
                    break;
                }
                System.out.println("Server a receptionat:"+line);

                try {
                    String[] parts = line.split(";");

                    double nr1 = Double.parseDouble(parts[0]);
                    double nr2 = Double.parseDouble(parts[1]);
                    String operatie = parts[2];

                    double rezultat;

                    switch (operatie) {
                        case "+":
                            rezultat = nr1 + nr2;
                            break;

                        case "-":
                            rezultat = nr1 - nr2;
                            break;

                        case "*":
                            rezultat = nr1 * nr2;
                            break;

                        case "/":
                            if (nr2 == 0) {
                                out.println("Eroare: impartire la 0");
                                continue;
                            }

                            rezultat = nr1 / nr2;
                            break;

                        default:
                            out.println("Eroare: operatie necunoscuta");
                            continue;
                    }

                    System.out.println("Rezultat: " + rezultat);

                    out.println(rezultat);

                } catch (Exception e) {
                    out.println("Eroare: date invalide");
                }
            }

            System.out.println("Aplicatie server gata.");
            out.flush();

        }catch(Exception e){e.printStackTrace();}
        finally{
            ss.close();
            if(s!=null) s.close();
        }
    }
}