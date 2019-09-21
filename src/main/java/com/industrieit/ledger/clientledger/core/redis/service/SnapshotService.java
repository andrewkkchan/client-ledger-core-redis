package com.industrieit.ledger.clientledger.core.redis.service;

import com.industrieit.ledger.clientledger.core.redis.entity.Account;
import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import com.industrieit.ledger.clientledger.core.redis.model.request.impl.SnapshotRequest;

/**
 * Serializable isolated transactional service to mutate {@link Account}
 */
public interface SnapshotService {
    /**
     * snapshot by capturing {@link JournalEntry} for required {@link Account}
     * and calculated {@link Account} exactly and precisely up to the snapshot epoch
     * @param snapshotRequest requires  which {@link Account} to snapshot, null means all accounts are required
     * @return timestamp of the snapshot epoch which the snapshot are accurate as at.
     */
    long snapshot(SnapshotRequest snapshotRequest);
}
