/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.credential;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.modules.models.common.enums.Csp;
import org.eclipse.xpanse.modules.models.credential.CreateCredential;
import org.eclipse.xpanse.modules.models.credential.CredentialVariable;
import org.eclipse.xpanse.modules.models.credential.enums.CredentialType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/** Configuration class to load Huawei Cloud master credentials from environment variables. */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "huawei.cloud.temporary.credentials.enabled", havingValue = "true")
public class MasterIamKeyConfiguration {

    @Value("${HUAWEI_CLOUD_MASTER_ACCESS_KEY}")
    private String accessKey;

    @Value("${HUAWEI_CLOUD_MASTER_SECRET_KEY}")
    private String secretKey;

    private final CredentialCenter credentialCenter;

    public MasterIamKeyConfiguration(CredentialCenter credentialCenter) {
        this.credentialCenter = credentialCenter;
    }

    /** Initializes and registers the Huawei Cloud master credentials at startup. */
    @PostConstruct
    public void init() {
        if (accessKey == null || secretKey == null) {
            throw new IllegalStateException("❌ Missing environment variables: AK/SK");
        }

        CreateCredential createCredential = new CreateCredential();
        createCredential.setCsp(Csp.HUAWEI_CLOUD);
        createCredential.setType(CredentialType.VARIABLES);
        createCredential.setName("HuaweiCloudMasterKeys");
        createCredential.setUserId("HuaweiCloudMasterKeys");
        createCredential.setTimeToLive(315360000); // 10 years

        CredentialVariable ak =
                new CredentialVariable("ACCESS_KEY", "Access Key", true, true, accessKey);
        CredentialVariable sk =
                new CredentialVariable("SECRET_KEY", "Secret Key", true, true, secretKey);
        createCredential.setVariables(List.of(ak, sk));

        credentialCenter.addCredential(createCredential);
        log.info("✅ Huawei Master Credential successfully loaded");
    }
}
