
import java.net.*;

public class Client{
    private int port;
    private InetAddress address;
    private int clientNumber;

    public Client(){
        this.clientNumber = 0;
        this.port = 0;
        this.address =null;
    }
    public Client(int p, InetAddress add, int cn){
        this.port = p;
        this.address = add;
        this.clientNumber = cn;
    }

    public void setAddress(InetAddress add){
        this.address = add;
    }

    public void setPort(int p){
        this.port = p;
    }

    public void setClientNumber(int cn){
        this.clientNumber = cn;
    }

    public InetAddress getAddress(){
        return this.address;
    }

    public int getPort(){
        return this.port;
    }

    public int getClientNumber(){
        return this.clientNumber;
    }

    public boolean equals(Client other){
        if(this.address.equals(other.address) && this.port == other.port){
            return true;
        }return false;
    }

    public String toString(){
        return "port:" + port + "\nClientNumber: " + clientNumber + "\nAddress: " + address;
    }
}