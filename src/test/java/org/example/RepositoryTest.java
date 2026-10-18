package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    @Test
    void addStoresItemInRepository() {

        Repository<String> repository = new Repository<>();
        String item = "mat";

        repository.add(item);

        assertEquals(List.of("mat"), repository.findAll());
    }

    @Test
    void findAllReturnEmptyListWhenRepositoryIsEmpty() {
        Repository<String> repository = new Repository<>();
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void findAllTestForUnmodifiableList() {
        Repository<String> repository = new Repository<>();
        repository.add("hem");

        List<String> result = repository.findAll();

        assertThrows(UnsupportedOperationException.class, () -> result.add("el"));
    }

    @Test
    void findAllReturnAllAddedItems() {
        Repository<String> repository = new Repository<>();

        repository.add("hem");
        repository.add("el");

        assertEquals(List.of("hem", "el"), repository.findAll());

    }

    @Test
    void findWhereReturnsNothingInListWhenNothingMatches() {
        Repository<Integer> repository = new Repository<>();
        repository.add(1);
        repository.add(3);

        List<Integer> result = repository.findWhere(number -> number % 2 == 0);
        assertTrue(result.isEmpty());
    }

    @Test
    void findWhereWhenReturningMatchingItems() {
        Repository<Integer> repository = new Repository<>();
        repository.add(1);
        repository.add(2);
        repository.add(3);

        List<Integer> result = repository.findWhere(number -> number % 2 == 0);
        assertEquals(List.of(2), result);
    }
}

