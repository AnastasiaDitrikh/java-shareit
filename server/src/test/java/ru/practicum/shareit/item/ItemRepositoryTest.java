package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final User user = User.builder()
            .name("name")
            .email("email@email.com")
            .build();

    private final Item item = Item.builder()
            .name("name")
            .description("description")
            .available(true)
            .owner(user)
            .build();

    @BeforeEach
    private void addItems() {
        testEntityManager.persist(user);
        testEntityManager.flush();
        itemRepository.save(item);
    }

    @AfterEach
    private void deleteAll() {
        itemRepository.deleteAll();
    }

    @Test
    void findAllByOwnerIdOrderByIdAsc() {
        List<Item> items = itemRepository.findAllByOwnerIdOrderByIdAsc(1L, PageRequest.of(0, 1)).getContent();

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getName(), "name");
    }
}