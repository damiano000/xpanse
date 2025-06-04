package org.eclipse.xpanse.plugins.huaweicloud;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.CreateLoginTokenResponse;
import com.huaweicloud.sdk.iam.v3.model.CreateTemporaryAccessKeyByTokenRequest;
import com.huaweicloud.sdk.iam.v3.model.CreateTemporaryAccessKeyByTokenResponse;
import java.util.List;
import java.util.Map;
import org.eclipse.xpanse.modules.models.credential.AbstractCredentialInfo;
import org.eclipse.xpanse.modules.models.credential.Credential;
import org.eclipse.xpanse.modules.models.credential.CredentialVariable;
import org.eclipse.xpanse.modules.models.credential.CredentialVariables;
import org.eclipse.xpanse.modules.models.credential.enums.CredentialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class HuaweiCloudOrchestratorPluginOneTest {

    private HuaweiCloudOrchestratorPlugin plugin;
    private IamClient iamClient;

    @BeforeEach
    void setup() {
        plugin = new HuaweiCloudOrchestratorPlugin();
        iamClient = mock(IamClient.class);
        ReflectionTestUtils.setField(plugin, "iamClient", iamClient);
    }

    @Test
    void credentialDefinitionsContainUsernamePasswordWhenFlagEnabled() {
        ReflectionTestUtils.setField(plugin, "huaweiCloudCspModeEnabled", true);

        List<AbstractCredentialInfo> defs = plugin.getCredentialDefinitions();

        assertEquals(2, defs.size());
        AbstractCredentialInfo up = defs.get(1);
        assertTrue(up instanceof CredentialVariables);
        assertEquals(CredentialType.USERNAME_PASSWORD, up.getType());
        assertFalse(up.isFinalCredential());

        List<String> names =
                ((CredentialVariables) up)
                        .getVariables().stream().map(CredentialVariable::getName).toList();
        assertTrue(names.containsAll(List.of("USERNAME", "PASSWORD", "DOMAIN", "PROJECT")));
    }

    @Test
    void convertUsernamePasswordToAkSk_success() {
        // input USERNAME_PASSWORD
        Credential input =
                new Credential(
                        CredentialType.USERNAME_PASSWORD,
                        Map.of("USERNAME", "user", "PASSWORD", "pass"));

        /* mock 1: createLoginToken */
        CreateLoginTokenResponse loginResp =
                new CreateLoginTokenResponse().withXSubjectLoginToken("subject‑token");
        when(iamClient.createLoginToken(any())).thenReturn(loginResp);

        /* mock 2: createTemporaryAccessKeyByToken */
        com.huaweicloud.sdk.iam.v3.model.Credential akSkSdk =
                new com.huaweicloud.sdk.iam.v3.model.Credential()
                        .withAccess("ak")
                        .withSecret("sk")
                        .withSecuritytoken("st");

        CreateTemporaryAccessKeyByTokenResponse takResp =
                new CreateTemporaryAccessKeyByTokenResponse().withCredential(akSkSdk);
        when(iamClient.createTemporaryAccessKeyByToken(
                        any(CreateTemporaryAccessKeyByTokenRequest.class)))
                .thenReturn(takResp);

        /* call & assert  */
        Credential result = plugin.getTempDeploymentCredentials("AK_SK", input);

        assertEquals(CredentialType.AK_SK, result.getType());
        assertEquals("ak", result.get("ACCESS_KEY"));
        assertEquals("sk", result.get("SECRET_KEY"));
        assertEquals("st", result.get("SECURITY_TOKEN"));
    }

    @Test
    void convertFailsIfUsernameMissing() {
        Credential bad =
                new Credential(CredentialType.USERNAME_PASSWORD, Map.of("PASSWORD", "pass"));

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> plugin.getTempDeploymentCredentials("AK_SK", bad));

        assertTrue(ex.getMessage().contains("USERNAME and PASSWORD"));
    }

    @Test
    void convertFailsIfSubjectTokenMissing() {
        Credential input =
                new Credential(
                        CredentialType.USERNAME_PASSWORD,
                        Map.of("USERNAME", "user", "PASSWORD", "pass"));

        when(iamClient.createLoginToken(any())).thenReturn(new CreateLoginTokenResponse());

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> plugin.getTempDeploymentCredentials("AK_SK", input));

        assertTrue(ex.getMessage().contains("Missing subject token"));
    }

    @Test
    void convertFailsIfAkSkMissing() {
        Credential input =
                new Credential(
                        CredentialType.USERNAME_PASSWORD,
                        Map.of("USERNAME", "user", "PASSWORD", "pass"));

        when(iamClient.createLoginToken(any()))
                .thenReturn(new CreateLoginTokenResponse().withXSubjectLoginToken("token"));

        when(iamClient.createTemporaryAccessKeyByToken(any()))
                .thenReturn(new CreateTemporaryAccessKeyByTokenResponse()); // credential null

        RuntimeException ex =
                assertThrows(
                        RuntimeException.class,
                        () -> plugin.getTempDeploymentCredentials("AK_SK", input));

        assertTrue(ex.getMessage().contains("No temporary AK/SK"));
    }
}
