package uk.ac.gla.sed.clients.transactionservice.jdbi;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface TransactionDAO {
    @SqlUpdate("CREATE TABLE transactions (id int PRIMARY KEY, status int NOT NULL DEFAULT 0 CHECK (status >=0 AND status <= 2), reason text DEFAULT NULL)")
    void createTransactionTable();

    @SqlUpdate("DROP TABLE IF EXISTS transactions")
    void deleteTableIfExists();

    @SqlUpdate("INSERT INTO transactions (id) VALUES (:id)")
    void addTransaction(@Bind("id") int transactionID);

    @SqlQuery("SELECT status FROM transactions WHERE id=:id")
    Integer getStatus(@Bind("id") int transactionID);

    @SqlQuery("SELECT reason FROM transactions WHERE id=:id")
    String getReason(@Bind("id") int transactionID);

    @SqlQuery("SELECT id FROM transactions ORDER BY id DESC LIMIT 1;")
    Integer getHighestAccountId();

    @SqlUpdate("UPDATE transactions SET status=:status, reason=:reason WHERE id=:id")
    void updateStatus(@Bind("id") int transactionID, @Bind("status") int status, @Bind("reason") String reason);

    @SqlUpdate("UPDATE transactions SET status=:status WHERE id=:id")
    void updateStatus(@Bind("id") int transactionID, @Bind("status") int status);
}
