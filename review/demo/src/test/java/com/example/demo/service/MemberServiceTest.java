package com.example.demo.service;

import com.example.demo.Domain.Member;
import com.example.demo.Repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest // spring을 띄워서 테스트를 한다.
@Transactional // 테스트에서는 자동으로 rollback. service같은데에서는 정상 작동(not rollback, commit)
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

//    @Autowired
//    EntityManager em;


    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");
        // when
        Long savedId = memberService.join(member);
        // then
//        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
        // insert 문이 없다.. why?
        // em.persist는 db에 insert를 기본적으로는 날리지 않음. = jpa가 flush 해줘야함.
        // 그리고 spring transactional은 기본적으로 rollback을 함..(이건 앞에것도 마찬가지 아닌가)
        // 따라서 Rollback annotation이 필요
        // 혹은, em.flush()
    }

    @Test(expected = IllegalStateException.class) // try catch 가 없어도 됨
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        // when
        memberService.join(member1);
//        try {
//            memberService.join(member2);
//        } catch {
//            return ;
//        }
        memberService.join(member2); // 예외가 발생해야함.


        // then
        // 여기로 오면 안된다.
        fail("예외가 발생해야 한다!");
    }

}