
import java.io.*;
import java.net.*;


class MessageSender implements Runnable {
    public final static int PORT = 7331;
    private DatagramSocket sock;
    MessageSender(DatagramSocket s) {
        sock = s;
    }
    private void sendMessage(String s) throws Exception {
        byte buf[] = s.getBytes();
        InetAddress address = InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        sock.send(packet);
    }
    public void run() {
        boolean connected = false;
        do {
            try {
                sendMessage("GREETINGS");
                connected = true;
            } catch (Exception e) {
                System.out.println("not Connected");
            }
        } while (!connected);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                while (!in.ready()) {
                    Thread.sleep(100);
                }
                sendMessage(in.readLine());
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
}
class MessageReceiver implements Runnable {
    DatagramSocket sock;
    byte buf[];
    MessageReceiver(DatagramSocket s) {
        sock = s;
        buf = new byte[1024];
    }
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                sock.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
}

   public class UDPChatClient { 
    public static void main(String args[]) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        MessageReceiver receiver = new MessageReceiver(socket);
        MessageSender sender = new MessageSender(socket);
        Thread recieveThread = new Thread(receiver);
        Thread sendThread = new Thread(sender);
        recieveThread.start(); sendThread.start();
    }
}
