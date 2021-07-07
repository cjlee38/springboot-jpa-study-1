package com.example.demo.Repository;

import com.example.demo.Domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    @Test
    @Rollback(false)
    public void testMember() throws Exception {
        // given
//        Member member = new Member();
//        member.setUsername("memberA");

        // when
//        Long savedId = memberRepository.save(member);
//        Member findMember = memberRepository.find(savedId);

        // then
//        Assertions.assertThat(member.getId()).isEqualTo(findMember.getId());
//        Assertions.assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
    }
}