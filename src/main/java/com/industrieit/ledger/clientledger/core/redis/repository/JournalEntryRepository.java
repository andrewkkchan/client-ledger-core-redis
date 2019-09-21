package com.industrieit.ledger.clientledger.core.redis.repository;


import com.industrieit.ledger.clientledger.core.redis.entity.JournalEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JournalEntryRepository extends CrudRepository<JournalEntry, String> {
    Iterable<JournalEntry> findAllByAccountId(String accountId);

    @Override
    <S extends JournalEntry> Iterable<S> saveAll(Iterable<S> iterable);

    Iterable<JournalEntry> findAllByRequestId(String requestId);
}
