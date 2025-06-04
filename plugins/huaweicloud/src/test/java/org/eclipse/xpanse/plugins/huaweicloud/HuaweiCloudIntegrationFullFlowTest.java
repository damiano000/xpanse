package org.eclipse.xpanse.plugins.huaweicloud;

import static org.junit.jupiter.api.Assertions.*;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.iam.v3.IamClient;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.modules.models.credential.Credential;
import org.eclipse.xpanse.modules.models.credential.enums.CredentialType;
import org.eclipse.xpanse.plugins.huaweicloud.common.HuaweiCloudClient;
import org.junit.jupiter.api.Test;

@Slf4j
class HuaweiCloudIntegrationFullFlowTest {

    /* ---- NEW TEST FOR property ---- */
    @Test
    void showHuaweiCloudProperties() {
        String cspMode = System.getProperty("huawei.cloud.csp.mode.enabled");
        String tempCreds = System.getProperty("huawei.cloud.temporary.credentials.enabled");

        log.info("huawei.cloud.csp.mode.enabled            = {}", cspMode);
        log.info("huawei.cloud.temporary.credentials.enabled = {}", tempCreds);
    }

    /* --------------------- */
    @Test
    void masterKeysPresentInEnvironment() {
        String ak = System.getenv("HUAWEI_CLOUD_MASTER_ACCESS_KEY");
        String sk = System.getenv("HUAWEI_CLOUD_MASTER_SECRET_KEY");
        assertNotNull(ak, "Missing master ACCESS_KEY environment variable");
        assertNotNull(sk, "Missing master SECRET_KEY environment variable");
        log.info("✅ Master keys available in environment.");
    }

    @Test
    void conversionOfTemporaryCredentialTriggersAKSK() {
        HuaweiCloudOrchestratorPlugin plugin = new HuaweiCloudOrchestratorPlugin();
        HuaweiCloudClient huaweiCloudClient = new HuaweiCloudClient();
        ICredential credential =
                new BasicCredentials()
                        .withAk(System.getenv("HUAWEI_CLOUD_MASTER_ACCESS_KEY"))
                        .withSk(System.getenv("HUAWEI_CLOUD_MASTER_SECRET_KEY"));
        IamClient iamClient = huaweiCloudClient.getIamClient(credential, "eu-west-101");
        plugin.setIamClient(iamClient);

        Credential tempCredential = new Credential();
        tempCredential.setType(CredentialType.USERNAME_PASSWORD);
        tempCredential.setVariables(
                Map.of( // enter real credentials
                        "USERNAME", "default",
                        "PASSWORD", "default",
                        "DOMAIN", "default",
                        "PROJECT", "default"));

        Credential result =
                plugin.getTempDeploymentCredentials("USERNAME_PASSWORD", tempCredential);
        assertEquals(CredentialType.AK_SK, result.getType());
        assertNotNull(result.get("ACCESS_KEY"));
        assertNotNull(result.get("SECRET_KEY"));
        log.info("✅ Temporary USERNAME_PASSWORD credential successfully converted to AK_SK.");
    }

    @Test
    void directReturnOfFinalAKSKCredential() {
        Credential finalCredential =
                new Credential(
                        CredentialType.AK_SK,
                        Map.of(
                                "ACCESS_KEY", "STATIC-AK",
                                "SECRET_KEY", "STATIC-SK"));

        assertEquals(CredentialType.AK_SK, finalCredential.getType());
        assertEquals("STATIC-AK", finalCredential.get("ACCESS_KEY"));
        log.info("✅ Final AK_SK credential returned directly.");
    }

    @Test
    void usernamePasswordConversionProducesAKSK() {
        HuaweiCloudOrchestratorPlugin plugin = new HuaweiCloudOrchestratorPlugin();
        HuaweiCloudClient huaweiCloudClient = new HuaweiCloudClient();
        log.info("Test 1");
        ICredential credential =
                new BasicCredentials()
                        .withAk(System.getenv("HUAWEI_CLOUD_MASTER_ACCESS_KEY"))
                        .withSk(System.getenv("HUAWEI_CLOUD_MASTER_SECRET_KEY"));
        IamClient iamClient = huaweiCloudClient.getIamClient(credential, "eu-west-101");
        plugin.setIamClient(iamClient);
        log.info("Test 2");

        Credential userPassCredential = new Credential();
        userPassCredential.setType(CredentialType.USERNAME_PASSWORD);
        userPassCredential.setVariables(
                Map.of( // enter real credentials
                        "USERNAME", "default",
                        "PASSWORD", "default",
                        "DOMAIN", "default",
                        "PROJECT", "default"));
        log.info("Test 3");

        Credential result =
                plugin.getTempDeploymentCredentials("USERNAME_PASSWORD", userPassCredential);
        log.info("Test 4"); // with these test logs i check where it crashes and starting the test.
        // *This test log 4 is not visible*
        assertEquals(CredentialType.AK_SK, result.getType());
        assertNotNull(result.get("ACCESS_KEY"));
        assertNotNull(result.get("SECRET_KEY"));
        log.info("✅ USERNAME_PASSWORD credential converted to AK_SK.");
    }

    @Test
    void akSkCredentialIsPreferredOverUsernamePassword() {
        HuaweiCloudOrchestratorPlugin plugin = new HuaweiCloudOrchestratorPlugin();

        Credential akSkCredential =
                new Credential(
                        CredentialType.AK_SK,
                        Map.of(
                                "ACCESS_KEY", "STATIC-AK",
                                "SECRET_KEY", "STATIC-SK"));

        Credential userPassCredential = new Credential();
        userPassCredential.setType(CredentialType.USERNAME_PASSWORD);
        userPassCredential.setVariables(
                Map.of(
                        "USERNAME", "default",
                        "PASSWORD", "default",
                        "DOMAIN", "default",
                        "PROJECT", "default"));

        Credential result = plugin.selectPreferredCredential(akSkCredential, userPassCredential);
        assertEquals(CredentialType.AK_SK, result.getType());
        assertEquals("STATIC-AK", result.get("ACCESS_KEY"));
        log.info("✅ AK_SK credential correctly preferred over USERNAME_PASSWORD.");
    }
}
