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


}