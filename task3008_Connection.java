package com.javarush.task.task30.task3008;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void send(Message message) throws IOException {
        synchronized (this.out) {
            this.out.writeObject(message);
            out.flush();
        }
    }

    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (this.in) {
            return (Message) this.in.readObject();
        }
    }

    public SocketAddress getRemoteSocketAddress() {
        return this.socket.getRemoteSocketAddress();
    }


    public void close() throws IOException {
       this.socket.close();
       this.out.close();
       this.in.close();
    }
}
