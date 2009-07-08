package webServer;

import implementation.ControlerFactory;
import jaxcentServer.*;
import java.io.IOException;

import domainLayer.TheController;

public class RunBothServers {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Get port
			int port = 1234;
			TheController controller = ControlerFactory.getControler();
			MultipleClientProtocolServer server = new MultipleClientProtocolServer(port,controller);
			Thread ourServerThread = new Thread(server);
			ourServerThread.start();
			
			int jaxcentPort = 2000;
//			String htmlFolder = ".\\bin\\webServer\\jaxcent";
			String htmlFolder = "./bin/webServer/jaxcent";
//			String classFolder = ".\\bin";
			String classFolder = "./bin";
//			String configFile = ".\\bin\\webServer\\jaxcent\\JaxcentConfig.xml";
			String configFile = "./bin/webServer/jaxcent/JaxcentConfig.xml";
			ServerMain jaxcentServer = new ServerMain(jaxcentPort, htmlFolder, configFile, null);
			jaxcentServer.start();
//			Process jaxcentServer = Runtime.getRuntime().exec("java -jar src\\webServer\\jaxcent\\jaxtut.jar");
			ourServerThread.join();
			jaxcentServer.join();
//			jaxcentServer.destroy();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			System.out.println("Server stopped");
		}
	}

}
