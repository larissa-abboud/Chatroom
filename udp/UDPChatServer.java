
import java.io.*;
import java.net.*;
import java.util.*;

public class UDPChatServer extends Thread {
    public final static int PORT = 7331;
    private final static int BUFFER = 1024;
    
    private DatagramSocket socket;

    private ArrayList <Client> clients;

    public UDPChatServer() throws IOException {
        socket = new DatagramSocket(PORT);

        clients = new ArrayList<Client>();
    }
    
    public void run() {
        byte[] buf = new byte[BUFFER];
        while (true) {
            try {
                Arrays.fill(buf, (byte)0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet); //create packet 
                
                String content = new String(packet.getData()); //obtain data
                
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                 
                Client newClient = new Client(clientPort, clientAddress, clients.size()); 

                boolean clientFound = false;
                for(int i = 0; i < clients.size(); i++){
                    if(clients.get(i).equals(newClient)){
                        newClient = clients.get(i);
                        clientFound = true;
                        break;
                    }
                }


                if(!clientFound || clients.size() == 0){
                    clients.add(newClient);
                    clientFound = false;
                }

                
                System.out.println("Client" + newClient.getClientNumber() + " : " + content);
                byte[] data = ("Client" + newClient.getClientNumber() + " : " +  content).getBytes();
                for (int i=0; i < clients.size(); i++) {
                    InetAddress cl = clients.get(i).getAddress();
                    int cp = clients.get(i).getPort();
                    packet = new DatagramPacket(data, data.length, cl, cp);
                    socket.send(packet);
                }
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
    
    public static void main(String args[]) throws Exception {
        UDPChatServer s = new UDPChatServer();
        s.start();
    }
}
