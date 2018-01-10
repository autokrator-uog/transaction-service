Feature: Transaction Service
  The Transaction Service is responsible for handling user requests to send money.
  The Transaction Service is responsible for allowing users to check the status of their transactions.

  Scenario: User performs a transaction
    When the user with accountId 1 submits a transaction request to send £50 to accountId 2
    Then a PendingTransaction event was created from account 1 to 2 for amount £50 with transactionId 1
    And the transaction service has stored a transaction with id 1 with status "PENDING"

  Scenario: Transaction is accepted
    Given there exists a PendingTransaction with id 20
    When an "AcceptedTransaction" event is received for transactionId 20
    Then the transaction service has stored a transaction with id 20 with status "ACCEPTED"

  Scenario: Transaction is rejected
    Given there exists a PendingTransaction with id 35
    When a "RejectedTransaction" event is received for transactionId 35
    Then the transaction service has stored a transaction with id 35 with status "REJECTED"

#  Scenario: Alice's browser requests the status of her transaction, and it is ready
#    Given the status of Transaction ID 1 is "ACCEPTED"
#    When Alice's browser submits a polling request for the status of transaction "1"
#    Then the transaction service responds with "ACCEPTED"
#
#  Scenario: Alice's browser requests the status of her transaction, and it is still being processed
#    Given the status of Transaction ID 1 is "PENDING"
#    When Alice's browser submits a polling request for the status of transaction "1"
#    Then the transaction service responds with "PENDING"
