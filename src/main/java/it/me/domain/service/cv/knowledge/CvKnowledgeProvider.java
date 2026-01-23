package it.me.domain.service.cv.knowledge;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CvKnowledgeProvider {
    private static final Logger logger = Logger.getLogger(CvKnowledgeProvider.class.getName());

    private volatile String cachedCv;

    public String getCvFromResources() {
        if (cachedCv != null) {
            return cachedCv;
        }

        synchronized (this) {
            if (cachedCv != null) {
                return cachedCv;
            }

            String resourceName = "andreaciambella_cv_ai.md";
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName)) {
                if (is == null) {
                    throw new IllegalStateException("Resource not found %s".formatted(resourceName));
                }
                cachedCv = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                logger.infof("Loaded cv from resources: %s", resourceName);
                return cachedCv;
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load cv from resources", e);
            }
        }
    }
}
