package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() {
        Book book = em.find(Book.class, 1L);

        book.setName("blahblah");
        // 여기서 book.setName()을 하면 적용이 됨 => dirty checking

    }
}
