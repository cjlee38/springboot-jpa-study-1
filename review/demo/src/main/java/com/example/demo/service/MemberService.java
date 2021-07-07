package com.example.demo.service;

import com.example.demo.Domain.Member;
import com.example.demo.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
// 1. JPA 수행(데이터 변경)은 transaction 내에서 일어나야 한다.
// 2. Transactional Annotation은 javax보다 spring 권장. 더 많은 기능이 있음
// 3. DB에 리소스를 너무 많이 쓰지 않거나, dirty checking 등과 관련해서 이득이 있다.
// 즉, 읽기 일때는 readOnly = true를 넣어주는게 좋다.
@RequiredArgsConstructor // lombok + autowired. final 이기 때문에 자동으로 생성자 주입 가능.
public class MemberService {

    //    @Autowired // 이거보다 더 좋은 방법이 있는데, 그건 set 방식, 그리고 이것보다 더 좋은 방식이 생성자
    private final MemberRepository memberRepository;

//    @Autowired // 최신 spring에서는 생성자가 하나인 경우에 Autowired를 자동으로 넣어준다.
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Autowired // best
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional // 얘는 readOnly가 아니기 위해서 사용
    public Long join(Member member) {
        // 중복 회원 검증 로직?
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 왜 이런짓을 하느냐?
        // em.persist()를 하려고 하면, 자동으로 GeneratedValue로 인해 id sequence가 증가됨.
        // 따라서, 올바르게 저장을 했는지 확인하는 과정이 필요함
        if (!(memberRepository.findByName(member.getName())
                .isEmpty())) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        // 멀티스레드 상황을 고려해서 더 뭔가 하긴 해야함.(db insert시)
        // Exception
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
