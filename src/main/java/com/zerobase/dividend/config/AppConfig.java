package com.zerobase.dividend.config;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Trie<String, String> trie(){ // 코드의 일관성을 위해 빈으로 관리
        return new PatriciaTrie<>();
    }
}
