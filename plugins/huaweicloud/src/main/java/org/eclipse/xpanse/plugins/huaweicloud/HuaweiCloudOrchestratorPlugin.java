/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.plugins.huaweicloud;

import static org.eclipse.xpanse.modules.cache.consts.CacheConstants.REGION_AZS_CACHE_NAME;
import static org.eclipse.xpanse.modules.cache.consts.CacheConstants.SERVICE_FLAVOR_PRICE_CACHE_NAME;

import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.CreateLoginTokenRequest;
import com.huaweicloud.sdk.iam.v3.model.CreateLoginTokenRequestBody;
import com.huaweicloud.sdk.iam.v3.model.CreateLoginTokenResponse;
import com.huaweicloud.sdk.iam.v3.model.CreateTemporaryAccessKeyByTokenRequest;
import com.huaweicloud.sdk.iam.v3.model.CreateTemporaryAccessKeyByTokenRequestBody;
import com.huaweicloud.sdk.iam.v3.model.CreateTemporaryAccessKeyByTokenResponse;
import com.huaweicloud.sdk.iam.v3.model.IdentityToken;
import com.huaweicloud.sdk.iam.v3.model.LoginTokenAuth;
import com.huaweicloud.sdk.iam.v3.model.LoginTokenSecurityToken;
import com.huaweicloud.sdk.iam.v3.model.TokenAuth;
import com.huaweicloud.sdk.iam.v3.model.TokenAuthIdentity;
import com.huaweicloud.sdk.iam.v3.region.IamRegion;
import jakarta.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.modules.models.billing.FlavorPriceResult;
import org.eclipse.xpanse.modules.models.common.enums.Csp;
import org.eclipse.xpanse.modules.models.credential.AbstractCredentialInfo;
import org.eclipse.xpanse.modules.models.credential.Credential;
import org.eclipse.xpanse.modules.models.credential.CredentialVariable;
import org.eclipse.xpanse.modules.models.credential.CredentialVariables;
import org.eclipse.xpanse.modules.models.credential.enums.CredentialType;
import org.eclipse.xpanse.modules.models.monitor.Metric;
import org.eclipse.xpanse.modules.models.service.enums.DeployResourceKind;
import org.eclipse.xpanse.modules.models.servicetemplate.Ocl;
import org.eclipse.xpanse.modules.models.servicetemplate.Region;
import org.eclipse.xpanse.modules.models.servicetemplate.enums.DeployerKind;
import org.eclipse.xpanse.modules.models.servicetemplate.enums.ServiceTemplateReviewPluginResultType;
import org.eclipse.xpanse.modules.models.servicetemplate.exceptions.UnavailableServiceRegionsException;
import org.eclipse.xpanse.modules.orchestrator.OrchestratorPlugin;
import org.eclipse.xpanse.modules.orchestrator.audit.AuditLog;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployResourceHandler;
import org.eclipse.xpanse.modules.orchestrator.monitor.ResourceMetricsRequest;
import org.eclipse.xpanse.modules.orchestrator.monitor.ServiceMetricsRequest;
import org.eclipse.xpanse.modules.orchestrator.price.ServiceFlavorPriceRequest;
import org.eclipse.xpanse.modules.orchestrator.servicestate.ServiceStateManageRequest;
import org.eclipse.xpanse.plugins.huaweicloud.common.HuaweiCloudConstants;
import org.eclipse.xpanse.plugins.huaweicloud.manage.HuaweiCloudResourceManager;
import org.eclipse.xpanse.plugins.huaweicloud.manage.HuaweiCloudVmStateManager;
import org.eclipse.xpanse.plugins.huaweicloud.monitor.HuaweiCloudMetricsService;
import org.eclipse.xpanse.plugins.huaweicloud.monitor.constant.HuaweiCloudMonitorConstants;
import org.eclipse.xpanse.plugins.huaweicloud.price.HuaweiCloudPriceCalculator;
import org.eclipse.xpanse.plugins.huaweicloud.resourcehandler.HuaweiCloudTerraformResourceHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/** Plugin to deploy managed services on Huawei cloud. */
@Slf4j
@Component
public class HuaweiCloudOrchestratorPlugin implements OrchestratorPlugin {

