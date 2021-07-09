package com.example.demo.service;

import com.example.demo.Domain.item.Book;
import com.example.demo.Domain.item.Item;
import com.example.demo.Repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // read only false를 override 하려고
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        // 여기서 save를 호출할 필요가 없음. 왜 ?
        // findItem은 한번 담그고 온 녀석이기 때문
        // 기존의 book은 새로 만든 객체이기 때문에 dirty checking을 안한다.
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
