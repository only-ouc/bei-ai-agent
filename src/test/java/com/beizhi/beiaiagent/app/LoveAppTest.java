package com.beizhi.beiaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;


import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "spring.ai.model.audio.speech=none",
        "spring.ai.model.audio.transcription=none",
        "spring.ai.model.image=none",
        "spring.ai.model.video=none",
        "spring.ai.model.rerank=none",
        "spring.autoconfigure.exclude=com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioSpeechAutoConfiguration,com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioTranscriptionAutoConfiguration",
        // 修改点 1：原来这里只排除了 DashScope 音频配置，现在补充排除 DataSource 相关自动配置
        "spring.autoconfigure.exclude=" +
                "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioSpeechAutoConfiguration," +
                "com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioTranscriptionAutoConfiguration," +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration," +
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
@ActiveProfiles("test")
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    // 修改点 2：保留你的 pgVectorVectorStore mock，避免测试时真的连接 PGVector
    @MockitoBean(name = "pgVectorVectorStore")
    private VectorStore pgVectorVectorStore;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是北北";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想让金钱更爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我想让谁更爱我来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是北北，我想让全天下的钱都爱我，但我不知到该怎么做";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }


    @Test
    void doChatWithRag() {

        // 修改点 3：因为 pgVectorVectorStore 是 mock 的，所以这里手动指定检索结果为空列表
        // 否则 RAG 调用 similaritySearch 时，Mockito 默认返回 null，可能导致空指针
        when(pgVectorVectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of());
        String chatId = UUID.randomUUID().toString();
        String message = "我已经步入社会了，但是工作挣钱不太行，怎么办？";
        String answer =  loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);

    }
}