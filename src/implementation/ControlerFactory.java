package implementation;

import domainLayer.PersistenceSystem;
import domainLayer.TheController;

/**
 * This is a static class that only makes sure that the controler
 * is a signeleton
 * @author NARAD
 *
 */
public class ControlerFactory {
	private static TheController _controler;
	
	public static TheController getControler()
	{
		if (_controler == null)
		{
			_controler = new TheController();
		}
		return _controler;
	}
	
}
