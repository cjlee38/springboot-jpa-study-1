package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = false)
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, int price, String name, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
//        fidnItem.change(name, price, stockQuantity); // 이런식으로 만드는게 훨씬 좋다!!
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        // 적당히 다 했다고 해보자.

        // itemRepository.save(findItem); // 이거 필요 없음. 왜? 영속성컨텍스트가 관리하는 객체이기 때문에.
        // 즉, transactio이 commit, 즉 jpa가 flush
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }


}
