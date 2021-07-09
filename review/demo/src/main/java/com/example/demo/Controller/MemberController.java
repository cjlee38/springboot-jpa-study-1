package com.example.demo.controller;

import com.example.demo.Domain.Address;
import com.example.demo.Domain.Member;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        // Model = controller와 ui의 연결다리.
        model.addAttribute("memberForm", new MemberForm());
        // 빈 껍데기를 갖고 간다.
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm,
                         BindingResult result) {
        // @Valid -> NotEmpty를 실행하겠다.
        // BindingResult -> Valid에서 에러가 발생하면,
        // BindingResult 에 그 에러를 담고 아래 코드를 실행
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; // 메인 화면으로 redirect 하겠다.
    }

    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}
