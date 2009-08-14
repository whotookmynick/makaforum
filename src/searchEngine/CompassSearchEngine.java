package searchEngine;
import java.text.*;
import implementation.Message;
import implementation.MessageSearchAnswer;

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
        session.close();
	}

	@Override
	public void removeMessageFromEngine(long mid) {
		CompassSession session = _compass.openSession();
        session.delete(Message.class,mid);
        session.close();
		
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
	public Vector<Message> searchByDate(Date fromDate, Date toDate)
	{
		Vector<Message> ans = new Vector<Message>();
		if (fromDate == null | toDate == null)
		{
			return ans;
		}
		long fromLong = fromDate.getTime();
		long toLong = toDate.getTime();
		ans = searchByGeneric("time", "[" + fromLong + " TO " + toLong + "]");
		return ans;
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
            MessageSearchAnswer messageWithScore = new MessageSearchAnswer(m2,detachedHits[i].getScore());
            ans.add(messageWithScore);
        }
        session.close();
        return ans;
	}

}
