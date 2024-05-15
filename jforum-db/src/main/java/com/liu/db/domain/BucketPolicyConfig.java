package com.liu.db.domain;

import java.util.List;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/15 16:06
 */
public class BucketPolicyConfig {
    private String Version;
    private List<Statement> Statement;

    public static class Statement {
        private String Effect;
        private String Principal;
        private String Action;
        private String Resource;

        public String getEffect() {
            return Effect;
        }

        public void setEffect(String effect) {
            Effect = effect;
        }

        public String getPrincipal() {
            return Principal;
        }

        public void setPrincipal(String principal) {
            Principal = principal;
        }

        public String getAction() {
            return Action;
        }

        public void setAction(String action) {
            Action = action;
        }

        public String getResource() {
            return Resource;
        }

        public void setResource(String resource) {
            Resource = resource;
        }
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public List<BucketPolicyConfig.Statement> getStatement() {
        return Statement;
    }

    public void setStatement(List<BucketPolicyConfig.Statement> statement) {
        Statement = statement;
    }
}
