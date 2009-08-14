package implementation;

public class MessageSearchAnswer extends Message {
	private float _score;
	public MessageSearchAnswer(Message m,float score) {
		super(m);
		_score = score;
	}
	
	@Override
	public String toString()
	{
		return super.toString() + "%score" + _score;
	}
	
}
