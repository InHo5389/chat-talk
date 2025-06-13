package chatserver.member.service;

import chatserver.member.domain.Member;
import chatserver.member.domain.Role;
import chatserver.member.dto.MemberJoinDto;
import chatserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Member join(MemberJoinDto dto) {
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
}
