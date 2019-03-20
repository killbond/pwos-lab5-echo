package client;

import network.TCPConnection;
import network.TCPConnectionListener;
import org.joda.time.Instant;
import org.joda.time.Interval;
import java.io.IOException;

public class Client implements TCPConnectionListener {

    private TCPConnection connection;

    private static final int PORT = 8189;

    private Instant start;

    public static void main(String[] args) throws IOException, InterruptedException {
        new Client();
    }

    public Client() throws IOException, InterruptedException {
        try {
            connection = new TCPConnection(this, "127.0.0.1", PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }

        while(true) {
            Thread.sleep(1000);
            start = new Instant();
            connection.sendString("echo");
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        Instant end = new Instant();
        Interval interval = new Interval(start, end);
        printMsg("\n[" + end.toDateTime() + "] received a response from the server for " + interval.toDurationMillis() +" ms");
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception: " + e);

    }

    private synchronized void printMsg(String msg) {
        System.out.print(msg);
    }
}
