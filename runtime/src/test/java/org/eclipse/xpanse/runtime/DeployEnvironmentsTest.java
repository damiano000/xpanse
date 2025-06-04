package org.eclipse.xpanse.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.xpanse.modules.credential.CredentialCenter;
import org.eclipse.xpanse.modules.deployment.utils.DeployEnvironments;
import org.eclipse.xpanse.modules.models.common.enums.Csp;
import org.eclipse.xpanse.modules.models.credential.CredentialVariable;
import org.eclipse.xpanse.modules.models.credential.CredentialVariables;
import org.eclipse.xpanse.modules.models.service.deployment.DeployRequest;
import org.eclipse.xpanse.modules.models.servicetemplate.CloudServiceProvider;
import org.eclipse.xpanse.modules.models.servicetemplate.Deployment;
import org.eclipse.xpanse.modules.models.servicetemplate.Ocl;
import org.eclipse.xpanse.modules.models.servicetemplate.Region;
import org.eclipse.xpanse.modules.orchestrator.OrchestratorPlugin;
import org.eclipse.xpanse.modules.orchestrator.PluginManager;
import org.eclipse.xpanse.modules.orchestrator.deployment.DeployTask;
import org.eclipse.xpanse.modules.security.secrets.SecretsManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

/** Unit-test per check isFinalCredential() logic in DeployEnvironments#getEnvironmentVariables. */
class DeployEnvironmentsTest {

    private CredentialCenter credentialCenter;
    private SecretsManager secretsManager;
    private PluginManager pluginManager;
    private Environment environment;
    private OrchestratorPlugin orchestratorPlugin;

    private DeployEnvironments deployEnvironments;

    @BeforeEach
    void setUp() {
        credentialCenter = mock(CredentialCenter.class);
        secretsManager = mock(SecretsManager.class);
        pluginManager = mock(PluginManager.class);
        environment = mock(Environment.class);
        orchestratorPlugin = mock(OrchestratorPlugin.class);

        deployEnvironments =
                new DeployEnvironments(
                        credentialCenter, secretsManager, pluginManager, environment);
    }

    @Test
    void getEnvironmentVariables_returnsVariablesFromFinalCredential() {

        /* Mock per DeployTask */
        DeployTask deployTask = mock(DeployTask.class);
        DeployRequest deployReq = mock(DeployRequest.class);
        Ocl ocl = mock(Ocl.class);
        Deployment deployment = mock(Deployment.class);
        Region region = mock(Region.class);
        CloudServiceProvider cspObj = mock(CloudServiceProvider.class);

        when(deployTask.getDeployRequest()).thenReturn(deployReq);
        when(deployTask.getOcl()).thenReturn(ocl);

        when(deployReq.getCsp()).thenReturn(Csp.HUAWEI_CLOUD);
        when(deployReq.getRegion()).thenReturn(region);
        when(region.getSite()).thenReturn("site-1");

        when(ocl.getCloudServiceProvider()).thenReturn(cspObj);
        when(cspObj.getName()).thenReturn(Csp.HUAWEI_CLOUD);

        when(ocl.getDeployment()).thenReturn(deployment);
        when(deployment.getInputVariables()).thenReturn(Collections.emptyList());

        /* Credential: already final */
        CredentialVariable ak = new CredentialVariable("access_key", "AK", true, true, "real-ak");

        CredentialVariables finalCred = mock(CredentialVariables.class);
        when(finalCred.isFinalCredential()).thenReturn(true);
        when(finalCred.getVariables()).thenReturn(List.of(ak));

        when(credentialCenter.getCredential(any(), any(), any(), any())).thenReturn(finalCred);

        /* Plugin manager / orchestrator plugin */
        when(pluginManager.getOrchestratorPlugin(Csp.HUAWEI_CLOUD)).thenReturn(orchestratorPlugin);
        when(orchestratorPlugin.getEnvVarKeysMappingMap()).thenReturn(Collections.emptyMap());

        /* ACT */
        Map<String, String> env = deployEnvironments.getEnvironmentVariables(deployTask);

        /* ASSERT */
        assertThat(env).containsEntry("access_key", "real-ak");
    }
}
