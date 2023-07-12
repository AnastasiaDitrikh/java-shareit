package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;


@Repository
@RequiredArgsConstructor
public class ItemServiceDaoImpl implements ItemServiceDao {

    private final Map<Long, List<Item>> items = new HashMap<>();

    private Long generatorId=1L;

    @Override
    public Item add(Item item) {
        item.setId(generatorId);
        generatorId++;
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);
        items.put(item.getOwner(), listItems);
        return item;
    }

    @Override
    public Item update(Item item) {
        List<Item> userItems = items.get(item.getOwner());
        List<Item> toRemove = new ArrayList<>();
        for (Item userItem : userItems) {
            if (userItem.getId().equals(item.getId())) {
                toRemove.add(userItem);
            }
        }
        userItems.removeAll(toRemove);
        userItems.add(item);

        return item;
    }


    @Override
    public Optional<Item> findItemById(Long itemId) {
        for (List<Item> itemList : items.values()) {
            for (Item item : itemList) {
                if (item.getId().equals(itemId)) {
                    return Optional.of(item);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Item> findAll(Long userId) {
        return items.get(userId);
    }

    @Override
    public List<Item> search(String text) {
        List<Item> itemListSearch = new ArrayList<>();
        for (List<Item> itemList : items.values()) {
            for (Item item : itemList) {
                if (item.getAvailable().equals(true)) {
                    if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                        itemListSearch.add(item);
                    }
                }
            }
        }
        return itemListSearch;
    }
}
