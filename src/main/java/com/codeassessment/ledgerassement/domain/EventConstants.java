package com.codeassessment.ledgerassement.domain;

@SuppressWarnings({"unused"})
public interface EventConstants {

    String DESTINATION = "accounting-v1";
    String SELECTOR_NAME = "action";

    // system events
    String INITIALIZE = "initialize";
    String SELECTOR_INITIALIZE = SELECTOR_NAME + " = '" + INITIALIZE + "'";

    // ledger events
    String POST_LEDGER = "post-ledger";
    String PUT_LEDGER = "put-ledger";
    String DELETE_LEDGER = "delete-ledger";
    String POST_SUB_LEDGER = "post-sub-ledger";

    String SELECTOR_POST_LEDGER = SELECTOR_NAME + " = '" + POST_LEDGER + "'";
    String SELECTOR_PUT_LEDGER = SELECTOR_NAME + " = '" + PUT_LEDGER + "'";
    String SELECTOR_DELETE_LEDGER = SELECTOR_NAME + " = '" + DELETE_LEDGER + "'";
    String SELECTOR_POST_SUB_LEDGER = SELECTOR_NAME + " = '" + POST_SUB_LEDGER + "'";

    // account events
    String POST_ACCOUNT = "post-account";
    String PUT_ACCOUNT = "put-account";
    String DELETE_ACCOUNT = "delete-account";
    String LOCK_ACCOUNT = "lock-account";
    String UNLOCK_ACCOUNT = "unlock-account";
    String CLOSE_ACCOUNT = "close-account";
    String REOPEN_ACCOUNT = "reopen-account";

    String SELECTOR_POST_ACCOUNT = SELECTOR_NAME + " = '" + POST_ACCOUNT + "'";
    String SELECTOR_PUT_ACCOUNT = SELECTOR_NAME + " = '" + PUT_ACCOUNT + "'";
    String SELECTOR_DELETE_ACCOUNT = SELECTOR_NAME + " = '" + DELETE_ACCOUNT + "'";
    String SELECTOR_LOCK_ACCOUNT = SELECTOR_NAME + " = '" + LOCK_ACCOUNT + "'";
    String SELECTOR_UNLOCK_ACCOUNT = SELECTOR_NAME + " = '" + UNLOCK_ACCOUNT + "'";
    String SELECTOR_CLOSE_ACCOUNT = SELECTOR_NAME + " = '" + CLOSE_ACCOUNT + "'";
    String SELECTOR_REOPEN_ACCOUNT = SELECTOR_NAME + " = '" + REOPEN_ACCOUNT + "'";

    // journal events
    String POST_JOURNAL_ENTRY = "post-entry";
    String RELEASE_JOURNAL_ENTRY = "release-entry";

    String SELECTOR_POST_JOURNAL_ENTRY = SELECTOR_NAME + " = '" + POST_JOURNAL_ENTRY + "'";
    String SELECTOR_RELEASE_JOURNAL_ENTRY = SELECTOR_NAME + " = '" + RELEASE_JOURNAL_ENTRY + "'";

    String POST_TX_TYPE = "post-tx-type";
    String SELECTOR_POST_TX_TYPE = SELECTOR_NAME + " = '" + POST_TX_TYPE + "'";
    String PUT_TX_TYPE = "put-tx-type";
    String SELECTOR_PUT_TX_TYPE = SELECTOR_NAME + " = '" + PUT_TX_TYPE + "'";
}
