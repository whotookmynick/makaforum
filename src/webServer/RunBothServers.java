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
			
			int jaxcentPort = 80;
			String htmlFolder = "C:\\workspace\\Maka\\bin\\webServer\\jaxcent";
			String classFolder = "C:\\workspace\\Maka\\bin";
			String configFile = "C:\\workspace\\Maka\\bin\\webServer\\jaxcent\\JaxcentConfig.xml";
			ServerMain jaxcentServer = new ServerMain(jaxcentPort, htmlFolder, configFile, classFolder);
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
