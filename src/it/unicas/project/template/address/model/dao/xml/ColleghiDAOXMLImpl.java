package it.unicas.project.template.address.model.dao.xml;

import it.unicas.project.template.address.model.Colleghi;
import it.unicas.project.template.address.model.dao.DAO;
import it.unicas.project.template.address.model.dao.DAOException;
import it.unicas.project.template.address.model.dao.mysql.DAOMySQLSettings;
import it.unicas.project.template.address.util.DateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ColleghiDAOXMLImpl implements DAO<Colleghi> {

    private ColleghiDAOXMLImpl(){}

    private static DAO dao = null;
    private static Logger logger = null;
    public static DAO getInstance(){
        if (dao == null){
            dao = new ColleghiDAOXMLImpl();
            logger = Logger.getLogger(ColleghiDAOXMLImpl.class.getName());

            // printing methods:
            //logger.info("Hello World!");
        }
        return dao;
    }

    public static void main(String args[]) throws DAOException {
        ColleghiDAOXMLImpl c = new ColleghiDAOXMLImpl();

        /*
        c.insert(new Colleghi("Mario", "Rossi", "0824981", "molinara@uni.it", "21-10-2017", null));
        c.insert(new Colleghi("Carlo", "Ciampi", "0824982", "ciampi@uni.it", "22-02-2017", null));
        c.insert(new Colleghi("Ornella", "Vaniglia", "0824983", "vaniglia@uni.it", "23-05-2017", null));
        c.insert(new Colleghi("Cornelia", "Crudelia", "0824984", "crudelia@uni.it", "24-05-2017", null));
        c.insert(new Colleghi("Franco", "Bertolucci", "0824985", "bertolucci@uni.it", "25-10-2017", null));
        c.insert(new Colleghi("Carmine", "Labagnara", "0824986", "lagbagnara@uni.it", "26-10-2017", null));
        c.insert(new Colleghi("Mauro", "Cresta", "0824987", "cresta@uni.it", "27-12-2017", null));
        c.insert(new Colleghi("Andrea", "Coluccio", "0824988", "coluccio@uni.it", "28-01-2017", null));
        */

    }


    @Override
    public List<Colleghi> select(Colleghi a) throws DAOException {
        throw new DAOException("Not yet implemented!");
    }


    @Override
    public void delete(Colleghi a) throws DAOException {
        throw new DAOException("Not yet implemented!");
    }

    @Override
    public void insert(Colleghi a) throws DAOException {
        throw new DAOException("Not yet implemented!");
    }


    @Override
    public void update(Colleghi a) throws DAOException {
        throw new DAOException("Not yet implemented!");
    }


}
