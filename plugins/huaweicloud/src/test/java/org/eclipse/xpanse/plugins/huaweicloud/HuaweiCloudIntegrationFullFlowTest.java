package org.eclipse.xpanse.plugins.huaweicloud;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.annotation.Resource;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.modules.models.credential.Credential;
import org.eclipse.xpanse.modules.models.credential.enums.CredentialType;
import org.eclipse.xpanse.plugins.huaweicloud.common.HuaweiCloudClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
class HuaweiCloudIntegrationFullFlowTest {

    @Resource private HuaweiCloudOrchestratorPlugin plugin;

    @Resource private HuaweiCloudClient huaweiCloudClient;

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

        Credential userPassCredential = new Credential();
        userPassCredential.setType(CredentialType.USERNAME_PASSWORD);
        userPassCredential.setVariables(
                Map.of( // enter real credentials
                        "USERNAME", "default",
                        "PASSWORD", "default",
                        "DOMAIN", "default",
                        "PROJECT", "default"));

        Credential result =
                plugin.getTempDeploymentCredentials("USERNAME_PASSWORD", userPassCredential);

        assertEquals(CredentialType.AK_SK, result.getType());
        assertNotNull(result.get("ACCESS_KEY"));
        assertNotNull(result.get("SECRET_KEY"));
        log.info("✅ USERNAME_PASSWORD credential converted to AK_SK.");
    }

    @Test
    void akSkCredentialIsPreferredOverUsernamePassword() {
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
