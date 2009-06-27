package webServer;

import implementation.ControlerFactory;

import java.io.*;
import java.net.*;

import domainLayer.TheController;

public class MultipleClientProtocolServer implements Runnable {
	ServerSocket _serverSocket;
	int _listenPort;
	TheController _controller;
	
	public MultipleClientProtocolServer(int port, TheController controller) {
		_serverSocket = null;
		_listenPort = port;
		_controller = controller;
	}
	
	public void run() {
		try {
			_serverSocket = new ServerSocket(_listenPort);
			System.out.println("Listening...");
		} catch (IOException e) {
			System.out.println("Cannot listen on port " + _listenPort);
		}
		while (true) {
			try {
				ConnectionHandler newConnection = new ConnectionHandler(_serverSocket.accept(), new ServerProtocolImp(_controller),_controller);
				new Thread(newConnection).start();
			} catch (IOException e) {
				System.out.println("Failed to accept on port " + _listenPort);
			}
		}
	}

	// Closes the connection
	public void close() throws IOException {
		_serverSocket.close();
	}
	
}
