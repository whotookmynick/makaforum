package webServer;

import java.io.*;
import java.net.*;

import domainLayer.TheController;

public class ConnectionHandler implements Runnable{

	private BufferedReader _in;
	private PrintWriter _out;
	Socket _clientSocket;
	ServerProtocol _protocol;
	TheController _controller;
	
	public ConnectionHandler(Socket acceptedSocket, ServerProtocol p) {
		_in = null;
		_out = null;
		_clientSocket = acceptedSocket;
		_protocol = p;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + 
				acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
	}
	
	public ConnectionHandler(Socket acceptedSocket, ServerProtocol p,TheController controller) {
		_in = null;
		_out = null;
		_clientSocket = acceptedSocket;
		_protocol = p;
		_controller = controller;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + 
				acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
	}

	public void run() {

		try {
			initialize();
		} catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}
		try {
			process();
		} catch (IOException e) {
			System.out.println("Error in I/O");
		} 
		System.out.println("Connection closed - bye bye...");
		close();
	}

	public void process() throws IOException {
		String msg;
		while ((msg = _in.readLine()) != null) {
			System.out.println("Received \"" + msg + "\" from client");
			String response = _protocol.processMessage(msg);
			if (response != null) {
				_out.println(response);
			}
			if (_protocol.isEnd(msg)) {
				break;
			}
		}
	}

	// Starts listening
	public void initialize() throws IOException {
		// Initialize I/O
		_in = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream()));
		_out = new PrintWriter(_clientSocket.getOutputStream(), true);
		System.out.println("I/O initialized");
	}

	/**
	 * This method runs whenever a connection is closed. 
	 * There is a need to add a way to log out users when the connection is closed.
	 */
	public void close() {
		try {
			if (_in != null) {
				_in.close();
			}
			if (_out != null) {
				_out.close();
			}
			_clientSocket.close();
		} catch (IOException e) {
			System.out.println("Exception in closing I/O");
		}
	}

}
