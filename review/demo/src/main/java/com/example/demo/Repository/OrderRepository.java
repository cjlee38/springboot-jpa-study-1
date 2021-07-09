package com.example.demo.Repository;

import com.example.demo.Domain.Order;
import com.example.demo.Domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /*
    문제는 무엇이냐, 만약 status랑 name이 없으면 ? 즉, "전부 다 들고와" 라고 한다면?
     */
    public List<Order> findAll(OrderSearch orderSearch) {
        /* 문제가 되는 코드 */
//        return findAllDefault(orderSearch);

        /* 해결 방법 1 */
        return findAllByString(orderSearch);
        /* 해결 방법 2 */
//        return findAllByCriteria(orderSearch);
        /* 해결 방법 3 */
//        return findAllByQueryDSL(orderSearch);
    }

    private List<Order> findAllByQueryDSL(OrderSearch orderSearch) {
        return new ArrayList<>();
//        QOrder order = QOrder.order;
    }

    /* 문제가 되는 코드 */
    private List<Order> findAllDefault(OrderSearch orderSearch) {
        return em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status" +
                        " and m.name like :name",
                Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100) // 이러면 100개서부터 시작
                .setMaxResults(1000) // 최대 1000개 까지만
                .getResultList();
    }

    /* 1번 해결 방법. 무식하게 = 실무에서 안쓴다 */
    private List<Order> findAllByString(OrderSearch orderSearch) {
        // 일단 코드도 너무 길어지지만, 오타에 치명적임
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null)
            query = query.setParameter("status", orderSearch.getOrderStatus());
        if (StringUtils.hasText(orderSearch.getMemberName()))
            query = query.setParameter("name", orderSearch.getMemberName());

        return query.getResultList();
    }

    /* 2번 해결 방법. JPA에서 표준으로 제공하는 방법. 역시 권장하지 않는다 */
    private List<Order> findAllByCriteria(OrderSearch orderSearch) {
        // JPA가 "쿼리를 만들어주는" 방법인데, 유지보수성이 zero
        // 이게 무슨 JPQL 을 만들어주는지 코드만 읽고는 알 수 없다.
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }


}