    @Resource private HuaweiCloudTerraformResourceHandler terraformResourceHandler;
    @Resource private HuaweiCloudMetricsService metricsService;
    @Resource private HuaweiCloudVmStateManager vmStateManager;
    @Resource private HuaweiCloudResourceManager resourceManager;
    @Resource private HuaweiCloudPriceCalculator priceCalculator;
    @Resource private IamClient iamClient;

    @Value("${huaweicloud.auto.approve.service.template.enabled:false}")
    private boolean autoApproveServiceTemplateEnabled;

    @Value("${huawei.cloud.csp.mode.enabled:false}")
    private boolean huaweiCloudCspModeEnabled;

    @Override
    public Map<DeployerKind, DeployResourceHandler> resourceHandlers() {
        Map<DeployerKind, DeployResourceHandler> resourceHandlers = new HashMap<>();
        resourceHandlers.put(DeployerKind.TERRAFORM, terraformResourceHandler);
        resourceHandlers.put(DeployerKind.OPEN_TOFU, terraformResourceHandler);
        return resourceHandlers;
    }

    @Override
    public List<String> getExistingResourceNamesWithKind(
            String site, String region, String userId, DeployResourceKind kind, UUID serviceId) {
        return resourceManager.getExistingResourceNamesWithKind(site, region, userId, kind);
    }

    @Override
    @Cacheable(cacheNames = REGION_AZS_CACHE_NAME)
    public List<String> getAvailabilityZonesOfRegion(
            String siteName,
            String regionName,
            String userId,
            UUID serviceId,
            UUID serviceTemplateId) {
        return resourceManager.getAvailabilityZonesOfRegion(siteName, regionName, userId);
    }

    @Override
    public Csp getCsp() {
        return Csp.HUAWEI_CLOUD;
    }

    @Override
    public List<String> requiredProperties() {
        return Collections.emptyList();
    }

    @Override
    public Map<String, String> getEnvVarKeysMappingMap() {
        return Collections.emptyMap();
    }

    @Override
    public ServiceTemplateReviewPluginResultType validateServiceTemplate(Ocl ocl) {
        if (autoApproveServiceTemplateEnabled) {
            return ServiceTemplateReviewPluginResultType.APPROVED;
        }
        return ServiceTemplateReviewPluginResultType.MANUAL_REVIEW_REQUIRED;
    }

    @Override
    public void prepareServiceTemplate(Ocl ocl) {
        log.info("prepare service template.");
    }

    @Override
    public List<String> getSites() {
        return List.of(
                HuaweiCloudConstants.INTERNATIONAL_SITE,
                HuaweiCloudConstants.CHINESE_MAINLAND_SITE,
                HuaweiCloudConstants.EUROPE_SITE);
    }

    @Override
    public boolean validateRegionsOfService(Ocl ocl) {
        List<String> errors = new ArrayList<>();
        List<Region> regions = ocl.getCloudServiceProvider().getRegions();
        regions.forEach(
                region -> {
                    try {
                        IamRegion.valueOf(region.getName());
                    } catch (IllegalArgumentException e) {
                        String errorMsg =
                                String.format(
                                        "Region with name %s is unavailable in " + "Csp %s.",
                                        region.getName(), getCsp().toValue());
                        errors.add(errorMsg);
                    }
                    if (!getSites().contains(region.getSite())) {
                        String errorMsg =
                                String.format(
                                        "Region with site %s is unavailable in Csp %s. "
                                                + "Available sites %s",
                                        region.getName(), getCsp().toValue(), getSites());
                        errors.add(errorMsg);
                    }
                });
        if (CollectionUtils.isEmpty(errors)) {
            return true;
        }
        throw new UnavailableServiceRegionsException(errors);
    }

    @Override
    public Map<String, String> getComputeResourcesInServiceDeployment(File scriptFile) {
        return resourceManager.getComputeResourcesInServiceDeployment(scriptFile);
    }

