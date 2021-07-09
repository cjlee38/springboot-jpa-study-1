package com.example.demo.service;

import com.example.demo.Domain.*;
import com.example.demo.Domain.item.Item;
import com.example.demo.Repository.ItemRepository;
import com.example.demo.Repository.MemberRepository;
import com.example.demo.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAdress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // Order의 Cascade 옵션 덕분에, delivery를 persist 할 필요가 없다.
        // 딱 Order만 delivery와 orderItem을 사용하기 때문에, cascade를 사용해도 된다. 여러 곳에서 사용되면 따로 해야함.
        // 잘 모르면 cascade를 쓰지 말자.
        return order.getId();
    }

    // 취소
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
        // 이걸로 끝?? 데이터를 변경한다음에 update를 날려줘야 하는거 아냐??
        // JPA 는 그럴 필요가 없음. 알아서 해줌.
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

    // 검색
}
