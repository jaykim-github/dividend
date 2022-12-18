package com.zerobase.dividend.service;


import com.zerobase.dividend.exception.Impl.AlreadyExistUserException;
import com.zerobase.dividend.exception.Impl.NoExistUserException;
import com.zerobase.dividend.exception.Impl.NotMatchPasswordException;
import com.zerobase.dividend.model.Auth;
import com.zerobase.dividend.model.MemberEntity;
import com.zerobase.dividend.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //log.info(username);
        return this.memberRepository.findByUsername(username)
            .orElseThrow(() -> new NoExistUserException());
    }

    public MemberEntity register(Auth.SignUp member) {
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new AlreadyExistUserException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        var result = this.memberRepository.save(member.toEntity());

        return result;
    }

    public MemberEntity authenticate(Auth.SignIn member) {
        //패스워드 인증 작업
        var usr = this.memberRepository.findByUsername(member.getUsername())
                                                    .orElseThrow(() -> new NoExistUserException());

        if(!this.passwordEncoder.matches(member.getPassword(), usr.getPassword())){
            throw new NotMatchPasswordException();
        }
        return usr;
    }
}
