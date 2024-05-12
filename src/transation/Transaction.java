package transation;

public interface Transaction {
    void begin();
    void commit();
    void rollback();
}
