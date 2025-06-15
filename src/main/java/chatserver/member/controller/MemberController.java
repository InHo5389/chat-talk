package chatserver.member.controller;

import chatserver.member.common.auth.JwtTokenProvider;
import chatserver.member.domain.Member;
import chatserver.member.dto.LoginResponse;
import chatserver.member.dto.MemberJoinDto;
import chatserver.member.dto.MemberListDto;
import chatserver.member.dto.MemberLoginDto;
import chatserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberJoinDto request) {
        log.info("MemberController join email :{}, name :{}",request.getEmail(),request.getName());
        Member member = memberService.join(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(member.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto request) {
        Member member = memberService.login(request);

        String jwtToken = jwtTokenProvider.createToken(request.getEmail(), member.getRole().toString());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(member.getId(), jwtToken));
    }

    @GetMapping("/list")
    public ResponseEntity<?> memberList() {
        List<MemberListDto> dtos = memberService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
