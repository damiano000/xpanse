/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 */

package org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker;

import static org.eclipse.xpanse.modules.logging.LoggingKeyConstant.SERVICE_ID;
import static org.eclipse.xpanse.modules.logging.LoggingKeyConstant.TRACKING_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.xpanse.modules.deployment.deployers.opentofu.exceptions.OpenTofuMakerRequestFailedException;
import org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.api.OpenTofuFromGitRepoApi;
import org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.api.OpenTofuFromScriptsApi;
import org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.model.OpenTofuRequestWithScripts;
import org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.model.OpenTofuRequestWithScriptsGitRepo;
import org.eclipse.xpanse.modules.deployment.deployers.opentofu.tofumaker.generated.model.OpenTofuValidationResult;
import org.eclipse.xpanse.modules.models.servicetemplate.Deployment;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeploymentScriptValidationResult;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

/** Bean to validate openTofu scripts via tofu-maker. */
@Slf4j
@Component
@Profile("tofu-maker")
public class TofuMakerScriptValidator {

    private final OpenTofuFromScriptsApi openTofuFromScriptsApi;
    private final OpenTofuFromGitRepoApi openTofuFromGitRepoApi;
    private final TofuMakerHelper tofuMakerHelper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** constructor for OpenTofuMakerScriptValidator. */
    public TofuMakerScriptValidator(
            OpenTofuFromScriptsApi openTofuFromScriptsApi,
            OpenTofuFromGitRepoApi openTofuFromGitRepoApi,
            TofuMakerHelper tofuMakerHelper) {
        this.openTofuFromScriptsApi = openTofuFromScriptsApi;
        this.openTofuFromGitRepoApi = openTofuFromGitRepoApi;
        this.tofuMakerHelper = tofuMakerHelper;
    }

    /** Validate scripts provided in the OCL. */
    public DeploymentScriptValidationResult validateOpenTofuScripts(Deployment deployment) {
        DeploymentScriptValidationResult deployValidationResult = null;
        try {
            OpenTofuValidationResult validate =
                    openTofuFromScriptsApi.validateWithScripts(
                            getValidateScriptsInOclRequest(deployment));
            try {
                deployValidationResult =
                        objectMapper.readValue(
                                objectMapper.writeValueAsString(validate),
                                DeploymentScriptValidationResult.class);
            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException", e);
            }
        } catch (RestClientException restClientException) {
            log.error("Request to tofu-maker API failed", restClientException);
            throw new OpenTofuMakerRequestFailedException(restClientException.getMessage());
        }
        return deployValidationResult;
    }

    /** Validate scripts in the GIT repo. */
    public DeploymentScriptValidationResult validateOpenTofuScriptsFromGitRepo(
            Deployment deployment) {
        DeploymentScriptValidationResult deployValidationResult = null;
        try {
            OpenTofuValidationResult validate =
                    openTofuFromGitRepoApi.validateScriptsFromGitRepo(
                            getValidateScriptsInGitRepoRequest(deployment));
            try {
                deployValidationResult =
                        objectMapper.readValue(
                                objectMapper.writeValueAsString(validate),
                                DeploymentScriptValidationResult.class);
            } catch (JsonProcessingException e) {
                log.error("JsonProcessingException", e);
            }
        } catch (RestClientException restClientException) {
            log.error("Request to tofu-maker API failed", restClientException);
            throw new OpenTofuMakerRequestFailedException(restClientException.getMessage());
        }
        return deployValidationResult;
    }

    private OpenTofuRequestWithScripts getValidateScriptsInOclRequest(Deployment deployment) {
        OpenTofuRequestWithScripts request = new OpenTofuRequestWithScripts();
        request.setRequestType(OpenTofuRequestWithScripts.RequestTypeEnum.VALIDATE);
        request.setRequestId(getRequestId());
        request.setOpenTofuVersion(deployment.getDeployerTool().getVersion());
        request.setIsPlanOnly(false);
        request.setScriptFiles(deployment.getScriptFiles());
        return request;
    }

    private OpenTofuRequestWithScriptsGitRepo getValidateScriptsInGitRepoRequest(
            Deployment deployment) {
        OpenTofuRequestWithScriptsGitRepo request = new OpenTofuRequestWithScriptsGitRepo();
        request.setRequestType(OpenTofuRequestWithScriptsGitRepo.RequestTypeEnum.VALIDATE);
        request.setRequestId(getRequestId());
        request.setOpenTofuVersion(deployment.getDeployerTool().getVersion());
        request.setIsPlanOnly(false);
        request.setGitRepoDetails(
                tofuMakerHelper.convertOpenTofuScriptsGitRepoDetailsFromDeployFromGitRepo(
                        deployment.getScriptsRepo()));
        return request;
    }

    private UUID getRequestId() {
        if (StringUtils.isNotBlank(MDC.get(TRACKING_ID))) {
            try {
                return UUID.fromString(MDC.get(SERVICE_ID));
            } catch (Exception e) {
                return UUID.randomUUID();
            }
        } else {
            return UUID.randomUUID();
        }
    }
}