    @Override
    public List<CredentialType> getAvailableCredentialTypes() {
        List<CredentialType> credentialTypes = new ArrayList<>();
        credentialTypes.add(CredentialType.VARIABLES);
        return credentialTypes;
    }

    @Override
    public List<AbstractCredentialInfo> getCredentialDefinitions() {
        List<AbstractCredentialInfo> credentialInfos = new ArrayList<>();

        // Access Key + Secret Key
        List<CredentialVariable> accessKeyVars = new ArrayList<>();
        accessKeyVars.add(
                new CredentialVariable(
                        HuaweiCloudMonitorConstants.HW_ACCESS_KEY, "The access key.", true));
        accessKeyVars.add(
                new CredentialVariable(
                        HuaweiCloudMonitorConstants.HW_SECRET_KEY, "The security key.", true));
        CredentialVariables accessKeyCreds =
                new CredentialVariables(
                        getCsp(),
                        getSites().getFirst(),
                        CredentialType.VARIABLES,
                        HuaweiCloudMonitorConstants.IAM,
                        "Using access key and secret key for authentication.",
                        null,
                        accessKeyVars);
        credentialInfos.add(accessKeyCreds);

        // Username + Password (new Credential)
        if (huaweiCloudCspModeEnabled) {
            List<CredentialVariable> userPassVars = new ArrayList<>();
            userPassVars.add(new CredentialVariable("USERNAME", "Huawei Cloud username", true));
            userPassVars.add(new CredentialVariable("PASSWORD", "Huawei Cloud password", true));
            userPassVars.add(new CredentialVariable("DOMAIN", "Huawei Cloud domain", false));
            userPassVars.add(new CredentialVariable("PROJECT", "Huawei Cloud project", false));
            CredentialVariables userPassCreds =
                    new CredentialVariables(
                            getCsp(),
                            getSites().getFirst(),
                            CredentialType.USERNAME_PASSWORD,
                            "huawei.username.password",
                            "Using username/password authentication.",
                            null,
                            userPassVars);
            userPassCreds.setFinalCredential(false);
            credentialInfos.add(userPassCreds);
        }

        return credentialInfos;
    }

    /**
     * Get metrics for resource instance by the @resourceMetricRequest.
     *
     * @param resourceMetricRequest The request model to query metrics for resource instance.
     * @return Returns list of metric result.
     */
    @Override
    public List<Metric> getMetricsForResource(ResourceMetricsRequest resourceMetricRequest) {
        return metricsService.getMetricsByResource(resourceMetricRequest);
    }

    /**
     * Get metrics for service instance by the @serviceMetricRequest.
     *
     * @param serviceMetricRequest The request model to query metrics for service instance.
     * @return Returns list of metric result.
     */
    @Override
    public List<Metric> getMetricsForService(ServiceMetricsRequest serviceMetricRequest) {
        return metricsService.getMetricsByService(serviceMetricRequest);
    }

    @Override
    public boolean startService(ServiceStateManageRequest serviceStateManageRequest) {
        return vmStateManager.startService(serviceStateManageRequest);
    }

    @Override
    public boolean stopService(ServiceStateManageRequest serviceStateManageRequest) {
        return vmStateManager.stopService(serviceStateManageRequest);
    }

    @Override
    public boolean restartService(ServiceStateManageRequest serviceStateManageRequest) {
        return vmStateManager.restartService(serviceStateManageRequest);
    }

    @Override
    public void auditApiRequest(AuditLog auditLog) {
        log.info(auditLog.toString());
    }

    @Override
    @Cacheable(cacheNames = SERVICE_FLAVOR_PRICE_CACHE_NAME, key = "#request")
    public FlavorPriceResult getServiceFlavorPrice(ServiceFlavorPriceRequest request) {
        return priceCalculator.getServiceFlavorPrice(request);
    }

    @Override
    public void getTempDeploymentCredentials(Credential credential) {
        // Delegates to the typed getTempDeploymentCredentials for AK_SK conversion.
        getTempDeploymentCredentials("AK_SK", credential);
    }

