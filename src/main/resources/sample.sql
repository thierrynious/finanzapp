

// Alle Kategorien laden
SELECT c FROM Category c

// Nur bestimmte Namen
SELECT c FROM Category c WHERE c.name = 'Lebensmittel'

// Profile mit Mähnenlänge > 10
SELECT p FROM Profile p WHERE p.manelength > 10

// Name enthält "Fat"
SELECT p FROM Profile p WHERE p.nickname LIKE '%Fat%'

String jpql = "SELECT c FROM Category c WHERE c.name = :name";
TypedQuery<Category> query = em.createQuery(jpql, Category.class);
query.setParameter("name", "Lebensmittel");


