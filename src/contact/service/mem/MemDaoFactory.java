package contact.service.mem;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import contact.entity.Contact;
import contact.entity.XMLKeeper;
import contact.service.ContactDao;
import contact.service.DaoFactory;

/**
 * MemDaoFactory is a factory for getting instances of entity DAO object
 * that use memory-based persistence, which isn't really persistence at all!
 * 
 * @see contact.service.DaoFactory
 * @version 2014.09.19
 * @author jim
 */
public class MemDaoFactory extends DaoFactory {
	/** instance of the entity DAO */
	private ContactDao daoInstance;
	
	public MemDaoFactory() {
		daoInstance = new MemContactDao();
		try {
			XMLKeeper importContacts = new XMLKeeper();
			JAXBContext context = JAXBContext.newInstance( XMLKeeper.class ) ;
			File inputFile = new File( "D:\\workspace\\ContactsSevice.xml" );
			Unmarshaller unmarshaller = context.createUnmarshaller();	
			importContacts = (XMLKeeper) unmarshaller.unmarshal( inputFile );
			if ( importContacts.getContacts() == null ) {
				return;
			}
			for ( Contact contact : importContacts.getContacts() ) {
				daoInstance.save( contact );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ContactDao getContactDao() {
		return daoInstance;
	}
	
	@Override
	public void shutdown() {
		List<Contact> list = getContactDao().findAll();
		XMLKeeper exportContacts = new XMLKeeper();
		exportContacts.setContacts(list);
		try {
			JAXBContext context = JAXBContext.newInstance( XMLKeeper.class );
			File outputFile = new File("D:\\workspace\\ContactsSevice.xml");
			Marshaller marshaller = context.createMarshaller();	
			marshaller.marshal( exportContacts, outputFile );
		} catch ( JAXBException e ) {
			e.printStackTrace();
		}
	}
}
