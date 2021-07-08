package com.example.demo.Repository;

import com.example.demo.Domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item); // 기존에 item이 없다 -> 아이템을 처음 등록할때는 아직 id가 없음.
        } else {
            em.merge(item); // 업데이트 비슷한것. -> 디비에 이미 있는 녀석을 변경하겠다. (나중에 설명)
        }
    }

    public Item
    findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


}
