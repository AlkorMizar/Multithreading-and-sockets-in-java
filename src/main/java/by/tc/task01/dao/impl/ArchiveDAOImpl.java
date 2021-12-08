package by.tc.task01.dao.impl;

import by.tc.task01.dao.ArchiveDAO;
import by.tc.task01.entity.TableElement;
import by.tc.task01.entity.impl.ArchiveElement;
import by.tc.task01.entity.impl.User;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


public class ArchiveDAOImpl implements ArchiveDAO {
	Table usersTable, archiveTable;
	JAXBContext jaxbContext;

	public ArchiveDAOImpl(){
		File usersXml = new File("src\\main\\resources\\users.xml"),
			 archiveXml=new File("src\\main\\resources\\archive.xml")	;
		var classes=new Class[] {User.class, ArchiveElement.class, Table.class,TableElement.class};
		try
		{
			jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
					.createContext(classes, null);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			usersTable = (Table) jaxbUnmarshaller.unmarshal(usersXml);
			archiveTable = (Table) jaxbUnmarshaller.unmarshal(archiveXml);
		} catch (JAXBException e)
		{
			//e.printStackTrace();
		}
		Thread saveHook = new Thread(() -> {
			this.save(ArchiveDAO.TablesEn.ARCHIVE);
			this.save(ArchiveDAO.TablesEn.USERS);
		}
		);
		Runtime.getRuntime().addShutdownHook(saveHook);
	}
	@Override
	public boolean update(TableElement oldAppl, TableElement newAppl,TablesEn table) {
		return getTable(table).update(oldAppl,newAppl) && save(table) ;
	}

	@Override
	public boolean add(TableElement obj, TablesEn table) {
		return getTable(table).add(obj) && save(table) ;
	}

	@Override
	public boolean delete(TableElement t,TablesEn table) {
		return getTable(table).remove(t) && save(table) ;
	}

	@Override
	public Optional<TableElement> find(String[] criteria,TableElement elemToCompare, TablesEn table) {
		return getTable(table).find(criteria,elemToCompare);
	}

	@Override
	public Optional<TableElement> get(int id, TablesEn table) {
		return getTable(table).get(id);
	}

	@Override
	public List<TableElement> getAll(TablesEn table) {
		return getTable(table).getCopy();
	}

	@Override
	public boolean save(TablesEn table) {
		File usersXml = new File("src\\main\\resources\\users.xml"),
			archiveXml=new File("src\\main\\resources\\archive.xml")	;
		Marshaller jaxbMarshaller = null;
		try {
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			if (table==TablesEn.ARCHIVE){
				jaxbMarshaller.marshal(archiveTable, archiveXml);
			}else{
				jaxbMarshaller.marshal(usersTable, usersXml);
			}
			return true;
		} catch (JAXBException e) {
			return false;
			//e.printStackTrace();
		}

	}

	private Table getTable(TablesEn table){
		switch (table){
			case ARCHIVE:
				return archiveTable;
			case USERS:
				return usersTable;
		}
		return null;
	}
}