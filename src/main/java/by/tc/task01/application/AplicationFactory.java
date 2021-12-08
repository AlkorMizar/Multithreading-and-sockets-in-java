package by.tc.task01.application;

import by.tc.task01.dao.ArchiveDAO;
import by.tc.task01.dao.DAOFactory;
import by.tc.task01.dao.impl.ArchiveDAOImpl;

public final class  AplicationFactory {
    private static final AplicationFactory instance = new AplicationFactory();

    private final Application app = new Application();

    private AplicationFactory() {}

    public Application getApplication() {
        return app;
    }

    public static AplicationFactory getInstance() {
        return instance;
    }
}
