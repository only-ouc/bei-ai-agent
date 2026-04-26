package com.beizhi.beiaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(properties = {
        "spring.ai.model.audio.speech=none",
        "spring.ai.model.audio.transcription=none",
        "spring.ai.model.image=none",
        "spring.ai.model.video=none",
        "spring.ai.model.rerank=none",
        "spring.autoconfigure.exclude=com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioSpeechAutoConfiguration,com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeAudioTranscriptionAutoConfiguration"
})
@ActiveProfiles("test")
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

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
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
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
        String chatId = UUID.randomUUID().toString();
        String message = "我已经步入社会了，但是工作挣钱不太行，怎么办？";
        String answer =  loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);

    }
}