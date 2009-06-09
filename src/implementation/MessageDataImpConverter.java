package implementation;

import org.compass.core.converter.ConversionException;
import org.compass.core.converter.basic.AbstractBasicConverter;
import org.compass.core.mapping.ResourcePropertyMapping;
import org.compass.core.marshall.MarshallingContext;
import org.compass.annotations.*;

public class MessageDataImpConverter extends AbstractBasicConverter<MessageDataImp> {

	@Override
	protected MessageDataImp doFromString(String arg0, ResourcePropertyMapping arg1,
			MarshallingContext arg2) throws ConversionException {
		return new MessageDataImp(arg0);
	}

}
