/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: Huawei Inc.
 *
 */

package org.eclipse.xpanse.api.controllers;

import static org.eclipse.xpanse.modules.security.auth.common.RoleConstants.ROLE_ADMIN;
import static org.eclipse.xpanse.modules.security.auth.common.RoleConstants.ROLE_USER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.xpanse.api.config.AuditApiRequest;
import org.eclipse.xpanse.modules.models.common.enums.Csp;
import org.eclipse.xpanse.modules.models.policy.userpolicy.UserPolicy;
import org.eclipse.xpanse.modules.models.policy.userpolicy.UserPolicyCreateRequest;
import org.eclipse.xpanse.modules.models.policy.userpolicy.UserPolicyQueryRequest;
import org.eclipse.xpanse.modules.models.policy.userpolicy.UserPolicyUpdateRequest;
import org.eclipse.xpanse.modules.policy.UserPolicyManager;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** REST interface methods for user managing policies of the cloud service provider. */
@Slf4j
@RestController
@RequestMapping("/xpanse")
@CrossOrigin
@Secured({ROLE_ADMIN, ROLE_USER})
@ConditionalOnProperty(name = "enable.agent.api.only", havingValue = "false", matchIfMissing = true)
public class UserPolicyManageApi {

    @Resource private UserPolicyManager userPolicyManager;

    /**
     * List the policies created by the user.
     *
     * @param csp The cloud service provider.
     * @param enabled Is the policy enabled.
     * @return Returns list of the policies created by the user.
     */
    @Tag(name = "UserPoliciesManagement", description = "APIs for managing user's infra policies.")
    @GetMapping(value = "/policies", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "List the policies defined by the user.")
    @AuditApiRequest(methodName = "getCspFromRequestUri")
    public List<UserPolicy> getAllUserPolicies(
            @Parameter(name = "cspName", description = "Name of csp which the policy belongs to.")
                    @RequestParam(name = "cspName", required = false)
                    Csp csp,
            @Parameter(name = "enabled", description = "Is the policy enabled.")
                    @RequestParam(name = "enabled", required = false)
                    Boolean enabled) {
        UserPolicyQueryRequest queryModel = userPolicyManager.getUserPolicyQueryModel(csp, enabled);
        return userPolicyManager.listUserPolicies(queryModel);
    }

    /**
     * Get the details of the policy created by the user.
     *
     * @param userPolicyId The id of the policy.
     * @return Returns list of the policies defined by the user.
     */
    @Tag(name = "UserPoliciesManagement", description = "APIs for managing user's infra policies.")
    @GetMapping(value = "/policies/{userPolicyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get the details of the policy created by the user.")
    @AuditApiRequest(methodName = "getCspFromUserPolicyId", paramTypes = UUID.class)
    public UserPolicy getPolicyDetails(@PathVariable UUID userPolicyId) {
        return userPolicyManager.getUserPolicyDetails(userPolicyId);
    }

    /**
     * Add policy created by the user.
     *
     * @param userPolicyCreateRequest The policy to be created.
     */
    @Tag(name = "UserPoliciesManagement", description = "APIs for managing user's infra policies.")
    @PostMapping(value = "/policies", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Add policy created by the user.")
    @AuditApiRequest(methodName = "getCspFromRequestUri")
    public UserPolicy addUserPolicy(
            @Valid @RequestBody UserPolicyCreateRequest userPolicyCreateRequest) {
        return userPolicyManager.addUserPolicy(userPolicyCreateRequest);
    }

    /**
     * Update the policy created by the user.
     *
     * @param updateRequest The policy to be updated.
     */
    @Tag(name = "UserPoliciesManagement", description = "APIs for managing user's infra policies.")
    @PutMapping(value = "/policies/{userPolicyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update the policy created by the user.")
    @AuditApiRequest(methodName = "getCspFromRequestUri")
    public UserPolicy updateUserPolicy(
            @Parameter(
                            name = "userPolicyId",
                            description = "id of the policy created by user to be updated")
                    @PathVariable("userPolicyId")
                    UUID userPolicyId,
            @Valid @RequestBody UserPolicyUpdateRequest updateRequest) {
        return userPolicyManager.updateUserPolicy(userPolicyId, updateRequest);
    }

    /**
     * Delete the policy created by the user.
     *
     * @param userPolicyId The id of the policy created by user.
     */
    @Tag(name = "UserPoliciesManagement", description = "APIs for managing user's infra policies.")
    @DeleteMapping(value = "/policies/{userPolicyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(description = "Delete the policy created by the user.")
    @AuditApiRequest(methodName = "getCspFromUserPolicyId", paramTypes = UUID.class)
    public void deleteUserPolicy(
            @Parameter(
                            name = "userPolicyId",
                            description = "id of the policy created by user to be updated")
                    @PathVariable("userPolicyId")
                    UUID userPolicyId) {
        userPolicyManager.deleteUserPolicy(userPolicyId);
    }
}
