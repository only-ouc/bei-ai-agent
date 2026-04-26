package com.beizhi.beiaiagent.rag;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

class LoveAppDocumentLoaderTest {

    @Test
    void loadMarkdowns() {
        LoveAppDocumentLoader loader =
                new LoveAppDocumentLoader(new PathMatchingResourcePatternResolver());

        loader.loadMarkdowns();
    }
}