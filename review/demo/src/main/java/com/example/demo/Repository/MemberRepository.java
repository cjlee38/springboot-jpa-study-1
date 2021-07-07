package com.example.demo.Repository;


import com.example.demo.Domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId(); // 왜 ID만 반환하느냐 => CQS(Command Query Seperation)
    }

//    public Member find(Long id) {
//        return em.find(Member.class, id);
//    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
        // JPQL 사용, SQL이랑 문법이 거의 비슷하다(조금 다르긴함)
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class) // :name 이게 바인딩 대상
                .setParameter("name", name) // 여기서 바인딩
                .getResultList();
    }
}
