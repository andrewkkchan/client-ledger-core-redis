# client-ledger-core-redis: Redis-based consumer to demonstrate competing consumer in event sourcing
## What it does?
This code base is the part 3 of the 3 Part demo of event sourcing.  This code implements the consumption on Redis instead of Database to illustrate competing consumers and eventual consistency.
## Quick Start
### Step 1
```
redis-cli
```
Get you into the redis cli. Flush all keys.
```
flushall
```

### Step 2
Run the consumer in Redis, make sure the events are already in Kafka log.

### Step 3

In the Redis-Cli, run the follows:
```
key *
```

You will see all the journal entries are put in the Redis.
