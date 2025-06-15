package chatserver.member.dto;

import chatserver.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberListDto {

    private Long id;
    private String name;
    private String email;

    public static MemberListDto from(Member member) {
        return new MemberListDto(member.getId(), member.getName(), member.getEmail());
    }
}
