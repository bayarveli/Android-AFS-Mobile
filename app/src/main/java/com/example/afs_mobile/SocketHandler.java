package com.example.afs_mobile;

import java.net.Socket;

public class SocketHandler {
    private static Socket mTcpClientSocket;

    public static synchronized  Socket getSocket() {
        return mTcpClientSocket;
    }

    public static synchronized  void setSocket(Socket aSocket) {
        SocketHandler.mTcpClientSocket = aSocket;
    }
}
