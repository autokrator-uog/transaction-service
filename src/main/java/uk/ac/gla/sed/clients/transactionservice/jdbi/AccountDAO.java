package uk.ac.gla.sed.clients.transactionservice.jdbi;

import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public class AccountDAO {
    @SqlUpdate("CREATE TABLE transactions (id int PRIMARY KEY, ")
    void createTransactionTable();
}
