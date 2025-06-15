package chatserver.member.service;

import chatserver.member.domain.Member;
import chatserver.member.domain.Role;
import chatserver.member.dto.MemberJoinDto;
import chatserver.member.dto.MemberListDto;
import chatserver.member.dto.MemberLoginDto;
import chatserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Member join(MemberJoinDto dto) {
        log.info("MemberController join email :{}, name :{}", dto.getEmail(), dto.getName());

        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        Member saverMember = memberRepository.save(Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build());
        return saverMember;
    }

    @Transactional
    public Member login(MemberLoginDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new RuntimeException("존재하지 않는 회원입니다.")
        );

        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        return member;
    }

    @Transactional(readOnly = true)
    public List<MemberListDto> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberListDto::from)
                .toList();
    }
}
