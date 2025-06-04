package org.eclipse.xpanse.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.xpanse.modules.credential.CredentialCenter;
import org.eclipse.xpanse.modules.credential.MasterIamKeyConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MasterIamKeyConfiguration.class)
@TestPropertySource(
        properties = {
            "huawei.cloud.temporary.credentials.enabled=true",
            "HUAWEI_CLOUD_MASTER_ACCESS_KEY=dummy-access-key",
            "HUAWEI_CLOUD_MASTER_SECRET_KEY=dummy-secret-key"
        })
public class MasterIamKeyConfigurationTest {

    @MockitoBean private CredentialCenter credentialCenter;

    @Autowired private MasterIamKeyConfiguration masterIamKeyConfiguration;

    @Test
    void testMasterIamKeyConfigurationBeanLoaded() {
        assertThat(masterIamKeyConfiguration).isNotNull();
    }
}
