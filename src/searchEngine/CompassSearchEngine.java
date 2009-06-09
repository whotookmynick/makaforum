package searchEngine;
import java.util.*;
import java.text.*;

import implementation.Message;

import java.util.Date;
import java.util.Vector;

import org.compass.core.Compass;
import org.compass.core.CompassHit;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassConfigurationFactory;

import domainLayer.TheController;

public class CompassSearchEngine implements Search {

	Compass _compass;
	TheController _controller;

	public CompassSearchEngine(TheController controller)
	{
		_controller = controller;
		/* The genData directory is where the search engine will save its data */
        CompassConfiguration conf = CompassConfigurationFactory
                .newConfiguration()
                .setConnection("genData");
        conf.addClass(Message.class);

        _compass = conf.buildCompass();
	}

	@Override
	public void insertMessageToEngine(Message msg) {
        /* Open a session (not thread safe!) for this thread */
        CompassSession session = _compass.openSession();
        session.save(msg);
	}

	@Override
	public Vector<Message> searchByAuthor(String authorName) {
		/* Open a session (not thread safe!) for this thread */
        long authorId = _controller.get_uidFromUserName(authorName);
        if (authorId == -1)
        	return new Vector<Message>();
        String uidString ="" + authorId;
        return searchByGeneric("author",uidString );
	}

	@Override
	public Vector<Message> searchByContent(String sentence) {
		if (sentence == null)
        	return new Vector<Message>();
		return searchByGeneric("content",sentence);

	}

	@Override
	public Vector<Message> searchByDate(Date from, Date to) {
		Vector<Message> finalAns = null;
		long day = (1000*3600*24);
		long fromDate = from.getTime();
		long toDate = to.getTime();
		//get all results of dates in the specific period
		while(fromDate < toDate){
			String dateString = this.getDateFromLong(fromDate);
			Vector<Message> dayAns = searchByGeneric("time", dateString);
			for(int i=0; i<dayAns.size();i++){
				finalAns.add(dayAns.get(i));
			}
			fromDate = fromDate + day;
		}//End while

		return finalAns;
	}

	/*convert long to date and then to string*/
	private String getDateFromLong(long fromDate) {
	      DateFormat dataformat =  DateFormat.
	      getDateInstance(DateFormat.LONG);
	      return dataformat.format(fromDate);
	       //System.out.println(dataformat.format(date));
	}

	private Vector<Message> searchByGeneric(String propertyName, String value)
	{
        Vector<Message> ans = new Vector<Message>();
		/* Open a session (not thread safe!) for this thread */
        CompassSession session = _compass.openSession();
        CompassHits hits = session.find(propertyName + ": " + value);
        CompassHit[] detachedHits = hits.detach(0, 10).getHits();
        for (int i = 0; i < detachedHits.length; i++) {
            //System.out.println("score: "+detachedHits[i].getScore());
            Message m2 = (Message)(detachedHits[i].data());
            ans.add(m2);
        }
        return ans;
	}

}
