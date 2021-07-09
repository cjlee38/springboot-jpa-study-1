package com.example.demo.service;

import com.example.demo.Domain.Address;
import com.example.demo.Domain.Member;
import com.example.demo.Domain.Order;
import com.example.demo.Domain.OrderStatus;
import com.example.demo.Domain.item.Book;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.exception.NotEnoughStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("jpa book", 10000, 10);
        // persist를 해야 Id가 생기니까 그런것같음.

        int orderCount = 2;

        // when

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다", 8, book.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Book item = createBook("some book", 10000, 10);

        int orderCount = 2;
        Long orderedId = orderService.order(member.getId(), item.getId(), orderCount);


        // when
        System.out.println("취소 전 = " + item.getStockQuantity());
        orderService.cancelOrder(orderedId);
        System.out.println("취소 후 = " + item.getStockQuantity());

        // then
        Order getOrdered = orderRepository.findOne(orderedId);



        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, getOrdered.getStatus());
        assertEquals("주문 취소된 상품의 재고는 복원되어야 함", 10, item.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Book book = createBook("jpa book", 10000, 10);

        int orderCount = 11;

        // when
        orderService.order(member.getId(), book.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생해야 함");
    }


    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "도로", "집코드"));
        em.persist(member);
        return member;
    }
}