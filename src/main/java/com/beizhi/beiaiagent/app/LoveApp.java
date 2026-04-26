package com.beizhi.beiaiagent.app;

import com.beizhi.beiaiagent.advisor.MyLoggerAdvisor;
import com.beizhi.beiaiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LoveApp {


    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕挣钱的专家。开场向用户表明身份，告知用户可倾诉有关挣钱难题。" +
            "围绕刚毕业初入社会、工作10-20年、已经实现财富自由三种状态提问：初入社会询问是否适应职场生活；" +
            "工作10-20年状态询问沟通、习惯差异、经验成长等；财富自由状态询问其下一阶段人生目标，并给当代马上要步入职场的大学生一点建议" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    /**
     * 初始化AI 客户端
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel) {
        //初始化基于文件存储的对话记忆
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        // 初始化基于内存的对话记忆
//        ChatMemory chatMemory = MessageWindowChatMemory.builder()
//                .chatMemoryRepository(new InMemoryChatMemoryRepository())
//                .maxMessages(10)
//                .build();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .build()
                )
                .build();
    }

    /**
     * AI 基础对话 支持多轮对话记忆
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    record LoveReport(String title, List<String> suggestions) {
    }

    /**
     * AI 挣钱报告功能 （支持结构化输出）
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话生成结果，标题为{用户名}的对话报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    /**
     * AI 挣钱大师知识库问答功能
     */
//    @Resource
//    private VectorStore loveAppVectorStore;

    @Resource
    private Advisor loveAppRagCloudAdvisor;

    public String doChatWithRag(String message, String chatId) {
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId)
                )
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                // 应用知识库问答
//                ..advisors(QuestionAnswerAdvisor.builder(travelVectorStore).build())
                //应用 RAG 知识库问答 (基于云知识库服务)
                .advisors(loveAppRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }






}
