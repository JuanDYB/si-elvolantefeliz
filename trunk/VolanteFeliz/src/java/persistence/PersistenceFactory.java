package persistence;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class PersistenceFactory {
    public static PersistenceInterface getInstance (String persistenceMethod){
        if (persistenceMethod.equals("MySQL")){
            return PersistenceMySQL.getInstance();
        }
        return null;
    }
}
