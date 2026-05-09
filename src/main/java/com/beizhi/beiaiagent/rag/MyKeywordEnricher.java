package com.beizhi.beiaiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  基于AI 的文档元信息增强器 为文档补充元信息
 */
@Component
class MyKeywordEnricher {
    @Resource
    private ChatModel dashscopeChatModel;

    List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(this.dashscopeChatModel, 5);
        return enricher.apply(documents);
    }
}

//@Bean
//VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
//    SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
//            .build();
//    // 加载文档
//    List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
//    // 自动补充关键词元信息
//    List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documents);
//    simpleVectorStore.add(enrichedDocuments);
//    return simpleVectorStore;
//}