    @Override
    public Credential getTempDeploymentCredentials(
            String credentialType, Credential storedCredential) {
        // Only support conversion for USERNAME_PASSWORD
        if (!"USERNAME_PASSWORD".equals(credentialType)
                || storedCredential.getType() != CredentialType.USERNAME_PASSWORD) {
            throw new UnsupportedOperationException("Conversion not supported");
        }

        // Extract required variables
        String username = storedCredential.get("USERNAME");
        String password = storedCredential.get("PASSWORD");
        String domain = storedCredential.get("DOMAIN");
        String project = storedCredential.get("PROJECT");

        if (username == null || password == null || domain == null || project == null) {
            throw new IllegalArgumentException("USERNAME, PASSWORD, DOMAIN, PROJECT are required");
        }

        // Build Login Token Request
        LoginTokenSecurityToken loginTokenSecurityToken =
                new LoginTokenSecurityToken()
                        .withAccess(username)
                        .withSecret(password)
                        .withId(domain);

        LoginTokenAuth loginTokenAuth =
                new LoginTokenAuth().withSecuritytoken(loginTokenSecurityToken);

        CreateLoginTokenRequestBody loginTokenRequestBody =
                new CreateLoginTokenRequestBody().withAuth(loginTokenAuth);

        CreateLoginTokenRequest loginTokenRequest =
                new CreateLoginTokenRequest().withBody(loginTokenRequestBody);

        // Call SDK: Receive Login Token
        CreateLoginTokenResponse loginTokenResponse = iamClient.createLoginToken(loginTokenRequest);

        String subjectLoginToken = loginTokenResponse.getXSubjectLoginToken();
        if (subjectLoginToken == null || subjectLoginToken.isBlank()) {
            throw new RuntimeException("Missing subject login token");
        }

        // Build Temporary AK/SK Request
        IdentityToken identityToken = new IdentityToken().withId(subjectLoginToken);

        TokenAuthIdentity tokenAuthIdentity =
                new TokenAuthIdentity()
                        .addMethodsItem(TokenAuthIdentity.MethodsEnum.TOKEN)
                        .withToken(identityToken);

        TokenAuth tokenAuth = new TokenAuth().withIdentity(tokenAuthIdentity);

        CreateTemporaryAccessKeyByTokenRequestBody takBody =
                new CreateTemporaryAccessKeyByTokenRequestBody().withAuth(tokenAuth);

        CreateTemporaryAccessKeyByTokenRequest takRequest =
                new CreateTemporaryAccessKeyByTokenRequest().withBody(takBody);

        // Call SDK: Get Temporary AK/SK
        CreateTemporaryAccessKeyByTokenResponse takResponse =
                iamClient.createTemporaryAccessKeyByToken(takRequest);

        com.huaweicloud.sdk.iam.v3.model.Credential sdkCred = takResponse.getCredential();
        if (sdkCred == null) {
            throw new RuntimeException("No temporary AK/SK received from Huawei");
        }

        // Map result to Xpanse Credential
        Credential result = new Credential();
        result.setType(CredentialType.AK_SK);
        result.setVariables(
                Map.of(
                        "ACCESS_KEY", sdkCred.getAccess(),
                        "SECRET_KEY", sdkCred.getSecret(),
                        "SECURITY_TOKEN", sdkCred.getSecuritytoken()));

        return result;
    }

    public void setIamClient(IamClient iamClient) {
        this.iamClient = iamClient;
    }

    /**
     * Returns AK_SK if available, otherwise converts USERNAME_PASSWORD to AK_SK. Throws if no valid
     * credentials found.
     */
    public Credential selectPreferredCredential(Credential akSk, Credential usernamePassword) {
        if (akSk != null && akSk.getType() == CredentialType.AK_SK) {
            return akSk;
        }
        if (usernamePassword != null
                && usernamePassword.getType() == CredentialType.USERNAME_PASSWORD) {
            return getTempDeploymentCredentials("USERNAME_PASSWORD", usernamePassword);
        }
        throw new IllegalArgumentException("No valid AK_SK or USERNAME_PASSWORD credential found.");
    }
}
