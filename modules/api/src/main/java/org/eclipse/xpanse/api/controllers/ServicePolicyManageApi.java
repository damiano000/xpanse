/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.api.controllers;

import static org.eclipse.xpanse.modules.security.auth.common.RoleConstants.ROLE_ADMIN;
import static org.eclipse.xpanse.modules.security.auth.common.RoleConstants.ROLE_ISV;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.api.config.AuditApiRequest;
import org.eclipse.xpanse.modules.models.policy.servicepolicy.ServicePolicy;
import org.eclipse.xpanse.modules.models.policy.servicepolicy.ServicePolicyCreateRequest;
import org.eclipse.xpanse.modules.models.policy.servicepolicy.ServicePolicyUpdateRequest;
import org.eclipse.xpanse.modules.policy.ServicePolicyManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** REST interface methods for service managing policies of the cloud service provider. */
@Slf4j
@RestController
@RequestMapping("/xpanse")
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_ISV})
@ConditionalOnProperty(name = "enable.agent.api.only", havingValue = "false", matchIfMissing = true)
public class ServicePolicyManageApi {

    @Resource private ServicePolicyManager servicePolicyManager;

    /**
     * List the policies by the id of registered service template.
     *
     * @param serviceTemplateId the id of registered service template which the policy belongs to.
     * @return Returns list of the policies belongs to the registered service template.
     */
    @Tag(
            name = "ServicePoliciesManagement",
            description = "APIs for managing service's infra policies.")
    @GetMapping(value = "/service/policies", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "List the policies belongs to the service.")
    @AuditApiRequest(methodName = "getCspFromServiceTemplateId", paramTypes = UUID.class)
    public List<ServicePolicy> getAllServicePolicies(
            @Parameter(
                            name = "serviceTemplateId",
                            description = "The id of service template which the policy belongs to.")
                    UUID serviceTemplateId) {
        return servicePolicyManager.listServicePolicies(serviceTemplateId);
    }

    /**
     * Get the details of the policy belongs to the registered service template.
     *
     * @param servicePolicyId The id of the policy.
     * @return Returns list of the policies defined by the service.
     */
    @Tag(
            name = "ServicePoliciesManagement",
            description = "APIs for managing service's infra policies.")
    @GetMapping(
            value = "/service/policies/{servicePolicyId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get details of policy belongs to the registered service template.")
    @AuditApiRequest(methodName = "getCspFromServicePolicyId", paramTypes = UUID.class)
    public ServicePolicy getServicePolicyDetails(@PathVariable UUID servicePolicyId) {
        return servicePolicyManager.getServicePolicyDetails(servicePolicyId);
    }

    /**
     * Add policy belongs to the registered service template.
     *
     * @param servicePolicyCreateRequest The policy to be created.
     */
    @Tag(
            name = "ServicePoliciesManagement",
            description = "APIs for managing service's infra policies.")
    @PostMapping(value = "/service/policies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Add policy for the registered service template.")
    @AuditApiRequest(methodName = "getCspFromServiceTemplateId", paramTypes = UUID.class)
    public ServicePolicy addServicePolicy(
            @Valid @RequestBody ServicePolicyCreateRequest servicePolicyCreateRequest) {
        return servicePolicyManager.addServicePolicy(servicePolicyCreateRequest);
    }

    /**
     * Update the policy belonged to the registered service template.
     *
     * @param updateRequest The policy to be updated.
     */
    @Tag(
            name = "ServicePoliciesManagement",
            description = "APIs for managing service's infra policies.")
    @PutMapping(
            value = "/service/policies/{servicePolicyId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update the policy belongs to the registered service template.")
    @AuditApiRequest(methodName = "getCspFromServicePolicyId", paramTypes = UUID.class)
    public ServicePolicy updateServicePolicy(
            @Parameter(name = "servicePolicyId", description = "ID of the policy to be updated")
                    @PathVariable("servicePolicyId")
                    UUID servicePolicyId,
            @Valid @RequestBody ServicePolicyUpdateRequest updateRequest) {
        return servicePolicyManager.updateServicePolicy(servicePolicyId, updateRequest);
    }

    /**
     * Delete the policy belongs to the registered service template.
     *
     * @param servicePolicyId The id of policy.
     */
    @Tag(
            name = "ServicePoliciesManagement",
            description = "APIs for managing service's infra policies.")
    @DeleteMapping(
            value = "/service/policies/{servicePolicyId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Delete the policy belongs to the registered service template.")
    @AuditApiRequest(methodName = "getCspFromServicePolicyId", paramTypes = UUID.class)
    public void deleteServicePolicy(@PathVariable("servicePolicyId") UUID servicePolicyId) {
        servicePolicyManager.deleteServicePolicy(servicePolicyId);
    }
}
