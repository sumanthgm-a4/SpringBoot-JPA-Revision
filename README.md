# JPA Reference Notes

> A concise reference for commonly used JPA concepts and relationships.

---

# Table of Contents

- JPA
- Entity
- Primary Keys
- Entity Relationships
- Cascade Types
- Fetch Types
- `mappedBy`
- Owning Side
- `orphanRemoval`
- Entity States
- Persistence Context
- Transactions
- Common Annotations
- Best Practices

---

# JPA

**JPA (Java Persistence API)** is a specification for mapping Java objects to relational database tables.

Hibernate is the most commonly used JPA implementation.

```
Java Objects  <--JPA-->  Database Tables
```

---

# Entity

Represents a database table.

```java
@Entity
@Table(name = "customers")
public class Customer {
}
```

Useful annotations

| Annotation | Purpose |
|------------|---------|
| @Entity | Marks a class as a JPA entity |
| @Table | Maps to a table |
| @Id | Primary key |
| @GeneratedValue | Auto-generate ID |
| @Column | Configure column |
| @Transient | Ignore field |
| @Enumerated | Store enums |
| @Lob | Large objects |

---

# Primary Key Strategies

```java
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

Strategies

- IDENTITY
- AUTO
- SEQUENCE
- TABLE

---

# Entity Relationships

## One-to-One

```
Person ------ Passport
```

```java
@OneToOne
```

---

## One-to-Many

```
Customer

    |

    +---- Order

    +---- Order

    +---- Order
```

```java
@OneToMany(mappedBy = "customer")
private List<Order> orders;
```

---

## Many-to-One

```
Order

   |

Customer
```

```java
@ManyToOne
private Customer customer;
```

This is usually the **owning side**.

---

## Many-to-Many

```
Student

    |

Course

```

Uses a join table.

```java
@ManyToMany
```

---

# Unidirectional vs Bidirectional

## Unidirectional

```
Customer -----> Orders
```

Only one object knows about the relationship.

---

## Bidirectional

```
Customer <------> Order
```

Both objects know each other.

---

# Owning Side

The owning side contains the foreign key.

Example

```java
@ManyToOne
private Customer customer;
```

This is the owning side.

The database looks at this field.

---

# mappedBy

Example

```java
@OneToMany(mappedBy = "customer")
private List<Order> orders;
```

`mappedBy` tells Hibernate:

> "The relationship is managed by the `customer` field inside Order."

Without it, Hibernate creates an unnecessary join table.

---

# Cascade Types

Cascade propagates operations from parent to child.

```
Customer

   |

Orders
```

Saving Customer can automatically save Orders.

---

## PERSIST

Parent save

↓

Child save

```java
CascadeType.PERSIST
```

---

## MERGE

Updating parent

↓

Updates child

```java
CascadeType.MERGE
```

---

## REMOVE

Deleting parent

↓

Deletes child

```java
CascadeType.REMOVE
```

---

## REFRESH

Reload entity from database.

---

## DETACH

Detach parent

↓

Detach children

---

## ALL

Equivalent to

```
PERSIST
MERGE
REMOVE
REFRESH
DETACH
```

---

# orphanRemoval

```java
@OneToMany(
    orphanRemoval = true
)
```

If a child is removed from the parent's collection

```java
customer.getOrders().remove(order);
```

Hibernate executes

```sql
DELETE FROM orders ...
```

without deleting the parent.

Difference

| Cascade REMOVE | orphanRemoval |
|---------------|---------------|
| Delete parent → delete children | Remove child from collection → delete child |

---

# Fetch Types

## LAZY

```
Customer

↓

Orders loaded only when needed
```

```java
fetch = FetchType.LAZY
```

Recommended for collections.

---

## EAGER

Loads immediately.

```java
fetch = FetchType.EAGER
```

Can cause unnecessary queries.

---

# Entity States

## Transient

```
new Customer()
```

Not managed.

---

## Managed

```
repository.save(customer)
```

Inside Persistence Context.

Hibernate tracks changes.

---

## Detached

Entity was managed but no longer attached.

Needs merge.

---

## Removed

Marked for deletion.

Deleted on transaction commit.

---

# Persistence Context

A cache of managed entities.

```
Database

     |

Persistence Context

     |

Java Objects
```

Hibernate checks this first before querying the database.

---

# Dirty Checking

Managed entities are automatically monitored.

```java
customer.setName("John");
```

No explicit save required inside a transaction.

Hibernate detects changes.

---

# Flush

Synchronizes Persistence Context with the database.

Does **NOT** commit.

```java
entityManager.flush();
```

---

# Commit

Commits the transaction.

Changes become permanent.

---

# Transactions

```java
@Transactional
public void update() {

}
```

Provides

- Atomicity
- Automatic rollback
- Dirty checking

---

# Common Annotations

| Annotation | Purpose |
|------------|---------|
| @Entity | Entity |
| @Table | Table |
| @Id | Primary Key |
| @GeneratedValue | Auto ID |
| @Column | Column |
| @OneToOne | One-to-One |
| @OneToMany | One-to-Many |
| @ManyToOne | Many-to-One |
| @ManyToMany | Many-to-Many |
| @JoinColumn | Foreign Key |
| @JoinTable | Join Table |
| @Embedded | Embedded object |
| @Embeddable | Embeddable class |
| @Enumerated | Enum |
| @Version | Optimistic locking |

---

# Creating Parent-Child Objects

Typical flow

```
HTTP Request

      |

DTO

      |

Create Parent

      |

Create Child

      |

Set Child -> Parent

      |

Add Child to Parent Collection

      |

repository.save(parent)
```

Helper method

```java
public void addOrder(Order order) {

    orders.add(order);
    order.setCustomer(this);
}
```

Always keep both sides synchronized.

---

# Best Practices

✅ Use DTOs instead of entities in APIs.

✅ Use `mappedBy` for bidirectional relationships.

✅ Prefer `LAZY` fetching.

✅ Use helper methods (`addChild`, `removeChild`).

✅ Initialize collections.

```java
private List<Order> orders = new ArrayList<>();
```

✅ Override `equals()` and `hashCode()` carefully.

✅ Avoid exposing entities directly as JSON.

✅ Use `CascadeType.ALL` only when child lifecycle depends on parent.

---

# Quick Cheatsheet

| Concept | Remember |
|----------|----------|
| `mappedBy` | Inverse side |
| Owning Side | Contains FK |
| `@ManyToOne` | Usually owning side |
| `CascadeType.ALL` | Propagate all operations |
| `orphanRemoval` | Delete child when removed from collection |
| LAZY | Load on demand |
| EAGER | Load immediately |
| Persistence Context | First-level cache |
| Dirty Checking | Automatic UPDATE generation |
| Flush | Sync SQL |
| Commit | Persist transaction |
| `@Transactional` | Transaction boundary |
| DTO | API layer |
| Entity | Database model |