package com.industrieit.ledger.clientledger.core.redis.service;

import com.industrieit.ledger.clientledger.core.redis.model.request.impl.CreateAccountRequest;
import com.industrieit.ledger.clientledger.core.redis.entity.Account;

/**
 * Serializable isolated transactional service to mutate {@link Account}
 */
public interface AccountService {
    /**
     * Create an {@link Account} in serialized isolated transaction
     * @param createAccountRequest all the info needed for creating {@link Account}
     * @return {@link Account} successfully created
     */
    Account createAccount(CreateAccountRequest createAccountRequest);

}
