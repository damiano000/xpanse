/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.modules.orchestrator;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.eclipse.xpanse.modules.models.common.enums.Csp;
import org.eclipse.xpanse.modules.models.credential.AbstractCredentialInfo;
import org.eclipse.xpanse.modules.models.credential.Credential;
import org.eclipse.xpanse.modules.models.servicetemplate.Ocl;
import org.eclipse.xpanse.modules.orchestrator.audit.OperationalAudit;
import org.eclipse.xpanse.modules.orchestrator.credential.AuthenticationCapabilities;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployTask;
import org.eclipse.xpanse.modules.orchestrator.deployment.ServiceResourceHandler;
import org.eclipse.xpanse.modules.orchestrator.monitor.ServiceMetricsExporter;
import org.eclipse.xpanse.modules.orchestrator.price.ServicePriceCalculator;
import org.eclipse.xpanse.modules.orchestrator.servicestate.ServiceStateManager;
import org.eclipse.xpanse.modules.orchestrator.servicetemplate.ServiceTemplateManager;

/**
 * This interface describes orchestrator plugin in charge of interacting with backend fundamental
 * APIs.
 */
public interface OrchestratorPlugin
        extends ServiceResourceHandler,
                AuthenticationCapabilities,
                ServiceMetricsExporter,
                ServiceStateManager,
                OperationalAudit,
                ServicePriceCalculator,
                ServiceTemplateManager {

    /**
     * Get the Csp of the plugin.
     *
     * @return cloud service provider.
     */
    Csp getCsp();

    /**
     * Get the required properties of the plugin.
     *
     * @return required properties.
     */
    List<String> requiredProperties();

    /** Get all sites of the cloud service provider. */
    List<String> getSites();

    /**
     * Validate regions of service.
     *
     * @param ocl Ocl object.
     * @return true if all regions are valid.
     */
    boolean validateRegionsOfService(Ocl ocl);

    Map<String, String> getComputeResourcesInServiceDeployment(File scriptFile);

    default AbstractCredentialInfo getTempDeploymentCredentials(
            DeployTask task, AbstractCredentialInfo abstractCredentialInfo) {
        // By default, return the same credential (no temporary credential logic).
        return abstractCredentialInfo;
    }

    /**
     * Convert credential to temporary access key if supported by CSP. For HuaweiCloud, convert
     * USERNAME_PASSWORD to AK/SK using the SDK.
     *
     * @param credential the credential to convert.
     */
    default void getTempDeploymentCredentials(Credential credential) {
        throw new UnsupportedOperationException("getTempDeploymentCredentials is not implemented");
    }

    /**
     * Convert a stored credential to another type (e.g. USERNAME_PASSWORD → AK_SK).
     *
     * @param credentialType the desired output type, e.g. "AK_SK".
     * @param storedCredential the original credential to convert.
     * @return a new Credential of the requested type.
     */
    default Credential getTempDeploymentCredentials(
            String credentialType, Credential storedCredential) {
        throw new UnsupportedOperationException(
                "getTempDeploymentCredentials not implemented for this CSP");
    }
}
