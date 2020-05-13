package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    public class SocketThread extends Thread {

        public void run () {
            String address = null;
            try {
                address = getServerAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }
            int port = 0;
            try {
                port = getServerPort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Socket socket;
            try {
                socket = new Socket(address, port);
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException e) {
                notifyConnectionStatusChanged(false);
            } catch (ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + "added" );
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + "left chat" );
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws Exception {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.NAME_REQUEST) {
                    try {
                        connection.send(new Message(MessageType.USER_NAME, getUserName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (message.getType() == MessageType.NAME_ACCEPTED) {
                        notifyConnectionStatusChanged(true);
                        return;
                    } else {
                        throw new IOException("Unexpected MessageType" );
                    }
                }
            }



        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {


            while (true) {
                Message message = connection.receive();
                if (message.getType()!=MessageType.TEXT&&message.getType()!=MessageType.USER_ADDED&&message.getType()!=MessageType.USER_REMOVED)throw new IOException("Unexpected MessageType");
                switch (message.getType()){
                    case TEXT:
                        processIncomingMessage(message.getData());
                        break;
                    case USER_ADDED:
                        informAboutAddingNewUser(message.getData());
                        break;
                    case USER_REMOVED:
                        informAboutDeletingNewUser(message.getData());
                        break;

                    default: throw new IOException("Unexpected MessageType");
                }

            }
        }
    }

    protected String getServerAddress() throws Exception {
        ConsoleHelper.writeMessage("Enter server address:" );
        return ConsoleHelper.readString();
    }

    protected int getServerPort() throws Exception {
        ConsoleHelper.writeMessage("hey" );
        int portAdress = ConsoleHelper.readInt();
        return portAdress;
    }

    protected String getUserName() throws Exception {
        ConsoleHelper.writeMessage("hi" );
        String userName = ConsoleHelper.readString();
        return userName;
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected SocketThread getSocketThread() {
        SocketThread socketThread = new SocketThread();
        return socketThread;
    }

    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));

        } catch (IOException e) {
            System.out.println("Client is not connected" );
            clientConnected = false;
            e.getMessage();
        }
    }


    public void run() throws Exception {
        SocketThread thread = getSocketThread();
        thread.setDaemon(true);
        thread.start();

        synchronized (this) {
            try {
                this.wait();
                while (clientConnected) {
                    String line = ConsoleHelper.readString();
                    if (line.equals("exit" )) break;
                    if (shouldSendTextFromConsole()) sendTextMessage(line);
                }
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage("Socket thread is interrupted!" );
            }
        }


    }


    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.run();

    }
}
