package by.tc.task01.dao;

import by.tc.task01.dao.impl.ArchiveDAOImpl;

public final class DAOFactory {
	private static final DAOFactory instance = new DAOFactory();

	private final ArchiveDAO applianceDAO = new ArchiveDAOImpl();
	
	private DAOFactory() {}

	public ArchiveDAO getApplianceDAO() {
		return applianceDAO;
	}

	public static DAOFactory getInstance() {
		return instance;
	}
}
