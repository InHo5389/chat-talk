package chatserver.member.controller;

import chatserver.member.domain.Member;
import chatserver.member.dto.MemberJoinDto;
import chatserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberJoinDto request) {
        Member member = memberService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(member.getId());
    }
}
