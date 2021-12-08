package by.tc.task01.dao;

import by.tc.task01.dao.impl.Table;
import by.tc.task01.entity.TableElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface ArchiveDAO {

	enum TablesEn{USERS,ARCHIVE};

	Optional<TableElement> get(int id,TablesEn table);

	List<TableElement> getAll(TablesEn table);

	boolean save(TablesEn table);

	boolean update(TableElement oldObj,TableElement newObj,TablesEn table);

	boolean add(TableElement obj,TablesEn table);

	boolean delete(TableElement t,TablesEn table);

	Optional<TableElement> find(String criteria[],TableElement elemToCompare,TablesEn table);
}
