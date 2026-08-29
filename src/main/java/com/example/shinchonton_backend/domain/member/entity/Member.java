package com.example.shinchonton_backend.domain.member.entity;

import com.example.shinchonton_backend.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "members",
        uniqueConstraints = @UniqueConstraint(name = "uk_members_login_id", columnNames = "login_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    private Member(String loginId, String passwordHash, MemberRole role, String name, String phone) {
        this.loginId = requireText(loginId, "로그인 아이디");
        this.passwordHash = requireText(passwordHash, "비밀번호 해시");
        this.role = requireNonNull(role, "회원 역할");
        this.name = requireText(name, "이름");
        this.phone = requireText(phone, "전화번호");
    }

    public static Member register(
            String loginId,
            String passwordHash,
            MemberRole role,
            String name,
            String phone
    ) {
        return new Member(loginId, passwordHash, role, name, phone);
    }

    public boolean isStudent() {
        return role == MemberRole.STUDENT;
    }

    public boolean isMerchant() {
        return role == MemberRole.MERCHANT;
    }

    public void updateContact(String name, String phone) {
        this.name = requireText(name, "이름");
        this.phone = requireText(phone, "전화번호");
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + "은(는) 비어 있을 수 없습니다.");
        }
        return value;
    }

    private static <T> T requireNonNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + "은(는) 비어 있을 수 없습니다.");
        }
        return value;
    }
}
