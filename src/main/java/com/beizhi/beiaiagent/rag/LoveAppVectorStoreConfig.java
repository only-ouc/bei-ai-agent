package com.beizhi.beiaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;
import java.util.List;

/**
 * 向量数据库配置 初始化基于内存的向量数据库
 */
@Configuration
@ConditionalOnProperty(
        name = "app.rag.local-vector-store.enabled",
        havingValue = "true"
)
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    public LoveAppVectorStoreConfig(LoveAppDocumentLoader loveAppDocumentLoader) {
        this.loveAppDocumentLoader = loveAppDocumentLoader;
    }

    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        //加载文档
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        //写入向量数据库
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }
}
