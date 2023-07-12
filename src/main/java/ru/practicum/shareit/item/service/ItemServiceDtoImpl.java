package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.service.UserServiceDtoImpl;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceDtoImpl {
        private final ItemServiceDao itemDao;
        private final UserServiceDtoImpl userService;

        public ItemDto add(Long userId, ItemDto itemDto) {
            UserDto user = userService.findById(userId);
            Item item = ItemMapper.toItem(itemDto);
            item.setOwner((UserMapper.toUser(user)).getId());
            return ItemMapper.toItemDto(itemDao.add(item));
        }

        public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
            UserDto user = userService.findById(userId);
            Optional<Item> itemOptional = itemDao.findItemById(itemId);
            if (itemOptional.isPresent()) {
                if (!itemOptional.get().getOwner().equals(userId)) {
                    throw new NotFoundException(String.format("Пользователь с id %s " +
                            "не является владельцем вещи id %s.", userId, itemId));
                }
                Item itemFromStorage = itemOptional.get();
                Item item = ItemMapper.toItem(itemDto);
                if (Objects.isNull(item.getAvailable())) {
                    item.setAvailable(itemFromStorage.getAvailable());
                }
                if (Objects.isNull(item.getDescription())) {
                    item.setDescription(itemFromStorage.getDescription());
                }
                if (Objects.isNull(item.getName())) {
                    item.setName(itemFromStorage.getName());
                }
                item.setId(itemFromStorage.getId());
                item.setRequest(itemFromStorage.getRequest());
                item.setOwner(itemFromStorage.getOwner());

                return ItemMapper.toItemDto(itemDao.update(item));
            }
            return null;
        }

        public ItemDto findItemById(Long userId, Long itemId) throws NotFoundException {
            userService.findById(userId);
            Optional<Item> itemGet = itemDao.findItemById(itemId);
            if (itemGet.isEmpty()) {
                throw new NotFoundException(String.format("У пользователя с id %s не " +
                        "существует вещи с id %s", userId, itemId));
            }
            return ItemMapper.toItemDto(itemGet.get());
        }

        public List<ItemDto> findAll(Long userId) {
            userService.findById(userId);
            List<Item> itemList = itemDao.findAll(userId);
            List<ItemDto> itemDtoList = new ArrayList<>();
            for (Item item : itemList) {
                itemDtoList.add(ItemMapper.toItemDto(item));
            }
            return itemDtoList;
        }

        public List<ItemDto> search(Long userId, String text) {
            userService.findById(userId);
            if (text.isBlank()) {
                return Collections.emptyList();
            }
            List<Item> itemList = itemDao.search(text);
            List<ItemDto> itemDtoList = new ArrayList<>();
            for (Item item : itemList) {
                itemDtoList.add(ItemMapper.toItemDto(item));
            }
            return itemDtoList;
        }
    }



